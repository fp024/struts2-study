package org.fp024.struts2.study.register.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "person")
public class Person {
  public Person(String email) {
    this.email = email;
  }

  @Id private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private Integer age;

  @Override
  public String toString() {
    return "First Name: "
        + getFirstName()
        + " Last Name:  "
        + getLastName()
        + " Email:      "
        + getEmail()
        + " Age:      "
        + getAge();
  }
}
