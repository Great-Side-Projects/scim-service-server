package com.service.scim.interceptors;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.service.scim.models.Request;
import com.service.scim.repositories.IRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class RequestInterceptorTest {

    @Mock
    private IRequestRepository requestDatabase;

    @InjectMocks
    private RequestInterceptor requestInterceptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void preHandleSavesRequestWithDecodedQueryString() throws UnsupportedEncodingException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/scim/v2/users");
        when(request.getQueryString()).thenReturn("param=%20value");

        boolean result = requestInterceptor.preHandle(request, response, new Object());

        verify(requestDatabase, times(1)).save(any(Request.class));
        assertTrue(result);
    }

    @Test
    void preHandleSavesRequestWithoutQueryString() throws UnsupportedEncodingException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/scim/v2/users");
        when(request.getQueryString()).thenReturn(null);

        boolean result = requestInterceptor.preHandle(request, response, new Object());

        verify(requestDatabase, times(1)).save(any(Request.class));
        assertTrue(result);
    }

    @Test
    void preHandleSetsRequestIdAttribute() throws UnsupportedEncodingException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/scim/v2/users");
        when(request.getQueryString()).thenReturn(null);

        requestInterceptor.preHandle(request, response, new Object());

        verify(request).setAttribute(eq("rid"), anyString());
    }

    @Test
    void preHandleHandlesUnsupportedEncodingException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/scim/v2/users");
        when(request.getQueryString()).thenReturn("filter=userName eq \"angelm@ubits.co\"&startIndex=1&count=100");
        //when(request.

        assertDoesNotThrow(() -> requestInterceptor.preHandle(request, response, new Object()));
    }
}