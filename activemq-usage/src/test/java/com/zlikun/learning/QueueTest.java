package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.jms.*;

/**
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 16:41
 */
@Slf4j
public class QueueTest extends TestBase {

    @Test
    public void produce() {

        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session = null;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer = null;
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动连接
            connection.start();
            // 创建session实例，启用事务、自动应答
            session = connection.createSession(Boolean.TRUE ,Session.AUTO_ACKNOWLEDGE);
            // 创建消息目的地实例
            destination = session.createQueue(QUEUE_NAME);
            // 创建消息发送者
            producer = session.createProducer(destination);
            // 设置消息持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // 构造消息
            TextMessage message = session.createTextMessage("这是一条测试消息") ;

            // 发送消息
            producer.send(message);

            // 提交事务
            session.commit();
//            // 回滚事务
//            session.rollback();

            log.info("发送消息：{}" ,message.getText());
        } catch (Exception e) {
            log.error("发送消息到ActiveMQ出错!" ,e);
        } finally {
            closeQuietly(producer);
            closeQuietly(connection, session);
        }

    }

    @Test
    public void consume() {
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
            // 接收消息，指定超时时间，单位：毫秒，0表示永不超时
            // the timeout value (in milliseconds), a time out of zero never expires
            TextMessage message = (TextMessage) consumer.receive(1000);
            if (null != message) {
                log.info("Consumer收到消息：{}" ,message.getText());
            }
        } catch (Exception e) {
            log.error("从ActiveMQ接收消息出错!" ,e);
        } finally {
            closeQuietly(consumer);
            closeQuietly(connection, session);
        }
    }

}
