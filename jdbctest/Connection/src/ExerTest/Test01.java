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
		System.out.println("����������");
		String name = scanner.next();
		System.out.println("����������");
		String email = scanner.next();
		System.out.println("����������");
		String birthday = scanner.next();
		
		String sql="insert into customers(name,email,birth)value(?,?,?)";
		int insertCount = Update(sql,name,email,birthday);
		if(insertCount>0) {
			System.out.print("��ӳɹ�");
		}else {
			System.out.print("���ʧ��");
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
}
