//
//	simple smtp sender
//

package mxl2.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;


public class SmtpSender
{
	String host;
	String helo = "localhost";
	int port = 25;
	
	String charset = "utf-8";
	
	private Socket sock;
	private BufferedReader in;
	private OutputStream  out;
	
	public SmtpSender( String host )
	{
		this.host = host;
	}
	
	/**
	 * smtp send implementation
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param body
	 * @return
	 */
	public boolean sendMail( String from, String to, String subject, String body )
	{
		boolean rc = false;
		try {
			connect();
			sendCmd( "HELO", helo );
			sendCmd( "MAIL", "FROM:"+from );
			sendCmd( "RCPT", "TO:"+to );
			sendCmd( "DATA", "" );
			sendHdr( from, to, subject, charset );
			sendMsg( body );
			
			String s = in.readLine();
			rc = s.charAt(0) == '2' || s.charAt(0) == '3';
			
			sendCmd( "QUIT", "" );
		}
		catch( Exception ex ) {
			System.err.println( ex.getMessage() );
		}
		
		try { out.flush(); out.close(); in.close(); sock.close(); }
		catch( Exception ex ) {} 
		
		return rc;
	}

	void connect() throws Exception, IOException
	{
		sock = new Socket( host, port );
		in  = new BufferedReader( new InputStreamReader( sock.getInputStream() ) );
		out = sock.getOutputStream();
		
		String s = in.readLine();
		if( s.charAt(0) == '2' || s.charAt(0) == '3' ) return;
		
		throw new Exception( "smtp connection failure" );
	}
	
	void sendLine( String l ) throws IOException
	{
		out.write( l.getBytes( charset ) );	out.write( 0x0D ); out.write( 0x0A );
	}
	
	void sendCmd( String cmd, String param ) throws Exception
	{
		out.write( cmd.getBytes() ); out.write( 0x20 );
		out.write( param.getBytes() ); out.write( 0x0D ); out.write( 0x0A );

		String s = in.readLine();
		if( s.charAt(0) == '2' || s.charAt(0) == '3' ) return;

		throw new Exception( "smtp sendCmd failed: "+cmd );
	}
	
	void sendHdr( String from, String to, String subject, String charset ) throws Exception
	{
		try {
			sendLine( "From: "+from );
			sendLine( "To: "+to );
			if( subject != null ) sendLine( "Subject: "+subject );
			sendLine( "Content-Type: text/plain; charset=\""+charset+"\"" );
			sendLine( "" );
		}
		catch( Exception ex ) {
			throw new Exception( "smtp sendHdr failed" );
		}
	}
	
	void sendMsg( String body ) throws Exception
	{
		String[] b = body.split( "\\r?\\n" );
		
		for( int i = 0; i < b.length; i++ ) {
			String s = b[i];
			if( s.length() > 0 && s.charAt(0) == '.' ) out.write( 0x2E );
			out.write( s.getBytes( charset ) );	out.write( 0x0D ); out.write( 0x0A );
		}
		out.write( 0x2E ); out.write( 0x0D ); out.write( 0x0A );
	}
}

//
//eof
