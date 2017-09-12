package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * 发布订阅模式
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 17:03
 */
@Slf4j
public class TopicTest extends TestBase {

    @Test
    public void produce() {
        TopicConnectionFactory factory = (TopicConnectionFactory) connectionFactory ;
        TopicConnection connection = null;
        TopicSession session = null;
        TopicPublisher publisher = null;
        try {
            connection = factory.createTopicConnection() ;
            connection.start();
            session = connection.createTopicSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE) ;
            publisher = session.createPublisher(session.createTopic(TOPIC_NAME)) ;
            publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage message = session.createTextMessage("发出一个通知!") ;
            publisher.send(message);
            log.info("发送消息：{}" ,message.getText());
        } catch (JMSException e) {
            log.error("发送消息到ActiveMQ出错!" ,e);
        } finally {
            closeQuietly(publisher);
            closeQuietly(connection, session);
        }

    }

    @Test
    public void consume() throws InterruptedException {

        TopicConnectionFactory factory = (TopicConnectionFactory) connectionFactory ;
        TopicConnection connection = null;
        TopicSession session = null;
        MessageConsumer consumer = null ;
        try {
            connection = factory.createTopicConnection();
            connection.start();
            session = connection.createTopicSession(Boolean.FALSE ,Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(session.createTopic(TOPIC_NAME));
            consumer.setMessageListener(message -> {
                if (null != message) {
                    try {
                        log.info("收到消息：{}" ,((TextMessage) message).getText());
                    } catch (JMSException e) {
                        log.error("从ActiveMQ接收消息出错!" ,e);
                    }
                }
            });
        } catch (JMSException e) {
            log.error("从ActiveMQ接收消息出错!" ,e);
        } finally {
            // 休眠30秒，等待监听器接收消息
            TimeUnit.SECONDS.sleep(30);
            closeQuietly(consumer);
            closeQuietly(connection, session);
        }
    }

}
