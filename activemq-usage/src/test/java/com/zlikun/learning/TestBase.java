package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.After;
import org.junit.Before;

import javax.jms.*;

/**
 * 点对点消息队列
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 16:41
 */
@Slf4j
public class TestBase {

    // http://192.168.9.132:8161	admin / admin
    // String BROKER_URL = "tcp://192.168.9.132:61616" ;
    final String BROKER_URL = "tcp://127.0.0.1:61616" ;

    final String QUEUE_NAME = "A.QUEUE" ;
    final String TOPIC_NAME = "A.TOPIC" ;

    protected ConnectionFactory connectionFactory ;

    @Before
    public void init() throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                BROKER_URL);
    }

    @After
    public void destroy() {
        log.info("== Test End ==");
    }

    protected void closeQuietly(Connection connection , Session session) {
        try {
            if(null != session) session.close();
            if (null != connection) connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    protected void closeQuietly(MessageConsumer consumer) {
        try {
            if(null != consumer) consumer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected void closeQuietly(MessageProducer producer) {
        try {
            if(null != producer) producer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
