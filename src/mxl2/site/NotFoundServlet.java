package mxl2.site;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * empty stub to generate HTTP response 404 - Not Found
 */
public class NotFoundServlet extends HttpServlet
{
	private static final long serialVersionUID = 4122614278150978870L;

	public void doGet( HttpServletRequest req, HttpServletResponse resp ) throws IOException
	{
		resp.sendError( HttpServletResponse.SC_NOT_FOUND );
	}
}

// eof
