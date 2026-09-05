package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AddContactForm {


  @NotBlank(message ="name cant be empty!")
  private String name;
  @NotBlank(message = "email cant be empty")
  @Email
  private String email;
  @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number!")
  private String phone;
  private String address;
  @Column(length = 1000)
  private String description;
  @Builder.Default
  private boolean favorite = false;
  private String linkdinLink;
  private String xLink;
  private String instaLink;
  private String websiteLink;
  
  
  // we will accept the direct image thats why we have to treat this differently

 
  @ValidFile(message="invalid file")
  private MultipartFile profileLink;
  private String picture;
}
