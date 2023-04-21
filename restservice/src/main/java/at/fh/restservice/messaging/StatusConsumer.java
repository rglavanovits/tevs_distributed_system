package at.fh.restservice.messaging;

import at.fh.restservice.service.StatusService;
import at.fh.restservice.util.IConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
public class StatusConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StatusService statusService;

    @Value("${spring.rabbitmq.username}")
    private String rmqUser;

    @Value("${spring.rabbitmq.password}")
    private String rmqPassword;

    @Value("${spring.rabbitmq.host}")
    private String rmqHost;

    @Value("${spring.rabbitmq.port}")
    private String rmqPort;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void postConstruct() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rmqHost);
        connectionFactory.setPort(Integer.parseInt(rmqPort));
        connectionFactory.setUsername(rmqUser);
        connectionFactory.setPassword(rmqPassword);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(IConstants.STATUS_EXCHANGE, BuiltinExchangeType.FANOUT, true);

        String queueName = IConstants.STATUS_QUEUE;
        channel.queueBind(queueName, IConstants.STATUS_EXCHANGE, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            listen(new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }

    //@RabbitListener(queues = IConstants.STATUS_QUEUE)
    private void listen(String data) {
        try {
            StatusMessageData statusMessageData = objectMapper.readValue(data, StatusMessageData.class);
            processMessage(statusMessageData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void processMessage(StatusMessageData data) {
        if(IConstants.STATUS_QUEUE.equals(data.getOrigin())){
            return;
        }

        logger.info("Received data: {}", data);

        switch(data.getChangeType()){
            case CREATE:
                statusService.addStatus(data.getStatusDto(), false);
                break;
            case UPDATE:
                statusService.updateStatus(data.getStatusDto(), false);
                break;
            case DELETE:
                statusService.deleteStatus(data.getStatusDto(), false);
                break;
            case FULL_SYNC_REQUEST:
                statusService.syncRequestReceived();
                break;
            case FULL_SYNC_RESPONSE:
                statusService.syncResponseReceived(data);
                break;
        }
    }
}
