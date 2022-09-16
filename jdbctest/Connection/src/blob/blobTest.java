package blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import Util.JDBCUtil;

public class blobTest {
	@Test
	public void blobTest() throws Exception {
		Connection conn = JDBCUtil.getConnection();
		
		String sql = "insert into customers (name,email,birth,photo) values(?,?,?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setObject(1, "makabaka");
		ps.setObject(2, "makabaka@qq.com");
		ps.setObject(3, "2000-01-01");
		FileInputStream is = new FileInputStream(new File("111.jpg"));
		ps.setBlob(4, is);
		
		ps.execute();
		
		JDBCUtil.closeResource(conn, ps);
	}
	
	@Test
	public void TestQuerry() {
		Connection conn = null;
		PreparedStatement ps = null;
		InputStream is = null;
		FileOutputStream fos =null;
		ResultSet rs = null;
		try {
			conn = JDBCUtil.getConnection();
			String sql = "select id,name,email,birth,photo from customers where id = ?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, 16);
			rs = ps.executeQuery();
			if(rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				Date birth = rs.getDate("birth");
				
				Customer cust = new Customer(id,name,email,birth);
				System.out.println(cust);
				
				Blob photo = rs.getBlob("photo");
				is = photo.getBinaryStream();
				fos = new FileOutputStream(new File("zhuying.jpg"));
				byte[] buffer = new byte[1024];
				int len;
				while((len = is.read(buffer))!=-1) {
					fos.write(buffer,0,len);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if(is!=null)
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(fos!=null)
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			JDBCUtil.closeResource(conn, ps, rs);
		}
	}
}
