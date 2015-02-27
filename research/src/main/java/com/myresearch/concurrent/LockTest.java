package com.myresearch.concurrent;

public class LockTest {
	public static void main(String[] args) throws InterruptedException {
		Counter counter = new Counter();
		Thread a = new Thread(new A(counter));
		Thread b = new Thread(new B(counter));
		a.start();
		b.start();

	}
}

class A implements Runnable {
	private Counter counter;

	A(Counter counter) {
		this.counter = counter;
	}

	@Override
	public void run() {
		while (true) {
			System.out.println(Thread.currentThread().getName()+":"+counter.getCounter());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

class B implements Runnable {
	private Counter counter;

	B(Counter counter) {
		this.counter = counter;
	}
	@Override
	public void run() {
		while (true) {
			counter.incr();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

class Counter {
	private  volatile int counter = 0;
	
	public  void  incr() {
			counter++;
			System.out.println(Thread.currentThread().getName()+":"+counter);
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
}