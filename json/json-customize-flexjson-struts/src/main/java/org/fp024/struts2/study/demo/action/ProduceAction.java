package org.fp024.struts2.study.demo.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import org.fp024.struts2.study.demo.domain.Address;
import org.fp024.struts2.study.demo.domain.Phone;
import org.fp024.struts2.study.demo.domain.User;
import org.fp024.struts2.study.demo.domain.Zipcode;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProduceAction extends ActionSupport {
  @Getter private User user;

  public String execute() throws Exception {
    user = new User();
    user.setName("William Shakespeare");

    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    user.setBirthday(formatter.parse("26-04-1564"));

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

    // https://howtodoinjava.com/java/date-time/localdatetime-to-date/
    user.setLastLogin(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
    user.setPassword("will123shak456");

    return SUCCESS;
  }
}
