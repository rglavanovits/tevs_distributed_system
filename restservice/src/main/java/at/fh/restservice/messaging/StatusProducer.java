package at.fh.restservice.messaging;

import at.fh.restservice.service.data.ChangeType;
import at.fh.restservice.util.IConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StatusProducer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void postConstruct() {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(new ObjectMapper()));
    }

    public void sendMessage(StatusMessageData data) {
        logger.info("Sending data: {}", data);

        data.setOrigin(IConstants.STATUS_QUEUE);
        rabbitTemplate.convertAndSend(IConstants.STATUS_EXCHANGE, "", data);
    }

    public void requestSyncMessage() {
        StatusMessageData data = new StatusMessageData();

        data.setOrigin(IConstants.STATUS_QUEUE);
        data.setChangeType(ChangeType.FULL_SYNC_REQUEST);
        rabbitTemplate.convertAndSend(IConstants.STATUS_EXCHANGE, "", data);
    }
}
