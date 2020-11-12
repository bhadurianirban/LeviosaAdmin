/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leviosa.ui.login;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author bhaduri
 */
public class LoginFilter implements Filter{
    @Inject 
    LoginController loginController;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // For the first application request there is no loginBean in the session so user needs to log in
        // For other requests loginBean is present but we need to check if user has logged in successfully
        if (loginController == null || loginController.getUserAuthDTO().getUserId() == null) {
            String contextPath = ((HttpServletRequest)request).getContextPath();
            contextPath = contextPath+"/faces/SessionExpired.xhtml";
            ((HttpServletResponse)response).sendRedirect(contextPath);
        }
         
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        
    }
    
}
