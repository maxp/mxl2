package mxl2.inode;

public class Itype 
{
	public static enum Prim
	{
		INODE( Inode.TBL_INODE ), 
		IREF ( Inode.TBL_IREF  ), 
		VINT ( Inode.TBL_VINT  ), 
		VSTR ( Inode.TBL_VSTR  ), 
		VIMG ( Inode.TBL_VIMG  ), 
		VBIN ( Inode.TBL_VBIN  ), 
		VTIME( Inode.TBL_VTIME );
		
		Prim( String tbl ) { this.tbl = tbl; }
		public final String tbl;
	};
	
	public final int	id;
	public final Prim	prim;
	public final String	name;
	public final String	descr;
	
	public Itype( int id, Prim prim, String name, String descr ) 
	{
		this.id = id; this.prim = prim; this.name = name; this.descr = descr;
	}
	
	
	
}

// eof
