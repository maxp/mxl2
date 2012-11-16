package mxl2.site;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Lmenu
{
	static final Logger L = Logger.getLogger( Lmenu.class.getName() );
	
	public class Item 
	{ 
		public String id, url, text;
		public Item( String id, String url, String text ) { this.id = id; this.url = url; this.text = text; }
	};

	public String id;
	public String url;
	public String title;
	public List<Item> items;

	/**
	 * DEPRECATED
	 * @param id - menu name
	 * @param url - menu name href
	 * @param title - menu title text
	 */
	public Lmenu( String id, String url, String title ) 
	{
		this.id = id; 
		this.url = url; 
		this.title = title;
		this.items = new ArrayList<Item>(10);
	}

	/**
	 * build lmenu map from conf file
	 * @param rd
	 * @return lmenu map
	 */
	public static Map<String,Lmenu> load( BufferedReader rd )
	{
		Map<String,Lmenu> lmap = new HashMap<String,Lmenu>();
		try {
			Lmenu lm = null;

			int line = 0;
			String s;
			while( (s = rd.readLine()) != null )
			{
				line++;

				s = s.trim();
				if( s.length() == 0 || s.charAt(0) == '#' ) 
				{ 
					// skip comments
				}
				else if( s.charAt(0) == '.' )
				{
					// parse menu_title
					String[] a = s.substring(1).split( "\\s+", 3 );
					if( a.length == 3 ) 
					{
						lm = new Lmenu( a[0], a[1], a[2] );
						lmap.put( lm.id, lm );
					}
					else {
						L.warning( "lmenu.conf["+line+"] incorrect format" );
						lm = null;
					}
				}
				else {
					String[] a = s.split( "\\s+", 3 );
					if( a.length == 3 ) 
					{ 
						if( lm != null )
							lm.items.add( lm.new Item( a[0], a[1], a[2] ) );								 
						else
							L.info( "lmenu.conf["+line+"] discarded" );
					}
					else {
						L.info( "lmenu.conf["+line+"] incorrect format" );
					}
				}
			} // while

		}
		catch( Exception ex ) {	L.warning( ex.toString() ); }
		finally { try { rd.close(); } catch( Exception ign ) { } }
		return lmap;
	}
	
}

// eof
