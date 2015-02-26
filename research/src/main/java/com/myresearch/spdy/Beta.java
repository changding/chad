package com.myresearch.spdy;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;


import org.springframework.util.StringUtils;

public class Beta {
public static void main(String[] args) throws IOException {
	//FileInputStream is = new FileInputStream(new File("/Users/jingsheng/Documents/beta.rtf"));
	BufferedReader br = new BufferedReader(new FileReader(new File("/Users/jingsheng/Documents/betaUser.txt")));
	String line = br.readLine();
	int count = 0;
	while(line != null){
		if(!StringUtils.isEmpty(line)){	
			try{			
				Long.parseLong(line.trim());
				System.out.println(line);
				count ++;
			}catch(Exception e){
				
			}
		}
		line = br.readLine();
	}
	br.close();
	System.out.println("sum is:"+count);
}
}
