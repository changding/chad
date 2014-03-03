package org.mysearch.crawl.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mysearch.crawl.CrawManager;
import org.mysearch.util.URLUtil;

import com.google.common.collect.Lists;

public class CrawManagerImpl implements CrawManager{
    private static final String IMG_SUFFIX[] = new String[]{".jpg",".jpeg",".png",".gif",".bmp"};
    private static final Logger logger =Logger.getLogger(CrawManager.class.getName());
	@Override
	public List<String> crawlUrl(String url) throws Exception {
		List<String> urlList = Lists.newArrayList();
		InputStream is = null;
		URLConnection urlConnection = null;
		BufferedReader br = null;
		try{
			URL realUrl = new URL(url);
			urlConnection = realUrl.openConnection();
			is = urlConnection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,Charset.forName("utf-8")));
			String line = null;
			while(br!= null && ((line = br.readLine())!= null)){
				for(String suffix:IMG_SUFFIX){
					if(StringUtils.isNoneEmpty(line) && StringUtils.contains(line, suffix)){
						urlList.add(URLUtil.getUrlFromLine(line, suffix));
					}
				}
			}
		}catch(Exception e){
			logger.error("occur exception",e);
		}finally{
			is.close();
			br.close();
		}
		return urlList;
	}

	@Override
	public List<String> crawImg(String url) {
		try{
			URL realUrl = new URL(url);
			URLConnection urlConnection = realUrl.openConnection();
			InputStream is = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("utf-8")));
			String line = null;
			while(br!= null && ((line = br.readLine())!= null)){
				System.out.println(line);
			}
			
		}catch(Exception e){
			
		}
		
		return null;
	}

}
