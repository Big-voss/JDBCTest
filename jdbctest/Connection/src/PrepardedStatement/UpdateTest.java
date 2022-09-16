package PrepardedStatement;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.Test;

import ConnectionTest.Connection01;

public class UpdateTest {
	@Test
	public void update() {
		Connection coon = null;
		PreparedStatement ps = null;
		try {
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
			Properties pros = new Properties();
			pros.load(is);

			String user = pros.getProperty("user");
			String password = pros.getProperty("password");
			String url = pros.getProperty("url");
			String driverClass = pros.getProperty("driverClass");

			Class.forName(driverClass);

			coon = DriverManager.getConnection(url, user, password);

			String sql = "insert into customers(name,email,birth)values(?,?,?)";
			ps = coon.prepareStatement(sql);

			ps.setString(1, "makabaka");
			ps.setString(2, "makabaka@.com");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse("2000-01-01");
			ps.setDate(3, new Date(date.getTime()));

			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(coon!=null)
				coon.close();
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
	}
}
