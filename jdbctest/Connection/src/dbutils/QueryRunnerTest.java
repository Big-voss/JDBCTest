package dbutils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.junit.Test;

import Util.JDBCUtil;

public class QueryRunnerTest {

	//���Բ������
	@Test
	public void testInsert() {
		Connection conn = null;
		try {
			QueryRunner runner = new QueryRunner();
			conn = JDBCUtil.getConnection3();
			String sql = "insert into customers (name,email,birth)values(?,?,?)";
			int insertcount = runner.update(conn, sql, "ߴߴߴ","ߴ@126.com","2000-11-11");
			System.out.println("�����"+insertcount+"����¼");
		} catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			JDBCUtil.closeResource(conn, null);			
		}
	}
	
	//���Բ�ѯ
	
}
