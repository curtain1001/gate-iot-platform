package net.pingfang.framework.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-20 16:53
 */
@Component
public class RequestListener implements ServletRequestListener {
	public void requestInitialized(ServletRequestEvent sre) {
		// 将所有request请求都携带上httpSession
		((HttpServletRequest) sre.getServletRequest()).getSession();

	}

	public RequestListener() {
		// TODO Auto-generated constructor stub
	}

	public void requestDestroyed(ServletRequestEvent arg0) {
		// TODO Auto-generated method stub
	}
}
