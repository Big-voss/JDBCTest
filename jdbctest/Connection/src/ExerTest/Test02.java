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
		System.out.println("请输入4级or6级");
		int type = scanner.nextInt();
		System.out.println("请输入身份证号");
		String IDCard = scanner.next();
		System.out.println("请输入准考证号");
		String ExamCard = scanner.next();
		System.out.println("请输入学生姓名");
		String StudentName = scanner.next();
		System.out.println("请输入地址");
		String Location = scanner.next();
		System.out.println("请输入成绩");
		int grade = scanner.nextInt();
		
		String sql = "insert into examstudent(type,IDCard,ExamCard,StudentName,Location,grade)values(?,?,?,?,?,?)";
		int insertCount = Update(sql,type,IDCard,ExamCard,StudentName,Location,grade);
		if(insertCount>0) {
			System.out.println("添加成功");
		}else {
			System.out.println("添加失败");
		}
	}
	
	@Test
	public void queryWithIDCardOrExamCard() {
		System.out.println("请选择输入类型:");
		System.out.println("a.准考证号");
		System.out.println("b.身份证号");
		Scanner scanner = new Scanner(System.in);
		String selection = scanner.next();
		if("a".equalsIgnoreCase(selection)) {
			System.out.println("请输入准考证号:");
			String examCard = scanner.next();
			String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
			Student student = getInstance(Student.class,sql,examCard);
			if(student != null) {
				System.out.println(student);				
			}else {
				System.out.println("输入的准考证号有误!");
			}
		}else if("b".equalsIgnoreCase(selection)) {
			System.out.println("请输入身份证号:");
			String IDCard = scanner.next();
			String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where IDCard = ?";
			Student student = getInstance(Student.class,sql,IDCard);
			if(student != null) {
				System.out.println(student);				
			}else {
				System.out.println("输入的身份证号有误!");
			}
		}else {
			System.out.println("输入有误请重新进入程序");
		}
	}
	
	
	@Test
	public void test03DeleteByExamCard() {
		System.out.println("请输入学生的考号: ");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		
		String sql = "select FlowID flowID,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard = ?";
		Student student = getInstance(Student.class,sql ,examCard);
		if(student==null) {
			System.out.println("查无此人,重新输入");
		}else {
			String sql1 = "delete from examstudent where examCard = ?";
			int deleteCount = Update(sql1,examCard);
			if(deleteCount>0) {
				System.out.println("删除成功");
			}
		}
	}
	@Test
	public void testDeleteByExamCard1(){
		System.out.println("请输入学生的考号：");
		Scanner scanner = new Scanner(System.in);
		String examCard = scanner.next();
		String sql = "delete from examstudent where examCard = ?";
		int deleteCount = Update(sql, examCard);
		if(deleteCount > 0){
			System.out.println("删除成功");
		}else{
			System.out.println("查无此人，请重新输入");
		}
		
	}
	
	
	
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
			JDBCUtil.closeResource(conn, ps, rs);

		}

		return null;
	}
}
