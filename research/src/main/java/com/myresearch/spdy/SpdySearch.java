package com.myresearch.spdy;

import java.io.IOException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class SpdySearch {
	public static void main(String[] args) throws IOException {
		String test_url = "https://raw.github.com/square/okhttp/master/README.md";
		run(test_url);
		System.out.println("this line");
	}

	static void run(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).head().build();

		Response response = client.newCall(request).execute();
		System.out.println(response.body().string());		
		
		System.out.println(response.isSuccessful());
		
//		client.newCall(request).enqueue(new Callback(){
//
//			@Override
//			public void onFailure(Request request, IOException e) {
//				
//			}
//
//			@Override
//			public void onResponse(Response response) throws IOException {
//				System.out.println(response.body().string());
//			}
//			
//		})
		System.out.println("end");
	}
}
