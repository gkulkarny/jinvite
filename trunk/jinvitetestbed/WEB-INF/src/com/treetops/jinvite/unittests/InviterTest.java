package com.treetops.jinvite.unittests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import com.treetops.jinvite.Inviter;

import junit.framework.TestCase;

public class InviterTest extends TestCase {
	
	private Connection conn = null;
	private boolean noTableDrop = false;

	
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
			} catch (Exception e) { }		
		
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

	public void testGetInviteCode() {
		
		Inviter inviter = new Inviter("user1", conn);
		String code = inviter.getInviteCode();
		assertEquals(code.length(), 36);
	
		try {
			ResultSet rs = conn.createStatement().executeQuery("select * from invite where code='"+code+"'");
			if ( rs.next() ) {
				assertEquals(code, rs.getString("code"));
			} else { fail("Code never got inserted into invite table"); }
			
		} catch (Exception e ) { e.printStackTrace(); }
	}

	public void testConfirm() {
		
		try {
			
			Inviter inviter = new Inviter("user1", conn);
			String sql = "insert into invite(code, user, createdOn) values ('somecode','someuser',now())";
			conn.createStatement().executeUpdate(sql);
			
			boolean confirmed = inviter.confirm("somecode");
			assertTrue(confirmed);
			
			ResultSet rs = conn.createStatement().executeQuery("select * from invite where code='somecode'");
			assertFalse(rs.next());
			rs.close();

			inviter = new Inviter("user1", conn);
			sql = "insert into invite(code, user, createdOn) values ('asdfsomecode','someuser',now())";
			conn.createStatement().executeUpdate(sql);
			
			confirmed = inviter.confirm("somecode");
			assertFalse(confirmed);

			rs = conn.createStatement().executeQuery("select * from invite where code='asdfsomecode'");
			assertTrue(rs.next());
			rs.close();
			
		} catch (Exception e ) { e.printStackTrace(); }
		
	}
}
