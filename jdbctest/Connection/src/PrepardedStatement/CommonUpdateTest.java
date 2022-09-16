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
		/*ɾ��
		 * String sql = "delete from customers where id = ?"; Update(sql,3);
		 */
		//�޸�
		String sql ="update customers set name = ? where id = ?";
		Update(sql,"�Ծ���","10");
	}
	
public void Update(String sql,Object ...args) {
	//��ȡ����
	Connection conn = null;
	//Ԥ����sql���
	PreparedStatement ps = null;
	try {
		conn = JDBCUtil.getConnection();
		ps = conn.prepareStatement(sql);
		//���ռλ��
		for(int i =0;i<args.length;i++) {
			ps.setObject(i+1, args[i]);
		}
		//ִ��sql���
		ps.execute();
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		//�ر���Դ
		JDBCUtil.closeResource(conn, ps);
	}
}
}
