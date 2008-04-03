package com.treetops.jinvite.pojo;

import java.util.UUID;

/**
 * Represents a single invitation.
 * @author chad
 *
 */
public class Invite {

	private String user = null;
	private String code = null;
	private String createdOn = null;

	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	
	
	
}
