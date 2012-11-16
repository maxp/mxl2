package mxl2.site;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mxl2.Lib;

/**
 * this class intended to process .htm content files
 * 
 * web.xml sample:
 * 
 *	<context-param>
 * 		<param-name>text-encoding</param-name><param-value>windows-1251</param-value>
 *	</context-param>
 *
 * <servlet>
 * 		<servlet-name>HtmServlet</servlet-name>
 * 		<servlet-class>mxl2.site.HtmServlet</servlet-class>
 * 		<init-param>
 * 			<param-name>text-encoding</param-name><param-value>windows-1251</param-value>
 * 		</init-param>
 * 	</servlet>
 * 	<servlet-mapping>
 * 		<servlet-name>HtmServlet</servlet-name><url-pattern>*.htm</url-pattern>
 * 	</servlet-mapping>
 *
 *	header vars
 *		redir:	<redirect_url>
 *
 *	content (at) macros supported
 *	first character in line is (at)
 *		inc servletName
 */
public class HtmServlet extends HttpServlet 
{
	static final String DEFAULT_TEXT_ENCODING = "utf-8";
	static final String WEBXML_TEXT_ENCODING = "text-encoding";
	
	private static final long serialVersionUID = 6999195609709488990L;

	private static final Logger L = Logger.getLogger( HtmServlet.class.getName() );
	
	static final Pattern RE_EMPTY_LINE   = Pattern.compile( "^\\s*$" );
	static final Pattern MACRO_DELIMITER = Pattern.compile( "\\s+" );
	static final int     MACRO_PARAM_MAX = 20;
	
	/**
	 * content source encoding
	 */
	String encoding;
	ServletContext app;
//	long 	expires = -1;
	boolean etag 	= false;
	
	@Override
	public void init( ServletConfig cfg ) throws ServletException
	{
		super.init( cfg );
		this.encoding = cfg.getInitParameter( WEBXML_TEXT_ENCODING );
		if( this.encoding == null ) 
		{
			this.encoding = cfg.getServletContext().getInitParameter( WEBXML_TEXT_ENCODING );
			if( this.encoding == null ) this.encoding = DEFAULT_TEXT_ENCODING;
		}
		
		this.app = cfg.getServletContext();
	}
	
	@Override
	public void destroy()
	{
		// do nothing
	}
	
	boolean readHeader( LineNumberReader br, Ctx ctx ) throws IOException
	{
		String nm = null; 
		String val = null;
		
		String s = br.readLine();
		while( s != null )
		{
			if( s.length() == 0 ) break;	// empty line - end of header
			if( s.charAt(0) != '#' ) 
			{
				if( s.charAt(0) == ' ' )
				{
					if( s.matches( "\\w+") ) break;
					if( val == null ) return false;		// continuation on the first line
					val += s;
				}
				else {
					String[] a = s.split( ":\\s+", 2 );
					if( a.length != 2 ) return false;	// wrong header line format
					
					if( nm != null ) ctx.addStr( nm, val );
					nm = a[0]; val = a[1]; 
				}
			}
			s = br.readLine();
		}
		if( nm != null ) ctx.addStr( nm, val );
		return true;
	}
	
	File getHtmFile( HttpServletRequest req )
	{
		try { return new File( getServletContext().getRealPath( req.getServletPath() ) ); }
		catch( NullPointerException ign ) { }
		return null;
	}
	
	@Override
	public long getLastModified( HttpServletRequest req ) 
	{
		File f = getHtmFile( req );
		long lastmod = (f != null)? f.lastModified(): Lib.now();
		return (lastmod/1000) * 1000;
	}	
	
	@Override
	public void doGet( HttpServletRequest req, HttpServletResponse resp ) throws IOException
	{
		Ctx ctx = Ctx.make( req, resp );
		try {
			LineNumberReader rd = null;
			String fname = getServletContext().getRealPath( req.getServletPath() );
			File htmFile = new File( fname );
			try {
				rd = new LineNumberReader( Lib.fileReader( htmFile, encoding ) );
	
//				if( rd == null ) {
//					resp.sendError( HttpServletResponse.SC_NOT_FOUND );
//					return;
//				}
				
				if( !readHeader( rd, ctx ) )
				{
					L.warning( "doGet() !readHeader() line:"+rd.getLineNumber() );
					resp.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
					rd.close();
					return;
				}
			}
			catch( NullPointerException ex ) 
			{
				if( rd != null ) rd.close();
				resp.sendError( HttpServletResponse.SC_NOT_FOUND );
				return;
			}
			catch( Exception ex ) 
			{ 
				L.warning( "doGet(): "+ex.toString() );
				if( rd != null ) rd.close();
				resp.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
				return;
			}
			
			// rd positioned on the first line after header
			
			String redir = ctx.str( "redir", null );
			if( redir != null ) { rd.close(); resp.sendRedirect( redir ); return; }
	
			PrintWriter out = resp.getWriter();
			
			try { processContent( ctx, rd, out ); }
			catch( Exception ex ) {	L.warning( "doGet() process line: "+ex ); }
			
			rd.close();
		}
		finally {
			ctx.destroy();		// cleanup request context
		}
	}
	
	@Override
	public void doPost( HttpServletRequest req, HttpServletResponse resp ) throws IOException
	{
		resp.sendError( HttpServletResponse.SC_BAD_REQUEST );
	}

	void processContent( Ctx ctx, LineNumberReader rd, PrintWriter out ) throws ServletException, IOException
	{
		String s;
		while( (s = rd.readLine()) != null ) 
		{
			if( s.length() == 0 )
			{
				// handle empty line state change
			}
			else {
				switch( s.charAt(0) )
				{
				case '@':
					String[] arg = MACRO_DELIMITER.split( s, MACRO_PARAM_MAX );
					
					// do switch by map
					//
					if( "@".equals( arg[0]) ) 
					{
						// comment line - do nothing
					}
					else if( "@inc".equals( arg[0] ) ) 
						macro_inc( out, ctx, arg ); 
					else
						L.warning( "wrong macro at line("+rd.getLineNumber()+"): "+arg[0] );
					
					break;
				
				default:
					if( RE_EMPTY_LINE.matcher(s).matches() )
					{
						// empty line state change
					}
					else {
						// substitute {{ }}
						// escape & < >
						out.println( s );
					}
				}
			}
		}
		
	}
	
	boolean macro_inc( PrintWriter out, Ctx ctx, String[] arg ) throws ServletException, IOException
	{
		if( arg.length < 2 ) 
		{
			L.warning( "no @inc parameter uri:"+ctx.uri );
			return false;
		}
		RequestDispatcher disp = app.getNamedDispatcher( arg[1] );
		if( disp == null ) 
		{
			L.warning( "@inc name not found:'"+arg[1]+"' uri:"+ctx.uri );
			return false;
		}
		disp.include( ctx.req, ctx.resp );
		return true;
	}
	
}

// eof
