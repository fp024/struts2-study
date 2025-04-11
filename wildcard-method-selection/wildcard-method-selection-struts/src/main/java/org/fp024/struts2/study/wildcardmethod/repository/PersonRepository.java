package org.fp024.struts2.study.wildcardmethod.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.fp024.struts2.study.wildcardmethod.model.PersonEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {
  @PersistenceContext private EntityManager entityManager;

  public void save(PersonEntity person) {
    entityManager.persist(person);
  }

  public PersonEntity findById(int id) {
    return entityManager.find(PersonEntity.class, id);
  }

  public void remove(PersonEntity person) {
    entityManager.remove(person);
  }

  public List<PersonEntity> list() {
    return entityManager
        .createQuery("SELECT p FROM PersonEntity p ORDER BY p.id", PersonEntity.class)
        .getResultList();
  }
}
