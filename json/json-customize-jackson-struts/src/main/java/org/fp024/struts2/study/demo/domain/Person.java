package org.fp024.struts2.study.demo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Person {
  String name;
  Date birthday;
  String nickname;
  List<Address> addresses;
  List<Phone> phoneNumbers;
}
