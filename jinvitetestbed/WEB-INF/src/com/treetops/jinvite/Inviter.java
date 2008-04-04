package com.treetops.jinvite;

import java.sql.Connection;
import java.util.UUID;
import com.treetops.jinvite.dao.InviteDAO;
import com.treetops.jinvite.pojo.Invite;

public class Inviter {

	public static String getInviteCode(Connection conn, String user) {
		String code = null;

		// user must be set, leave the code set to null if this class was mistakenly constructed without a user argument
		if ( user != null ) {
			InviteDAO inviteDAO = new InviteDAO(conn);
			do {
				code = UUID.randomUUID().toString();
			} while ( inviteDAO.codeExists(code) );
			Invite invite = new Invite();
			invite.setCode(code);
			invite.setUser(user);
			inviteDAO.save(invite);
		}
	
		return code;
		
	}

	public static boolean confirm( Connection conn, String code ) {
		boolean confirmed = false;
		
		InviteDAO inviteDAO = new InviteDAO(conn);
		if ( inviteDAO.codeExists(code) ) {
			confirmed = true;
			inviteDAO.deleteCode(code);
		}
		
		return confirmed;
	}

}
