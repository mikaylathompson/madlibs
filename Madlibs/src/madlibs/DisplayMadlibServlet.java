package madlibs;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DisplayMadlibServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		
		outHeader( resp );
		
		String fileName = req.getParameter("file");
		
		File file = new File(fileName);
		
		if ( file.exists() ) {
			Scanner scan = new Scanner(file);
			fillPlaceHolders(scan, req, resp);
		} else {
			resp.getWriter().println("File does not exist.");
		}
		outFooter( resp );
	} // end of doGet
	
	// scan file to find all placeholders
	public static void fillPlaceHolders(Scanner scanTemplate, 
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		int startPH = 0; int endPH; int endPrevPH;
		int phIndex = 1;
		while (scanTemplate.hasNextLine()) {
			endPH = 0;
			endPrevPH = 0;
			resp.getWriter().println();
			String line = scanTemplate.nextLine();
			while ((endPrevPH + 2 < line.length()) && (line.substring(endPrevPH).contains("<"))) {
				String partLine = line.substring(endPrevPH);
				if (partLine.contains("<") && 											//the line has a <
						(partLine.substring(partLine.indexOf("<")).contains(">")) && 		//followed by a >
						(partLine.substring(partLine.indexOf("<")).indexOf(">") > 1) &&		//with at least one character
						!(partLine.substring(partLine.indexOf("<"), (partLine.indexOf("<") + 	//but no spaces in between them
								partLine.substring(partLine.indexOf("<")).indexOf(">"))).contains(" "))) {
					//find start/finish index of next placeholder
					
					startPH = partLine.indexOf("<");
					endPH = startPH + partLine.substring(partLine.indexOf("<")).indexOf(">");
					
					if (partLine.substring(0, 1).equals(">")) {
						resp.getWriter().print(partLine.substring(1, startPH));
					} else {
						resp.getWriter().print(partLine.substring(0, startPH));
					}
					
					resp.getWriter().print(req.getParameter("ph" + phIndex));
					phIndex++;
									

					endPrevPH += endPH;
				} //end if contains <, >, !<>, !<a b>
			} //end while the remainder of the line has a <
			if (line.substring(endPrevPH).length() > 2) {
				if (endPrevPH == 0) {
					resp.getWriter().print(line);
				} else {
					resp.getWriter().print(line.substring(endPrevPH + 1));
				}
			} //end if length
		}//end while hasNextLine
		
		
		
	}

	public static void outHeader(HttpServletResponse resp) throws IOException {
		String head = "<head>\n<style type=\"text/css\">\nbody { background-color: #DDDDDD }" +
			"h1 { font-family: Outage, Verdana, sans-serif; src: url('Outage.ttf'); text-" +
			"align: center; padding-top: 50px; line-height: 50%; font-size: 10em; color: " +
			"#666666; letter-spacing: 10px; height: 0em; }\npre { position: relative; left:" +
			" 10em; font-family: Helvetica, sans -serif; font-size: 1.5em; line-height: " +
			"150%; color: #333333; }\na { font-weight: bold; color: #006699;}\n</style>\n</head>";
		
		resp.getWriter().println("<html>");
		resp.getWriter().println(head);
		resp.getWriter().println("<body>");
		resp.getWriter().println("<h1>STORY</h1>");
		resp.getWriter().println("<pre>"); // pre means the format is predtermined
	}
	
	public static void outLine(HttpServletResponse resp, String line, String value) throws IOException {
		resp.getWriter().println(line + ": " + value);
					
	}
	
	public static void outFooter(HttpServletResponse resp) throws IOException {
		resp.getWriter().println("<br><br><a href=\"index.html\">Back</a>");
		resp.getWriter().println("</pre>");
		resp.getWriter().println("</body>");
		resp.getWriter().println("</html>");
	}
}
