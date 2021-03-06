package com.flex.controllers;

import com.flex.config.jwt.JwtProvider;
import com.flex.models.ExtendedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class BaseController {

    private static final String spaClientName = "client";

    protected JwtProvider provider;

    @Autowired
    public BaseController(JwtProvider provider) {
        this.provider = provider;
    }


    protected String getViewByHeader(HttpServletRequest request, Model model, String partialView) {
        String header = request.getHeader("partial");
        if (header != null && header.equals("true")) {
            return partialView;
        }
        prepareSpaClient(request, model);
        return spaClientName;
    }

    protected void prepareSpaClient(HttpServletRequest request, Model model) {
        setAuthenticated(request, model);
        setContent(request, model);
    }

    protected void setAuthenticated(HttpServletRequest request, Model model) {
        Optional<Cookie> cookie = getTokenCookies(request);
        if (cookie.isPresent()) {
            String value = cookie.get().getValue();
            if (provider.validateToken(value)) {
                model.addAttribute("authenticated", true);
            }
        }
    }

    protected void setContent(HttpServletRequest request, Model model) {
        String path = request.getServletPath() + '?';
        Enumeration<String> paramsNames = request.getParameterNames();
        for (; paramsNames.hasMoreElements(); ) {
            String param = paramsNames.nextElement();
            path += String.format("%s=%s&", param, request.getParameter(param));
        }
        model.addAttribute("content", path.substring(1));
    }

    protected Optional<Cookie> getTokenCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return Optional.empty();
        return Arrays.stream(cookies).
                filter(item -> item.getName().equals("jwt")).findFirst();
    }

    public static boolean hasRole(ExtendedUserDetails details, String role) {
        Collection<GrantedAuthority> auth = details.getAuthorities();
        return auth.contains(new SimpleGrantedAuthority(role));
    }
}