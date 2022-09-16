package transaction;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.mysql.jdbc.ResultSetMetaData;

import Util.JDBCUtil;

public class TransactionTest {
	//δ�������ݿ�����ת�˲���:
	
	@Test
	public void testUpdate() {
		String sql1 = "update user_table set balance = balance - 100 where user =  ?";
		Update(sql1,"AA");
		
		String sql2 = "update user_table set balance = balance + 100 where user =  ?";
		Update(sql2,"BB");
		
		System.out.println("ת�˳ɹ�");
	}
	
	//ͨ�õ���ɾ��---version 1.0
	public int Update(String sql,Object ...args) {
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
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//�ر���Դ
			JDBCUtil.closeResource(conn, ps);
		}
		return 0;
	}
	
	
	//�������ݿ�����Ĳ���
	
	@Test
	public void testUpdateWithTx() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			
			//ȡ�������Զ��ύ����
			conn.setAutoCommit(false);
			
			String sql1 = "update user_table set balance = balance - 100 where user =  ?";
			Update(conn,sql1,"AA");
			
			//ģ�������쳣
			System.out.println(10/0);
			
			String sql2 = "update user_table set balance = balance + 100 where user =  ?";
			Update(conn,sql2,"BB");
			
			System.out.println("ת�˳ɹ�");
			
			//�ύ����
			conn.commit();
			
		} catch (Exception e) {
			//�ع�����
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			JDBCUtil.closeResource(conn,null);			
		}
		
	}
	
	//ͨ�õ���ɾ��---version 2.0
		public int Update(Connection conn,String sql,Object ...args) {
			//Ԥ����sql���
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				//���ռλ��
				for(int i =0;i<args.length;i++) {
					ps.setObject(i+1, args[i]);
				}
				//ִ��sql���
				return ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				//�ָ��Զ��ύĬ��Ϊtrue(��Ҫ���ʹ�����ݿ����ӳ�ʱ)
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				//�ر���Դ
				JDBCUtil.closeResource(null, ps);
			}
			return 0;
		}
		
		
		/**
		 * @throws Exception *****************************************/
		
		@Test
		public void testTransactionSelect() throws Exception {
			Connection conn = JDBCUtil.getConnection();
			//��ȡ��ǰ���ӵĸ��뼶��
			//System.out.println(conn.getTransactionIsolation());
			//�������ݿ�ĸ��뼶��:
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			//ȡ���Զ��ύ����
			conn.setAutoCommit(false);
			
			String sql = "select user,password,balance from user_table where user = ?";
			User user = getInstance(conn,User.class,sql,"CC");
			System.out.println(user);
		}
		
		@Test
		public void testTransactionUpdate() throws Exception {
			Connection conn = JDBCUtil.getConnection();
			//ȡ���Զ��ύ����
			conn.setAutoCommit(false);
			
			String sql = "update user_table set balance = ? where user = ?";
			Update(conn,sql,5000,"CC");
			
			Thread.sleep(5000);
			System.out.println("�޸Ľ���");
		}
		
		
		//ͨ�õĲ�ѯ����,���ڷ������ݱ��е�һ����¼(version 2.0�������ݿ�����)
		public <T> T getInstance(Connection conn,Class<T> clazz,String sql, Object... args) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = JDBCUtil.getConnection();

				ps = conn.prepareStatement(sql);
				for (int i = 0; i < args.length; i++) {
					ps.setObject(i + 1, args[i]);
				}

				rs = ps.executeQuery();
				// ��ȡ�������Ԫ���� :ResultSetMetaData
				ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
				// ͨ��ResultSetMetaData��ȡ������е�����
				int columnCount = rsmd.getColumnCount();

				if (rs.next()) {
					T t = clazz.newInstance();
					// ��������һ�������е�ÿһ����
					for (int i = 0; i < columnCount; i++) {
						// ��ȡ��ֵ
						Object columValue = rs.getObject(i + 1);

						// ��ȡÿ���е�����
						// String columnName = rsmd.getColumnName(i + 1);
						String columnLabel = rsmd.getColumnLabel(i + 1);

						// ��t����ָ����columnName���ԣ���ֵΪcolumValue��ͨ������
						Field field = clazz.getDeclaredField(columnLabel);
						field.setAccessible(true);
						field.set(t, columValue);
					}
					return t;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.closeResource(null, ps, rs);

			}

			return null;
		}
		
		
}
