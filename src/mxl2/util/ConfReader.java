package mxl2.util;

import java.io.BufferedReader;
import java.io.File;

import mxl2.Lib;

public class ConfReader 
{
	static final int DEFAULT_INTERVAL = 5000;	// 5 seconds
	
	long lastCheck;
	long lastModified;
	long checkInterval;

	File file;
	String encoding;
	
	/**
	 * @param file config file name
	 * @param encoding config file encoding
	 * constructor sets default check interval
	 */
	public ConfReader( String file, String encoding ) { this( file, encoding, DEFAULT_INTERVAL ); } 
	
	/**
	 * @param file config file name
	 * @param encoding config file encoding
	 * @param checkInterval check interval
	 */
	public ConfReader( String file, String encoding, long checkInterval )
	{
		this.lastCheck = 0;
		this.lastModified = 0;
		this.checkInterval = checkInterval;
		this.file = new File( file );
		this.encoding = encoding;
	}
	
	/**
	 * @return config File 
	 */
	public File getFile() { return file; }
	
	/**
	 * @return config file name
	 */
	public String getFileName() { if( file == null ) return null; else return file.getName(); }
	
	/**
	 * @return true if file timestamp was changed
	 */
	public boolean changed()
	{
		long now = Lib.now();
		if( now < lastCheck+checkInterval ) return false;
		lastCheck = now;
		long lm = file.lastModified();
		if( lm != lastModified ) { lastModified = lm; return true; }
		return false;
	}
	
	/**
	 * @return file reader
	 */
	public BufferedReader reader() 
	{ 
		return Lib.fileReader( file, encoding ); 
	}
	
}

// eof
