package com.fashionstore.controller;

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

public class LoginServletTest {

    private LoginServlet loginServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private RequestDispatcher mockDispatcher;

    @BeforeEach
    public void setUp() {
        loginServlet = new LoginServlet();
        loginServlet.init();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockDispatcher = mock(RequestDispatcher.class);

        when(mockRequest.getSession()).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockDispatcher);
        when(mockRequest.getContextPath()).thenReturn("");
    }

    @Test
    public void testDoGetForwardsToLoginJsp() throws ServletException, IOException {
        loginServlet.doGet(mockRequest, mockResponse);
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    public void testDoPostSuccessRedirectsToHome() throws ServletException, IOException {
        when(mockRequest.getParameter("email")).thenReturn("user@example.com");
        when(mockRequest.getParameter("password")).thenReturn("correctPass");

        User validUser = new User();
        validUser.setId(1);
        validUser.setName("Test User");
        validUser.setEmail("user@example.com");

        try (MockedConstruction<UserDAOImpl> mockUserDao = Mockito.mockConstruction(UserDAOImpl.class,
                (mock, context) -> when(mock.loginUser("user@example.com", "correctPass")).thenReturn(validUser))) {

            loginServlet.init();
            loginServlet.doPost(mockRequest, mockResponse);

            verify(mockSession).setAttribute("user", validUser);
            verify(mockResponse).sendRedirect(contains("home"));
        }
    }

    @Test
    public void testDoPostInvalidCredentialsForwardsWithError() throws ServletException, IOException {
        when(mockRequest.getParameter("email")).thenReturn("user@example.com");
        when(mockRequest.getParameter("password")).thenReturn("wrongPass");

        try (MockedConstruction<UserDAOImpl> mockUserDao = Mockito.mockConstruction(UserDAOImpl.class,
                (mock, context) -> when(mock.loginUser("user@example.com", "wrongPass")).thenReturn(null))) {

            loginServlet.init();
            loginServlet.doPost(mockRequest, mockResponse);

            verify(mockRequest).setAttribute(eq("error"), anyString());
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }
}
