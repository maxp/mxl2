/*
 * Select.java
 *
 */

package mxl2.html;

import java.util.List;

/**
 *	'select' tag implementation
 */
public class Select extends Tag
{
	static final String CLS = "sel";
	
	public int size;
	public boolean multi;
	public List<Option> options;

	public Select( String name ) { this( name, 1, false ); }
	
	public Select( String name, int size, boolean multi )
	{
		super( "select", false );
		this.name = name; this.size = size; this.multi = multi;
		ext = "size=\""+size+"\""+( (multi)?" multiple=\"1\"":"");
		cls = CLS;
	}
	
	public String html()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append( start() );
		if( options != null ) for( Option opt: options ) sb.append( opt.html() );
		sb.append( end() );
		return sb.toString();
	}
	
	public String getSelectedValue()
	{
		if( options != null ) {
			for( Option opt: options ) { if( opt.sel ) return opt.val; }
		}
		return "";
	}
	
	public void setSelected( String val )
	{
		if( options != null && val != null ) 
		{
			for( Option opt: options ) opt.sel = val.equals( opt.val );
		}
	}
	
}

// eof
