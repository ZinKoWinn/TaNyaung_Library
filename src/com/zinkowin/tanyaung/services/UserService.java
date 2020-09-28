package com.zinkowin.tanyaung.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zinkowin.tanyaung.models.User;
import com.zinkowin.tanyaung.utils.ConnectionManager;

public class UserService {
	
	private static UserService INSTANCE;
	
	public UserService() {}
	
	public static UserService getInstance() {
		return INSTANCE == null ? INSTANCE = new UserService() : INSTANCE;
	}
	
	public User login(String name, String password) {
		String finduser = "select * from user where 1 = 1 and username like ? and password like ?";
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement stmt = con.prepareStatement(finduser)){
			stmt.setString(1, name);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				return getObject(rs);
			}
		} catch (Exception e) {
			
		}
		return null;
		
	}
	
	public User getObject(ResultSet rs)throws SQLException{
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setName(rs.getString("username"));
		u.setPassword(rs.getString("password"));
		return u;
	}

}
