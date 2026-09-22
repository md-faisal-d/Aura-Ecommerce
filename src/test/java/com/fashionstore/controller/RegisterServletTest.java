package com.fashionstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;

public class RegisterServletTest {

    private RegisterServlet registerServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    public void setUp() {
        registerServlet = new RegisterServlet();
        registerServlet.init();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockDispatcher = mock(RequestDispatcher.class);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockDispatcher);
        when(mockRequest.getContextPath()).thenReturn("");
    }

    @Test
    public void testDoGetForwardsToRegisterJsp() throws ServletException, IOException {
        registerServlet.doGet(mockRequest, mockResponse);
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostDuplicateEmailForwardsWithError() throws ServletException, IOException {
        when(mockRequest.getParameter("name")).thenReturn("Test");
        when(mockRequest.getParameter("email")).thenReturn("existing@example.com");

        try (MockedConstruction<UserDAOImpl> mockUserDao = Mockito.mockConstruction(UserDAOImpl.class,
                (mock, context) -> when(mock.getUserByEmail("existing@example.com")).thenReturn(new User()))) {

            registerServlet.init();
            registerServlet.doPost(mockRequest, mockResponse);

            verify(mockRequest).setAttribute(eq("error"), eq("Email already registered"));
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }
}
