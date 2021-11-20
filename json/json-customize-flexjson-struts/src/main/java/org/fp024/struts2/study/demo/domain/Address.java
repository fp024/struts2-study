package org.fp024.struts2.study.demo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Address {
  private String name;
  private String street;
  private String city;
  private String state;
  private List<Zipcode> zipcodes;
}
