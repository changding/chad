package com.myresearch.jmx.exposure;

public class HelloWorld implements HelloWorldMBean {
	private String name;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	public void printHello() {
		System.out.println("Hello World, " + name);
	}

	public void printHello(String whoName) {
		System.out.println("Hello , " + whoName);
	}

}
