/*
 * oxTrust is available under the MIT License (2008). See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (c) 2017, Gluu
 */
package org.gluu.oxauth.client.logout;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.gluu.oxauth.client.session.AbstractOAuthFilter;
import org.gluu.oxauth.client.session.SignOutHandler;

/**
 * Filter implementation to intercept all requests and attempt to logout
 * the client by redirecting them to OAuth.
 * 
 * @author Dmitry Ognyannikov
 */
public class LogoutFilter extends AbstractOAuthFilter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
	final HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        HttpSession session = request.getSession(false);
        
        String urlToRedirectTo;
        try {
            SignOutHandler signOutHandler = SignOutHandler.instance();
            urlToRedirectTo = signOutHandler.getOAuthLogoutUrl(request);
            if (urlToRedirectTo == null) {
                urlToRedirectTo = signOutHandler.constructRedirectUrl(request);
            }
        } catch (Exception ex) {
            log.error("Failed to preapre request to OAuth server", ex);
            if (session != null) {
                // clear session
                session.invalidate();
            }
            return;
        }
        
        if (session != null) {
            // clear session
            session.invalidate();
        }
        
        log.debug("Redirecting to \"" + urlToRedirectTo + "\"");
        response.sendRedirect(urlToRedirectTo);
    }
    
}
