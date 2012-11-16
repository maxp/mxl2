/*
 * Tag.java
 *
 */

package mxl2.html;

/**
 *	base html tag class
 */
public abstract class Tag
{
	public final String tag;
	public final boolean single;
	
	public String name;
	public String id;
	public String cls;
	public String sty;
	public String ext;
	
	public Tag( String tag, boolean single )
	{
		this.tag = tag; this.single = single;
	}
	
	public Tag( String tag, boolean single, String name, String id, String cls, String sty, String ext )
	{
		this.tag = tag; this.single = single;
		
		this.name = name;
		this.id   = id;
		this.cls  = cls;
		this.sty  = sty;
		this.ext  = ext;
	}
	
	public String start()
	{
		StringBuilder sb = new StringBuilder(10);
		sb.append( '<' ).append( tag );
		if( name != null ) sb.append( " name=\""  ).append( name ).append( '"' );
		if( id   != null ) sb.append( " id=\""    ).append( id   ).append( '"' );
		if( cls  != null ) sb.append( " class=\"" ).append( cls  ).append( '"' );
		if( sty  != null ) sb.append( " style=\"" ).append( sty  ).append( '"' );
		if( ext  != null ) sb.append( ' ' ).append( ext );
		if( single ) sb.append( "/>" ); else sb.append( '>' );
		return sb.toString();
	}
	
	public String end() 
	{ 
		return (single)?"":"</"+tag+">"; 
	}
	
	public String html()
	{
		return (single)? start(): start()+end();
	}
	
}

// eof
