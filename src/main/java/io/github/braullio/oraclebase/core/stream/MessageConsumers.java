package io.github.braullio.oraclebase.core.stream;

import io.github.braullio.oraclebase.CreateOrderCommand;
import io.github.braullio.oraclebase.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageConsumers {

    @RabbitListener(queues = QueueConstants.COMMAND_CREATE_ORDER, containerFactory = "rabbitListenerContainerFactory")
    public void handle(CreateOrderCommand command) {
        log.warn("<- [CONSUMER COMMAND] Recebido comando: {}", command);
    }

    @RabbitListener(queues = QueueConstants.EVENT_ORDER_CREATED, containerFactory = "rabbitListenerContainerFactory")
    public void handle(OrderCreatedEvent event) {
        log.info("<- [CONSUMER EVENT] Recebido evento: {}", event);
    }
}
