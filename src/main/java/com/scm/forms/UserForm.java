package com.scm.forms;

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
public class UserForm {
  @NotBlank(message = "name cant be empty")
  @Size(min =3, message = "name should contain at least 3 characters")
  private String name;
  
  @Email(message = "enter the valid email")
  @NotBlank
  private String email;
  @NotBlank(message = "password required")
  @Size(min =  6, message = "password must be 6 characte long")
  private String password;
  
  @NotBlank
  @Size(min = 10, max = 10)
  private String mobile;
  @NotBlank(message = "please write something about yourself..")
  private String about;
}
