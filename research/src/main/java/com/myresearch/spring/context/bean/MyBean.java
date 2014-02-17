package com.myresearch.spring.context.bean;

import org.springframework.beans.factory.annotation.Autowired;

import com.myresearch.spring.hellointerface.IHelloService;

public class MyBean {
	@Autowired
	private IHelloService helloService;

	public void print() {
		helloService.sayHello();
	}
}
