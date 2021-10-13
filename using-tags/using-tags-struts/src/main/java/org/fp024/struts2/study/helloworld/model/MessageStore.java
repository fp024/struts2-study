package org.fp024.struts2.study.helloworld.model;

import lombok.Getter;

public class MessageStore {
	@Getter
	private String message;

	public MessageStore() {
		message = "Hello Struts User";
	}

	@Override
	public String toString() {
		return message + " (from toString)";
	}
}