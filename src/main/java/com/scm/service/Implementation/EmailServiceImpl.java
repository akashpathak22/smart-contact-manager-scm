package com.scm.service.Implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.scm.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {


  private JavaMailSender mailSender;
  @Value("${spring.mail.properties.domain_name}")
  private String domainname ;
  public EmailServiceImpl (JavaMailSender mailSender){
    this.mailSender = mailSender;
  }



  @Override
  public void sendEmail(String to, String subject, String text) {
    
    try {
      var message = new SimpleMailMessage();

      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);
      message.setFrom(domainname);


      mailSender.send(message);

    } catch (Exception e) {
      
      System.out.println("Error occured while sending verification email!!");
      e.printStackTrace();
      
    }

  }








  

  @Override
  public void sendEmailWithHTML() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithHTML'");
  }

  @Override
  public void sendEmailWithAttachment() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'sendEmailWithAttachment'");
  }

}
