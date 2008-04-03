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
	 * Returns the invite for the given code if it exists, null otherwise
	 * @param inviteCode
	 * @return
	 */
	public Invite getByCode(String inviteCode) {
		Invite invite = null;
		try {
			String sql = "select * from invite where code=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1,inviteCode);
			ResultSet rs = pStmt.executeQuery();
			if ( rs.next() ) {
				invite = new Invite();
				invite.setUser(rs.getString("user"));
				invite.setCode(inviteCode);
				invite.setCreatedOn(rs.getString("createdOn"));
			}
			rs.close();
			pStmt.close();
		} catch(Exception e) { e.printStackTrace(); }
		return invite;
	}

	/**
	 * Returns true if the given invite code exists false otherwise
	 * @param code
	 * @return true if the code exists, false otherwise
	 */
	public boolean codeExists(String code) {
		boolean exists = false;
		try {
			String sql = "select code from invite where code=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1,code);
			ResultSet rs = pStmt.executeQuery();
			if ( rs.next() ) { exists = true; }
			rs.close();
			pStmt.close();
		} catch (Exception e) { e.printStackTrace(); }
		return exists;
	}
	
	/**
	 * Saves the existing invite if it does not already exist
	 * @param invite
	 */
	public void save(Invite invite) {
		try {
			String sql = "insert into invite(user,code,createdOn) values (?,?,now())";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1,invite.getUser());
			pStmt.setString(2,invite.getCode());
			pStmt.executeUpdate();
			pStmt.close();
		} catch (Exception e) { e.printStackTrace(); }
	}

	/**
	 * Deletes the given invite
	 * @param invite
	 */
	public void deleteCode(String code) {
		try {
			String sql = "delete from invite where code=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1,code);
			pStmt.executeUpdate();
			pStmt.close();
		} catch (Exception e) { e.printStackTrace(); }
	}


}
