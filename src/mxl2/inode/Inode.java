package mxl2.inode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class Inode 
{
	static final String TBL_ITYPE	= "itype";
	static final String TBL_INODE	= "inode";
	static final String TBL_IREF	= "iref";
	static final String TBL_VINT	= "ivint";
	static final String TBL_VSTR	= "ivstr";
	static final String TBL_VIMG	= "ivimg";
	static final String TBL_VBIN	= "ivbin";
	static final String TBL_VTIME	= "ivtime";
	
	static final Logger L = Logger.getLogger( Inode.class.getName() );
	
	private Connection conn;
	
	int 	id, cls, parent, ord;
	long	ctime, mtime;
	boolean	publ;
	
	protected Inode( Connection conn, int id, int cls, int parent, int ord, long ctime, long mtime, boolean publ )
	{
		this.conn = conn;
		this.id = id; this.cls = cls; this.parent = parent; this.ord = ord;  
		this.ctime = ctime; this.mtime = mtime; this.publ = publ;
	}
	
	public Inode( Connection conn, ResultSet rs ) throws SQLException
	{
		this.conn = conn;
		this.id = rs.getInt(1); this.cls = rs.getInt(2); this.parent = rs.getInt(3); this.ord = rs.getInt(4);
		this.ctime = rs.getTimestamp(5).getTime(); this.mtime = rs.getTimestamp(6).getTime();
		this.publ = rs.getBoolean(7);
	}
	
	static final String INODE_FIELDS  = "id,cls,parent,ord,ctime,mtime,publ";
	static final String INODE_FIELDS4 = "id,cls,parent,ord";
	
	public static Inode create( Connection conn, int cls, int parent, int ord )
	{
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement( "select nextval('"+TBL_INODE+"_id_seq')" );
			ResultSet rs = st.executeQuery(); rs.next(); int id = rs.getInt(1); rs.close();

			st = conn.prepareStatement( "insert into "+TBL_INODE+"("+INODE_FIELDS4+") values(?,?,?,?)" );
			st.setInt(1,id); st.setInt(2,cls); st.setInt(3,parent); st.setInt(4,ord);
			if( st.executeUpdate() == 1 ) 
				return new Inode( conn, id, cls, parent, ord, 0, 0, false );
		}
		catch( SQLException ex ) { L.warning( "inode create failed: "+ex.toString() ); }
		finally{ try { st.close(); } catch( Exception ign ) { } }
		return null;
	}
	
	public static Inode load( Connection conn, int id )
	{
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement( "select "+INODE_FIELDS+" from "+TBL_INODE+" where id = ?" );
			st.setInt( 1, id );
			ResultSet rs = st.executeQuery();
			if( rs.next() ) return new Inode( conn, rs ); 
		}
		catch( SQLException ex ) { L.warning( ex.toString() ); }
		finally{ try { st.close(); } catch( Exception ign ) { } }
		return null;
	}
	
	public static List<Inode> children( Connection conn, int parentId, int cls )
	{
		List<Inode> res = new LinkedList<Inode>();
		PreparedStatement st = null;
		try {
			String q = "select "+INODE_FIELDS+" from "+TBL_INODE+" where parent=?";
			if( cls != 0 ) q += " and cls=?";
			st = conn.prepareStatement( q );
			st.setInt( 1, parentId ); if( cls != 0 ) st.setInt( 2, cls );
			ResultSet rs = st.executeQuery();
			while( rs.next() ) res.add( new Inode( conn, rs ) );
		}
		catch( SQLException ex ) { L.warning( ex.toString() ); }
		finally{ try { st.close(); } catch( Exception ign ) { } }
		return res;
	}
	
	public int getId()     { return id; }
	public int getCls()    { return cls; }
	public int getParent() { return parent; }
	public int getOrd()    { return ord; }
	public long getCtime() { return ctime; } 
	public long getMtime() { return mtime; }
	public boolean getPubl() { return publ; }
	
	public void setCls( int cls ) { this.cls = cls; }
	public void setParent( int parent ) { this.parent = parent; }
	public void setOrd( int ord ) { this.ord = ord; }
	public void setPubl( boolean publ ) { this.publ = publ; }

	static final String SQL_INODE_UPD = 
		"update "+TBL_INODE+" set cls=?, parent=?, ord=?, mtime=CURRENT_TIMESTAMP, publ=? where id=?";
	
	public boolean update()
	{
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement( SQL_INODE_UPD );
			st.setInt( 1, cls ); st.setInt( 2, parent ); st.setInt( 3, ord ); 
			st.setBoolean( 4, publ ); st.setInt( 5, id );
			return (st.executeUpdate() == 1);
		}
		catch( SQLException ex ) { L.warning( "inode["+id+"] update failed: "+ex.toString() ); }
		finally{ try { st.close(); } catch( Exception ign ) { } }
		return false;
	}
	
	public Ivar getVar( int name )
	{
		return null;
	}
	
	public List<Ivar> listVars( int name ) 
	{
		return null;
	}
	
	public Ivar addVar( int name, int ord, int val )
	{
		return null;
	}

	public Ivar addVar( int name, int ord, String val )
	{
		return null;
	}

	public Ivar addVar( int name, int ord, long val )
	{
		return null;
	}

	public Ivar addVar( int name, int ord, byte[] val )
	{
		return null;
	}
	
	public Ivar addVar( int name, int ord, int pictype, int width, int height, byte[] pic )
	{
		return null;
	}
	
	public void link( Inode dst, int cls, int ord )
	{
		// insert into iref ( src, dst, cls, ord ) values ( this.getId(), dst.getId(), cls, ord ); 
	}

	/**
	 * @return reference inode list, forward or back
	 */
	public List<Inode> refNodes( int cls, boolean forward )
	{
		List<Inode> res = new LinkedList<Inode>();
		PreparedStatement st = null;
		try {	
			st = conn.prepareStatement(
				"select "+INODE_FIELDS+" from "+TBL_INODE+" i join "+TBL_IREF+" r on " 
				+(forward? "(r.src = i.id)": "(r.dst = i.id)")
				+((cls != 0)? " where cls=?":"")
				+" order by r.ord"					
			);
			if( cls != 0 ) st.setInt( 1, cls );
			ResultSet rs = st.executeQuery();
			while( rs.next() ) res.add( new Inode( conn, rs ) );
		}
		catch( SQLException ex ) { L.warning( ex.toString() ); }
		finally{ try { st.close(); } catch( Exception ign ) { } }
		return res;
	}

	/**
	 * @return forward reference inode list
	 */
	public List<Inode> links( int cls ) { return refNodes( cls, true ); }
	
	/**
	 * @return back reference inode list
	 */
	public List<Inode> refers( int cls ) {return refNodes( cls, false ); }
	
}

// eof

