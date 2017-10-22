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
@WebServlet("/TrashServlet")
public class TrashServlet extends HttpServlet {

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
			
			String msg=(String)request.getAttribute("msg");
			if(msg!=null){
				out.print("<p>"+msg+"</p>");
			}
			
			try{
				Connection con=ConProvider.getConnection();
				PreparedStatement ps=con.prepareStatement("select * from company_mailer_message where (receiver=? OR sender=?) and trash=? order by id desc");
				ps.setString(1,email);
				ps.setString(2,email);
				ps.setString(3,"yes");
				
				ResultSet rs=ps.executeQuery();
				out.print("<div class=inputform style=float:left;>");
				out.print("<h2 class=design align=centre>Trash Messages</h2>");
				out.print("<table width=100%>");				
				out.print("<tr class=designrow><td>Sender</td><td>Subject</td><td>Action</td></tr>");
				while(rs.next()){
					out.print("<tr><td>"+rs.getString("sender")+"</td><td>"+rs.getString("subject")+"</td>");
					out.print("<td><a href='PermanentDeleteMailServlet?id="+rs.getString(1)+"'>Delete Permanently</a></td></tr>");								
				}
				out.print("</table></div>");
				
				con.close();
			}catch(Exception e){out.print(e);}
		}
		
		request.getRequestDispatcher("footer.html").include(request, response);
		out.close();

	}

}
