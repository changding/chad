package com.myresearch.asynchttpclient;

import java.util.concurrent.ExecutionException;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;

public class AsyncHttpClientTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		AsyncHttpClient client = new AsyncHttpClient();
		ListenableFuture<String> f = client.prepareGet("http://www.taobao.com").execute(new AsyncCompletionHandler<String>(){

			@Override
			public String onCompleted(Response response) throws Exception {
				System.out.println("complete");
				return response.getResponseBody("utf-8");
			}

			});
		System.out.println("end");
		System.out.println(f.get());
	}
	
	
}
