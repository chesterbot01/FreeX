/**
 * 
 */
package io.github.chesterboy01.freex.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:     CommonObj.java
 * @Description:   Common entity, for the value passing, not for persistence
 * @author         Freddy Lee
 * @version        V1.0  
 * @Date           2016.10.14 11:02:13 AM 
 */
public class CommonObj implements Serializable{
	
	/* user */
	private int uid;
	private String username;
	private String password;
	private String email;
	
	/* user history */
	private int uhid;
	private int uhuid;
	private Date uhtime;
	private String action;
	
	/* transaction history */
	private int thid;
	private int thuid;
	private int cidout;
	private int cidin;
	private String thamount;
	private String rate;
	private Date thtime;
	
	/* currency */
	private int cid;
	private String cname;
	
	/* balance */
	private int bid;
	private int buid;
	private int bcid;
	private String bamount;
	
	public CommonObj() {
		super();
	}

	public CommonObj(int uid, String username, String password, String email,
			int uhid, int uhuid, Date uhtime, String action, int thid,
			int thuid, int cidout, int cidin, String thamount, String rate,
			Date thtime, int cid, String cname, int bid, int buid, int bcid,
			String bamount) {
		super();
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.email = email;
		this.uhid = uhid;
		this.uhuid = uhuid;
		this.uhtime = uhtime;
		this.action = action;
		this.thid = thid;
		this.thuid = thuid;
		this.cidout = cidout;
		this.cidin = cidin;
		this.thamount = thamount;
		this.rate = rate;
		this.thtime = thtime;
		this.cid = cid;
		this.cname = cname;
		this.bid = bid;
		this.buid = buid;
		this.bcid = bcid;
		this.bamount = bamount;
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
		return password;
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

	public int getThid() {
		return thid;
	}

	public void setThid(int thid) {
		this.thid = thid;
	}

	public int getThuid() {
		return thuid;
	}

	public void setThuid(int thuid) {
		this.thuid = thuid;
	}

	public int getCidout() {
		return cidout;
	}

	public void setCidout(int cidout) {
		this.cidout = cidout;
	}

	public int getCidin() {
		return cidin;
	}

	public void setCidin(int cidin) {
		this.cidin = cidin;
	}

	public String getThamount() {
		return thamount;
	}

	public void setThamount(String thamount) {
		this.thamount = thamount;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public Date getThtime() {
		return thtime;
	}

	public void setThtime(Date thtime) {
		this.thtime = thtime;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public int getBuid() {
		return buid;
	}

	public void setBuid(int buid) {
		this.buid = buid;
	}

	public int getBcid() {
		return bcid;
	}

	public void setBcid(int bcid) {
		this.bcid = bcid;
	}

	public String getBamount() {
		return bamount;
	}

	public void setBamount(String bamount) {
		this.bamount = bamount;
	}

}
