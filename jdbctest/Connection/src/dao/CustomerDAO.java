package dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import blob.Customer;



public interface CustomerDAO {

	void insert(Connection conn,Customer cust);

	void deleteById(Connection conn,int id);

	void update(Connection conn,Customer cust);

	Customer getCustomerById(Connection conn,int id);

	List<Customer> getAll(Connection conn);

	Long getCount(Connection conn);
	
	Date getMaxBirth(Connection conn);
	
}	
