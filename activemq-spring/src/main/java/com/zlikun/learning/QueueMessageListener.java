package com.zlikun.learning;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息接收监听器
 */
@Slf4j
public class QueueMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if (message != null && message instanceof TextMessage) {
			TextMessage tm = (TextMessage) message;
			try {
				log.info("Listener接收消息：{}", tm.getText());
				Thread.sleep(10);
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
