package dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.ResultSetMetaData;

import Util.JDBCUtil;

/*
 * 封装针对于数据表的DAO
 */
public abstract class BaseDAO<T> {
	private Class<T> clazz = null;
	
	{	
		//获取当前BaseDAO的子类继承的父类中的泛型
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) genericSuperclass;
		
		Type[] typeArguments = paramType.getActualTypeArguments();//获取了父类的泛型参数
		clazz = (Class<T>) typeArguments[0];//泛型的第一个参数
		
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
	
			//通用的查询操作,用于返回数据表中的一条记录(version 2.0考虑数据库事务)
			public  T getInstance(Connection conn,String sql, Object... args) {
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
	
			// 通用的查询操作，用于返回数据表中的多条记录构成的集合（version 2.0：考虑上事务）
			public  List<T> getForList(Connection conn,String sql, Object... args) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {

					ps = conn.prepareStatement(sql);
					for (int i = 0; i < args.length; i++) {
						ps.setObject(i + 1, args[i]);
					}

					rs = ps.executeQuery();
					// 获取结果集的元数据 :ResultSetMetaData
					ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
					// 通过ResultSetMetaData获取结果集中的列数
					int columnCount = rsmd.getColumnCount();
					// 创建集合对象
					ArrayList<T> list = new ArrayList<T>();
					while (rs.next()) {
						T t = clazz.newInstance();
						// 处理结果集一行数据中的每一个列:给t对象指定的属性赋值
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
						list.add(t);
					}

					return list;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					JDBCUtil.closeResource(null, ps, rs);

				}

				return null;
			}
			
			//用于查询特殊值的通用的方法
			public <E> E getValue(Connection conn,String sql,Object...args){
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = conn.prepareStatement(sql);
					for(int i = 0;i < args.length;i++){
						ps.setObject(i + 1, args[i]);
						
					}
					
					rs = ps.executeQuery();
					if(rs.next()){
						return (E) rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					JDBCUtil.closeResource(null, ps, rs);
					
				}
				return null;
				
			}
}