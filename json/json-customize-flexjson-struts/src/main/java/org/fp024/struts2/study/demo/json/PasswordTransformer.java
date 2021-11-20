package org.fp024.struts2.study.demo.json;

import flexjson.transformer.AbstractTransformer;

public class PasswordTransformer extends AbstractTransformer {
  public void transform(Object o) {
    getContext().writeQuoted("******");
  }
}
