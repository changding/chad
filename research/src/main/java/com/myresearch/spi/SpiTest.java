package com.myresearch.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiTest {
	public static void main(String[] args) {
		ServiceLoader<SpiInterface>  inte = ServiceLoader.load(SpiInterface.class);
		Iterator<SpiInterface> iter  = inte.iterator();
		while(iter.hasNext()){
			iter.next().sayHello();
		}

	}
}
