package cl.zeke.framework.jsf.support;

/**
 * Created by takeda on 04-01-16.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewExpiredFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(ViewExpiredFilter.class);

    private static final String URL_REDIRECT = "/";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("VIEW EXPIRED FILTER - INIT");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            // chain...
            HttpServletRequest request = (HttpServletRequest) req;
            if (isAjax(request) && !request.isRequestedSessionIdValid()) {
                LOG.warn("Session expiration during ajax request, partial redirect to login page");
                HttpServletResponse response = (HttpServletResponse) resp;
                response.getWriter().print(xmlPartialRedirectToPage(request, getUrlRedirect()));
                response.flushBuffer();
            } else {
                chain.doFilter(req, resp);
            }
        } catch (IOException | ServletException e) {
            // redirect to error page
            HttpServletRequest request = (HttpServletRequest) req;
            request.getSession().setAttribute("lastException", e);
            request.getSession().setAttribute("lastExceptionUniqueId", e.hashCode());
            LOG.error("EXCEPTION unique id: " + e.hashCode(), e);
            HttpServletResponse response = (HttpServletResponse) resp;
            if (!isAjax(request)) {
                response.sendRedirect(request.getContextPath() + request.getServletPath() + getUrlRedirect());
            } else {
                // let's leverage jsf2 partial response
                response.getWriter().print(xmlPartialRedirectToPage(request, getUrlRedirect()));
                response.flushBuffer();
            }
        }
    }

    @Override
    public void destroy() {
        LOG.info("VIEW EXPIRED FILTER - DESTROY");
    }

    private String xmlPartialRedirectToPage(HttpServletRequest request, String page) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<partial-response><redirect url=\"").append(request.getContextPath()).append(page).append("\"/></partial-response>");
        return sb.toString();
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    protected String getUrlRedirect() {
        return URL_REDIRECT;
    }

}