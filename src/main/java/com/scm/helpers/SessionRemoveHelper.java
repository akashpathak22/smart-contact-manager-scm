package com.scm.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionRemoveHelper {

  public static String removeSession() {
    try {
      HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
          .getRequest()
          .getSession();

      session.removeAttribute("message");
      
      System.out.println("removing session from app");
    } catch (Exception e) {
      System.out.println("an error occured: " + e);
      e.printStackTrace();
    }
      return "";
  }
}