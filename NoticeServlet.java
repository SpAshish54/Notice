package com.learning.hello;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import controller.Notice;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/notice")
public class NoticeServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private JakartaServletWebApplication application;
	private TemplateEngine templateEngine;
	
	static {

        try {

            // Load the MySQL JDBC driver

            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            e.printStackTrace();

        }

    }
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		application = JakartaServletWebApplication.buildApplication(getServletContext());
		final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
    	final IWebExchange webExchange = this.application.buildExchange(req, resp);
    	
    	final WebContext ctx = new WebContext(webExchange);
    	
    	
		
		String name = req.getParameter("name");
		String phoneNumber = req.getParameter("phone");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		
		Connection connection = null;
		PreparedStatement contactStatement = null;
    	
    	try {
    		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Notice", "Ashish", "welcome");
    		
    		String Query = "INSERT INTO Details(Name, PhoneNumber, Title, content) VALUES(?,?,?,?)";
    		contactStatement = connection.prepareStatement(Query);
    		contactStatement.setString(1, name);
    		contactStatement.setString(2, phoneNumber);
    		contactStatement.setString(3, title);
    		contactStatement.setString(4, content);
    		contactStatement.executeUpdate();
    	
    		
    	}catch (SQLException e) {
    		e.printStackTrace();
    	} 
//    	ctx.setVariable("name", name);
//    	ctx.setVariable("phone", phoneNumber);
//    	ctx.setVariable("title", title);
//    	ctx.setVariable("content", content);
//    	templateEngine.process("Notice",ctx,out);
    	doGet(req, resp);
		
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final IWebExchange webExchange = this.application.buildExchange(req, resp);
		final WebContext ctx = new WebContext(webExchange);
		var out = resp.getWriter();
		  List<Notice> notices = new ArrayList<>();

        Connection connection = null;
        PreparedStatement noticeQueryStatement = null;
        ResultSet resultSet = null;

        try {
            // database connection 
        	Class.forName("com.mysql.cj.jdbc.Driver");
    		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Notice", "Ashish", "welcome");


    		String noticeQuery = "SELECT * FROM Details limit 6";
            noticeQueryStatement = connection.prepareStatement(noticeQuery);
            resultSet = noticeQueryStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                String content = resultSet.getString("content");
                String contactName = resultSet.getString("Name");
                String phoneNumber = resultSet.getString("PhoneNumber");

                Notice notice = new Notice(title, content, contactName, phoneNumber);
                notices.add(notice);
            }

           ctx.setVariable("notices", notices);

           templateEngine.process("Notice", ctx, resp.getWriter());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            
            resp.getWriter().write("An error occurred while fetching data from the database.");
        }	
       }
	
}
