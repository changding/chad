package com.myresearch.spring.context;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.myresearch.spring.context.bean.Application;
import com.myresearch.spring.context.bean.MyBean;
import com.myresearch.spring.hellointerface.IHelloService;

/**
 * @author jingsheng
 * @description 对于新的context的测试
 * 
 */
public class AnnotationConfigApplicationContextTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		//根据类名获取
		MyBean bean = context.getBean(MyBean.class);
		IHelloService hello = context.getBean(IHelloService.class);
		bean.print();
		hello.sayHello();
		//根据名字获取
		bean = context.getBean("myBean",MyBean.class);
		hello = context.getBean("helloService",IHelloService.class);
		bean.print();
		hello.sayHello();
		//获取context
		Application app = context.getBean("application",Application.class);
		app.helloService().sayHello();
		
	}
}
