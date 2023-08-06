package com.service.scim.interceptors;

import com.service.scim.database.RequestDatabase;
import com.service.scim.models.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Intercepts all requests for logging purposes
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Autowired
    RequestDatabase db;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UnsupportedEncodingException {
        Request req = new Request()
                .generateId()
                .setTimestamp()
                .setMethod(request.getMethod())
                .setEndpoint(request.getRequestURI() + (request.getQueryString() != null ? "?" + URLDecoder.decode(request.getQueryString(), "UTF-8") : ""));

        request.setAttribute("rid", req.id);

        db.save(req);

        return true;
    }
}
