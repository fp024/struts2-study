package org.fp024.struts2.study.demo.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import org.fp024.struts2.study.demo.domain.Address;
import org.fp024.struts2.study.demo.domain.Phone;
import org.fp024.struts2.study.demo.domain.User;
import org.fp024.struts2.study.demo.domain.Zipcode;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProduceAction extends ActionSupport {
  @Getter private User user;

  public String execute() throws Exception {
    user = new User();

    user.setName("William Shakespeare");

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    user.setBirthday(formatter.parse("26-4월-1564"));

    List<Phone> phoneNumbers = new ArrayList<>();
    Phone phone = new Phone();
    phone.setName("cell");
    phone.setNumber("555-123-4567");
    phoneNumbers.add(phone);
    phone = new Phone();
    phone.setName("home");
    phone.setNumber("555-987-6543");
    phoneNumbers.add(phone);
    phone = new Phone();
    phone.setName("work");
    phone.setNumber("555-678-3542");
    phoneNumbers.add(phone);
    user.setPhoneNumbers(phoneNumbers);

    List<Address> addresses = new ArrayList<>();
    Address address = new Address();
    address.setName("home");
    address.setCity("Stratford-upon-Avon");
    address.setStreet("Henley");
    List<Zipcode> zipcodes = new ArrayList<>();
    Zipcode zipcode = new Zipcode();
    zipcode.setCode("CV37");
    zipcodes.add(zipcode);
    address.setZipcodes(zipcodes);
    addresses.add(address);
    user.setAddresses(addresses);

    user.setLogin("WillShak");
    user.setHashedPassword("9e107d9d372bb6826bd81d3542a419d6");

    /**
     * Java 8 date/time type `java.time.LocalDateTime` not supported by default:<br>
     * add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
     *
     * <p>Jackson에서 JavaTimeModule 추가 및 설정 없이 Java 8 이상의 LocalDateTime을 사용하면 위의 오류가 발생한다.<br>
     * 설정 참고는 {@link org.fp024.struts2.study.demo.json.JacksonJSONWriter} 을 확인하자.
     */
    user.setLastLogin(LocalDateTime.now());
    user.setPassword("will123shak456");

    return SUCCESS;
  }
}
