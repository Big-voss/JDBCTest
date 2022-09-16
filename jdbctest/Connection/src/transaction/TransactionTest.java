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
	//未考虑数据库事务转账操作:
	
	@Test
	public void testUpdate() {
		String sql1 = "update user_table set balance = balance - 100 where user =  ?";
		Update(sql1,"AA");
		
		String sql2 = "update user_table set balance = balance + 100 where user =  ?";
		Update(sql2,"BB");
		
		System.out.println("转账成功");
	}
	
	//通用的怎删改---version 1.0
	public int Update(String sql,Object ...args) {
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
			return ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//关闭资源
			JDBCUtil.closeResource(conn, ps);
		}
		return 0;
	}
	
	
	//考虑数据库事务的操作
	
	@Test
	public void testUpdateWithTx() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			
			//取消数据自动提交功能
			conn.setAutoCommit(false);
			
			String sql1 = "update user_table set balance = balance - 100 where user =  ?";
			Update(conn,sql1,"AA");
			
			//模拟网络异常
			System.out.println(10/0);
			
			String sql2 = "update user_table set balance = balance + 100 where user =  ?";
			Update(conn,sql2,"BB");
			
			System.out.println("转账成功");
			
			//提交数据
			conn.commit();
			
		} catch (Exception e) {
			//回滚数据
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
	
	//通用的怎删改---version 2.0
		public int Update(Connection conn,String sql,Object ...args) {
			//预编译sql语句
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(sql);
				//填充占位符
				for(int i =0;i<args.length;i++) {
					ps.setObject(i+1, args[i]);
				}
				//执行sql语句
				return ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				//恢复自动提交默认为true(主要针对使用数据库连接池时)
				try {
					conn.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				//关闭资源
				JDBCUtil.closeResource(null, ps);
			}
			return 0;
		}
		
		
		/**
		 * @throws Exception *****************************************/
		
		@Test
		public void testTransactionSelect() throws Exception {
			Connection conn = JDBCUtil.getConnection();
			//获取当前连接的隔离级别
			//System.out.println(conn.getTransactionIsolation());
			//设置数据库的隔离级别:
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			//取消自动提交数据
			conn.setAutoCommit(false);
			
			String sql = "select user,password,balance from user_table where user = ?";
			User user = getInstance(conn,User.class,sql,"CC");
			System.out.println(user);
		}
		
		@Test
		public void testTransactionUpdate() throws Exception {
			Connection conn = JDBCUtil.getConnection();
			//取消自动提交数据
			conn.setAutoCommit(false);
			
			String sql = "update user_table set balance = ? where user = ?";
			Update(conn,sql,5000,"CC");
			
			Thread.sleep(5000);
			System.out.println("修改结束");
		}
		
		
		//通用的查询操作,用于返回数据表中的一条记录(version 2.0考虑数据库事务)
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
				// 获取结果集的元数据 :ResultSetMetaData
				ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
				// 通过ResultSetMetaData获取结果集中的列数
				int columnCount = rsmd.getColumnCount();

				if (rs.next()) {
					T t = clazz.newInstance();
					// 处理结果集一行数据中的每一个列
					for (int i = 0; i < columnCount; i++) {
						// 获取列值
						Object columValue = rs.getObject(i + 1);

						// 获取每个列的列名
						// String columnName = rsmd.getColumnName(i + 1);
						String columnLabel = rsmd.getColumnLabel(i + 1);

						// 给t对象指定的columnName属性，赋值为columValue：通过反射
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
