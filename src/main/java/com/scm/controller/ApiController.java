package com.scm.controller;

import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.Contacts;
import com.scm.service.ContactService;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api")
public class ApiController {

  private final ContactService contactService;


  ApiController(ContactService contactService) {
    this.contactService = contactService;
  }


  @RequestMapping("/contact/{contactId}")
 public Contacts getContacts(@PathVariable String contactId  ){
  return contactService.getById(contactId);
}


}
