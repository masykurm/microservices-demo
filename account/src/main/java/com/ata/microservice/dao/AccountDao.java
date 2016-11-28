package com.ata.microservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ata.microservice.db.MysqlConnectionManager;
import com.ata.microservice.representation.Account;

public class AccountDao {
	
	public AccountDao(){
	}
	
	public Account getAccount(String username) throws Exception {
		MysqlConnectionManager cm = new MysqlConnectionManager("mysql");
		Connection con = cm.getConnection();
		PreparedStatement ps = null;

		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement("select * from user where username=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				Account account = new Account();
				account.setAccountId(rs.getString("userid"));
				account.setUserName(rs.getString("username"));
				account.setFirstName(rs.getString("firstname"));
				account.setLastName(rs.getString("lastname"));
				
				return account;
			} else {
				return null;
			}
		} catch(SQLException sqle){
			throw new Exception(sqle);
		} finally {
			try {
				if(ps != null){
					ps.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
			}
		}
	}
	
	
	public boolean validateLogin(String username, String password) throws Exception {
		System.out.println("validating login ...");
		MysqlConnectionManager cm = new MysqlConnectionManager("mysql");
		Connection con = cm.getConnection();
		PreparedStatement ps = null;

		ResultSet rs = null;
		
		try {
			ps = con.prepareStatement("select * from user where username=? and userpass=md5(?)");
			ps.setString(1, username);
			ps.setString(2, password);
			rs = ps.executeQuery();
			
			if(rs.next()){
				System.out.println("login is valid!");
				return true;
			} else {
				System.out.println("login is not valid!");
				return false;
			}
		} catch(SQLException sqle){
			throw new Exception(sqle);
		} finally {
			try {
				if(ps != null){
					ps.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
			}
		}
	}
	
	public Account addAccount(String username, String firstName, String lastName,
			String password) throws Exception {
		MysqlConnectionManager cm = new MysqlConnectionManager("mysql");
		Connection con = cm.getConnection();
		PreparedStatement ps = null;

		int retval;
		
		try {
			ps = con.prepareStatement("insert into user (username, firstname, lastname, userpass) values "
					+ "(?,?,?,md5(?))");
			ps.setString(1, username);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, password);
			retval = ps.executeUpdate();
			
			if(retval == 1){
				
				Account account = new Account();
				account.setUserName(username);
				account.setFirstName(firstName);
				account.setLastName(lastName);
				
				return account;
			} else {
				return null;
			}
		} catch(SQLException sqle){
			throw new Exception(sqle);
		} finally {
			try {
				if(ps != null){
					ps.close();
				}
				if(con != null){
					con.close();
				}
			} catch (SQLException e) {
			}
		}
	}
}
