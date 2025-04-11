package org.fp024.struts2.study.wildcardmethod.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fp024.struts2.study.wildcardmethod.dto.PersonDTO;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "person")
public class PersonEntity {
  public PersonEntity(PersonDTO personDTO) {
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
