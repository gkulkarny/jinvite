package com.treetops.jinvite.pojo;

/**
 * Represents a single invitation.
 * @author chad
 *
 */
public class Invite {

	private String user;
	private String invitedGuest;
	private String inviteCode;
	private boolean confirmed;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public String getInvitedGuest() {
		return invitedGuest;
	}
	public void setInvitedGuest(String invitedGues) {
		this.invitedGuest = invitedGues;
	}
	
	
}
