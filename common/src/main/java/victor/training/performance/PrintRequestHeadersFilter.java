package victor.training.performance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConditionalOnWebApplication(type = Type.SERVLET)
public class PrintRequestHeadersFilter implements Filter {
  private static final Logger log = LoggerFactory.getLogger(PrintRequestHeadersFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    if (!httpRequest.getRequestURI().contains("prometheus")) {
      Map<String, String> headerMap = Collections.list(httpRequest.getHeaderNames()).stream().collect(Collectors.toMap(name -> name, httpRequest::getHeader));
      log.info("Request headers: " + headerMap);
    }
    chain.doFilter(request, response);
  }
}
