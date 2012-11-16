package mxl2.site.menu;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

public class SimpleWriter implements MenuWriter 
{
	String hdr, pat, ftr, empty;
	Writer out;
	
	/**
	 * @param out - output menu writer
	 * @param hdr - menu header
	 * @param pat - menu line template (java.text.MessageFormat)
	 * 		0 - level, 1 - mode ("","open","active"), 2 - url, 3 - title
	 * @param ftr - menu footer
	 * @param empty - empty menu stub
	 */
	public SimpleWriter( Writer out, String hdr, String pat, String ftr, String empty )
	{
		this.out = out; this.hdr = hdr; this.pat = pat; this.ftr = ftr; this.empty = empty;
	}

	public static final String DEF_HDR = "<div class=\"lmenu\">";
	public static final String DEF_PAT = "<div class=\"lmenu{0} {1}\"><a{2}>{3}</a></div>";
	public static final String DEF_FTR = "</div>";
	
	public SimpleWriter( Writer out )
	{
		this.out = out; this.hdr = DEF_HDR; this.pat = DEF_PAT; this.ftr = DEF_FTR;
	}

	@Override
	public boolean printHdr() 
	{
		try { out.write( (hdr == null)? "": hdr ); } catch( IOException ign ) { return false; }
		return true;
	}

	@Override
	public boolean printLine( State state, MenuItem item ) 
	{
		MessageFormat mf = new MessageFormat( DEF_PAT );
		String mode;
		switch( state ) 
		{
			case OPEN: mode = "open"; break; 
			case ACTIVE: mode = "active"; break; 
			default: mode = ""; 
		}
		if( "-".equals( item.title ) ) return true;
		String u = ("-".equals(item.url))? "": " href=\""+item.url+"\"";
		try { out.write( mf.format( new String[]{ Integer.toString(item.level), mode, u, item.title })); } 
		catch( IOException ign ) { return false; }
		return true;
	}
	
	@Override
	public boolean printFtr() 
	{
		try { out.write( (ftr == null)? "": ftr ); } catch( IOException ign ) { return false; }
		return true;
	}
	
	
	@Override
	public boolean printEmpty() 
	{
		try { out.write( (empty == null)? "": empty ); } catch( IOException ign ) { return false; }
		return true;
	}
	

}

// eof