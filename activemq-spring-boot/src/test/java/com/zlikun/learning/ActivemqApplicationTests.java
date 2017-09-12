package com.zlikun.learning;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivemqApplicationTests {

	@Autowired
	JmsTemplate jmsTemplate ;

	@Test
	public void contextLoads() {

		jmsTemplate.send("A.TEST" ,session -> {
			return session.createTextMessage("林花谢了春红 / 太 ...") ;
		});

	}

}
