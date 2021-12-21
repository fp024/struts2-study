package org.fp024.struts2.study.jasperreports.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.struts2.util.ServletContextAware;
import org.fp024.struts2.study.jasperreports.model.Person;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JasperAction extends ActionSupport implements ServletContextAware {
  @Setter private ServletContext servletContext;

  /** JasperReports 데이터 소스로 사용할 List. */
  @Getter private List<Person> myList;

  @Override
  public String execute() {
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

    try {
      JasperCompileManager.compileReportToFile(
          getClass().getResource("/jasper/our_jasper_template.jrxml").getFile(),
          servletContext.getRealPath("/WEB-INF/jasper") + "/our_compiled_template.jasper");
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return ERROR;
    }

    return SUCCESS;
  }
}
