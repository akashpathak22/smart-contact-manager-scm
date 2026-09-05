package com.scm.helpers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class UserProfileHelper {

  public static String getEmailFromLogedInUser(Authentication authentication) {

    Logger logger = LoggerFactory.getLogger(UserProfileHelper.class);

    String userName = "";

    if (authentication instanceof OAuth2AuthenticationToken) {

      var authenticationToken = (OAuth2AuthenticationToken) authentication;
      String providerId = authenticationToken.getAuthorizedClientRegistrationId();
      DefaultOAuth2User user = (DefaultOAuth2User) authenticationToken.getPrincipal();

      // for sign up with google accounts
      if (providerId.equalsIgnoreCase("google")) {
        userName = user.getAttribute("email");
      }
      // for sign up with github accounts
      else if (providerId.equalsIgnoreCase("github")) {
        userName = user.getAttribute("email") != null ?
             user.getAttribute("email").toString()
            : user.getAttribute("login").toString() + "@gmail.com";
      }

      return userName;

    } else {
        return authentication.getName();
    }
  }


  // for getting the verification link from the verification mail 
  public static String getMailVerificationLink(String emailToken){
    String link = "http://localhost:8080/auth/verify-email?token="+ emailToken;

    return link;
  }

}
