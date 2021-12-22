package org.fp024.struts2.study.jasperreports.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.fp024.struts2.study.jasperreports.model.Person;
import org.fp024.struts2.study.jasperreports.service.JasperTemplateInitializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JasperAction extends ActionSupport {
  private static final boolean isJasperInit = JasperTemplateInitializer.isInit;

  /** JasperReports 데이터 소스로 사용할 List. */
  @Getter private List<Person> myList;

  @Override
  public String execute() {
    LOGGER.info("jasper is init {}", isJasperInit);
    LocalDateTime registerDate = LocalDateTime.now();
    // 가상의 인물을 만듦.
    Person p1 = new Person(1L, "Patrick", "Lightbuddie", registerDate.minusSeconds(30));
    Person p2 = new Person(2L, "Jason", "Carrora", registerDate.minusSeconds(20));
    Person p3 = new Person(3L, "Alexandru", "Papesco", registerDate.minusSeconds(10));
    Person p4 = new Person(4L, "Jay", "Boss", registerDate);

    // 사람들을 dataSource 목록에 저장 (일반적으로 데이터베이스에서 가져옴).
    myList = new ArrayList<>();
    myList.add(p1);
    myList.add(p2);
    myList.add(p3);
    myList.add(p4);

    return SUCCESS;
  }
}
