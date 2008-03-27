package com.treetops.jinvite.unittests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.treetops.jinvite.dao.InviteDAO;
import com.treetops.jinvite.pojo.Invite;

import junit.framework.TestCase;

public class InviteDAOTest extends TestCase {
	
	private Connection conn = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		final String DB_USER = "jsfu";
		final String DB_PASS = "12jsfu0r";
		final String DB_URL = "jdbc:mysql://localhost/jsfu";
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
			
			String sql = "insert into invite(user,inviteCode,confirmed) values ('testuser','testcode',0)";
			conn.createStatement().executeUpdate(sql);
		} catch (Exception e) { e.printStackTrace(); }		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		conn.createStatement().executeUpdate("delete from invite");
		conn.close();
		conn = null;
	}

	public void testInviteDAO() {
		InviteDAO inviteDAO = new InviteDAO(conn);
		assertNotNull(inviteDAO);
	}

	public void testGetInvite() {
		InviteDAO inviteDAO = new InviteDAO(conn);
		Invite invite = inviteDAO.getInvite("testuser");
		assertEquals(invite.getUser(),"testuser");
		assertEquals(invite.getInviteCode(),"testcode");
		assertEquals(invite.isConfirmed(), false);
	}

	public void testExists() {
		InviteDAO inviteDAO = new InviteDAO(conn);
		Invite invite = new Invite();
		invite.setUser("testuser");
		assertEquals(inviteDAO.exists(invite), true);
		
		invite.setUser("someotheruser");
		assertEquals(inviteDAO.exists(invite), false);
	}

	public void testSaveInvite() {
		InviteDAO inviteDAO = new InviteDAO(conn);
		Invite invite = new Invite();
		invite.setUser("testuser2");
		invite.setInviteCode("testcode2");
		invite.setConfirmed(false);
		inviteDAO.saveInvite(invite);
	
		String sql = "select * from invite where user='testuser2'";
		try {
			ResultSet rs = conn.createStatement().executeQuery(sql);
			if ( rs.next() ) {
				assertEquals(invite.getUser(),"testuser2");
				assertEquals(invite.getInviteCode(),"testcode2");
				assertEquals(invite.isConfirmed(),false);
			} else {
				fail("save didn't work");
			}
			rs.close();
			conn.createStatement().executeUpdate("delete from invite where user='testuser2'");
		} catch (Exception e) { fail(e.toString()); }
		
	}

	public void testConfirmInvite() {
		InviteDAO inviteDAO = new InviteDAO(conn);
		inviteDAO.confirmInvite("testuser");
		try {
			String sql = "select confirmed from invite where user='testuser'";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			if ( rs.next() ) {
				assertEquals(true,rs.getBoolean("confirmed"));
			} else {
				fail("wtf");
			}
			rs.close();
		} catch (Exception e) { fail(e.toString()); }
	}

}
