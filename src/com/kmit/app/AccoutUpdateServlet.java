package com.kmit.app;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AccoutUpdateServlet")
public class AccoutUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String SAVE_DIR = "uploadFiles";

    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int user_id=(int) request.getSession().getAttribute("user_id");
		String name = (String) request.getSession().getAttribute("name");
		String uname = (String) request.getSession().getAttribute("uname");
		
		ServletContext sc = getServletContext();
	    String driverName=sc.getInitParameter("driverName"); 
	    String driverUrl=sc.getInitParameter("driverUrl"); 
	    String username=sc.getInitParameter("username"); 
	    String password=sc.getInitParameter("password");
	    
	    Connection conn=null;
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		
		String fileName = SAVE_DIR + File.separator + uname+"_"+user_id+".png";;
		
		try
		{
			Class.forName(driverName);
			conn = DriverManager.getConnection(driverUrl, username, password);
			
			User user=new User();
			
			String sql = "select * from login where user_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_id);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					user.setUser_id(rs.getInt("user_id"));
					user.setUsername(rs.getString("username"));
					user.setFullname(rs.getString("fullname"));
					user.setEmail(rs.getString("email"));
				}
			}
			
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>"
					+ "<head>"
					+ "<title>Todo - Account Details</title>"
					+ "<link href='css/bootstrap.min.css' rel='stylesheet'>"
					+ "<link href='css/login.css' rel='stylesheet'>"
					+ "<script src='js/jquery.min.js'></script>"
					+ "<script src='js/bootstrap.min.js'></script>"
					+ "<script src='js/imagepreview.js'></script>"
					+ "</head>"
					+ "<body>"
					+ "<header>"
					+ "<nav class='navbar navbar-default'>"
					+ "<a href='/' class='navbar-brand'>Brand</a>"
					+ "<ul class='nav navbar-nav'>"
					+ "<li class='active'><a href='#'>Home</a></li>"
					+ "<li><a href='TodoListServlet'>Todos</a></li>"
					+ "</ul>"
					+ "<ul class='nav navbar-nav navbar-right'>"
					+ "<li><a href='AccoutUpdateServlet'><font size='4'>Hi, "+name+"</font></a></li>"
					+ "<li><a href='LogoutServlet'>Logout</a></li>"
					+ "</ul>"
					+ "</nav>"
					+ "</header>"
					+ "<div class='container'>"
					+ "<h1 align='center'>Account Details</h1>"
					+ "<div class='row'>"
					+ "<div class='col-md-6'>"
					+ "<img  src='"+fileName+"' alt='...' class='img-responsive center-block' width='200px' height='200px'>  "
					+ "</div>"
					+ "<div class='col-md-6'>"
					
					
					+ "<form action='UploadServlet' method='post' enctype='multipart/form-data' class='form-signin' >"
					+ "<div class='input-group image-preview'>"
					+ "<input type='text' class='form-control image-preview-filename' disabled='disabled'>"
					+ "<button type='button' class='btn btn-default image-preview-clear' style='display:none;'>Clear</button>"
					+ "<div class='btn btn-default image-preview-input'>"
					+ "<input type='file' accept='image/png, image/jpeg, image/gif' name='input-file-preview'/>"
					+ "</div>"
					+ "</div>"
					+ "<button id='submit' class='btn btn-lg btn-primary btn-block' type='submit'>Upload Image/Avatar</button> "
					+ "</form>"
					+ "<form action='UpdateServlet' method='post' class='form-signin'>"
					+ "<input type='text' class='form-control' name='name' placeholde='Name' required value='"+user.getUsername()+"' />"
					+ "<input type='text' class='form-control' name='fullname' placeholder='Full Name' required  value='"+user.getFullname()+"' />"
					+ "<input type='text' class='form-control' name='email' placeholder='Email ID' required value='"+user.getEmail()+"' />"
					+ "<button id='submit' class='btn btn-lg btn-primary btn-block' type='submit'>Update Account</button>"
					+ "</form>"
					+ "</div>"
					+ "</div>"
					+ "</body>"
					+ "</html>");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			request.setAttribute("error","Account Populate failed : "+e.getMessage() );
			request.getRequestDispatcher("ErrorServlet").forward(request, response);
		}
		finally
		{
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error","Account Populate failed : "+e.getMessage() );
				request.getRequestDispatcher("ErrorServlet").forward(request, response);
			}
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
