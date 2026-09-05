package com.scm.config;

import com.scm.repositories.UserRepo;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.User;
import com.scm.enums.Provider;
import com.scm.helpers.AppConstraints;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

  private final UserRepo userRepo;
  Logger logger = LoggerFactory.getLogger(OAuthSuccessHandler.class);

  OAuthSuccessHandler(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public void onAuthenticationSuccess(

      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    logger.info("OAuthAuthenticationSuccessHandler");

    // we need to identity that by which prioveder user is getting loged in ex - google, github etc

    var authenticationToken = (OAuth2AuthenticationToken)authentication;
    String providerId = authenticationToken.getAuthorizedClientRegistrationId();

    logger.info("Provider : " +providerId);
    DefaultOAuth2User oauthuser = (DefaultOAuth2User) authentication.getPrincipal();
    User user = new User();

    // DISPLAY THE USER INFO COMING FROM THE GOOGLE OAUTH
    // oauthuser.getAttributes().forEach((key, value) -> {
    //   logger.info("{} => {}", key, value);
    // });

    user.setEmailVerified(true);
    user.setEnable(true);
    user.setRoles(List.of(AppConstraints.ROLE_USER));
    user.setUserId(UUID.randomUUID().toString());
    user.setPassword("password");

    if(providerId.equalsIgnoreCase("google")){
    user.setName(oauthuser.getAttribute("name").toString());
    user.setEmail(oauthuser.getAttribute("email").toString());
    user.setProfileLink(oauthuser.getAttribute("picture").toString());
    user.setProvider(Provider.GOOGLE);
    user.setAbout("this account is created via Google OAuth2 ");
    user.setProviderId(oauthuser.getName());

    }else if (providerId.equalsIgnoreCase("github")){
          user.setName(oauthuser.getAttribute("login").toString());

      user.setEmail(oauthuser.getAttribute("email") != null ? oauthuser.getAttribute("email").toString() : oauthuser.getAttribute("login").toString() + "@gmail.com" );

      user.setProfileLink(oauthuser.getAttribute("avatar_url").toString());
      user.setProvider(Provider.GITHUB);
      user.setAbout("this account is created via Github OAuth2 ");
      user.setProviderId(oauthuser.getName());
    
    }else {
      logger.info("OAuthSuccessHandler says : unknown provider");
    }

    String userEmail = user.getEmail();

    // // STORE IN DATA BASE
    User extstingUser = userRepo.findByEmail(userEmail).orElse(null);
    if (extstingUser == null) {
      userRepo.save(user);
      logger.info("user saved successfully in database " + userEmail);
    } else {
      System.out.println("user does not saved, its already exists!!");
    }



    new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

  }

}
