package mxl2.inode;

public class Ivar 
{
	public enum TYPE 
	{ 
		INT(  1, Inode.TBL_INODE ), 
		STR(  2, Inode.TBL_VSTR  ), 
		TIME( 3, Inode.TBL_VTIME ), 
		BIN(  4, Inode.TBL_VBIN  ), 
		IMG(  5, Inode.TBL_VIMG  );

		final int idx; final String tbl;
		
		TYPE( int idx, String tbl ) { this.idx = idx; this.tbl = tbl; }
		public int getIdx() { return idx; }
		public String getTbl() { return tbl; }
	};
	
	public enum IMGTYPE { JPEG, PNG, GIF };

	boolean	modified;
	Inode	inode;
	
	int		name;
	TYPE	type;
	int		ord;
	
	int		ival;
	long	lval;
	String	sval;
	byte[]	bin;
	int		imgWidth, imgHeight;
	IMGTYPE	imgType;
	
	// public void modify() { this.modified = true; }
	
	public String s() 
	{
		switch( type )
		{
			case STR: return sval;
			case INT: return Integer.toString( ival );
		}
		return null;
	}
	
	public int  i() { return i(0); }
	public int  i( int def ) { if( type == TYPE.INT ) return ival; else return def; }
	public long t() { if( type == TYPE.TIME ) return lval; else return 0; }
	public byte[] b() { if( type == TYPE.BIN ) return bin; else return null; }

	public int  name() { return name; } 
	public TYPE type() { return type; }
	
	public Ivar( int name, TYPE type ) { this.name = name; this.type = type; }
	
	public void setOrd( int ord )  { this.ord = ord; modified = true; }
	public void setInt( int ival ) { this.ival = ival; modified = true; }
	public void setStr( String s ) { this.sval = s; modified = true; }
	public void setTime( long t )  { this.lval = t; modified = true; }
	public void setBin( byte[] b)  { this.bin = b; modified = true; }
	public void setImg( IMGTYPE it, int w, int h, byte[] pic ) 
	{ 
		this.imgWidth = w; this.imgHeight = h; this.imgType = it; this.bin = pic; modified = true; 
	}
	
}

// eof