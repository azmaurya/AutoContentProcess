package com.hungama.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DataBaseConnection 
{

	public static Connection getconnection() throws ClassNotFoundException, SQLException
	{
	
		 Connection connection = null;
		  
		  String url = "jdbc:postgresql://34.86.84.35:5432/mvcms";
	   	  Properties props = new Properties();
	   	  props.setProperty("user","hungama");
	   	  props.setProperty("password","WQHd*TyT*2%sol-c4(WdUM)oiD%gsX");
	 
	   	  connection = DriverManager.getConnection(url, props);
	   	  return connection;
	
		}	
	
	public  static Connection getmysqlconnection() throws SQLException, ClassNotFoundException 
	{
		
		Connection connection = null;
		//String url="jdbc:mysql://35.245.83.228:3306/emi";
		String url="jdbc:mysql://35.245.83.228:3306/wbclient";
		//String url="jdbc:mysql://35.245.83.228:3306/orchard";
		Properties props = new Properties();
		props.setProperty("user", "dadclient");
		props.setProperty("password", "y@ntr@");
		
		connection = DriverManager.getConnection(url, props);
		return connection;
	 }

	

}
