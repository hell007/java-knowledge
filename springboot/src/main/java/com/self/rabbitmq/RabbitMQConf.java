package com.self.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring boot配置类，作用为指定队列，交换器类型及绑定操作
 * 共声明了2个队列，分别是hello.queue1，hello.queue2，交换器类型为TopicExchange,
 * 并与hello.queue1，hello.queue2队列分别绑定。
 */
@Configuration
public class RabbitMQConf {

    @Autowired
    private ConnectionFactory connectionFactory;

    // 管理
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

    // 声明队列
    @Bean
    public Queue loginQueue() {
        // 默认就是自动声明的
        return new Queue("kouyyTest1", true);
    }

    // 声明队列
    @Bean
    public Queue successQueue() {
        // 默认就是自动声明的
        return new Queue("kouyyTest2", true);
    }

    //声明交互器
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("YiChePoints");
    }

    //绑定
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(loginQueue()).to(topicExchange()).with("key.1");
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(successQueue()).to(topicExchange()).with("key.2");
    }

}
