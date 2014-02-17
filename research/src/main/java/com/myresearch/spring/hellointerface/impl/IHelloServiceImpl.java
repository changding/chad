package com.myresearch.spring.hellointerface.impl;

import com.myresearch.spring.hellointerface.IHelloService;

public class IHelloServiceImpl implements IHelloService{

	@Override
	public void sayHello() {
		System.out.println("hello");
	}

}
