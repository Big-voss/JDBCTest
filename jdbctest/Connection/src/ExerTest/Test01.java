package ExerTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import org.junit.Test;

import Util.JDBCUtil;

public class Test01 {
	@Test
	public void Insert() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("请输入姓名");
		String name = scanner.next();
		System.out.println("请输入邮箱");
		String email = scanner.next();
		System.out.println("请输入生日");
		String birthday = scanner.next();
		
		String sql="insert into customers(name,email,birth)value(?,?,?)";
		int insertCount = Update(sql,name,email,birthday);
		if(insertCount>0) {
			System.out.print("添加成功");
		}else {
			System.out.print("添加失败");
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
}
