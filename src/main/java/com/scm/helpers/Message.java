package com.scm.helpers;

import com.scm.enums.MessageTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

  private String notification;
  
  @Builder.Default
  private MessageTypes type= MessageTypes.blue;

}
