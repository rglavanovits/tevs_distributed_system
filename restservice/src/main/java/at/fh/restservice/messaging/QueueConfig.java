package at.fh.restservice.messaging;

import at.fh.restservice.util.IConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

@Component
public class QueueConfig {
    private AmqpAdmin amqpAdmin;

    public QueueConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void createQueues() {
        amqpAdmin.declareExchange(ExchangeBuilder.fanoutExchange(IConstants.STATUS_EXCHANGE).build());
        amqpAdmin.declareQueue(new Queue(IConstants.STATUS_QUEUE, true));
    }
}
