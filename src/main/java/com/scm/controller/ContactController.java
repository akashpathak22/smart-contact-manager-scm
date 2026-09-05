package com.scm.controller;

import com.scm.service.Implementation.ProfileImageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contacts;
import com.scm.enums.MessageTypes;
import com.scm.forms.AddContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstraints;
import com.scm.helpers.Message;
import com.scm.helpers.UserProfileHelper;
import com.scm.service.ContactService;
import com.scm.service.ProfileImageService;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

  Logger logger = LoggerFactory.getLogger(ContactController.class);

  private ContactService contactService;
  private UserService userService;
  private ProfileImageService profileImageService;

  public ContactController(ContactService contactService, UserService userService,
      ProfileImageService profileImageService, ProfileImageServiceImpl profileImageServiceImpl) {
    this.contactService = contactService;
    this.userService = userService;
    this.profileImageService = profileImageService;
  }

  @RequestMapping("/add")
  public String addContactView(Model model) {
    AddContactForm contactForm = new AddContactForm();
    model.addAttribute("addContactForm", contactForm);
    return "user/addcontact";
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  private String saveContact(@Valid @ModelAttribute AddContactForm contact, BindingResult result,
      Authentication authentication, HttpSession session) {

    if (result.hasErrors()) {
      result.getAllErrors().forEach(err -> logger.info(err.toString()));

      session.setAttribute("message",
          Message.builder()
              .notification("fix errors show below and try again")
              .type(MessageTypes.red)
              .build());
      return "user/addcontact";
    }

    String username = UserProfileHelper.getEmailFromLogedInUser(authentication);
    var user = userService.getUserByEmail(username);

    System.out.println(contact);
    // logger.info("profilE info {}",contact.getProfileLink());

    String fileUrl = profileImageService.uploadProfile(contact.getProfileLink());

    Contacts newcontact = new Contacts();

    newcontact.setName(contact.getName());
    newcontact.setEmail(contact.getEmail());
    newcontact.setAddress(contact.getAddress());
    newcontact.setFavorite(contact.isFavorite());
    newcontact.setDescription(contact.getDescription());
    newcontact.setPhone(contact.getPhone());
    newcontact.setLinkdinLink(contact.getLinkdinLink());
    newcontact.setInstaLink(contact.getInstaLink());
    newcontact.setWebsiteLink(contact.getWebsiteLink());
    newcontact.setXLink(contact.getXLink());
    // =Speatial tratment required to perform this operation =========
    newcontact.setUser(user);
    newcontact.setProfileLink(fileUrl);

    session.setAttribute("message",
        Message.builder()
            .notification("You have successfully added new Contact 😀 ")
            .type(MessageTypes.green)
            .build());

    contactService.save(newcontact);
    return "redirect:/user/contact/add";
  }

  // view all contacts
  @RequestMapping
  public String viewContacts(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = AppConstraints.PAGINATION_PAGE_SIZE + "") int size,
      @RequestParam(value = "sortBy", defaultValue = "email") String sortBy,
      @RequestParam(value = "direction", defaultValue = "asc") String direction,
      Model model, Authentication authentication) {

    String username = UserProfileHelper.getEmailFromLogedInUser(authentication);

    var user = userService.getUserByEmail(username);

    Page<Contacts> pageContactslist = contactService.getByUser(user, page, size, sortBy, direction);

    // pageContactslist.
    model.addAttribute("pageContactslist", pageContactslist);
    model.addAttribute("pageSize", AppConstraints.PAGINATION_PAGE_SIZE);
    model.addAttribute("contactSearchForm", new ContactSearchForm());
    return "user/contact";
  }

  // search controller for user contacts in contacts page
  @RequestMapping("/search")
  public String contactSearchHandler(

      @ModelAttribute ContactSearchForm contactSearchForm,

      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = AppConstraints.PAGINATION_PAGE_SIZE + "") int size,
      // @RequestParam(value = "sortBy", defaultValue = "email") String sortBy,
      @RequestParam(value = "direction", defaultValue = "asc") String direction,
      Model model, Authentication authentication) {

    // System.out.println("============================================");
    // logger.info("field {} keyword {}", field, keyword);

    var user = userService.getUserByEmail(UserProfileHelper.getEmailFromLogedInUser(authentication));

    Page<Contacts> pageContact = null;
    if (contactSearchForm.getField().equalsIgnoreCase("name")) {
      pageContact = contactService.searchByName(contactSearchForm.getKeyword(), page, size,
          contactSearchForm.getField(), direction, user);
    } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
      pageContact = contactService.searchByEmail(contactSearchForm.getKeyword(), page, size,
          contactSearchForm.getField(), direction, user);
    } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
      pageContact = contactService.searchByPhone(contactSearchForm.getKeyword(), page, size,
          contactSearchForm.getField(), direction, user);
    } else {

    }
    model.addAttribute("contactSearchForm", contactSearchForm);
    model.addAttribute("pageContact", pageContact);

    model.addAttribute("pageSize", AppConstraints.PAGINATION_PAGE_SIZE);
    return "user/search";
  }

  @RequestMapping("/delete/{id}")
  public String deleteContact(
      @PathVariable("id") String id,
      HttpSession session) {

    contactService.delete(id);
    session.setAttribute("message",
        Message.builder()
            .type(MessageTypes.green)
            .notification("Contact has been deleted Successfully!")
            .build());

    return "redirect:/user/contact";

  }

  // update contact form view
  @RequestMapping("/view/{id}")
  public String updateContactFormView(@PathVariable String id, Model model) {

    var contact = contactService.getById(id);

    var contactDetails = new AddContactForm();

    contactDetails.setName(contact.getName());
    contactDetails.setEmail(contact.getEmail());
    contactDetails.setPhone(contact.getPhone());
    contactDetails.setAddress(contact.getAddress());
    contactDetails.setInstaLink(contact.getInstaLink());
    contactDetails.setLinkdinLink(contact.getLinkdinLink());
    contactDetails.setXLink(contact.getXLink());
    contactDetails.setDescription(contact.getDescription());
    contactDetails.setFavorite(contact.isFavorite());
    // ========Specal Case=========
    contactDetails.setPicture(contact.getProfileLink());

    model.addAttribute("contactDetails", contactDetails);
    model.addAttribute("id", id);
    return "user/update_contact_view";
  }

  // it will handle the new changes of contact
  @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
  public String updateContactWithNewData(

      @PathVariable("id") String id,
      @Valid @ModelAttribute("contactDetails") AddContactForm addContactForm,
      BindingResult rBindingResult,
      Model model) {

    if (rBindingResult.hasErrors()) {
      model.addAttribute("id", id);
      return "user/update_contact_view";
    }

    var existingContact = contactService.getById(id);

    var contact = new Contacts();

    contact.setId(id);
    contact.setName(addContactForm.getName());
    contact.setEmail(addContactForm.getEmail());
    contact.setPhone(addContactForm.getPhone());
    contact.setAddress(addContactForm.getAddress());
    contact.setInstaLink(addContactForm.getInstaLink());
    contact.setLinkdinLink(addContactForm.getLinkdinLink());
    contact.setXLink(addContactForm.getXLink());
    contact.setFavorite(addContactForm.isFavorite());
    contact.setDescription(addContactForm.getDescription());

    // if picture is updated then ..
    if (addContactForm.getProfileLink() != null &&
        !addContactForm.getProfileLink().isEmpty()) {

      // User uploaded a brand new image file
      String fileUrl = profileImageService.uploadProfile(addContactForm.getProfileLink());
      contact.setProfileLink(fileUrl);
      // addContactForm.setPicture(fileUrl);
      // System.out.println("=================================");
      // System.out.println(contact.getProfileLink());
      // System.out.println("=================================");
    } else {

      contact.setProfileLink(existingContact.getProfileLink());
    }

    contactService.update(contact);

    model.addAttribute("message",
        Message.builder().notification("Contact updated Successfully!")
            .type(MessageTypes.green)
            .build());

    return "redirect:/user/contact/view/" + id;
  }

}
