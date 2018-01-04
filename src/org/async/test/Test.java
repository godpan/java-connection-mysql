package org.async.test;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import org.async.jdbc.AsyncConnection;
import org.async.jdbc.PreparedQuery;
import org.async.jdbc.PreparedStatement;
import org.async.jdbc.ResultSet;
import org.async.jdbc.ResultSetCallback;
import org.async.jdbc.Statement;
import org.async.jdbc.SuccessCallback;
import org.async.mysql.MysqlConnection;
import org.async.mysql.protocol.packets.OK;
import org.async.net.Multiplexer;

public class Test {

	public static void main(String[] args) throws IOException, SQLException {
		SuccessCallback successCallback = new SuccessCallback() {

			@Override
			public void onSuccess(OK ok) {
				System.out.println("OK");
			}

			@Override
			public void onError(SQLException e) {
				e.printStackTrace();

			}

		};
		Multiplexer mpx = new Multiplexer();
		AsyncConnection connection = new MysqlConnection("localhost",3306, "root", "",
				"big_data",mpx.getSelector(),successCallback);
		Statement st = connection.createStatement();
        st
                .executeUpdate("use big_data;",successCallback);
		st
				.executeUpdate(
						"CREATE TABLE IF NOT EXISTS test  ("
								+ "id int(11) NOT NULL auto_increment,"
								+ "text0 TEXT collate utf8_general_ci,"
								+ "varchar0 VARCHAR(255) collate utf8_general_ci NOT NULL,"
								+ "date0 DATETIME,"
								+ "PRIMARY KEY  (id)"
								+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;",
						successCallback);		st
				.executeUpdate(
						"CREATE TABLE IF NOT EXISTS test  ("
								+ "id int(11) NOT NULL auto_increment,"
								+ "text0 TEXT collate utf8_general_ci,"
								+ "varchar0 VARCHAR(255) collate utf8_general_ci NOT NULL,"
								+ "date0 DATETIME,"
								+ "PRIMARY KEY  (id)"
								+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;",
						successCallback);
		st.executeUpdate("TRUNCATE test",successCallback);
		st.executeUpdate("DROP procedure IF EXISTS test_procedure;", successCallback);
		st.executeUpdate("CREATE PROCEDURE test_procedure () BEGIN select * from test; END", successCallback);
		PreparedStatement insert = connection
				.prepareStatement("INSERT INTO test SET varchar0=?,date0=?");
		insert.executeUpdate(new PreparedQuery() {

			@Override
			public void query(PreparedStatement pstmt) throws SQLException {
				pstmt.setString(1,"text text text");
				pstmt.setDate(2,new Date(System.currentTimeMillis()));
			}

		},successCallback);
		PreparedStatement ps = connection
				.prepareStatement("SELECT * FROM `tb_big_data` where id=? and weixin_id = ? and openid = ? and openid = ? and openid = ? and openid = ? and openid = ? and openid = ? ");
		ResultSetCallback rsCallback = new ResultSetCallback() {
			public void onResultSet(ResultSet rs) {
				while (rs.hasNext()) {
					rs.next();
					System.out.println(rs.getLong(1) + " " + rs.getString(2)
							+ " " + rs.getString(3) + " " + rs.getTimestamp(4));
				}
			}
			//
			public void onError(SQLException e) {
				e.printStackTrace();
			}

		};
		st
				.executeQuery(
						"SELECT * FROM `tb_big_data` WHERE 1 order by id limit 1", rsCallback);
		insert.close();
		st.executeQuery("select * from test", rsCallback);
		ps.executeQuery(new PreparedQuery() {
			public void query(PreparedStatement pstmt) throws SQLException {
				pstmt.setInteger(1, null);
				pstmt.setString(2,null);
				pstmt.setString(3,null);
				pstmt.setString(4,null);
				pstmt.setString(5,null);
				pstmt.setString(6,null);
				pstmt.setString(7,null);
				pstmt.setString(8,null);
			}

		}, rsCallback);
		ps.close();
        st
                .executeQuery(
                        "SELECT * FROM `tb_big_data` WHERE 1 order by id limit 1", rsCallback);
		insert.close();
		st.executeQuery("select * from test", rsCallback);
		ps.executeQuery(new PreparedQuery() {
			public void query(PreparedStatement pstmt) throws SQLException {
				pstmt.setInteger(1, null);
                pstmt.setString(2,null);
                pstmt.setString(3,null);
                pstmt.setString(4,null);
                pstmt.setString(5,null);
                pstmt.setString(6,null);
                pstmt.setString(7,null);
                pstmt.setString(8,null);
			}

		}, rsCallback);
		ps.close();
		
		st.executeCall("CALL test_procedure()", rsCallback, successCallback);
		
		connection.close();
		while (true) {
			mpx.select();
		}
	}
}
