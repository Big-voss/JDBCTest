package ExerTest;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import org.junit.Test;

import com.mysql.jdbc.ResultSetMetaData;

import Util.JDBCUtil;

public class Test02 {
	@Test
	public void testInsert() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("������4��or6��");
		int type = scanner.nextInt();
		System.out.println("���������֤��");
		String IDCard = scanner.next();
		System.out.println("������׼��֤��");
		String ExamCard = scanner.next();
		System.out.println("������ѧ������");
		String StudentName = scanner.next();
		System.out.println("�������ַ");
		String Location = scanner.next();
		System.out.println("������ɼ�");
		int grade = scanner.nextInt();
		
		String sql = "insert into examstudent(type,IDCard,ExamCard,StudentName,Location,grade)values(?,?,?,?,?,?)";
		int insertCount = Update(sql,type,IDCard,ExamCard,StudentName,Location,grade);
		if(insertCount>0) {
			System.out.println("��ӳɹ�");
		}else {
			System.out.println("���ʧ��");
		}
	}
	
	@Test
	public void queryWithIDCardOrExamCard() {
		System.out.println("��ѡ����������:");
		System.out.println("a.׼��֤��");
		System.out.println("b.���֤��");
		Scanner scanner = new Scanner(System.in);
		String selection = scanner.next();
		if("a".equalsIgnoreCase(selection)) {
			System.out.println("������׼��֤��:");
			String examCard = scanner.next();
			String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
			Student student = getInstance(Student.class,sql,examCard);
			if(student != null) {
				System.out.println(student);				
			}else {
				System.out.println("�����׼��֤������!");
			}
		}else if("b".equalsIgnoreCase(selection)) {
			System.out.println("���������֤��:");
			String IDCard = scanner.next();
			String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where IDCard = ?";
			Student student = getInstance(Student.class,sql,IDCard);
			if(student != null) {
				System.out.println(student);				
			}else {
				System.out.println("��������֤������!");
			}
		}else {
			System.out.println("�������������½������");
		}
	}
	
	
	@Test
	public void test03DeleteByExamCard() {
		System.out.println("������ѧ���Ŀ���: ");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		
		String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
		Student student = getInstance(Student.class,sql ,examCard);
		if(student==null) {
			System.out.println("���޴���,��������");
		}else {
			String sql1 = "delete from examstudent where examCard = ?";
			int deleteCount = Update(sql1,examCard);
			if(deleteCount>0) {
				System.out.println("ɾ���ɹ�");
			}
		}
	}
	@Test
	public void testDeleteByExamCard1(){
		System.out.println("������ѧ���Ŀ��ţ�");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		String sql = "delete from examstudent where examCard = ?";
		int deleteCount = Update(sql, examCard);
		if(deleteCount > 0){
			System.out.println("ɾ���ɹ�");
		}else{
			System.out.println("���޴��ˣ�����������");
		}
		
	}
	
	
	
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
	
	public <T> T getInstance(Class<T> clazz,String sql, Object... args) {
		Connection conn = null;
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
			JDBCUtil.closeResource(conn, ps, rs);

		}

		return null;
	}
}
