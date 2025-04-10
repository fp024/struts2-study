package org.fp024.struts2.study.register.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.fp024.struts2.study.register.model.Person;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {
  @PersistenceContext private EntityManager entityManager;

  public void save(Person person) {
    entityManager.persist(person);
  }

  public Person findById(String email) {
    return entityManager.find(Person.class, email);
  }

  public void remove(Person person) {
    entityManager.remove(person);
  }

  public List<Person> list() {
    return entityManager
        .createQuery("SELECT p FROM Person p ORDER BY p.email", Person.class)
        .getResultList();
  }
}
