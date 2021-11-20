package org.fp024.struts2.study.demo.json;

import flexjson.transformer.AbstractTransformer;

/**
 * https://stackoverflow.com/questions/7886709/how-to-exclude-null-value-fields-when-using-flexjson
 */
public class ExcludeTransformer extends AbstractTransformer {
  @Override
  public Boolean isInline() {
    return true;
  }

  @Override
  public void transform(Object o) {}
}
