package com.ibm.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class JdbcServlet
 */
@WebServlet("/*")
public class EmpServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	@Resource(name = "jdbc/empDS")
	DataSource ds1;
	  
    Statement stmt = null;
    Connection con = null;
  
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    response.setContentType("text/html");
	    PrintWriter pw = response.getWriter();  

        try {
            
            con = getConnection(pw);
            stmt = con.createStatement();
            printForm(pw);
            
    		String action = request.getParameter("CMD_ACTION");
			if(action != null) {
				action = action.toLowerCase();
			}
			
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String empid = request.getParameter("empid");
			String dept = request.getParameter("dept");
			String dsgn = request.getParameter("dsgn");
			String sal = request.getParameter("sal");
			
			if ("populateemployeetable".equals(action))
			{
				populateEmpTable(pw);
			}else if ("getemployeelist".equals(action))
			{
				getEmployeeList(pw);
			}
			else if ("addemployee".equals(action))
			{
				addEmployeeForm(pw,fname,lname,empid,dept,dsgn,sal);
			}
			else if ("updateemployee".equals(action))
			{
				updateEmployeeForm(pw,empid,dsgn,sal);
			}
			else if ("getemployeedetails".equals(action))
			{
				getEmployeeForm(pw,empid);
			}
			else if ("deleteemployee".equals(action))
			{
				deleteEmployeeForm(pw,empid);
			}
			
			String crud = request.getParameter("CMD_CRUD");
			if(crud != null) {
				crud = crud.toLowerCase();
			}
			
			if ("add".equals(crud))
			{
				addEmployee(pw,fname,lname,empid,dept,dsgn,sal);
			}else if ("update".equals(crud))
			{
				updateEmployee(pw, empid,dsgn,sal);
			}else if ("get".equals(crud))
			{
				getEmployee(pw, empid);
			}else if ("delete".equals(crud))
			{
				deleteEmployee(pw, empid);
			}
			
	    } catch (Exception e) {
	    	response.getWriter().println("Exception");
	        response.getWriter().println(e.getMessage());
	    }
	    finally
	    {    
	    	
	    }
	}
	
	protected void printForm(PrintWriter pw)
	{
		pw.println("<form name=\"DisplayForm\">");
		pw.println("<fieldset>");
		pw.println("<body style=\"background-color:lightcyan;\"> ");
		pw.println("<legend><h1><center>Employee Information </center></h1></legend>");
        pw.println("<input type=\"submit\" name=\"CMD_ACTION\" style=\"width :15em\" value=\"PopulateEmployeeTable\">");
        pw.println("<input type=\"submit\" name=\"CMD_ACTION\" style=\"width :10em\" value=\"GetEmployeeList\">");
 		pw.println("<input type=\"submit\" name=\"CMD_ACTION\" style=\"width :10em\" value=\"AddEmployee\">");
 		pw.println("<input type=\"submit\" name=\"CMD_ACTION\" style=\"width :10em\" value=\"UpdateEmployee\">");
 		pw.println("<input type=\"submit\" name=\"CMD_ACTION\" style=\"width :15em\" value=\"GetEmployeeDetails\">");
 		pw.println("<input type=\"submit\" name=\"CMD_ACTION\" style=\"width :10em\" value=\"DeleteEmployee\">");
 		pw.println("</fieldset>");
 		pw.println("</form>");
	}

	protected Connection getConnection(PrintWriter pw)
	{
		try {
			con = ds1.getConnection();
		} catch (SQLException e) {
			
			pw.println("Establishing connection with the database failed");
			e.printStackTrace();
		}
		return con;
	}
	protected void populateEmpTable(PrintWriter pw)
	{
		try {
			stmt = con.createStatement();
		
			try{
                // create the table
                stmt.executeUpdate("create table employee (firstname varchar(15) not null, lastname varchar(15), empid varchar(10) not null primary key, dept varchar(15), designation varchar(30), salary double)");
			}
			catch(SQLException e)
			{
			     // drop the table and recreate the table
			     stmt.executeUpdate(" drop table employee ");
	             stmt.executeUpdate("create table employee (firstname varchar(15) not null, lastname varchar(15), empid varchar(10) not null primary key, dept varchar(15), designation varchar(30), salary double)");
			     
			}
        String fname[] = { "Michael" , "Lucas" , "Paul" , "Susan", "Matt", "Mary","Sam", "Jim"};
        String lname[] = { "Chang" , "John" , "Leming" , "Jason", "Paton", "Peter","Jones", "James"};
        String dept[] = {" Sales " ," Marketing", " Production ", " HR " , "Operations"};
        String dsgn[] = {" Assistant ", " Manager ", " Lead"}; 
        Double sal[] = { 10000.00 , 20000.00, 30000.00 };

        String id = "11111";

        String firstname;
        String lastname;
        String empid;
        String department;
        String designation;
        Double salary;
        for (int  i = 0; i<50 ; i++)
        {	     
        	
        	firstname =fname[new Random().nextInt(fname.length)];
        	lastname = lname[new Random().nextInt(lname.length)];
        	empid = id+""+i+"";
        	department = dept[new Random().nextInt(dept.length)];
        	designation =dsgn[new Random().nextInt(dsgn.length)];
    	    salary = sal[new Random().nextInt(sal.length)];
    	    
    	    stmt.executeUpdate("insert into employee values ("+"'"+ firstname +"'"+  ","+"'"+  lastname +"'"+ ","+ "'"+ empid +"'"+ ","+"'"+  department +"'"+ ","+"'"+  designation +"'"+ ", "+ salary +")");
        }
		} catch (SQLException e) {
	        pw.println("Inserting Data into the table failed");
			e.printStackTrace();
		}
		pw.println("Employee table populated successfully");
		
	}
	
	protected void addEmployeeForm(PrintWriter pw, String fname, String lname, String empid, String dept, String dsgn, String sal)
	{
		pw.println("<form name=\"ActionForm\">");
		pw.println("<legend><h2><center> Add New Employee </center></h2></legend>");
		pw.println("<table>");
        pw.println("<tr>");
		pw.println("<p><td><label for=\"value\">First Name                   :</label></td>");
		pw.println("<td><input type=\"text\" name=\"fname\" value=\"" + (fname==null?"":fname) + "\"></td>");
		pw.println("</p>");
        pw.println("</tr>");
        pw.println("<tr>");
		pw.println("<p><td><label for=\"value\">Last Name                    :</label></td>");
		pw.println("<td><input type=\"text\" name=\"lname\" value=\"" + (lname==null?"":lname) + "\"></td>");
		pw.println("</p>");
	    pw.println("</tr>");
	    pw.println("<tr>");
		pw.println("<p><td><label for=\"value\">ID                           :</label></td>");
		pw.println("<td><input type=\"text\" name=\"empid\" value=\"" + (empid==null?"":empid) + "\"></td>");
		pw.println("</p>");
		pw.println("</tr>");
	    pw.println("<tr>");
		pw.println("<p><td><label for=\"value\">Department                            :</label></td>");
		pw.println("<td><input type=\"text\" name=\"dept\" value=\"" + (dept==null?"":dept) + "\"></td>");
		pw.println("</p>");
		pw.println("</tr>");
	    pw.println("<tr>");
		pw.println("<p><td><label for=\"value\">Designation                           :</label></td>");
		pw.println("<td><input type=\"text\" name=\"dsgn\" value=\"" + (dsgn==null?"":dsgn) + "\"></td>");
		pw.println("</p>");
		pw.println("</tr>");
	    pw.println("<tr>");
		pw.println("<p><td><label for=\"value\">Salary                                :</label></td>");
		pw.println("<td><input type=\"text\" name=\"sal\" value=\"" + (sal==null?"":sal) + "\"></td>");
		pw.println("</p>");
		pw.println("</tr>");
	    pw.println("<tr>");
		pw.println("<td><input type=\"submit\" name=\"CMD_CRUD\" style=\"width :10em\" value=\"Add\"></td>");
		pw.println("</tr>");
		pw.println("</form>");
	}
	
	protected void updateEmployeeForm(PrintWriter pw, String empid, String dsgn, String sal)
	{
		pw.println("<form name=\"ActionForm\">");
		pw.println("<legend><h1><center> Update Employee </h1></center></legend>");
		pw.println("<table>");
		pw.println("<tr>");
		pw.println("<p><td><label for=\"key\">Emp Id                   :</label></td>");
		pw.println("<td><input type=\"text\" name=\"empid\" value=\"" + (empid==null?"":empid) + "\"></td>");
		pw.println("</p>");
		pw.println("</tr>");
		pw.println("<tr>");
		pw.println("<p><td><label for=\"key\">Designation                   :</label></td>");
		pw.println("<td><input type=\"text\" name=\"dsgn\" value=\"" + (dsgn==null?"":dsgn) + "\"></td>");
		pw.println("</p>");
		pw.println("</tr>");
		pw.println("<tr>");
		pw.println("<p><td><label for=\"key\">Salary                   :</label></td>");
		pw.println("<td><input type=\"text\" name=\"sal\" value=\"" + (sal==null?"":sal) + "\"></td>");
		pw.println("</p>");
		pw.println("</tr>");
		pw.println("<td><input type=\"submit\" name=\"CMD_CRUD\" style=\"width :10em\" value=\"Update\"></td>");		
		pw.println("</table>");
		pw.println("</form>");
	}
	
	protected void getEmployeeForm(PrintWriter pw, String empid)
	{
		pw.println("<form name=\"ActionForm\">");
		pw.println("<legend><h1><center> Get Employee Information </center></h1></legend>");

		// Key and value
		pw.println("<p><label for=\"key\">Emp Id                   :</label>");
		pw.println("<input type=\"text\" name=\"empid\" value=\"" + (empid==null?"":empid) + "\">");	
		pw.println("</p>");
		pw.println("<input type=\"submit\" name=\"CMD_CRUD\" style=\"width :10em\" value=\"Get\">");
		pw.println("</fieldset>");
		pw.println("</form>");
		
		
	}
	
	protected void deleteEmployeeForm(PrintWriter pw, String empid)
	{
		pw.println("<form name=\"ActionForm\">");
		pw.println("<legend><h1><center> Delete Employee Information </h1></center></legend>");
		// Key and value
		pw.println("<p><label for=\"key\">Emp Id                   :</label>");
		pw.println("<input type=\"text\" name=\"empid\" value=\"" + (empid==null?"":empid) + "\">");
		pw.println("</p>");
		pw.println("<input type=\"submit\" name=\"CMD_CRUD\" style=\"width :10em\" value=\"Delete\">");
		pw.println("</form>");
	}
	
	protected void addEmployee(PrintWriter pw,String fname, String lname, String empid, String dept, String dsgn, String sal)
	{
		try {
			int rv =stmt.executeUpdate("insert into employee values ("+"'"+ fname +"'"+  ","+"'"+  lname +"'"+ ","+ "'"+ empid +"'"+ ","+"'"+  dept +"'"+ ","+"'"+  dsgn +"'"+ ", "+ sal +")");
			if ( rv == 1)
				pw.println("Employee with empid '"+ empid +"' added successfully");
			
		} catch (SQLException e) {
			
            pw.println("Inserting data into the table failed");
			e.printStackTrace();
		}
	}

	protected void deleteEmployee(PrintWriter pw, String empid)
	{
		try {
			int rv = stmt.executeUpdate("delete from employee where empid='"+ empid +"'" );
			if ( rv == 1)
				pw.println("Employee with empid '"+ empid +"' deleted successfully");
			else
				pw.println("Employee ID '"+ empid +"' doesn't exist. Please provide a valid empid");
			
		} catch (SQLException e) {
			pw.println("Deleting employee data failed");
			e.printStackTrace();
		}
		
	}
	
	protected void updateEmployee(PrintWriter pw, String empid, String dsgn, String sal)
	{
		try {
			int rv = 0;
			if ((!dsgn.equals("")) && (sal.equals(""))) 
			     rv = stmt.executeUpdate("update employee set designation='"+ dsgn +"' where empid='"+ empid +"'" );
			else if ((dsgn.equals("")) && (!sal.equals("")))
				rv = stmt.executeUpdate("update employee set salary="+ sal +" where empid='"+ empid +"'" );
			else if ((!dsgn.equals("")) && (!sal.equals("")))
			    rv = stmt.executeUpdate("update employee set designation='"+ dsgn +"',salary="+ sal +" where empid='"+ empid +"'" );
			if (rv == 1)
				pw.println("Employee with empid  '"+ empid +"' updated successfully");
			else
				pw.println("Employee ID '"+ empid +"' doesn't exist. Please provide a valid empid");
		} catch (SQLException e) {
			pw.println("Updating Employee data failed");
			e.printStackTrace();
		}
	}
	protected void getEmployee(PrintWriter pw, String empid)
	{
		 

         ResultSet result;
		try {
			result = stmt.executeQuery("select * from employee where empid='"+ empid +"'");
		    
			if (result.next())
			{
				pw.println("<table border=\"1\">");
				pw.println("<tr>");
				pw.println("<td> First Name </td>");
				pw.println("<td> Last Name </td>");
				pw.println("<td> ID </td>");
				pw.println("<td> Department </td>");
				pw.println("<td> Designation </td>");
				pw.println("<td> Salary </td>");
				pw.println("</tr>");
				pw.println("<tr>");
				pw.println("<td>" + result.getString(1) + " </td>");
				pw.println("<td>" +result.getString(2)+ " </td>");
				pw.println("<td>" +result.getString(3)+ " </td>");
				pw.println("<td>" +result.getString(4)+ " </td>");
				pw.println("<td>" +result.getString(5)+ " </td>");
				pw.println("<td>" +result.getDouble(6)+ " </td>");
				pw.println("</tr>");
			}
			else
				pw.println("Employee ID '"+ empid +"' doesn't exist. Please provide a valid empid");
	         
		} catch (SQLException e) {
			
			pw.println("Retrieving data from the table failed");
			e.printStackTrace();
		}
         
         pw.println("</table>");
	}
	
	protected void getEmployeeList(PrintWriter pw)
	{
		 pw.println("<table border=\"1\">");
         pw.println("<tr>");
         pw.println("<td> First Name </td>");
         pw.println("<td> Last Name </td>");
         pw.println("<td> ID </td>");
         pw.println("<td> Department </td>");
         pw.println("<td> Designation </td>");
         pw.println("<td> Salary </td>");
         pw.println("</tr>");

         ResultSet result;
		try {
			result = stmt.executeQuery("select * from employee");
			while( result.next())
	         {
	         	pw.println("<tr>");
	         	pw.println("<td>" + result.getString(1) + " </td>");
	 	        pw.println("<td>" +result.getString(2)+ " </td>");
	 	        pw.println("<td>" +result.getString(3)+ " </td>");
	 	        pw.println("<td>" +result.getString(4)+ " </td>");
	 	        pw.println("<td>" +result.getString(5)+ " </td>");
	 	        pw.println("<td>" +result.getDouble(6)+ " </td>");
	 	        pw.println("</tr>");
	         }
	         
		} catch (SQLException e) {
			pw.println("Retrieving data from the table failed");
			e.printStackTrace();
		}
         
         pw.println("</table>");
	}
}