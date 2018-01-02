package com.ty.controller;
import java.util.List;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ty.entity.Customer;

public class ControllerTest {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction entityTransaction;
	@Before
	public void init(){
		//1.创建EntitymanagerFactory
		String persistenceUnitName = "jpa-1";
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		//2.创建EntityManager
		entityManager = entityManagerFactory.createEntityManager();
		//3.开启事务
		entityTransaction = entityManager.getTransaction();
		//4.开启事务
		entityTransaction.begin();
	}
	@After
	public void destroy(){
		//5.提交事务
		entityTransaction.commit();
		//6.关闭EntityManager
		entityManager.close();
		//7.关闭EntityManagerFactory
		entityManagerFactory.close();
	}
	//jpa的persistent和hibernate的save方法类似，只是persistent不能设置id值
	@Test
	public void testPersistent() {
		//4.持久化操作
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setEmail("@12300");
		customer.setLastName("ty");
		entityManager.persist(customer);
	}
	
	//和hibernate中的get方法类似,不能查询游离状态
	@Test
	public void testFind(){
		Customer customer = entityManager.find(Customer.class, 1);
		System.out.println(customer);
	}
	
	//和hibernate中的load方法类似，不能直接查询数据库，所以session关闭之后会产生懒加载异常
	@Test
	public void testGetReference(){
		Customer customer = entityManager.getReference(Customer.class, 1);
		System.out.println(customer.getClass().getName());
	}
	
	//与hibernate的delete方法类似，但是该方法只能移除持久化状态，不能移除游离态
	@Test
	public void testRemove(){
		//--------称为游离态------
//		Customer customer = new Customer();
//		customer.setId(2);
		Customer customer = entityManager.find(Customer.class, 1);
		entityManager.remove(customer);
	}
	/**
	 * 1.若传入一个临时对象，会创建一个新的对象，然后把临时的对象赋值到新的对象中，然后进行持久化操作
	 * 2.如果是游离对象：就是设置了id值，但是持久化游离状态时，不会把设置的id值保存在数据库中
	 * 3.如果游离对象设置的id值和数据库中的id值对应，则merge会执行update操作，而不是insert操作
	 */
	@Test 
	public void testMerge1(){
		Customer customer = new Customer();
		customer.setAge(21);
		customer.setEmail("461@qq.com");
		customer.setLastName("mr");
		Customer customer2 = entityManager.merge(customer);
		System.out.println(customer2);
	}
	@Test 
	public void testMerge2(){
		Customer customer = new Customer();
		customer.setId(100);
		customer.setAge(20);
		customer.setEmail("461@qq.com");
		customer.setLastName("mr_yy");
		Customer customer2 = entityManager.merge(customer);
		System.out.println(customer2);
	}
	@Test 
	public void testMerge3(){
		Customer customer = new Customer();
		customer.setId(3);
		customer.setAge(20);
		customer.setEmail("461@qq.com");
		customer.setLastName("mr_rrr");
		Customer customer2 = entityManager.merge(customer);
		System.out.println(customer2);
	}
	@Test
	public void testFlush(){
		Customer customer = entityManager.find(Customer.class, 2);
		System.out.println(customer);
		customer.setLastName("kaka");
	}
	/**
	 * 1.查询
	 */
	@Test
	public void testJPQL(){
		String jpql = "From Customer c where c.age>?";
		Query query  =  entityManager.createQuery(jpql);
		query.setParameter(1, 2);
		List<Customer> customers = query.getResultList();
		System.out.println(customers.size());
	}
	/**
	 * 获取部分的属性:在实体类中创建对应的构造器，然后在JPQL语句中，利用对应的构造器返回实体类的对象
	 */
	@Test
	public void testApart(){
		String jpql = "SELECT new Customer(c.lastName,c.age) FROM Customer c Where c.id>?";
		Query query = entityManager.createQuery(jpql);
		//设置参数的相对位置从1开始
		query.setParameter(1, 1);
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
	}
	
	@Test
	public void testOrderBy(){
		String jpql = "From Customer c where c.age>? ORDER BY c.age DESC";
		Query query  =  entityManager.createQuery(jpql);
		query.setParameter(1, 2);
		List<Customer> customers = query.getResultList();
		System.out.println(customers);
	}
}
