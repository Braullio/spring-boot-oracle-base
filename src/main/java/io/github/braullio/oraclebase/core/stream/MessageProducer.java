package io.github.braullio.oraclebase.core.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

    private final TopicExchange eventExchange;
    private final DirectExchange commandExchange;
    private final RabbitTemplate rabbitTemplate;
    private final CommandLogRepository commandLogRepository;
    private final EventLogRepository eventLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishCommand(String queueName, Object commandPayload) {
        CommandLog logEntry = CommandLog.builder()
                .queueName(queueName)
                .commandType(commandPayload.getClass().getSimpleName())
                .payload(commandPayload.toString())
                .timestamp(Instant.now())
                .sent(false)
                .error(0)
                .build();

        try {
            rabbitTemplate.convertAndSend(commandExchange.getName(), queueName, commandPayload);

            log.info("-> [COMMAND] Enviado '{}' para '{}'", logEntry.getCommandType(), queueName);
            logEntry.setSent(true);
        } catch (Exception e) {
            log.error("Erro ao enviar comando para '{}'", queueName, e);
            logEntry.setError(1);
        } finally {
            commandLogRepository.save(logEntry);
        }
    }

    public void publishEvent(String routingKey, Object eventPayload) {
        EventLog logEntry = EventLog.builder()
                .routingKey(routingKey)
                .eventType(eventPayload.getClass().getSimpleName())
                .payload(eventPayload.toString())
                .timestamp(Instant.now())
                .sent(false)
                .error(0)
                .build();

        try {
            rabbitTemplate.convertAndSend(eventExchange.getName(), routingKey, eventPayload);

            log.info("-> [EVENT] Publicado '{}' com chave '{}'", logEntry.getEventType(), routingKey);
            logEntry.setSent(true);
        } catch (Exception e) {
            log.error("Erro ao enviar evento para '{}'", routingKey, e);
            logEntry.setError(1);
        } finally {
            eventLogRepository.save(logEntry);
        }
    }

}