package org.fp024.struts2.study.wildcardmethod.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.fp024.struts2.study.wildcardmethod.model.Person;

/** 엔티티를 외부에 직접 노출하지 않기위해 DTO 모델을 별도로 만듦. */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {
  public PersonDTO(Person person) {
    this.id = person.getId();
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
  }

  private Integer id;
  private String firstName;
  private String lastName;
}
