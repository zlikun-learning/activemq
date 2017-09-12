package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * 点对点消息消费者
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 16:59
 */
@Slf4j
public class ConsumerTest extends TestBase {

    @Test
    public void test() throws InterruptedException {

        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        Destination destination;

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE ,Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue(QUEUE_NAME);
            consumer = session.createConsumer(destination);
            // 设置一个消息监听器
            consumer.setMessageListener(message -> {
                if (null != message) {
                    try {
                        log.info("Listener收到消息：{}" ,((TextMessage) message).getText());
                    } catch (JMSException e) {
                        log.error("从ActiveMQ接收消息出错!" ,e);
                    }
                }
            });
        } catch (Exception e) {
            log.error("从ActiveMQ接收消息出错!" ,e);
        } finally {
            // 休眠30秒，等待监听器接收消息
            TimeUnit.SECONDS.sleep(30);
            closeQuietly(consumer);
            closeQuietly(connection, session);
        }
    }

}
