package com.treetops.jinvite;

import java.sql.Connection;
import java.util.UUID;
import com.treetops.jinvite.dao.InviteDAO;
import com.treetops.jinvite.pojo.Invite;

public final class Inviter {

	Connection conn = null;
	String user = null;
	
	public Inviter( String user, Connection conn ) {
		this.conn = conn;
		this.user = user;
	}
	
	public String getInviteCode() {
		String code = null;

		InviteDAO inviteDAO = new InviteDAO(conn);
		do {
			code = UUID.randomUUID().toString();
		} while ( inviteDAO.codeExists(code) );
		Invite invite = new Invite();
		invite.setCode(code);
		invite.setUser(user);
		inviteDAO.save(invite);
	
		return code;
		
	}

	public boolean confirm( String code ) {
		boolean confirmed = false;
		
		InviteDAO inviteDAO = new InviteDAO(conn);
		if ( inviteDAO.codeExists(code) ) {
			confirmed = true;
			inviteDAO.deleteCode(code);
		}
		
		return confirmed;
	}

}
