package com.ata.microservice.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnectionManager {
	  private Connection con = null;

	  /**
	   * constructor
	   */
	  public MysqlConnectionManager(String server) {
	    try {
	      Class.forName("com.mysql.jdbc.Driver").newInstance();
	    }
	    catch (ClassNotFoundException ex) {
	      System.out.println("error: " + ex.getMessage());
	    }
	    catch (IllegalAccessException ex) {
	      System.out.println("error: " + ex.getMessage());
	    }
	    catch (InstantiationException ex) {
	      System.out.println("error: " + ex.getMessage());
	    }

	    String constr = "";
	    String username = "";
	    String password = "";

	    if(server.equals("mysql")){
	      constr = "jdbc:mysql://localhost:3306/commerce";
	      username = "microservices";
	      password = "microservices";
	    } 

	    try {
	      con = DriverManager.getConnection(constr, username, password);
	    } catch (Exception exc) {
	      exc.printStackTrace();
	    }
	  }

	  /**
	   * get the connection object
	   * @return the connection object
	   */
	  public Connection getConnection(){
	    return con;
	  }
	  
	  public static void main2(String[] args){
		  MysqlConnectionManager cm = new MysqlConnectionManager("mysql");
		  System.out.println("obtaining connection ...");
		  Connection conn = cm.getConnection();
		  if(conn != null){
			  System.out.println("Connection obtained successfully");
		  }
		  if(conn != null){
			  try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
	  }
}
