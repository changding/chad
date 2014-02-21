package org.mysearch;

import org.apache.log4j.xml.DOMConfigurator;
import org.mysearch.crawl.CrawManager;
import org.mysearch.crawl.impl.CrawManagerImpl;
import org.mysearch.download.DownLoadManager;
import org.mysearch.download.impl.DownLoaderManagerImpl;


/**
 * Hello world!
 *
 */
public class CrawImage 
{
    public static void main( String[] args ) throws Exception
    {
    	DOMConfigurator.configure(CrawImage.class.getClassLoader().getResource("log4j.properties"));
    	if(args.length != 2){
    		System.out.println("args.length is not 2");
    	}
    	String url = args[0];//url
    	String absolutePath = args[1];//path
    	CrawManager cm = new CrawManagerImpl();
    	DownLoadManager dm = new DownLoaderManagerImpl();

    	dm.getImages(cm.crawlUrl(url), absolutePath);
    }
}
