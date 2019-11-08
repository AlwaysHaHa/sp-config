package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	public static String getCookieValue(HttpServletRequest request,String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length==0) {
			return null;
		}
		String value = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(cookieName)) {
				value = cookie.getValue();
				break;
			}
		}
		return value;
	} 
	
	public static void addCookie(HttpServletRequest request,HttpServletResponse response,String cookieName,String cookieValue,int seconds,String domain) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(seconds);
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}
	
}
