package com.backendcore.backendcore.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LanguageInterceptor implements HandlerInterceptor {

    private static final String DEFAULT_LANG = "en";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Extract the lang parameter from the request
        String lang = request.getParameter("lang");
        if (lang == null || lang.isEmpty()) {
            lang = DEFAULT_LANG;  // Default to English if lang parameter is missing
        }

        // Set the lang attribute in the request scope
        request.setAttribute("lang", lang);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // No post-handle action required for this interceptor
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // No after-completion action required for this interceptor
    }
}
