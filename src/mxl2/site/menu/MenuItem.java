package mxl2.site.menu;

public class MenuItem 
{
	public String	id;
	public int		level;
	public String	url;
	public String	title;

	public MenuItem parent, prev, next, firstchild, lastchild;
	
	public MenuItem( String id, int level, String url, String title )
	{
		this.id = id; this.level = level; this.url = url; this.title = title;
	}
	
	public void addChild( MenuItem mi )
	{
		mi.parent = this;
		if( firstchild == null ) { firstchild = mi; lastchild = mi; }
		else { lastchild.next = mi; mi.prev = lastchild; lastchild = mi; }
	}
	
}

// eof
