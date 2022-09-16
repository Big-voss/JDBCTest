package dbutils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import Util.JDBCUtil;

public class QueryRunnerTest {

	//测试插入操作
	@Test
	public void testInsert() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtil.getConnection3();
			String sql = "insert into customers (name,email,birth)values(?,?,?)";
			int insertcount = runner.update(conn, sql, "叽叽叽","叽@126.com","2000-11-11");
			System.out.println("添加了"+insertcount+"条记录");
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			JDBCUtil.closeResource(conn, null);			
		}
	}
	
	//测试查询
	
}
