package com.atguigu.jpa.test;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.ejb.QueryHints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atguigu.jpa.helloworld.Customer;
import com.atguigu.jpa.helloworld.Order;

public class JPATest {

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction transaction;
	
	@Before
	public void init(){
		entityManagerFactory = Persistence.createEntityManagerFactory("jpa-1");
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}
	
	@After
	public void destroy(){
		transaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}
	
	@Test
	public void testExecuteUpdate(){
		Customer customer = new Customer();
		customer.setAge(18);
		customer.setBirth(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("m@163.com");
		customer.setLastName("xx");
		
		Order order1 = new Order();
		order1.setOrderName("O-MM-1");
		
		Order order2 = new Order();
		order2.setOrderName("O-MM-2");
		
		//设置关联关系
		order1.setCustomer(customer);
		order2.setCustomer(customer);
		
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}
}
