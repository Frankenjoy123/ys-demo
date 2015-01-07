package com.yunsoo.yunsooDataService;

//import java.io.*;
import java.util.Date;

import org.hibernate.Session;
//import org.springframework.beans.factory.BeanFactory;  
//import org.springframework.beans.factory.xml.XmlBeanFactory;  
//import org.springframework.core.io.ClassPathResource;  
//import org.springframework.core.io.Resource;  
import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationEvent;
import com.yunsoo.dao.util.*;
import com.yunsoo.model.*;
import com.yunsoo.hibernate.HibernateSessionManager;
import com.yunsoo.service.*;
/**
 * Run app!
 *
 */
public class App {
	public static void main(String[] args){
		System.out.println("Start running App.Main()...");

		//Resource r = new ClassPathResource("applicationContext.xml");
		//BeanFactory factory = new XmlBeanFactory(r);	
		//ProductKeyDao pkeyDao = (ProductKeyDao)factory.getBean("d");		
		ApplicationContext applicationContext = SpringDaoUtil.getApplicationContext();
		EmployeeService employeeService = (EmployeeService) applicationContext.getBean("employeeService");        
        System.out.println("List : "+employeeService.getAllEmployees());
        
		//ProductKeyDaoImpl daoImpl = (ProductKeyDaoImpl)applicationContext.getBean("productkeyDao");
		ProductKeyService productKeyService = (ProductKeyService)applicationContext.getBean("productKeyService");
		
		ProductKey prodKey = new ProductKey();
		// prodKey.setId(0);
		prodKey.setKey("DTEST8932");
		prodKey.setProductId(25434L);
		prodKey.setCreatedAccountId(45);
		prodKey.setCreatedClientId(19);
		prodKey.setkeyStatusId(3);
		prodKey.setkeyTypeId(7);
		prodKey.setCreatedDateTime(new Date());
		
		productKeyService.save(prodKey);
		ProductKey theKey = productKeyService.getById(1);
		System.out.println("The key is: " + theKey.getKey());

		prodKey.setkeyStatusId(10);
		productKeyService.update(prodKey);

		// CreateProductKey();
		// Session session = HibernateSessionManager.getSessionFactory()
		// .openSession();
		// session.beginTransaction();
		// Product myproduct = new Product();
		// // myproduct.setId(1);
		// myproduct.setProductStatusId(4);
		// myproduct.setProductTypeId(81);
		// myproduct.setCreatedDateTime(new Date());
		// myproduct.setManufacturingDate(new Date());
		// session.save(myproduct);
		// session.getTransaction().commit();
		System.out.println("Done!");
	}

	public static void CreateProductKey() {
		Session session = HibernateSessionManager.getSessionFactory()
				.openSession();
		session.beginTransaction();

		ProductKey prodKey = new ProductKey();
		// prodKey.setId(0);
		prodKey.setKey("ABS234S");
		prodKey.setProductId(23434L);
		prodKey.setCreatedAccountId(1);
		prodKey.setCreatedClientId(89);
		prodKey.setkeyStatusId(3);
		prodKey.setkeyTypeId(5);
		prodKey.setCreatedDateTime(new Date());
		session.save(prodKey);
		session.getTransaction().commit();
	}
}
