package org.fp024.struts2.study.demo.domain;

import flexjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fp024.struts2.study.demo.json.PasswordTransformer;

import java.util.Date;

@Getter
@Setter
@ToString
public class User extends Person {
  @Getter(onMethod = @__({@JSON(name = "username")}))
  private String login;

  @Getter(onMethod = @__({@JSON(include = false)}))
  private String hashedPassword;

  private Date lastLogin;

  @Getter(onMethod = @__({@JSON(transformer = PasswordTransformer.class)}))
  private String password;
}
