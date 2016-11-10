/**
 * 
 */
package io.github.chesterboy01.freex.entity;

import java.io.Serializable;

import io.github.chesterboy01.freex.FreeXUser;

/**
 * @ClassName: User.java
 * @Description: User entity
 * @author Freddy Lee
 * @version V1.0
 * @Date 2016.9.30 9:13:54 PM
 */

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private int uid;
	private String username;
	private String password;
	private String email;

	public User() {
		super();
	}

	public User(int uid, String username, String password, String email) {
		super();
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		//返回加密以后的密码
		return FreeXUser.Encryption(password);
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
