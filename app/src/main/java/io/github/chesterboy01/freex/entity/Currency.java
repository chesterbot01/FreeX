/**
 * 
 */
package io.github.chesterboy01.freex.entity;

import java.io.Serializable;

/**
 * @ClassName: Currency.java
 * @Description: Currency entity
 * @author Freddy Lee
 * @version V1.0
 * @Date 2016.10.1 1:55:05 pm
 */

public class Currency implements Serializable {

	private static final long serialVersionUID = 1L;
	private int cid;
	private String cname;

	public Currency() {
		super();
	}

	public Currency(int cid, String cname) {
		super();
		this.cid = cid;
		this.cname = cname;
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

	
}
