package org.mysearch.util;

import org.apache.commons.lang3.StringUtils;

public class URLUtil {
	private static final String HTTP_SCHEME = "http://";
	private static final String HTTPS_SCHEME = "https://";
	public static String getUrlFromLine(String line,String suffix){
		int endIndex = line.indexOf(suffix);
		int beginIndex = line.indexOf(HTTP_SCHEME);
	    line = line.substring(beginIndex, endIndex+suffix.length());
	    System.out.println(line);
		return StringUtils.EMPTY;
	}
	public static String getNameFromUrl(String url){
		return url.substring(url.lastIndexOf("/"));
	}
}
