package mxl2.site;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Ctx 
{
	Map<String,Object> varMap;
	
	public String uri;
	public HttpSession sess;
	public HttpServletRequest req;
	public HttpServletResponse resp;
	
	private Ctx()
	{
		varMap = new HashMap<String,Object>(8);
	}
	
	/**
	 * get Ctx instance of specified request or create new
	 * also set request/response encoding
	 * 
	 * @param req
	 * @param resp
	 * @return Ctx object
	 */
	public static Ctx make( HttpServletRequest req, HttpServletResponse resp )
	{
		Ctx ctx = (Ctx)req.getAttribute( Ctx.class.getName() );
		if( ctx != null ) { return ctx; }
		ctx = new Ctx(); req.setAttribute( Ctx.class.getName(), ctx );
	
		ctx.uri = req.getRequestURI();
		ctx.req = req;
		ctx.resp = resp;
		ctx.sess = req.getSession( true );
		
		try { 
			req.setCharacterEncoding( "utf-8" );
			resp.setContentType( "text/html; charset=utf-8" );
//			resp.setCharacterEncoding( HTTP_ENC ); 
		}
		catch( Exception ign ) { }
		
		return ctx;
	}
	
	/**
	 * context for includes
	 * @param req
	 * @return Ctx object or null
	 */
	public static Ctx get( HttpServletRequest req ) 
	{
		return (Ctx)req.getAttribute( Ctx.class.getName() );
	}
	
	/**
	 *	clean up Ctx variables
	 */
	public void destroy()
	{
		for( Object obj: varMap.values() )
			if( obj instanceof ICtxVar ) ((ICtxVar)obj).destroy();
		varMap.clear();
	}

	public HttpSession sess() { return sess; }
	
	public String str( String name ) { return str( name, "" ); }
	
	public String str( String name, String def )
	{
		Object obj = varMap.get( name ); if( obj == null ) return def;
		if( obj instanceof String ) return (String)obj;
		String res = obj.toString();
		return (res != null)? res: def;
	}
	
	public Ctx setStr( String name, String val ) { varMap.put( name, val ); return this; }
	
	public Ctx addStr( String name, String val ) 
	{
		// NOTE: implement handle multiple values with the same name
		varMap.put( name, val ); return this; 
	}
	
}

// eof
