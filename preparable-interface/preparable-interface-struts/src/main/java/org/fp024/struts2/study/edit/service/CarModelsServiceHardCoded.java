package org.fp024.struts2.study.edit.service;

/**
 * 이 클래스에서 하드코딩된 데이터를 사용하여 자동차 모델에 대한 정보를 제공합니다.
 *
 * @author bphillips
 */
public class CarModelsServiceHardCoded implements CarModelsService {
  @Override
  public String[] getCarModels() {
    String[] carModelsAvailable = {"Ford", "Chrysler", "Toyota", "Nissan"};
    return carModelsAvailable;
  }
}
