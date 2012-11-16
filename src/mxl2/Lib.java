/*
 * Lib.java
 *
 */

package mxl2;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 *	general purpose routines
 *
 */
public final class Lib
{

	public static final Pattern pattern_amp  = Pattern.compile( "&" );
	public static final Pattern pattern_quot = Pattern.compile( "\"");
	public static final Pattern pattern_apos = Pattern.compile( "\'");
	public static final Pattern pattern_lt   = Pattern.compile( "<" );
	public static final Pattern pattern_gt   = Pattern.compile( ">" );

	public static final Pattern pattern_wspc = Pattern.compile( "\\s+" );
	public static final Pattern pattern_emptystr = Pattern.compile( "^\\s*$" );

	
	/** @return parseInt() on success, 'def' otherwise */
	public static int to_i( String s, int def )
	{
		try { return Integer.parseInt( s );	} 
		catch( Exception ign ) { return def; } 
	}

	/** @return parseInt() on success, '0' otherwise */
	public static int to_i( String s ) { return to_i( s, 0 ); }
	

	/** @return parseLong() on success, '0' otherwise */
	public static long to_l( String s ) { return to_l( s, 0 ); }
	
	/** @return parseLong() on success, 'def' otherwise */
	public static long to_l( String s, long def )
	{
		try { return Long.parseLong( s ); } 
		catch( Exception ex ) { return def; }
	}

	/**
	 * @param ts - sql timestamp
	 * @return long representation of the stamp
	 */
	public static long to_l( Timestamp ts )
	{
		return (ts != null)? ts.getTime(): 0;
	}
	
	/** @return string representation of int */
	public static String to_s( int i ) { return Integer.toString(i); }
	
	/** @return string representation of long */
	public static String to_s( long l ) { return Long.toString(l); }
	
	/** @return toString() on success, "" otherwise */
	public static String to_s( Object o ) { return to_s( o, "" ); }

	/** @return toString() on success, 'def' otherwise */
	public static String to_s( Object o, String def )
	{
		if( o != null ) {
			try { return o.toString(); } catch( Exception ign ) { }
		}
		return def;
	}

	
	/* deprecated */
	
	/** @return parseInt() on success, 'def' otherwise */
	@Deprecated
	public static int i( String s, int def )
	{
		try { return Integer.parseInt( s );	} catch( Exception ign ) { return def; } 
	}

	/** @return parseInt() on success, '0' otherwise */
	@Deprecated
	public static int i( String s )	{ return i( s, 0 ); }
	
	/** @return parseInt() on success, 'def' otherwise */
	@Deprecated
	public static int toInt( String s, int def )
	{
		try { return Integer.parseInt( s );	} catch( Exception ign ) { return def; } 
	}

	/** @return parseInt() on success, '0' otherwise */
	@Deprecated
	public static int toInt( String s )	{ return toInt( s, 0 ); }

	/** @return parseLong() on success, 'def' otherwise */
	@Deprecated
	public static long toLong( String s, long def )
	{
		try { return Long.parseLong( s ); } catch( Exception ex ) { return def; }
	}
	
	
	/** @return parseLong() on success, '0' otherwise */
	@Deprecated
	public static long toLong( String s ) { return toLong( s, 0 ); }
	
	
	/** @return string representation of int */
	@Deprecated
	public static String str( int i ) { return Integer.toString(i); }
	
	/** @return string representation of long */
	@Deprecated
	public static String str( long l ) { return Long.toString(l); }
	
	/** @return toString() on success, "" otherwise */
	@Deprecated
	public static String str( Object o ) { return str( o, "" ); }

	/** @return toString() on success, 'def' otherwise */
	@Deprecated
	public static String str( Object o, String def )
	{
		if( o != null )	try { return o.toString(); } catch( Exception ign ) { } return def;
	}

	/** @return string representation of int */
	@Deprecated
	public static String s( int i ) { return Integer.toString(i); }
	
	/** @return string representation of long */
	@Deprecated
	public static String s( long l ) { return Long.toString(l); }
	
	/** @return toString() on success, "" otherwise */
	@Deprecated	
	public static String s( Object o ) { return s( o, "" ); }

	/** @return toString() on success, 'def' otherwise */
	@Deprecated
	public static String s( Object o, String def )
	{
		if( o != null )	try { return o.toString(); } catch( Exception ign ) { } return def;
	}

	/* - - - */
	

	public static boolean is_empty( String s )
	{
		return ( s == null || pattern_emptystr.matcher(s).matches() )? true: false;
	}

	@Deprecated
	public static boolean isEmpty( String s )
	{
		return ( s == null || pattern_emptystr.matcher(s).matches() )? true: false;
	}
	
	public static final char[] HEX_DIGITS = new char[] 
	{ 
		'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f' 
	};
	
	/** makes hex string (lowercase) of the byte array */
	public static String hex( byte[] b )
	{
		if( b == null ) return null;
		StringBuilder s = new StringBuilder( b.length*2 );
		for( byte d: b ) s.append( HEX_DIGITS[(d >> 4) & 0x0F] ).append( HEX_DIGITS[d & 0x0F]);
		return s.toString();
	}

	/** makes hex string (lowercase) of byte value */
	public static String hex( byte b )
	{
		StringBuilder s = new StringBuilder( 2 );
		s.append( HEX_DIGITS[(b >> 4) & 0x0F] ).append( HEX_DIGITS[b & 0x0F]);
		return s.toString();
	}


	/** makes hex string (lowercase) of integer value */
	public static String hex( int val )
	{
		StringBuilder s = new StringBuilder( 8 );
		byte b;
		b = (byte)((val >> 24) & 0xFF);
		s.append( HEX_DIGITS[(b >> 4) & 0x0F] ).append( HEX_DIGITS[b & 0x0F]);
		b = (byte)((val >> 16) & 0xFF);
		s.append( HEX_DIGITS[(b >> 4) & 0x0F] ).append( HEX_DIGITS[b & 0x0F]);
		b = (byte)((val >>  8) & 0xFF);
		s.append( HEX_DIGITS[(b >> 4) & 0x0F] ).append( HEX_DIGITS[b & 0x0F]);
		b = (byte)((val      ) & 0xFF);
		s.append( HEX_DIGITS[(b >> 4) & 0x0F] ).append( HEX_DIGITS[b & 0x0F]);
		return s.toString();
	}
	

    /** capitalize first letter */
	public static String firstCap( String s ) 
	{
		if( s == null || s.length() == 0 ) return s;
		return ""+Character.toUpperCase( s.charAt(0) )+s.substring(1).toLowerCase();
	}
	

	/** windows-1251 encoding */
	public static final String ENC_WIN = "Cp1251";
	
	/** koi8-r encoding */
	public static final String ENC_KOI = "KOI8_R";
	
	/** UTF-8 encoding */
	public static final String ENC_UTF = "UTF-8";

	
	
	/** make BufferedReader on InputStream using codepage */
	public static BufferedReader streamReader( InputStream ins, String cp )
	{
		try { return new BufferedReader( new InputStreamReader( ins, (cp != null)? cp: ENC_WIN ) );	}
		catch( Exception ign ) { }
		return null;
	}

	/** make BufferedWriter on OutputStream using codepage */
	public static BufferedWriter streamWriter( OutputStream outs, String cp )
	{
		try { return new BufferedWriter( new OutputStreamWriter( outs, (cp != null)? cp: ENC_WIN ) ); }
		catch( Exception ign ) { }
		return null;
	}
	
	/** make BufferedReader on File using codepage */
	public static BufferedReader fileReader( File f, String cp )
	{
		try { return streamReader( new FileInputStream( f ), cp ); } catch( Exception ign ) { }
		return null;
	}
	
	/** make BufferedWriter on File using codepage */
	public static BufferedWriter fileWriter( File f, String cp )
	{
		try { return streamWriter( new FileOutputStream( f ), cp ); } catch( Exception ign ) { }
		return null;
	}
	
	/** format date DD.MM.YYYY */
	public static String ddmmyyyy( long dt )
	{
		int[] dmy = int_dmy( dt );
		StringBuilder sb = new StringBuilder(10);
		if( dmy[0] < 10 ) sb.append( '0' ); sb.append( Integer.toString( dmy[0] ) ).append( '.' );
		if( dmy[1] < 9 ) sb.append( '0' ); sb.append( Integer.toString( dmy[1]+1 ) ).append( '.' );
		sb.append( Integer.toString( dmy[2] ) );
		return sb.toString();
	}

	/** format date DD.MM.YYYY */
	public static String ddmmyyyy( Date dt )
	{
		return (dt != null)? ddmmyyyy( dt.getTime() ) : null;
	}
	
	/** format date DD.MM.YY */
	public static String ddmmyy( long dt )
	{
		int[] dmy = int_dmy( dt );
		StringBuilder sb = new StringBuilder(10);
		if( dmy[0] < 10 ) sb.append( '0' ); sb.append( Integer.toString( dmy[0] ) ).append( '.' );
		if( dmy[1] < 9 ) sb.append( '0' ); sb.append( Integer.toString( dmy[1]+1 ) ).append( '.' );
		int y = dmy[2] % 100; if( y < 10 ) sb.append( '0' ); sb.append( Integer.toString( y ) );
		return sb.toString();
	}

	/** format date DD.MM.YY */
	public static String ddmmyy( Date dt )
	{
		return (dt != null)? ddmmyy( dt.getTime() ) : null;
	}
	
	/** format date HH:MM:SS */
	public static String hhmmss( long dt )
	{
		int[] hms = int_hms( dt );
		StringBuilder sb = new StringBuilder(8);
		if( hms[0] < 10 ) sb.append( '0' ); sb.append( Integer.toString( hms[0] ) ).append( ':' );
		if( hms[1] < 10 ) sb.append( '0' ); sb.append( Integer.toString( hms[1] ) ).append( ':' );
		if( hms[2] < 10 ) sb.append( '0' ); sb.append( Integer.toString( hms[2] ) );
		return sb.toString();
	}
	
	/** format date HH:MM:SS */
	public static String hhmmss( Date dt )
	{
		return (dt != null)? hhmmss( dt.getTime() ) : null;
	}

	/** format date HH:MM */
	public static String hhmm( long dt )
	{
		int[] hms = int_hms( dt );
		StringBuilder sb = new StringBuilder(5);
		if( hms[0] < 10 ) sb.append( '0' ); sb.append( Integer.toString( hms[0] ) ).append( ':' );
		if( hms[1] < 10 ) sb.append( '0' ); sb.append( Integer.toString( hms[1] ) );
		return sb.toString();
	}
	
	/** format date HH:MM */
	public static String hhmm( Date dt )
	{
		return (dt != null)? hhmm( dt.getTime() ) : null;
	}

	/** [day,month,year] day: 1-31, month: 0-11 */
	public static int[] int_dmy( long dt )
	{
		Calendar cal = new GregorianCalendar();	cal.setTimeInMillis( dt );
		return new int[] {
			cal.get( Calendar.DATE ), cal.get( Calendar.MONTH ), cal.get( Calendar.YEAR )
		};
	}
	
	/** [hour,min,sec] */
	public static int[] int_hms( long dt )
	{
		Calendar cal = new GregorianCalendar();	cal.setTimeInMillis( dt );
		return new int[] {
			cal.get( Calendar.HOUR_OF_DAY ), cal.get( Calendar.MINUTE ), cal.get( Calendar.SECOND )
		};
	}	

	/** @return currentTimeMillis() */
	public static long now() { return System.currentTimeMillis(); }

	/** parse group of three delimited ints into date value (millis) */
	public static long parseDate( String s )
	{
		try {
			String[] v = s.trim().split( "[ ,\\.\\-/]", 3 );

			if( v.length != 3 ) return 0;

			int d = Integer.parseInt( v[0] );
			int m = Integer.parseInt( v[1] );
			int y = Integer.parseInt( v[2] );

			if( d < 1 || d > 31 || m < 1 || m > 12 ) return 0;
			if( y < 0 || y > 2100 ) return 0;

			if( y < 30 ) y += 2000; else if( y < 100 ) y += 1900;

			return (new GregorianCalendar( y, m-1, d )).getTimeInMillis();
		}
		catch( Exception ignore ) { }
		return 0;
	}
	
	/**
	 * calls parseCSV( line, ';' )
	 * @param line
	 * @return	
	 */
	public static List<String> parseCSV( String line ) { return parseCSV( line, ';' ); }
	
	/**
	 * 
	 * @param line		CSV formatted string
	 * @param delimiter CSV delimiter
	 * @return CSV field string list or null on format error
	 */
	public static List<String> parseCSV( String line, char delimiter )
	{
		if( line == null ) return null;
		
		List<String> res = new LinkedList<String>();
		int pos = 0;
		while( true ) 
		{
			if( pos >= line.length() ) break;
			StringBuffer v = new StringBuffer(50);
		
			char c = line.charAt( pos++ );
		
			if( c == '"' ) {
				// state: parse quoted
				c = line.charAt( pos++ );
				while( true ) 
				{
					if( c == '"' ) {
						if( pos >= line.length() ) { res.add( v.toString() ); break; }
						else {
							c = line.charAt( pos++ );
							if( c == '"' ) 
							{ 
								v.append( '"' ); 
								if( pos >= line.length() ) return null;	// double qoute at the line end
								c = line.charAt( pos++ ); 
							}
							else 
								if( c == delimiter ) { res.add( v.toString() ); break; }
								else return null; // format error
						}
					}
					else {
						if( pos >= line.length() ) return null; // format error 						
						v.append( c ); 
						c = line.charAt( pos++ ); 
					}
				} // while
			}
			else {
				// state: non quoted
				while( true ) {
					if( c == '"' ) return null; // format error
					else if( c == delimiter ) { res.add( v.toString() ); break; }
					else v.append( c );
				
					if( pos >= line.length() ) { res.add( v.toString() ); break; }
				
					c = line.charAt( pos++ );
				}
			} // choose field type
		}
		
		return res; 
	}

	
	/**
	 * @param initial - array of string pairs
	 * @return (key,value) string map populated by initial data
	 */
	public static HashMap<String,String> stringMap( String[][] initial )
	{
		HashMap<String,String> res = new HashMap<String,String>(initial.length);
		for( String[] s: initial ) res.put( s[0], s[1] );
		return res;
	}
	
}

// eof
