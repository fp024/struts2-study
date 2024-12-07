package org.fp024.struts2.study.jasperreports.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Person {
  private Long id;

  private String name;

  private String lastName;

  private String registerDate;

  public Person(Long id, String name, String lastName, LocalDateTime registerDate) {
    this.id = id;
    this.name = name;
    this.lastName = lastName;
    this.registerDate = registerDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
