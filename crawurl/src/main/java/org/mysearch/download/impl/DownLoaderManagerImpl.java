package org.mysearch.download.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.log4j.Logger;
import org.mysearch.crawl.CrawManager;
import org.mysearch.download.DownLoadManager;
import org.mysearch.util.URLUtil;


public class DownLoaderManagerImpl implements DownLoadManager{
    private static final Logger logger =Logger.getLogger(CrawManager.class.getName());
	@Override
	public void getImages(List<String> imgUrls, String absolutePath) throws Exception {
		FileOutputStream fos = null;
		File f = null;
		InputStream is = null;
		URLConnection connection = null;
		for(String url :imgUrls ){
			try {
				URL realUrl = new URL(url);
				connection = realUrl.openConnection();
				is = connection.getInputStream();
				f = new File(absolutePath+File.separatorChar+URLUtil.getNameFromUrl(url));
				fos = new FileOutputStream(f);
				fos.write(readInputStream(is));
			} catch (Exception e) {
				logger.error("occur exception",e);
			}finally{
				fos.flush();
				fos.close();
				is.close();
			}
		}
		
	}
    public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[8192];//8K
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1 ){  
            outStream.write(buffer, 0, len);  
        }  
        inStream.close();  
        return outStream.toByteArray();  
    } 
}
