package com.scm.service;

public interface EmailService {

  void sendEmail(String to, String subject, String text); 
  
  void sendEmailWithHTML();

  void sendEmailWithAttachment();

}
