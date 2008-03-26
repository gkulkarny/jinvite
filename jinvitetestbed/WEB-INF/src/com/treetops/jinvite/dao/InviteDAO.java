package com.treetops.jinvite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.treetops.jinvite.pojo.Invite;

/**
 * Represents persistant storage and updates of Invites. Typically, an 
 * Invite is saved and then somtime later is confirmed. This class also
 * provides the ability to delete Invite objects.
 * 
 * Note, this class does not close or change the state of the Connection
 * object passed to it in any way.
 * @author chad
 *
 */
public class InviteDAO {

	private Connection conn;
	
	public InviteDAO(Connection conn) {
		this.conn = conn;
	}


	/**
	 * Returns the user's Invite object if the user exists, null otherwise
	 * @param user
	 * @return invite
	 */
	public Invite getInvite(String user) {
		Invite invite = null;
		try {
			String sql = "select user,inviteCode,confirmed from invite where user=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1,user);
			ResultSet rs = pStmt.executeQuery();
			if ( rs.next() ) {
				invite = new Invite();
				invite.setUser(user);
				invite.setInviteCode(rs.getString("inviteCode"));
				invite.setConfirmed(rs.getBoolean("confirmed"));
			}
			rs.close();
			pStmt.close();
		} catch(Exception e) { /* *shrug*, who cares */ }
		return invite;
	}

	/**
	 * Returns true if the primary key for the given invite exists false otherwise
	 * @param invite
	 * @return true if the invite exists, false otherwise
	 */
	public boolean exists(Invite invite) {
		boolean exists = false;
		try {
			String sql = "select user from invite where user=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1,invite.getUser());
			ResultSet rs = pStmt.executeQuery();
			if ( rs.next() ) { exists = true; }
			rs.close();
			pStmt.close();
		} catch (Exception e) {}
		return exists;
	}
	
	/**
	 * Saves the existing invite if it does not already exist
	 * @param invite
	 */
	public void saveInvite(Invite invite) {
	
		if ( !exists(invite)) {
			try {
				String sql = "insert into invite(user,inviteCode,confirmed) values (?,?,?)";
				PreparedStatement pStmt = conn.prepareStatement(sql);
				pStmt.setString(1,invite.getUser());
				pStmt.setString(2,invite.getInviteCode());
				pStmt.setBoolean(3,invite.isConfirmed());
				pStmt.executeUpdate();
				pStmt.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * Updates the column confirmed to true for the given user if they exist
	 * @param user 
	 */
	public void confirmInvite(String user) {
		Invite invite = new Invite();
		invite.setUser(user);
		if ( exists(invite) ) {
			try {
				String sql = "update invite set confirmed=true where user=?";
				PreparedStatement pStmt = conn.prepareStatement(sql);
				pStmt.setString(1,invite.getUser());
				pStmt.executeUpdate();
				pStmt.close();
			} catch (Exception e) {}
		}
	}

}
