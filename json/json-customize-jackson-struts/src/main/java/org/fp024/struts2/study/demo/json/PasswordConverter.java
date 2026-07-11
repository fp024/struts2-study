package org.fp024.struts2.study.demo.json;

import tools.jackson.databind.util.StdConverter;

public class PasswordConverter extends StdConverter<String, String> {
  @Override
  public String convert(String s) {
    return "******";
  }
}
