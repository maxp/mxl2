/*
 * FileWatch.java
 */

package mxl2.util;

import java.io.File;
import mxl2.Lib;

/**
 *	check file lastModified timestamp
 */
public class FileWatch
{
	static final long DEFAULT_INTERVAL = 3000;	// 3 seconds
	
	long lastCheck;
	long lastModified;
	long interval;
	
	File file;

	public FileWatch( String fn ) { this( fn, DEFAULT_INTERVAL ); }
	
	public FileWatch( String fn, long interval )
	{
		lastCheck = 0;
		file = new File( fn );
		lastModified = file.lastModified();
		this.interval = interval;
	}
	
	public File getFile() { return file; }
	
	public boolean updated()
	{
		long now = Lib.now();
		if( now < lastCheck+interval ) return false;
		lastCheck = now;
		long lm = file.lastModified();
		if( lm != lastModified ) { lastModified = lm; return true; }
		return false;
	}
	
}

// eof
