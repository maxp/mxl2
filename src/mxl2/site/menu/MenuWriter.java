package mxl2.site.menu;

public interface MenuWriter 
{
	public enum State { DISABLED, NORMAL, OPEN, ACTIVE };
	
	public boolean printHdr();
	public boolean printLine( State state, MenuItem item );
	public boolean printFtr();
	public boolean printEmpty();
	
}

// eof
