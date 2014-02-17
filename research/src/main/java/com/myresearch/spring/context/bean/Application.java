package com.myresearch.spring.context.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.myresearch.spring.hellointerface.IHelloService;
import com.myresearch.spring.hellointerface.impl.IHelloServiceImpl;

@Configuration
@ComponentScan
public class Application {
	@Bean
	public MyBean myBean() {
		return new MyBean();
	}
	@Bean
	public IHelloService helloService(){
		return new IHelloServiceImpl();
	}
}
