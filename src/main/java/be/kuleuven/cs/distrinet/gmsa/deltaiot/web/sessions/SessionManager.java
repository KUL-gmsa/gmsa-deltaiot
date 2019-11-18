package be.kuleuven.cs.distrinet.gmsa.deltaiot.web.sessions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.WebUtils;

import be.kuleuven.cs.distrinet.gmsa.deltaiot.util.Crypto;

public class SessionManager  {

	public static boolean startSession(String username, HttpServletResponse response) {
		Cookie cookie = new Cookie("username", Crypto.encrypt(username, ENCRYPTION_KEY));
		cookie.setMaxAge(3600);
		response.addCookie(cookie);
		return true;
	}


	public static boolean endSession(HttpServletResponse response) {
		Cookie removedCookie = new Cookie("username", null);
		removedCookie.setMaxAge(0);
		response.addCookie(removedCookie);
		return true;
	}
	
	public static boolean belongsToActiveSession(HttpServletRequest request) {
		var cookie = WebUtils.getCookie(request, "username");
		return cookie != null;
	}
	
	public static String getAssociatedUsername(HttpServletRequest request) {
		var cookie = WebUtils.getCookie(request, "username");
		return cookie != null ? Crypto.decrypt(cookie.getValue(), ENCRYPTION_KEY) : null;
	}

	
	public static final String ENCRYPTION_KEY = "@SUP3RS3CRE73NCRYP710NK3Y";
	
	
	
}
 