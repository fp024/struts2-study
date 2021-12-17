package org.fp024.struts2.study.edit.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State {

  private String stateAbbr;

  private String stateName;

  public State(String stateAbbr, String stateName) {
    this.stateAbbr = stateAbbr;
    this.stateName = stateName;
  }

  @Override
  public String toString() {
    return getStateAbbr();
  }
}
