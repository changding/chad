package com.myresearch.spring.context;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.myresearch.spring.context.bean.Application;
import com.myresearch.spring.context.bean.MyBean;
import com.myresearch.spring.hellointerface.IHelloService;

/**
 * @author jingsheng
 * @description �����µ�context�Ĳ���
 * 
 */
public class AnnotationConfigApplicationContextTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		//����������ȡ
		MyBean bean = context.getBean(MyBean.class);
		IHelloService hello = context.getBean(IHelloService.class);
		bean.print();
		hello.sayHello();
		//�������ֻ�ȡ
		bean = context.getBean("myBean",MyBean.class);
		hello = context.getBean("helloService",IHelloService.class);
		bean.print();
		hello.sayHello();
		//��ȡcontext
		Application app = context.getBean("application",Application.class);
		app.helloService().sayHello();
		
	}
}
