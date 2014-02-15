package com.myrearch.spring.hellointerface.impl;

import com.myrearch.spring.hellointerface.IHelloService;

public class IHelloServiceImpl implements IHelloService{

	@Override
	public void sayHello() {
		System.out.println("hello");
	}

}
