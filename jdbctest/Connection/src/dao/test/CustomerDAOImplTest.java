package dao.test;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import org.junit.Test;

import Util.JDBCUtil;
import blob.Customer;
import dao.CustomerDAOImpl;

public class CustomerDAOImplTest {

	private CustomerDAOImpl dao = new CustomerDAOImpl();
	
	@Test
	public void testInsert() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			Customer cust = new Customer(1, "��С��", "xiaofei@126.com",new Date(43534646435L));
			dao.insert(conn, cust);
			System.out.println("��ӳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeResource(conn, null);
			
		}
		
	}

	@Test
	public void testDeleteById() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			
			dao.deleteById(conn, 13);
			
			
			System.out.println("ɾ���ɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeResource(conn, null);
			
		}
	}

	@Test
	public void testUpdateConnectionCustomer() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			Customer cust = new Customer(18,"�����","beiduofen@126.com",new Date(453465656L));
			dao.update(conn, cust);
			
			
			System.out.println("�޸ĳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeResource(conn, null);
			
		}
	}

	@Test
	public void testGetCustomerById() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection3();
			
			Customer cust = dao.getCustomerById(conn, 19);
			System.out.println(cust);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeResource(conn, null);
			
		}
	}

	@Test
	public void testGetAll() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			
			List<Customer> list = dao.getAll(conn);
			list.forEach(System.out::println);
			
			
			System.out.println("");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeResource(conn, null);
			
		}
	}

	@Test
	public void testGetCount() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			
			Long count = dao.getCount(conn);
			
			System.out.println("���еļ�¼��Ϊ��" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeResource(conn, null);
			
		}
	}

	@Test
	public void testGetMaxBirth() {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			
			Date maxBirth = dao.getMaxBirth(conn);
			
			System.out.println("��������Ϊ��" + maxBirth);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeResource(conn, null);
			
		}
	}

}
