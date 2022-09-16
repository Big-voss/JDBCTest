package dao;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import blob.Customer;

public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO{

	@Override
	public void insert(Connection conn, Customer cust) {
		String sql = "insert into customers(name,email,birth)values(?,?,?)";
		Update(conn, sql, cust.getName(),cust.getEmail(),cust.getBirth());
	}

	@Override
	public void deleteById(Connection conn, int id) {
		String sql = "delete from customers where id = ?";
		Update(conn, sql , id);
		
	}

	@Override
	public void update(Connection conn, Customer cust) {
		String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
		Update(conn, sql, cust.getName(),cust.getEmail(),cust.getBirth(),cust.getId());
		
	}

	@Override
	public Customer getCustomerById(Connection conn, int id) {
		String sql = "select id,name,email,birth from customers where id = ?";
		Customer customer = getInstance(conn, sql , id);
		return customer;
	}

	@Override
	public List<Customer> getAll(Connection conn) {
		String sql = "select id,name,email,birth from customers";
		List<Customer> list = getForList(conn, sql);
		return list;
	}

	@Override
	public Long getCount(Connection conn) {
		String sql = "select count(*) from customers";
		return getValue(conn, sql);
	}

	@Override
	public Date getMaxBirth(Connection conn) {
		String sql = "select max(birth) from customers";
		return getValue(conn, sql);
	}

}
