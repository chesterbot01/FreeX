/**
 * 
 */
package io.github.chesterboy01.freex.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:     Transaction_history.java
 * @Description:   Transaction_history entity
 * @author         Freddy Lee
 * @version        V1.0  
 * @Date           2016.10.1 2:06:32  PM
 */
public class Transaction_history implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int thid;//主键，无用
	private int thuid;//userid
	private int cidout;//type out
	private int cidin; // type in
	private String thamount; //通用总量
	private String rate; //...
	private Date thtime;
	
	public Transaction_history() {
		super();
	}

	public Transaction_history(int thid, int thuid, int cidout, int cidin,
			String thamount, String rate, Date thtime) {
		super();
		this.thid = thid;
		this.thuid = thuid;
		this.cidout = cidout;
		this.cidin = cidin;
		this.thamount = thamount;
		this.rate = rate;
		this.thtime = thtime;
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

	

}
