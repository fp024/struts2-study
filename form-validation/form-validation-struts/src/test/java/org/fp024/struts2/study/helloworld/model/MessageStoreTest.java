package org.fp024.struts2.study.helloworld.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessageStoreTest {
	@Test
	void testGetMessage() {
		MessageStore messageStore = new MessageStore();
		assertEquals("Hello Struts User", messageStore.getMessage());
	}
}
