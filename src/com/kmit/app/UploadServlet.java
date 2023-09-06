package com.kmit.app;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String SAVE_DIR = "uploadFiles";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int fileID=(int) request.getSession().getAttribute("user_id");
			String name=(String) request.getSession().getAttribute("uname");

			Part filePart = request.getPart("input-file-preview");
			String fileName=name+"_"+fileID;
			
			//String str = filePart.getContentType();
			
			String appPath = request.getServletContext().getRealPath("");
			String savePath = appPath + File.separator + SAVE_DIR;
			
			File fileSaveDir = new File(savePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdir();
			}

			for (Part part : request.getParts()) {
				fileName = fileName+extractFileExtention(part);
				fileName = new File(fileName).getName();
				part.write(savePath + File.separator + fileName);
			}
			
			response.sendRedirect("AccoutUpdateServlet");


			
		}catch(Exception e) {
			e.printStackTrace();
			request.setAttribute("error","Upload failed : "+e.getMessage() );
			request.getRequestDispatcher("ErrorServlet").forward(request, response);
		}
		
		
	}
	
	private String extractFileExtention(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf(".") , s.length() - 1);
			}
		}
		return "";
	}

}
