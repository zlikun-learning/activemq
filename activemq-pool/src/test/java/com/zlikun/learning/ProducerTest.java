package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生产者、消费者示例，消费者使用QueueListenerTest代替
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 17:12
 */
@Slf4j
public class ProducerTest extends TestBase {

    private Connection connection = null;
    private Session session = null;

    @Before
    public void init() throws JMSException {
        super.init();
        connection = connectionFactory.createConnection() ;
        session = connection.createSession(Boolean.TRUE ,Session.AUTO_ACKNOWLEDGE);
    }

    @After
    public void destroy() {
        closeQuietly(connection, session);
        super.destroy();
    }

    @Test
    public void test() {

        ExecutorService exec = Executors.newFixedThreadPool(30) ;
        final String text = "林花谢了春红 / 太匆匆 / 无奈朝来寒雨晚来风 / 胭脂泪 / 相留醉 / 几时重 / 自是人生长恨水长东" ;
        for(int i = 0 ; i < 1000 ; i ++) {
            int index = i + 1;
            exec.execute(() -> produce(String.format("%5d - %s" ,index ,text)));
        }
        exec.shutdown();
        while(!exec.isTerminated()) ;

    }

    /**
     * 实际消息生产者
     * @param text
     */
    private void produce(final String text) {
        MessageProducer producer = null;
        Destination destination;
        try {
            destination = session.createQueue(QUEUE_NAME);
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);  // 设置不持久化，以提升发送性能
            TextMessage message = session.createTextMessage(text) ;
            producer.send(message);
            session.commit();
        } catch (Exception e) {
            log.error("发送消息到ActiveMQ出错!" ,e);
        } finally {
            closeQuietly(producer);
        }
    }

}
