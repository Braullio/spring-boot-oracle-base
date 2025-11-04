package io.github.braullio.oraclebase.core.stream;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter jsonMessageConverter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(5);
        return factory;
    }

    @Bean
    public Queue commandQueue() {
        return new Queue(QueueConstants.COMMAND_CREATE_ORDER, true);
    }

    @Bean
    public DirectExchange commandExchange() {
        return new DirectExchange(QueueConstants.EXCHANGE_COMMAND);
    }

    @Bean
    public Binding commandBinding(Queue commandQueue, DirectExchange commandExchange) {
        return BindingBuilder.bind(commandQueue).to(commandExchange).with(QueueConstants.COMMAND_CREATE_ORDER);
    }

    @Bean
    public Queue eventQueue() {
        return new Queue(QueueConstants.EVENT_ORDER_CREATED, true);
    }

    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(QueueConstants.EXCHANGE_EVENT);
    }

    @Bean
    public Binding eventBinding(Queue eventQueue, TopicExchange eventExchange) {
        return BindingBuilder.bind(eventQueue).to(eventExchange).with(QueueConstants.EVENT_ORDER_CREATED);
    }
}