package org.fp024.struts2.study.register.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person {
    private String firstName;
    private String lastName;
    private String email;
    private int age;

    public String toString() {
        return "First Name: " + getFirstName() + " Last Name:  " + getLastName() +
                " Email:      " + getEmail() + " Age:      " + getAge();
    }
}
