package com.service.scim.interceptors;

import com.service.scim.repositories.IRequestRepository;
import com.service.scim.models.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Intercepts all requests for logging purposes
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Autowired
    private final IRequestRepository requestDatabase;

    public RequestInterceptor(IRequestRepository requestDatabase) {
        this.requestDatabase = requestDatabase;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException {
        Request req = new Request()
                .generateId()
                .setTimestamp()
                .setMethod(request.getMethod())
                .setEndpoint(request.getRequestURI() + (request.getQueryString() != null ? "?" + URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8) : ""));

        request.setAttribute("rid", req.id);

        requestDatabase.save(req);

        return true;
    }
}
