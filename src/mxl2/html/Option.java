/*
 * Option.java
 *
 */

package mxl2.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mxl2.Html;

/**
 *	generic html 'option' tag
 */
public class Option extends Tag
{
	public String val;
	public String txt;
	public boolean sel;

	
	public Option( String val, String txt, boolean sel )
	{
		super( "option", false );
		this.val = val; this.txt = txt; this.sel = sel;
	}
	
	public Option( String val, String txt )	{ this( val, txt, false ); }
	public Option( String val )	{ this( val, val, false ); }
	
	public static List<Option> makeList( Collection<?> val, String selOpt )
	{
		if( val == null || val.size() == 0 ) return new ArrayList<Option>();
		
		List<Option> res = new ArrayList<Option>( val.size() );
		for( Object obj: val )
		{
			if( obj != null )
				try {
					String a = null, b = null;
					if( obj instanceof Object[] )
					{
						Object[] arr = (Object[])obj;
						if( arr.length > 0 ) a = arr[0].toString();
						if( arr.length > 1 ) b = arr[1].toString(); else b = a;
					}
					else {
						a = b = obj.toString();
					}
					if( a != null && b != null )
						res.add( new Option( Html.q(a), Html.q(b), (selOpt!=null && selOpt.equals(a)) ) );
				}
				catch( Exception ign ) { 
				}
		}
		return res;
	}
	
	public String html()
	{
		this.ext = "value=\""+this.val+"\"";
		if( sel ) this.ext = this.ext + " selected=\"1\"";
		return start() +this.txt+ end();
	}
	
}

// eof
