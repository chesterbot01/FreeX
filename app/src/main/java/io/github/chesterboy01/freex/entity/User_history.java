/**
 * 
 */
package io.github.chesterboy01.freex.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:     User_history.java
 * @Description:   User_history entity
 * @author         Freddy Lee
 * @version        V1.0  
 * @Date           2016.10.1 2:03:01 PM 
 */

public class User_history implements Serializable{


	private static final long serialVersionUID = 1L;
	private int uhid;
	private int uhuid;
	private Date uhtime;
	private String action;
	
	public User_history() {
		super();
	}

	public User_history(int uhid, int uhuid, Date uhtime, String action) {
		super();
		this.uhid = uhid;
		this.uhuid = uhuid;
		this.uhtime = uhtime;
		this.action = action;
	}

	public int getUhid() {
		return uhid;
	}

	public void setUhid(int uhid) {
		this.uhid = uhid;
	}

	public int getUhuid() {
		return uhuid;
	}

	public void setUhuid(int uhuid) {
		this.uhuid = uhuid;
	}

	public Date getUhtime() {
		return uhtime;
	}

	public void setUhtime(Date uhtime) {
		this.uhtime = uhtime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
}
