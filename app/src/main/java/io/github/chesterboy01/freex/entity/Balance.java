/**
 * 
 */
package io.github.chesterboy01.freex.entity;

import java.io.Serializable;

/**
 * @ClassName:     Balance.java
 * @Description:   Balance entity
 * @author         Freddy Lee
 * @version        V1.0  
 * @Date           2016.10.1 1:58:30 PM 
 */
public class Balance implements Serializable{

	private static final long serialVersionUID = 1L;
	private int bid;
	private int buid;
	private int bcid;
	private String bamount;
	
	public Balance() {
		super();
	}

	public Balance(int bid, int buid, int bcid, String bamount) {
		super();
		this.bid = bid;
		this.buid = buid;
		this.bcid = bcid;
		this.bamount = bamount;
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

