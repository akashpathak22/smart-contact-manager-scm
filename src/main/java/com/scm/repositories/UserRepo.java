package com.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;


@Repository 
// we use repository class to interact with database 
public interface UserRepo extends JpaRepository <User, String>{
  // HERE WE CAN ALSO WRITE THE 
  // db related method
  // custom queries 

  // custom finder methods 
  // this going to be used as custome finding method for our application
  Optional<User> findByEmail (String email);
  Optional<User> findByEmailToken(String token);

  User findByUserId(String Id);
}
