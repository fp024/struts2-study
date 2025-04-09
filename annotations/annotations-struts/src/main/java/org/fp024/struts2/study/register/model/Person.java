package org.fp024.struts2.study.register.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
