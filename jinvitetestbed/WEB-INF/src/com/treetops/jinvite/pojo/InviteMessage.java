package com.treetops.jinvite.pojo;

import com.treetops.jinvite.InviteMessageException;

/**
 * Represents the message that is sent to the invited email address. We need the fully
 * qualified servlet path so that we know how to build the link
 * 
 * The link will look like <fully qualified servlet>?code=<invite code> and appear 
 * at the bottom of the message body.
 * 
 * A typical Invite message might look like:
 * 
 * To: someuser@somedomain.com
 * From: noreply@mysite.com
 * Subject: You have been invited to join my awesome site!
 * Body:
 * 	Please click on the below link to confirm your invitation and get started!
 * 	http://www.mysite.com/confirminvitation?code=12345-12345-12345-12345
 * 
 * @author chad
 *
 */
public class InviteMessage {

	private String to;
	private String msgBody;
	private String FQServletPath;
	private String inviteCode;
	private String from;
	private String subject;
	
	public String getMsgBody() throws InviteMessageException {
		if ( to == null || msgBody == null || FQServletPath == null || inviteCode == null || from == null || subject == null ) {
			throw new InviteMessageException("All members of class InviteMessage must be set before a mail can be sent");
		}
		StringBuffer buff = new StringBuffer(msgBody.length()+20);
		buff.append(msgBody);
		buff.append('\n').append('\n');
		buff.append(FQServletPath);
		buff.append("?code=").append(inviteCode);
		return buff.toString();
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	public String getFQServletPath() {
		return FQServletPath;
	}
	public void setFQServletPath(String servletPath) {
		FQServletPath = servletPath;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	
}
