package com.self.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 由于定义了2个队列，所以分别定义不同的监听器监听不同的队列。
 * 由于最小消息监听线程数和最大消息监听线程数都是2，所以每个监听器各有2个线程实现监听功能。
 */
@Component
public class MessageRecriver {
	
    @RabbitListener(queues = "kouyyTest1")
    public String processMessage1(String msg) {
        System.out.println(Thread.currentThread().getName() + " 接收到来自queue1队列的消息：" + msg);
        System.out.println("啊哈哈哈哈哈哈哈哈哈哈");
        return msg.toUpperCase();
    }

    @RabbitListener(queues = "kouyyTest2")
    public String processMessage2(String msg) {
        System.out.println(Thread.currentThread().getName() + " 接收到来自queue2队列的消息：" + msg);
        System.out.println("呃呃呃鹅鹅鹅鹅鹅鹅饿鹅鹅鹅饿");
        return msg.toUpperCase();
    }
}
