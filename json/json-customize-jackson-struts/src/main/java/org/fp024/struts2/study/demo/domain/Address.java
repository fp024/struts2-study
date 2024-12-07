package org.fp024.struts2.study.demo.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
