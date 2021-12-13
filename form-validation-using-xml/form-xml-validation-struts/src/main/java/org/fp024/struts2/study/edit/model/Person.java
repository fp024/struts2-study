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
  private Integer age;

  @Override
  public String toString() {
      return "Person{" +
              "firstName='" + firstName + '\'' +
              ", lastName='" + lastName + '\'' +
              ", sport='" + sport + '\'' +
              ", gender='" + gender + '\'' +
              ", residency='" + residency + '\'' +
              ", over21=" + over21 +
              ", carModels=" + Arrays.toString(carModels) +
              ", email='" + email + '\'' +
              ", phoneNumber='" + phoneNumber + '\'' +
              ", age=" + age +
              '}';
  }
}
