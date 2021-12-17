package org.fp024.struts2.study.edit.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * 등록하는 Person 모델
 *
 * @author bruce phillips
 */
@Getter
@Setter
public class Person {
  private String firstName;
  private String lastName;
  private String sport;
  private String gender;
  private String residency;
  private boolean over21;
  private String[] carModels;
  private String email;
  private String phoneNumber;

  public String toString() {
    return "First Name: " + getFirstName() + " | " +
        " Last Name:  " + getLastName() + " | " +
        " Favorite Sport: " + getSport() + " | " +
        " Gender: " + getGender() + " | " +
        " Residency: " + getResidency() + " | " +
        " Over 21: " + isOver21() + " | " +
        " Car models: " + Arrays.asList(getCarModels()) + " | " +
        " Email: " + getEmail() + " | " +
        " Phone: " + getPhoneNumber();
  }
}
