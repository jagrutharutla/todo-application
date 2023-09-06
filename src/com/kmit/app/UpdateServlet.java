package com.kmit.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/UpdateServlet")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_id= (int)request.getSession().getAttribute("user_id");
		String fullname = request.getParameter("fullname");
		String email = request.getParameter("email");
		
		ServletContext sc = getServletContext();
	    String driverName=sc.getInitParameter("driverName"); 
	    String driverUrl=sc.getInitParameter("driverUrl"); 
	    String username=sc.getInitParameter("username"); 
	    String password=sc.getInitParameter("password");
	    
	    Connection conn=null;
		PreparedStatement pstmt =null;
		
		try
		{
			Class.forName(driverName);
			conn = DriverManager.getConnection(driverUrl, username, password);

			String sql = "update login set fullname=?, email=? where user_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fullname);
			pstmt.setString(2, email);
			pstmt.setInt(3, user_id);
			
			int update=pstmt.executeUpdate();
			if(update==1) {
				response.sendRedirect("AccoutUpdateServlet");
			}
			else {
				request.setAttribute("error","Account Update failed ");
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			request.setAttribute("error","Account Update failed : "+e.getMessage() );
			request.getRequestDispatcher("ErrorServlet").forward(request, response);
		}
		finally
		{
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error","Account Update failed : "+e.getMessage() );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
		}
		

	}

}
