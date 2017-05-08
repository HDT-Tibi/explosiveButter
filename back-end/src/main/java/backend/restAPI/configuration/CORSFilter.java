package backend.restAPI.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.GenericFilterBean;

/**
 * In CORS the browser makes a request before you're request is made with
 * OPTIONS method.
 *
 * <p>
 * This filter is used for specifying the headers and methods that the client is
 * allowed to add in the request.
 *
 */

public class CORSFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		if (((HttpServletRequest) req).getMethod().equals("OPTIONS")) {

			HttpServletResponse response = (HttpServletResponse) res;
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, x-auth-token");

		} else {

			chain.doFilter(req, res);

		}
	}
}
