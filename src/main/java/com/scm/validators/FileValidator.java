package com.scm.validators;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * FileValidator
 */

 public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

  // 2 MB in bytes = 2 * 1024 * 1024 = 2097152 bytes
  public final long MAX_FILE_SIZE = 1024 * 1024 * 2; 

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    
    // If no file is uploaded on edit, let it pass
    if (file == null && file.isEmpty()) {
      return true; 
    }

    System.out.println("Uploaded File Size in bytes: " + file.getSize());

    // Strict size check
    if (file.getSize() > MAX_FILE_SIZE) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate("File must be under 2 MB").addConstraintViolation();
      return false; // This triggers the validation error!
    }

    return true;
  }
}

