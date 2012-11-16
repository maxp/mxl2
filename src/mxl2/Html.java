/*
 * Html.java
 *
 */

package mxl2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *	basic html routines
 */
public class Html
{

	/** quote html special chars */
	public static String q( String s )
	{
		if( s == null ) return "";
		s = Lib.pattern_amp. matcher( s ).replaceAll( "&amp;" );
		s = Lib.pattern_quot.matcher( s ).replaceAll( "&quot;" );
		s = Lib.pattern_lt.  matcher( s ).replaceAll( "&lt;" );
		s = Lib.pattern_gt.  matcher( s ).replaceAll( "&gt;" );
		return s;
	}
	
	/** urlencode string */
	public static String urlenc( String s )
	{
		if( s == null ) s = "";
		try { return URLEncoder.encode( s, "UTF-8" ); }
		catch( UnsupportedEncodingException ex ) { return q(s); } // must not be there!
	}
	
	/** set of tag support methods */
	public static class Tag
	{
		public static String pair( String attr, String value )
		{
			if( attr == null || attr.length() == 0 ) return "";
			return " "+attr+"=\""+q(value)+"\"";
		}
	}

//	public static String pair( String attr, String value )
//	{
//		if( attr == null || attr.length() == 0 ) return "";
//		return " "+attr+"=\""+ ((value == null) ? "" : q(value))+"\"";
//	}
//
//	public static String tail( String val )
//	{
//		return (val == null || val.length() == 0) ? "" : " "+val;
//	}
//
//	public static String null2str( String val )
//	{
//		return (val==null) ? "" : val;
//	}
//
//	public static String tag( String tag, Map<String,Object> attr, String extra, boolean single )
//	{
//		StringBuilder sb = new StringBuilder( 80 );
//		sb.append( '<' ).append( tag );
//		if( attr != null && attr.size() > 0 )
//		{
//			for( Map.Entry<String,Object> m: attr.entrySet() )
//				try {
//					String k = m.getKey(); String v = m.getValue().toString();
//					if( k.length() > 0 && v != null )
//						sb.append( ' ').append( k ).append( "=\"").append( q(v) ).append( '"' );
//				}
//				catch( Exception ign ) { }
//		}
//		if( extra != null ) sb.append( ' ' ).append( extra );
//		return sb.append( (single) ? " />" : ">" ).toString();
//	}
//
//	public static String tagEnd( String tag ) { return "</"+tag+">"; }
//
//	public static String input( String name, String value, int maxlength, String extra )
//	{
//		HashMap<String,Object> a = new HashMap<String,Object>( 5 );
//		a.put( "type", "text" ); a.put( "class", "txt");
//		a.put( "name", name ); a.put( "value", value );
//		a.put( "maxlength", Integer.toString(maxlength) );
//		return tag( "input", a, extra, true );
//	}
//
//	public static String input( String name, String value, int maxlength )
//	{
//		return input( name, value, maxlength, null );
//	}
//
//	public static String number( String name, Integer value, int maxlength, String extra )
//	{
//		HashMap<String,Object> a = new HashMap<String,Object>( 6 );
//		a.put( "type", "text" ); a.put( "class", "num");
//		a.put( "name", name ); a.put( "value", value );
//		a.put( "maxlength", Integer.toString(maxlength) );
//		a.put( "size", Integer.toString(maxlength) );
//		return tag( "input", a, extra, true );
//	}
//
//	public static String number( String name, Integer value, int maxlength )
//	{
//		return number( name, value, maxlength, null );
//	}
//
//	public static String text( String name, String value, int cols, int rows, String extra )
//	{
//		return tag( "textarea", null,
//			new StringBuilder().append(pair("name", name)).append( " class=\"area\"" )
//					.append(pair("cols", Integer.toString(cols)))
//					.append(pair("rows", Integer.toString(rows)))
//					.append(tail(extra)).toString(), false )+q(value)+tagEnd( "textarea" );
//	}
//
//	public static String text( String name, String value, int cols, int rows )
//	{
//		return text( name, value, cols, rows, null );
//	}
//
//	/**
//	 *
//	 * @param name
//	 * @param opts  Collection of ( val{, opt{, extra} } )
//	 *              extra = (char(1) != ' ') means 'selected="1"'
//	 * @param size  1: drop down box, > 1 listbox
//	 * @param multi
//	 * @param extra
//	 * @return  html "select" construct
//	 */
//	public static String select( String name, Collection opts, int size, boolean multi, String extra )
//	{
//		if( opts == null ) opts = new LinkedList();
//
//		StringBuilder sb = new StringBuilder( 80 + 60*opts.size() );
//		sb.append( "<select" ).append( pair( "name", name ) ).append( " class=\"sel\"" )
//				.append( " size=\"" ).append( Integer.toString(size) ).append( '\"');
//		if( multi ) sb.append( " multiple=\"1\"" );
//		if( extra != null && extra.length() > 0 ) sb.append( ' ' ).append( extra );
//		sb.append( '>' );
//		for( Object op: opts )
//		{
//			if( op != null )
//			{
//				String v = null, o = null, x = null;
//
//				if( op instanceof String ) { v = (String)op; o = v; }
//				else if( op instanceof String[] ) {
//					String[] a = (String[])op;
//					if( a != null ) {
//						if( a.length == 1) { v = a[0]; o = v; }
//						else if( a.length == 2 ) { v = a[0]; o = a[1]; }
//						else if( a.length >= 3 ) { v = a[0]; o = a[1]; x = a[2]; }
//					}
//				}
//				else if( op instanceof Collection ) {
//					Iterator it = ((Collection)op).iterator();
//					try {
//						if( it.hasNext() ) { v = it.next().toString(); }
//						if( it.hasNext() ) { o = it.next().toString(); }
//						if( it.hasNext() ) { x = it.next().toString(); }
//					}
//					catch( Exception ign ) { }
//				}
//
//				if( v == null ) continue; /* NOTE: wrong option value */
//
//				sb.append( "<option value=\"" ).append( q(v) ).append( '"' );
//				if( x != null ) {
//					if( (x.length() == 1) && (x.charAt(0) != ' ') ) x = "selected=\"1\"";
//					sb.append( ' ' ).append( x );
//				}
//				sb.append( '>' ).append( q(o) ).append( "</option>" );
//			}
//		}
//		return sb.append( "</select>" ).toString();
//	}
//
//	public static String select( String name, Collection opts, String extra )
//	{
//		return select( name, opts, 1, false, extra );
//	}
//
//	public static String check( String name, boolean checked, String extra )
//	{
//		return tag( "input", null,
//				pair( "name", name )+pair( "type", "checkbox" )+pair( "class", "check" )
//				+((checked)? " \"checked\"=\"1\"":"")+tail( extra ), true );
//	}
//
//	/* bottons */
//
//	public static String button( String name, String value, String extra )
//	{
//		return tag( "button", null,	pair( "name", name )+pair( "class", "btn" )+tail( extra ), false )
//				+null2str( value )+tagEnd( "button" );
//	}
//
//	public static String submit( String name, String value, String extra )
//	{
//		return tag( "input", null,
//				new StringBuilder().append( "type=\"submit\" class=\"btn\"" )
//					.append( pair("name", name) ).append( pair("value", value) )
//					.append( tail(extra) ).toString(), true );
//	}
//
//	public static String hidden( String name, int value )
//	{
//		return hidden( name, Integer.toString(value) );
//	}
//
//	public static String hidden( String name, String value )
//	{
//		if( value == null ) value = "";
//		return "<input type=\"hidden\" name=\""+name+"\" value=\""+value+"\" />";
//	}
//
//	public static String date( String name, Date value )
//	{
//		String val = ( value == null )? "": Lib.fmt( value ); 
//		return input( name, val, 10, "style='width: 6em; text-align: center;'" );
//	}
//
	
	
	
}

// eof