package com.scm.service.Implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contacts;
import com.scm.entities.User;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

  private ContactRepo contactRepo;

  public ContactServiceImpl(ContactRepo contactRepo) {
    this.contactRepo = contactRepo;
  }

  @Override
  public Contacts save(Contacts contact) {
    String contactId = UUID.randomUUID().toString();
    contact.setId(contactId);
    return contactRepo.save(contact);
  }

  @Override
  public List<Contacts> getAll() {
    return contactRepo.findAll();
  }

  @Override
  public void delete(String id) {
    var contact = contactRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("contact does not exists" + id));
    contactRepo.delete(contact);
  }

  @Override
  public Contacts getById(String id) {
    return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("contact does not exists" + id));
  }

  @Override
  public List<Contacts> getUserId(String id) {
    return contactRepo.findByUserId(id);
  }

  @Override
  public Contacts update(Contacts contact) {
    var oldContact = contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));

     oldContact.setName(contact.getName());
     oldContact.setEmail(contact.getEmail());
     oldContact.setPhone(contact.getPhone());
     oldContact.setAddress(contact.getAddress());
     oldContact.setInstaLink(contact.getInstaLink());
     oldContact.setLinkdinLink(contact.getLinkdinLink());
     oldContact.setXLink(contact.getXLink());
     oldContact.setFavorite(contact.isFavorite()); 
     oldContact.setDescription(contact.getDescription());
    //  ========Specal Case=========
     oldContact.setProfileLink(contact.getProfileLink());
    // oldContact.setSocial(contact.getSocial());

    return contactRepo.save(oldContact);
  }

  @Override
  public Page<Contacts> getByUser(User user, int page, int size, String sortBy, String direction) {

    Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);

    return contactRepo.findByUser(user, pageable);

  }

  @Override
  public Page<Contacts> searchByName(String nameKey, int page, int size, String sortBy, String direction, User user) {

    Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);

    return contactRepo.findByUserAndNameContaining(
        user, nameKey, pageable);
  }

  @Override
  public Page<Contacts> searchByPhone(String emailKey, int page, int size, String sortBy, String direction, User user) {
    Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);

    return contactRepo.findByUserAndPhoneContaining(
        user, emailKey, pageable);
  }

  @Override
  public Page<Contacts> searchByEmail(String phoneKey, int page, int size, String sortBy, String direction,User user) {
    Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    var pageable = PageRequest.of(page, size, sort);

    return contactRepo.findByUserAndEmailContaining(user, phoneKey, pageable);
  }

  @Override
  public Contacts update(String id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public List <Contacts> getByUser(User user) {
   
  return contactRepo.findByUser(user);

  }

}
