package org.fp024.struts2.study.demo.json;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.fp024.struts2.study.demo.domain.Address;
import org.fp024.struts2.study.demo.domain.Phone;
import org.fp024.struts2.study.demo.domain.User;
import org.fp024.struts2.study.demo.domain.Zipcode;
import org.junit.jupiter.api.Test;

class JacksonJSONWriterTest {
  @Test
  void writeExcludesConfiguredProperties() throws Exception {
    JacksonJSONWriter writer = new JacksonJSONWriter();
    writer.setDateFormatter("MM/dd/yyyy");

    String json =
        writer.write(
            createUser(),
            List.of(Pattern.compile("addresses\\.name"), Pattern.compile("password")),
            null,
            true);

    assertThatJson(json).node("addresses").isArray().hasSize(1);
    assertThatJson(json).node("addresses[0].street").isEqualTo("Henley");
    assertThatJson(json).node("addresses[0].name").isAbsent();
    assertThatJson(json).node("password").isAbsent();
  }

  @Test
  void writeIncludesOnlyConfiguredProperties() throws Exception {
    JacksonJSONWriter writer = new JacksonJSONWriter();
    writer.setDateFormatter("MM/dd/yyyy");

    String json =
        writer.write(
            createUser(),
            null,
            List.of(Pattern.compile("name"), Pattern.compile("addresses\\.city")),
            true);

    assertThatJson(json).node("name").isEqualTo("William Shakespeare");
    assertThatJson(json).node("addresses").isArray().hasSize(1);
    assertThatJson(json).node("addresses[0].city").isEqualTo("Stratford-upon-Avon");
    assertThatJson(json).node("birthday").isAbsent();
    assertThatJson(json).node("addresses[0].street").isAbsent();
    assertThatJson(json).node("username").isAbsent();
  }

  private User createUser() throws Exception {
    User user = new User();
    user.setName("William Shakespeare");
    user.setBirthday(new SimpleDateFormat("dd-MM-yyyy").parse("26-04-1564"));

    List<Phone> phoneNumbers = new ArrayList<>();
    Phone phone = new Phone();
    phone.setName("cell");
    phone.setNumber("555-123-4567");
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
    user.setLastLogin(LocalDateTime.of(2021, Month.NOVEMBER, 21, 0, 0));
    user.setPassword("will123shak456");
    return user;
  }
}
