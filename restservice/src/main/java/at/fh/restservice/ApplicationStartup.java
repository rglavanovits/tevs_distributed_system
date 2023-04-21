package at.fh.restservice;

import at.fh.restservice.messaging.StatusProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup {

    @Autowired
    private StatusProducer statusProducer;

    @EventListener({ContextRefreshedEvent.class})
    public void onApplicationEvent() {
        statusProducer.requestSyncMessage();
    }
}
