package org.fp024.struts2.study.demo.domain;

import flexjson.JSON;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fp024.struts2.study.demo.json.PasswordTransformer;

@Getter
@Setter
@ToString
public class User extends Person {
  @Getter(onMethod_ = {@JSON(name = "username")})
  private String login;

  @Getter(onMethod_ = {@JSON(include = false)})
  private String hashedPassword;

  private LocalDateTime lastLogin;

  @Getter(onMethod_ = {@JSON(transformer = PasswordTransformer.class)})
  private String password;
}
