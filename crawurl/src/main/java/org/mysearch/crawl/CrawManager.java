package org.mysearch.crawl;

import java.util.List;

public interface CrawManager {
public List<String> crawlUrl(String url) throws Exception ;
public List<String> crawImg(String url) throws Exception ;
}
