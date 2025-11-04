package io.github.braullio.oraclebase.core.stream;

import io.github.braullio.oraclebase.CreateOrderCommand;
import io.github.braullio.oraclebase.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static io.github.braullio.oraclebase.core.stream.QueueConstants.COMMAND_CREATE_ORDER;
import static io.github.braullio.oraclebase.core.stream.QueueConstants.EVENT_ORDER_CREATED;

@Component
@RequiredArgsConstructor
@Slf4j
public class TesteRunner implements CommandLineRunner {

    private final MessageProducer producer;

    @Override
    public void run(String... args) {
        log.info("===== Iniciando teste de envio de messages =====");

        // 1️⃣ Criar e enviar command
        CreateOrderCommand command = new CreateOrderCommand(99L, "Smartphone X", "processar");
        log.info("Enviando Command: {}", command);
        producer.publishCommand(COMMAND_CREATE_ORDER, command);

        // 2️⃣ Criar e enviar event
        OrderCreatedEvent event = new OrderCreatedEvent(99L, "CRIADO", "2025-11-03T18:00:00Z");
        log.info("Enviando Event: {}", event);
        producer.publishEvent(EVENT_ORDER_CREATED, event);

        log.info("===== Teste concluído =====");
    }
}