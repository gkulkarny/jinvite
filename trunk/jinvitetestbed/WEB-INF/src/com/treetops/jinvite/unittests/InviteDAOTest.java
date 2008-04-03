package com.treetops.jinvite.unittests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.treetops.jinvite.Inviter;
import com.treetops.jinvite.dao.InviteDAO;
import com.treetops.jinvite.pojo.Invite;

import junit.framework.TestCase;

public class InviteDAOTest extends TestCase {
	private boolean noTableDrop = false;
	
	private Connection conn = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		
		final String DB_USER = "jsfu";
		final String DB_PASS = null; 
		final String DB_URL = "jdbc:mysql://localhost/jsfutest";
		try {
		
			if ( DB_PASS == null ) {
				fail("Need to set the username/password/host for the JDBC connection");
			}
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
			
			// make sure no one is running this test against a database with a populated invite table
			try {
				String sql = "select count(*) as cnt from invite";
				ResultSet rs = conn.createStatement().executeQuery(sql);
				if ( rs.next() ) {
					if ( rs.getInt("cnt") != 0 ) { 
						noTableDrop = true;  /* to signal method tearDown() not to drop the invite table when we're done */
						fail("Non empty invite table found, not touching it");
					}
				}
			} catch (Exception e) {  }		
		
			// create the invite table
			try {
				String sql = "create table invite(code varchar(36) primary key, user varchar(50) not null, createdOn datetime not null)";
				conn.createStatement().executeUpdate(sql);
			} catch (Exception e) {
				if ( e.toString().equals("Table 'invite' already exists"));
				noTableDrop = true;
				fail("Table invite already exists, not touching it");
			}
			
		} catch (Exception e) { e.printStackTrace(); }		
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if ( !noTableDrop ) { conn.createStatement().executeUpdate("drop table invite"); }
		conn.close();
		conn = null;
	}

	public void testInviteDAO() {
		InviteDAO inviteDAO = new InviteDAO(conn);
		assertNotNull(inviteDAO);
	}

	public void testGetByCode() {
	
		try {
			String sql = "insert into invite(code, user, createdOn) values ('somecode','someuser',now())";
			conn.createStatement().executeUpdate(sql);
	
			InviteDAO inviteDAO = new InviteDAO(conn);
			Invite invite = inviteDAO.getByCode("somecode");
	
			assertEquals(invite.getUser(), "someuser");
			assertEquals(invite.getCode(), "somecode");
	
			conn.createStatement().executeUpdate("delete from invite");
			
		} catch (Exception e) { e.printStackTrace(); }
	}

	public void testExists() {
		try {
			String sql = "insert into invite(code, user, createdOn) values ('somecode','someuser',now())";
			conn.createStatement().executeUpdate(sql);
	
			InviteDAO inviteDAO = new InviteDAO(conn);
			assertTrue( inviteDAO.codeExists("somecode"));
			assertFalse( inviteDAO.codeExists("someothercode"));
			
			conn.createStatement().executeUpdate("delete from invite");
			
		} catch (Exception e) { e.printStackTrace(); }
	}

	public void testSave() {
		try {
			Invite invite = new Invite();
			invite.setUser("asdf");
			invite.setCode("asdf");
			
			InviteDAO inviteDAO = new InviteDAO(conn);
			inviteDAO.save(invite);
		
			ResultSet rs = conn.createStatement().executeQuery("select * from invite where code='asdf' and user='asdf'");
			assertTrue(rs.next());
			rs.close();
			
			conn.createStatement().executeUpdate("delete from invite");
		} catch (Exception e) { e.printStackTrace(); }
	}

	public void testDeleteCode() {
		try {
			String sql = "insert into invite(code, user, createdOn) values ('somecode','someuser',now())";
			conn.createStatement().executeUpdate(sql);
			
			InviteDAO inviteDAO = new InviteDAO(conn);
			inviteDAO.deleteCode("somecode");
		
			ResultSet rs = conn.createStatement().executeQuery("select * from invite where code='somecode' and user='someuser'");
			assertFalse(rs.next());
			rs.close();
			
		} catch (Exception e) { e.printStackTrace(); }
		
	}

}
