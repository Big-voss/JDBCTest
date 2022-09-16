package Util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mysql.jdbc.ResultSetMetaData;

import ConnectionTest.Connection01;

public class JDBCUtil {
	//获取连接
	public static Connection getConnection() throws Exception {
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		Properties pros = new Properties();
		pros.load(is);
		
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		
		Class.forName(driverClass);
		
		Connection conn = DriverManager.getConnection(url,user,password);
		return conn;
	}
	//关闭资源
	public static void closeResource(Connection conn,Statement ps) {
		try {
			if(conn!=null)
			conn.close();
			try {
				if(ps!=null)
				ps.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeResource(Connection conn,Statement ps,ResultSet rs){
		try {
			if(ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	//德鲁伊数据库连接池技术
	private static DataSource source;
	static {
		try {
			Properties pros = new Properties();
			
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
			pros.load(is);
			
			source = DruidDataSourceFactory.createDataSource(pros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection3() throws Exception {
		
		Connection conn = source.getConnection();
		return  conn;
		
	}
}
