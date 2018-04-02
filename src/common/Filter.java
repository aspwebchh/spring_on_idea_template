package common;

import org.springframework.stereotype.Service;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Filter extends HttpServlet implements javax.servlet.Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Env.EnvBase env = Env.getEnv(request);
        HttpServletResponse httpServletResponse  =(HttpServletResponse) response;
        if( env instanceof Env.TestEnv) {
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://192.168.110.233");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        }
        chain.doFilter(request,httpServletResponse);
    }

    public void init(FilterConfig arg0) throws ServletException {

    }
}
