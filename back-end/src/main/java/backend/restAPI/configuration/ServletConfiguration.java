package backend.restAPI.configuration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ServletConfiguration implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    System.out.println("---------------------------");
    System.out.println("---------------------------");
    System.out.println("---------------------------");

    if (((HttpServletRequest) request).getRequestURI().equals("/")) {
      chain.doFilter(request, response);
      return;
    }

    if (((HttpServletRequest) request).getRequestURI().startsWith("/api/")
        || ((HttpServletRequest) request).getRequestURI().startsWith("/client/")) {
      chain.doFilter(request, response);
    } else {
      // return "forward:/client/index.html";
      request.getRequestDispatcher("/").forward(request, response);
    }

  }

  @Override
  public void destroy() {}

}
