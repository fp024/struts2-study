package org.fp024.struts2.study.demo.json;

import com.fasterxml.jackson.databind.util.StdConverter;

public class PasswordConverter extends StdConverter<String, String> {
  @Override
  public String convert(String s) {
    return "******";
  }
}
