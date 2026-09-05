package com.scm.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contacts;
import com.scm.entities.User;

public interface ContactService  {
Contacts save(Contacts contacts);
Contacts update(String id);
List<Contacts> getAll();
Contacts getById(String id );
void delete(String id);

List<Contacts> getUserId(String id);

// this is for contact list page where we need numbers to manage our view in the app
Page<Contacts> getByUser(User user,int page, int size, String sortBy, String direction);


// this is for dashboard to get insights for the particular user like how many contacts he has and more.
List <Contacts> getByUser(User user);


Page<Contacts> searchByName(String nameKey,int page, int size, String sortBy, String direction, User user);
Page<Contacts> searchByEmail(String emailKey,int page, int size, String sortBy, String direction, User user);
Page<Contacts> searchByPhone(String phoneKey,int page, int size, String sortBy, String direction, User user);
Contacts update(Contacts contact);

}
