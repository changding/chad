package org.myresearch;

import org.junit.Test;
import org.mysearch.crawl.CrawManager;
import org.mysearch.crawl.impl.CrawManagerImpl;
import org.mysearch.download.DownLoadManager;
import org.mysearch.download.impl.DownLoaderManagerImpl;
import org.mysearch.util.URLUtil;

import com.google.common.collect.Lists;

public class CrawManagerTest {
	@Test
public void test_抓取图片() throws Exception{
	CrawManager cm = new CrawManagerImpl();
	String url = "http://try.taobao.com/item.htm?spm=a1z0i.1000798.1000584.3.w2Q12y&id=10370079&scm=1007.133.0.0&jlogid=p2817051326940";
	cm.crawlUrl(url);
}
	@Test
	public void test_图片url(){
		String content = " <img src=\"http://img01.taobaocdn.com/tps/i1/T1G2xPXs4dXXb__l_X-140-46.png\" alt=\"????????Logo\">"; 
		URLUtil.getUrlFromLine(content, ".png");
		
	}
	@Test
	public void test_将文件写到本地() throws Exception{
		String content = "http://img01.taobaocdn.com/tps/i1/T1G2xPXs4dXXb__l_X-140-46.png";
		DownLoadManager dm = new DownLoaderManagerImpl();
		dm.getImages(Lists.newArrayList(content), "d://");
		
	}
}
