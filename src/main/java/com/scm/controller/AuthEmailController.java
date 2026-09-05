package com.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.enums.MessageTypes;
import com.scm.helpers.Message;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthEmailController {

  private UserService userService;

  public AuthEmailController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/verify-email")
  public String verifyEmail(@RequestParam("token") String token, HttpSession session) {

    var user = userService.getUserByEmailToken(token).orElse(null);

    if (user != null) {

      if (user.getEmailToken().equals(token)) {
        user.setEmailVerified(true);
        user.setEnable(true);
        userService.saveVerifiedUser(user);
        System.out.println("+++++++++++++++++++++++++++++");
        System.out.println(user);

        session.setAttribute("message", Message.builder()
            .type(MessageTypes.green)
            .notification("user is verified now you van login 😄")
            .build());

        return "utility/success_page";
      }
      session.setAttribute("message", Message.builder()
          .type(MessageTypes.red)
          .notification("invaid token for this user, try again 🥲")
          .build());
      return "utility/error_page";

    }
    session.setAttribute("message", Message.builder()
        .type(MessageTypes.red)
        .notification("invaid token for this user, try again 🥲")
        .build());
    return "utility/error_page";

  }

}
