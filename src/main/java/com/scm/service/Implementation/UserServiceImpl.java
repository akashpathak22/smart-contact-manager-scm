package com.scm.service.Implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scm.entities.User;
import com.scm.helpers.AppConstraints;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.helpers.UserProfileHelper;
import com.scm.repositories.UserRepo;
import com.scm.service.EmailService;
import com.scm.service.UserService;

    @Service
    @Transactional
    public class UserServiceImpl implements UserService {

      private final UserRepo userRepo;

      private final PasswordEncoder passwordEncoder;

      private EmailService emailService;



      private Logger logger = LoggerFactory.getLogger(this.getClass());

        UserServiceImpl(PasswordEncoder passwordEncoder, UserRepo userRepo, EmailService emailService) {
            this.passwordEncoder = passwordEncoder;
            this.userRepo = userRepo;
            this.emailService = emailService;
        }

      @Override
      public User saveUser(User user) {

        try {
          System.out.println(user.toString());
          if (passwordEncoder == null) {
            throw new RuntimeException("PasswordEncoder bean not injected!");
          }
          String userId = UUID.randomUUID().toString();
          user.setUserId(userId);
          // Encode password
          if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty!");
          }
          String encodedPassword = passwordEncoder.encode(user.getPassword());
          user.setPassword(encodedPassword);

          // Set other fields
          user.setProfileLink(AppConstraints.DEFAULT_PROFILE);
          user.setRoles(List.of(AppConstraints.ROLE_USER));
          user.setEnable(false);

          // Save to database
          
          String emailToken = UUID.randomUUID().toString();
          
          user.setEmailToken(emailToken);

          User savedUser = userRepo.save(user);
          String link = UserProfileHelper.getMailVerificationLink(emailToken);

          emailService.sendEmail(savedUser.getEmail(),
           "Verify your account on SCM, to access your account!",
            link);

          return savedUser;

        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("error occurred while saving the user" + e.getMessage());

          throw e;
        }
      }

      @Override
      public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
      }

      @Override
      public Optional<User> updateUser(User user) {
        User olduser = userRepo.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        olduser.setName(user.getName());
        olduser.setEmail(user.getEmail());
        olduser.setPassword(passwordEncoder.encode(user.getPassword()));
        olduser.setAbout(user.getAbout());
        olduser.setEnable(user.isEnabled());
        olduser.setMobile(user.getMobile());
        olduser.setEmailVerified(user.isEmailVerified());
        olduser.setProfileLink(user.getProfileLink());
        olduser.setPhoneVerified(user.isPhoneVerified());
        olduser.setProvider(user.getProvider());
        olduser.setProviderId(user.getProviderId());

        return Optional.of(userRepo.save(olduser));
      }

      @Override
      public void deleteUser(String id) {
        User olduser = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        userRepo.delete(olduser);
      }

      @Override
      public boolean isUserExists(String userId) {
        User user = userRepo.findById(userId).orElse(null);
        return user != null;
      }

      @Override
      public boolean isUserExistsByEmal(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        return user != null;
      }

      @Override
      public List<User> getAllUsers() {
        return userRepo.findAll();
      }

      @Override
      public User getUserByEmail(String email) {

        return userRepo.findByEmail(email).orElse(null);
        
      }

      @Override
      public Optional<User> getUserByEmailToken(String token) {
      
       return userRepo.findByEmailToken(token);
      }

      @Override
      public void saveVerifiedUser(User user) {
      
        user.setEmailVerified(true);
        user.setEnable(true);
        userRepo.save(user);

      }

      @Override
      public User getById(String id) {
    
        return userRepo.findByUserId(id);
      }
    }
