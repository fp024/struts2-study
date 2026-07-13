package org.fp024.struts2.study.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fp024.struts2.study.demo.json.PasswordConverter;
import tools.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@ToString
public class User extends Person {
  @JsonProperty("username")
  private String login;

  private String hashedPassword;

  private LocalDateTime lastLogin;

  @JsonSerialize(converter = PasswordConverter.class)
  private String password;
}
