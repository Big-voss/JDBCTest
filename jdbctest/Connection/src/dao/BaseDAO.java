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
 * ��װ��������ݱ��DAO
 */
public abstract class BaseDAO<T> {
	private Class<T> clazz = null;
	
	{	
		//��ȡ��ǰBaseDAO������̳еĸ����еķ���
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType) genericSuperclass;
		
		Type[] typeArguments = paramType.getActualTypeArguments();//��ȡ�˸���ķ��Ͳ���
		clazz = (Class<T>) typeArguments[0];//���͵ĵ�һ������
		
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
	
			//ͨ�õĲ�ѯ����,���ڷ������ݱ��е�һ����¼(version 2.0�������ݿ�����)
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
	
			// ͨ�õĲ�ѯ���������ڷ������ݱ��еĶ�����¼���ɵļ��ϣ�version 2.0������������
			public  List<T> getForList(Connection conn,String sql, Object... args) {
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {

					ps = conn.prepareStatement(sql);
					for (int i = 0; i < args.length; i++) {
						ps.setObject(i + 1, args[i]);
					}

					rs = ps.executeQuery();
					// ��ȡ�������Ԫ���� :ResultSetMetaData
					ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
					// ͨ��ResultSetMetaData��ȡ������е�����
					int columnCount = rsmd.getColumnCount();
					// �������϶���
					ArrayList<T> list = new ArrayList<T>();
					while (rs.next()) {
						T t = clazz.newInstance();
						// ��������һ�������е�ÿһ����:��t����ָ�������Ը�ֵ
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
			
			//���ڲ�ѯ����ֵ��ͨ�õķ���
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