package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.ConProvider;
@WebServlet("/ViewMailServlet")
public class ViewMailServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		request.getRequestDispatcher("header.html").include(request, response);
		request.getRequestDispatcher("link.html").include(request, response);
		
		HttpSession session=request.getSession(false);
		if(session==null){
			response.sendRedirect("index.html");
		}else{
			String email=(String)session.getAttribute("email");
			out.print("<span style='float:right'>Hi, "+email+"</span>");
			
			int id=Integer.parseInt(request.getParameter("id"));
			
			try{
				Connection con=ConProvider.getConnection();
				PreparedStatement ps=con.prepareStatement("select * from company_mailer_message where id=?");
				ps.setInt(1,id);
				ResultSet rs=ps.executeQuery();
				rs.next();
				out.print("<div class=inputform style=float:left;>");
				out.print("<h2 class=design align=centre>View Messages</h2>");
				out.print("<table width=100%>");
				out.print("<tr class=designrow><td>Sender</td><td>Subject</td><td>Message</td><td>Action</td></tr>");
				out.print("<tr><td>"+rs.getString("sender")+"</td><td>"+rs.getString("subject")+"</td><td>"+rs.getString("message")+"</td><td><a href='DeleteMailServlet?id="+rs.getString(1)+"'>Delete Mail</a></td><td></tr>");
				out.print("</table></div>");

				con.close();
			}catch(Exception e){out.print(e);}
		}
		request.getRequestDispatcher("footer.html").include(request, response);
		out.close();
	}
}
