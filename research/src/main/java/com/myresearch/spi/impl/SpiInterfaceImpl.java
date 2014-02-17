package com.myresearch.spi.impl;

import com.myresearch.spi.SpiInterface;

public class SpiInterfaceImpl implements SpiInterface {

	@Override
	public void sayHello() {
		System.out.println("hello");
	}

}
