package com.scm.service.Implementation;

import java.util.Map;
import java.util.UUID;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstraints;
import com.scm.service.ProfileImageService;

@Service
public class ProfileImageServiceImpl implements ProfileImageService {

  private final Cloudinary cloudinary;

  ProfileImageServiceImpl(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  @Override
  public String uploadProfile(MultipartFile file) {
    // Generate a unique filename or use original name
    String filename = UUID.randomUUID().toString();

    try {
      // 1. Safely read bytes using Spring's built-in method
      byte[] data = file.getBytes();
      
      // 2. Upload to Cloudinary and capture the response
      Map uploadResult = cloudinary.uploader().upload(data, ObjectUtils.asMap(
          "public_id", filename
      ));
      
      // 3. Return the fully transformed public URL
      return getProfileUrl(filename);

    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  public String getProfileUrl(String publicId) {
    // Pass the publicId into Cloudinary's URL generator
    return cloudinary
        .url()
        .transformation(
            new Transformation<>()
                .width(AppConstraints.CONTACT_IMAGE_WIDTH)
                .height(AppConstraints.CONTACT_IMAGE_HEIGHT)
                .crop("fill")               
                
        )
        .generate(publicId); // <--- Added publicId here so it generates the link for the specific image
  }
}