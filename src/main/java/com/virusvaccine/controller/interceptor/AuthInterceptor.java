package com.virusvaccine.controller.interceptor;

import com.virusvaccine.controller.annotation.Authorized;
import com.virusvaccine.exception.UnauthorizedException;
import com.virusvaccine.service.AccountService.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Objects;

import static com.virusvaccine.service.AccountService.SESSION_KEY_ROLE;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (!handlerMethod.hasMethodAnnotation(Authorized.class)) return true;
        else{
            Authorized authorizedAnnotation = handlerMethod.getMethodAnnotation(Authorized.class);
            Role role = getSessionRole(request);
            if (Objects.requireNonNull(authorizedAnnotation).value() != role)
                throw new UnauthorizedException();
            return true;
        }
    }

    private Role getSessionRole(HttpServletRequest request){
        HttpSession session = request.getSession();
        Object role = session.getAttribute(SESSION_KEY_ROLE);
        if (role == null)
            throw new UnauthorizedException();

        return (Role) role;
    }
}
