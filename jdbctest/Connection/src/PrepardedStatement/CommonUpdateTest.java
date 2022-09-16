package PrepardedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import Util.JDBCUtil;


public class CommonUpdateTest {
	
	@Test
	public void testdelete() {
		/*删除
		 * String sql = "delete from customers where id = ?"; Update(sql,3);
		 */
		//修改
		String sql ="update customers set name = ? where id = ?";
		Update(sql,"苍井空","10");
	}
	
public void Update(String sql,Object ...args) {
	//获取连接
	Connection conn = null;
	//预编译sql语句
	PreparedStatement ps = null;
	try {
		conn = JDBCUtil.getConnection();
		ps = conn.prepareStatement(sql);
		//填充占位符
		for(int i =0;i<args.length;i++) {
			ps.setObject(i+1, args[i]);
		}
		//执行sql语句
		ps.execute();
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		//关闭资源
		JDBCUtil.closeResource(conn, ps);
	}
}
}
