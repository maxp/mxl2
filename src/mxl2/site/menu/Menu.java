package mxl2.site.menu;

import java.io.BufferedReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import mxl2.Lib;

public class Menu 
{
	static final int LINE_SIZE_MAX = 4096;
	
	static final Logger L = Logger.getLogger( Menu.class.getName() );
	
	MenuItem root;
	HashMap<String,MenuItem> itemMap;
	
	class ItemState { 
		MenuWriter.State state; MenuItem item;  
		ItemState( MenuWriter.State state, MenuItem item ) { this.state = state; this.item = item; }
	}
	
	public Menu()
	{
		this.root = new MenuItem( "main", -1, "-", "-" );
		this.root.parent = this.root;
		this.itemMap = new HashMap<String,MenuItem>(22);
		this.itemMap.put( this.root.id, this.root );
	}
	
	/**
	 * read config file line by line, build menu tree
	 * @param rd
	 */
	public Menu( BufferedReader rd )
	{
		this();
		
		MenuItem cit = this.root;
		int lineNumber = 0;
		try {
			String s;
			while( (s = rd.readLine()) != null )
			{
				lineNumber++;
				if( s.length() > LINE_SIZE_MAX ) { 
					L.warning( "line too long:"+lineNumber ); continue; 
				}
				if( Lib.is_empty(s) || s.charAt(0) == '#' ) continue;
				
				String[] v = Lib.pattern_wspc.split( s, 3 ); 
				if( v.length != 3 || v[0].charAt(0) != '.' ) {
					L.info( "wrong line format:"+lineNumber ); continue;
				}
				
				String id = v[0];
				int dotnum = 0; 
				while( id.length() > dotnum && id.charAt(dotnum) == '.' ) { dotnum++; }
				id = (dotnum < id.length())? id.substring(dotnum): "";
				dotnum--;
				
				MenuItem mi;				
				if( dotnum == cit.level )
				{
					mi = new MenuItem( (cit.level <= 0)? id: cit.parent.id+"."+id, dotnum, v[1], v[2].trim() );
					cit.parent.addChild( mi );
				}
				else if( dotnum > cit.level ) 
				{
					mi = new MenuItem( (cit.level <0)? id: cit.id+"."+id, dotnum, v[1], v[2].trim() );
					cit.addChild( mi );
				}
				else {
					while( dotnum < cit.level && cit.level > 0 ) cit = cit.parent;
					String iid = (cit.level <= 0)? id: cit.parent.id+"."+id;
					mi = new MenuItem( iid, dotnum, v[1], v[2].trim() );
					cit.parent.addChild( mi );
				}
				itemMap.put( mi.id, mi );					
				cit = mi;
			}
		}
		catch( Exception ex ) { 
			L.warning( "exception line:"+lineNumber+ex.toString() ); 
		}
	}
	
	public boolean print( MenuWriter r, String curr )
	{
		MenuItem ci = itemMap.get( (curr == null)? "": curr.trim() ); 
		if( ci == null ) { return r.printEmpty(); }

		if( !r.printHdr() ) return false;
		
		LinkedList<ItemState> its = new LinkedList<ItemState>();
		for( MenuItem i = ci; i.level >= 0; ) 
		{
			MenuWriter.State st;
			if( i.prev != null ) { 
				i = i.prev; st = MenuWriter.State.NORMAL; 
			} 
			else { 
				i = i.parent; st = MenuWriter.State.OPEN; 
			} 
			its.add( 0, new ItemState( st, i ) );
		}
		for( ItemState st: its ) if( !r.printLine( st.state, st.item ) ) return false;
		
		if( !r.printLine( MenuWriter.State.ACTIVE, ci ) ) return false;
		for( MenuItem i = ci.firstchild; i != null; i = i.next ) 
			if(!r.printLine( MenuWriter.State.NORMAL, i ) ) return false;

		ci = (ci.next != null)? ci.next: ci.parent.next; 
		while( ci != null && ci.level >= 0 ) 
		{
			if( !r.printLine( MenuWriter.State.NORMAL, ci ) ) return false;
			ci = (ci.next != null)? ci.next: ci.parent.next; 
		}

		if( !r.printFtr() ) return false;

		return true;
	}
	
	public static MenuWriter writer( Writer out ) { return new SimpleWriter( out ); }
	
}

// eof
