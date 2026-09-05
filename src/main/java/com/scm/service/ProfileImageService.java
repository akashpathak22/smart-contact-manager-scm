package com.scm.service;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageService {

   String uploadProfile (MultipartFile file) ;
   String getProfileUrl(String file);
}
