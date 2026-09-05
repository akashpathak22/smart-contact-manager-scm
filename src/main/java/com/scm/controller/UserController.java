package com.scm.controller;


import java.util.List;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contacts;
import com.scm.entities.User;
import com.scm.enums.MessageTypes;
import com.scm.forms.UserDto;
import com.scm.helpers.Message;
import com.scm.helpers.UserProfileHelper;
import com.scm.service.ContactService;
import com.scm.service.ProfileImageService;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/user")
public class UserController {

  private UserService userService;
  private  ContactService contactService ;

  private ProfileImageService profileImageService;

  public UserController ( UserService userService,ContactService contactService, ProfileImageService profileImageService ){
    this.userService = userService;
    this.contactService = contactService;
    this.profileImageService = profileImageService;
  }




  Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

// we can add all required and user related views here 

  //user dashboard page

@GetMapping("/dashboard")
public String userDashboard(Model model, Authentication authentication) {

    String username = UserProfileHelper.getEmailFromLogedInUser(authentication);
    var user = userService.getUserByEmail(username);
    
    // This fetches the list of contacts
    List<Contacts> userDash = contactService.getByUser(user);
    
    // 1. Calculate total contacts count from the list size
    int totalContacts = userDash.size();
    
    // 2. Calculate favorite contacts count properly
    long favoriteContacts = userDash.stream()
            .filter(Contacts::isFavorite) // Filters for favorites
            .count();                     // Counts how many matched

    // 3. Add everything to the model for your HTML page
    model.addAttribute("userDash", userDash);
    model.addAttribute("totalContacts", totalContacts);
    model.addAttribute("favoriteContacts", favoriteContacts);

    return "user/dashboard";
}

  // user profile page
  @GetMapping("/profile")
  public String userProfile(Model model, Authentication authentication) {

    String username = UserProfileHelper.getEmailFromLogedInUser(authentication);
    var userProfile = userService.getUserByEmail(username);
    System.out.println("============================================================");
    System.out.println("Fetched user: " + userProfile);
    model.addAttribute("userProfile", userProfile);
    model.addAttribute("loggedInUser", userProfile);
      return "user/profile";
  }
  
  //user edit contact

  @GetMapping("/profile/edit/{userId}")
  public String editUserProfile(@PathVariable ("userId") String userId ,Model model, Authentication authentication) {
     String username = UserProfileHelper.getEmailFromLogedInUser(authentication);
    var userProfile = userService.getUserByEmail(username);

   
    model.addAttribute("userProfile", userProfile);
    model.addAttribute("loggedInUser", userProfile);
      return "user/profile_edit";
  }


  @RequestMapping("/profile/update/{userId}")
  public String updatedUser(@PathVariable("userId") String userId,
      @Valid @ModelAttribute("userProfile") UserDto userForm,
      BindingResult rBindingResult,
      Model model,HttpSession session){


    var existingUser = userService.getById(userId);

    var user = new User();
        // 2. Map form fields to user entity
    user.setUserId(userId); 
    user.setName(userForm.getName());
    user.setMobile(userForm.getMobile()); 
    user.setAbout(userForm.getAbout());
    
    // 3. PRESERVE CRITICAL FIELDS that aren't part of the edit form
    user.setEmail(existingUser.getEmail());
    user.setPassword(existingUser.getPassword()); // <--- THIS PREVENTS THE NULL PASSWORD ERROR
    user.setProvider(existingUser.getProvider());
    user.setEmailVerified(existingUser.isEmailVerified());
    user.setPhoneVerified(existingUser.isPhoneVerified());
      user.setEnable(existingUser.isEnable());
    // if picture is updated then ..
    if (userForm.getProfileLink() != null &&
        !userForm.getProfileLink().isEmpty()) {

      // User uploaded a brand new image file
      String fileUrl = profileImageService.uploadProfile(userForm.getProfileLink());
      user.setProfileLink(fileUrl);
      // userForm.setPicture(fileUrl);
      // System.out.println("=================================");
      // System.out.println(contact.getProfileLink());
      // System.out.println("=================================");
    } else {

      user.setProfileLink(existingUser.getProfileLink());
    }


    userService.updateUser(user);

  

    session.setAttribute("message", Message.builder()
    .notification("user updated successfully")
    .type(MessageTypes.green)
    .build());

      return "redirect:/user/profile/edit/" + userId;

  }
  


  // user add contact 

  // user view contact
  


  // user delete contatc 

}
