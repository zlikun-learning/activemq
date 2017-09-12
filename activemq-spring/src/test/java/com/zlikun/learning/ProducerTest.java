package com.zlikun.learning;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 生产者、消费者示例
 * @author zlikun <zlikun-dev@hotmail.com>
 * @date 2017/9/12 17:12
 */
@Slf4j
public class ProducerTest {

    private JmsTemplate jmsTemplate ;

    @Before
    public void init() throws JMSException {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml") ;
        jmsTemplate = context.getBean(JmsTemplate.class) ;
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
        jmsTemplate.send(session -> {
            return session.createTextMessage(text) ;
        });
    }

}
