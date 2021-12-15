package org.fp024.struts2.study.wildcardmethod.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fp024.struts2.study.wildcardmethod.dto.PersonDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "person")
public class Person {
  public Person(PersonDTO personDTO) {
    setByPersonDTO(personDTO);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  public void setByPersonDTO(PersonDTO personDTO) {
    this.id = personDTO.getId();
    this.firstName = personDTO.getFirstName();
    this.lastName = personDTO.getLastName();
  }
}
