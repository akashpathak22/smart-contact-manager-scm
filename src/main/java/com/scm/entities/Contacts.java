package com.scm.entities;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Contacts {
  @Id
  private String id;
  private String name;
  private String email;
  private String phone;
  private String profileLink;
  private String address;
  @Column(columnDefinition = "Text")
  private String description;
  @Builder.Default
  private boolean favorite = false;
  private String linkdinLink;
  private String xLink;
  private String instaLink;
  private String websiteLink; 
  @ManyToOne
  @JsonIgnore
  private User user ;

  @OneToMany (cascade = CascadeType.ALL, fetch =  FetchType.EAGER, orphanRemoval = true, mappedBy = "contact")
  @Builder.Default
  private List<SocialLinks> social = new ArrayList<>();

 

}
