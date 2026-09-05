package com.scm.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.scm.entities.User;
import com.scm.enums.MessageTypes;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {

  private final UserService userService;
  PageController(UserService userService) {
    this.userService = userService;
  }


  @RequestMapping(value ="/")
  public String index(){
    return "redirect:/home";
  }

   @RequestMapping("/home")
  public String home (Model model){
    model.addAttribute("name", "Aakash");
    model.addAttribute("project", "Smart Contact Manager using Spring Boot ");
    System.out.println("checking if application is runnig or not ");
    return "page";
  }


  @RequestMapping("/service")
  public String servicePage(){

    return "service";
  }


  
  @GetMapping("/register")
  public String registerPage(Model model){
    UserForm form = new UserForm();
    model.addAttribute("userForm", form);

    return "register";
  }


  @RequestMapping("/login")
  public String loginPage(){
    return "login";
  }

  @PostMapping("/do-register")
  public String doRegisterUser(@Valid  @ModelAttribute UserForm form, BindingResult rBindingResult,  HttpSession session){
    // fetch data
      // System.out.println("\n\n========================================");
      // System.out.println("🔥🔥🔥 REGISTRATION CONTROLLER CALLED 🔥🔥🔥");
      // System.out.println("========================================\n");
    // validate data
    // To validate data we are using the @Valid and bindingresult
    if(rBindingResult.hasErrors()){
       System.out.println("========================================\n");
      System.out.println(rBindingResult.getAllErrors());
      return "register";
    }
    // store data in database ---> we have to create user Service to store data in db


    //user service
    // User user = User.builder()
    // .name(form.getName())
    // .email(form.getEmail())
    // .password(form.getPassword())
    // .about(form.getAbout())
    // .mobile(form.getMobile())
    // .build();
    // userService.saveUser(user);

    User user = new User();
    BeanUtils.copyProperties(form, user);
    User savedUser= userService.saveUser(user);
    System.out.println("registration successful for user :" + savedUser.getName() );

    // send message --> for sending message we can use either request or session. her i am using session
    Message message = Message.builder().notification("Register successful").type(MessageTypes.green).build();
    session.setAttribute("message",message);

    // redirect to another page
    return "redirect:/register";
  }


}
