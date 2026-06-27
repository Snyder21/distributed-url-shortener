package com.pankaj.urlshortener.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.pankaj.urlshortener.ratelimiter.RateLimiter;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;
    private final ClientIpResolver clientIpResolver;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if ("POST".equalsIgnoreCase(request.getMethod())) {


            String clientIp = clientIpResolver.getClientIp(request);
            rateLimiter.validateRequest(clientIp);
        }

        return true;
    }
}
