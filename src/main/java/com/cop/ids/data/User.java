package com.cop.ids.data;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class User {
	

	
	@Id
	private ObjectId id;

	private String password;
	
	private String username;
	
	private String role;
	
	public User(String username, String password, String role) {
		this.username=username;
		this.password=password;
		this.setRole(role);
	}

	public User() {
		// TODO Auto-generated constructor stub
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}
