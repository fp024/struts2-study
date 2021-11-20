package org.fp024.struts2.study.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fp024.struts2.study.demo.json.PasswordConverter;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User extends Person {
  @JsonProperty("username")
  private String login;

  @JsonIgnore private String hashedPassword;

  private LocalDateTime lastLogin;

  @JsonSerialize(converter = PasswordConverter.class)
  private String password;
}
