package com.scm.controller;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.UserProfileHelper;
import com.scm.service.UserService;

@ControllerAdvice
public class RootControlller {

  UserService userService;
  public RootControlller (UserService userService){
    this.userService = userService;
  }
  
  Logger logger = org.slf4j.LoggerFactory.getLogger(RootControlller.class);
  @ModelAttribute
  public  void addLoggedInUserDetails(Model model, Authentication authentication){

    if(authentication == null) return ;

    //finding the email of user while he is loggin to account
    String username = UserProfileHelper.getEmailFromLogedInUser(authentication);
    //find user in db if user is in database it will return it else throw and exceptin of resource not found 
    User user = userService.getUserByEmail(username);
    System.out.println(user);
    logger.info(user.getEmail());
    logger.info(user.getName());
    model.addAttribute("loggedInUser", user);
    System.out.println("user email address is : " + username);
  }

}
