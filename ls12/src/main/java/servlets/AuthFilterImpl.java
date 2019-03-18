package servlets;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilterImpl implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        if (httpRequest.getSession(false) != null
                || ("POST".equals(httpRequest.getMethod())&& "/admin".equals(httpRequest.getRequestURI()))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            TemplateProcessor templateProcessor = new TemplateProcessor();
            servletResponse.setContentType("text/html;charset=utf-8");
            servletResponse.getWriter().println(templateProcessor.getPage("login.html", null));
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void destroy() {

    }
}
