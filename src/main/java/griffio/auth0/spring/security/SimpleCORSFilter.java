package griffio.auth0.spring.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * https://spring.io/guides/gs/rest-service-cors/
 *
 * Reference implementation: org.apache.catalina.filters.CorsFilter
 *
 */
public class SimpleCORSFilter implements Filter {

  public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) sres;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "Authorization");
    chain.doFilter(sreq, sres);
  }

  public void init(FilterConfig filterConfig) {
  }

  public void destroy() {
  }

}