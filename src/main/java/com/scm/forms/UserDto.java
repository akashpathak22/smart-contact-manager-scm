package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserDto {

private String userId;

  @NotBlank(message = "name cant be empty")
  @Size(min =3, message = "name should contain at least 3 characters")
  private String name;
  
  @Email(message = "enter the valid email")
  @NotBlank
  private String email;

  
  @NotBlank
  @Size(min = 10, max = 10)
  private String mobile;
  @NotBlank(message = "please write something about yourself..")
  private String about;

  
  @ValidFile(message="invalid file")
  private MultipartFile profileLink;
  private String picture;

}
