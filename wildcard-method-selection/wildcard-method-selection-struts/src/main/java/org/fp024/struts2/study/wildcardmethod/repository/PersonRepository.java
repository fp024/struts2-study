package org.fp024.struts2.study.wildcardmethod.repository;

import org.fp024.struts2.study.wildcardmethod.model.Person;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PersonRepository {
  @PersistenceContext private EntityManager entityManager;

  public void save(Person person) {
    entityManager.persist(person);
  }

  public Person findById(int id) {
    return entityManager.find(Person.class, id);
  }

  public void remove(Person person) {
    entityManager.remove(person);
  }

  public List<Person> list() {
    return entityManager
        .createQuery("SELECT p FROM Person p ORDER BY p.id", Person.class)
        .getResultList();
  }
}
