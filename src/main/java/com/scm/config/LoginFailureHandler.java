package com.scm.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.scm.enums.MessageTypes;
import com.scm.helpers.Message;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    
        if(exception instanceof DisabledException){
          System.out.println(exception);

          HttpSession session = request.getSession();
          session.setAttribute("message", Message.builder()
          .notification("Your Account is Disabled!,Please verify your email to continue login...")
          .type(MessageTypes.red)
          .build());

          response.sendRedirect("/login");
          return;
        }
        response.sendRedirect("/login?error=true");
        

  }

}
