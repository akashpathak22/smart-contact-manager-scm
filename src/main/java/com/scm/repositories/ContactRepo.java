package com.scm.repositories;

import com.scm.entities.Contacts;
import com.scm.entities.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ContactRepo extends JpaRepository <Contacts, String>{


  @Query("select c from Contacts c where c.user.id = :userId ")
  List <Contacts> findByUserId(String userId);
  


  // this is for managing the user contact view 
  Page<Contacts> findByUser(User user, Pageable pageable);

  // this is i am making for user dashboard 
  
  List<Contacts> findByUser(User user);


  Page<Contacts>findByUserAndNameContaining(User user, String nameKey,Pageable pageable);
  Page<Contacts>findByUserAndEmailContaining(User user, String emailKey,Pageable pageable);
  Page<Contacts>findByUserAndPhoneContaining(User user, String phoneKey,Pageable pageable);

}
