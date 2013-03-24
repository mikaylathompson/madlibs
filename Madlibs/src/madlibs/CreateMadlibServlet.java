package madlibs;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")


public class CreateMadlibServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		doGet(req, resp);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// web response: set the content type to be text/html
		resp.setContentType("text/html");

//		resp.setHeader("style", head);
		
		// get the file info
		// to get the value of each input, always use the name and then
		// req.getParameter(name);
		String fileName = req.getParameter("file");
		
		// create the file
		File file = new File(fileName);
		
		// check if file (template) exists
		if ( file.exists() ) {
			
			
			// output HTML Form header
			formHeader( resp );
			
			// Output a Form element to remember file (template) name
			formElement(resp, "", "hidden", "file", fileName);
			
			Scanner scan = new Scanner(file);
			genPlaceHolders(scan, resp);
			
			// the last element in the HTML Form is submit.
			formElement(resp, "", "mybutton", "submit", "display", "SUBMIT");
			
			formFooter( resp );
			
		} else {
			resp.getWriter().printf("Sorry, file %s does not exist.\n", file);
		}
	} // end of doPost
	
	// Scan file to find all placeholders
	public static void genPlaceHolders(Scanner scanTemplate, HttpServletResponse resp) 
              throws IOException {
	
		int phIndex = 1;
		int startPH = 0; int endPH = 0; phIndex = 1;
		while (scanTemplate.hasNextLine()) {
			endPH = 0;
			String line = scanTemplate.nextLine();
			while ((endPH + 2 < line.length()) && (line.substring(endPH).contains("<"))) {
				line = line.substring(endPH);
				if (line.contains("<") && 											//the line has a <
						(line.substring(line.indexOf("<")).contains(">")) && 		//followed by a >
						(line.substring(line.indexOf("<")).indexOf(">") > 1) &&		//with at least one character
						!(line.substring(line.indexOf("<"), (line.indexOf("<") + 	//but no spaces in between them
								line.substring(line.indexOf("<")).indexOf(">"))).contains(" "))) {
					//find start/finish index of next placeholder
					startPH = line.indexOf("<");
					endPH = startPH + line.substring(line.indexOf("<")).indexOf(">");

					//From the in-between space, make ithPH (all lowercase, replace - with " ")
					String ithPH = line.substring(startPH + 1, endPH);
					ithPH = ithPH.toLowerCase();
					ithPH = ithPH.replace('-', ' ');

					//Create a form element for this word:
					formElement(resp, ithPH + ": ", "text", "ph" + phIndex, "");
					phIndex++;
					
				} //end if contains <, >, !<>, !<a b>
			} //end if the remainder of the line has a <
		}//end while hasNextLine
	
	
	
	}
	
	public static void formHeader(HttpServletResponse resp) throws IOException {
		String head = "<head>\n<style type=\"text/css\">\nbody {\nbackground-color: #DDDDDD" +
				"}\nh1 {\nfont-family: Outage, Verdana, sans-serif;\nsrc: url('Outage.ttf');\ntext" +
				"-align: center;\npadding-top: 50px;\nline-height: 50%;\nfont-size: 10em;" +
				"color: #666666;\nletter-spacing: 10px;\nheight: 0em;\n}" + 
				"td { width: 50%; font-family: Helvetica, sans-serif; font-size: 1.5em; " +
				"line-height: 150%; text-align: right; color: #333333; }\ninput { vertical-align: "+
				"middle; width:50%; float:left; font-family: Helvetica, sans-serif; font-size: "+
				"1.5em; background-color:transparent; background-image:none; -webkit-appearance: " +
				"none; border: solid 2px #333333; box-shadow:none; }\ninput.mybutton { font-" +
				"family: Outage, Verdana, sans-serif; font-size: 1.5em; background-color: 006699; }" +
				"\n</style>\n</head>";
		resp.getWriter().println(head);
		resp.getWriter().println("<center>");
		resp.getWriter().println("<h1>WORDS</h1>");
		resp.getWriter().println("<form action=\"/display\" method=\"get\">");
		resp.getWriter().println("<table>");
	}
	
	public static void formElement(HttpServletResponse resp, String label, String type, String name, String value) throws IOException {
		resp.getWriter().printf("<div><tr><td>%s</td><td><input type=\"%s\" name=\"%s\" size =\"40\" value=\"%s\"></td></tr></div>\n",
					             label, type, name, value);
					
	}
	
	public static void formElement(HttpServletResponse resp, String label, String clss, String type, String name, String value) throws IOException {
		resp.getWriter().printf("<div><tr><td>%s</td><td><input class=\"%s\" type=\"%s\" name=\"%s\" size =\"40\" value=\"%s\"></td></tr></div>\n",
					             label, clss, type, name, value);
					
	}
	
	public static void formFooter(HttpServletResponse resp) throws IOException {
		resp.getWriter().println("</table>");
		resp.getWriter().println("</form>");
		resp.getWriter().println("</center>");
	}
}