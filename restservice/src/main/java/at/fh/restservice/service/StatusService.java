package at.fh.restservice.service;

import at.fh.restservice.service.data.ChangeType;
import at.fh.restservice.service.data.StatusDto;
import at.fh.restservice.messaging.StatusMessageData;
import at.fh.restservice.messaging.StatusProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatusService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StatusProducer statusProducer;

    private Map<String, StatusDto> statusMap = new HashMap<>();

    public Map<String, StatusDto> getStatus() {
        return statusMap;
    }

    public void addStatus(StatusDto statusDto, boolean emitMessage) {
        final String key = statusDto.getUsernameHash();
        this.statusMap.put(key, statusDto);

        if(emitMessage){
            emitMessage(ChangeType.CREATE, statusDto);
        }
    }

    public void updateStatus(StatusDto statusDto, boolean emitMessage) {
        final String key = statusDto.getUsernameHash();
        this.statusMap.put(key, statusDto);

        if(emitMessage){
            emitMessage(ChangeType.UPDATE, statusDto);
        }
    }

    public void deleteStatus(StatusDto statusDto, boolean emitMessage) {
        final String key = statusDto.getUsernameHash();
        statusMap.remove(key);

        if(emitMessage) {
            emitMessage(ChangeType.DELETE, statusDto);
        }
    }

    private void emitMessage(ChangeType changeType, StatusDto statusDto) {
        StatusMessageData data = new StatusMessageData();

        data.setChangeType(changeType);
        data.setStatusDto(statusDto);

        statusProducer.sendMessage(data);
    }

    public void syncRequestReceived() {
        if(statusMap.isEmpty()){
            return;
        }

        StatusMessageData data = new StatusMessageData();
        data.setChangeType(ChangeType.FULL_SYNC_RESPONSE);
        data.setFullSync(statusMap);

        statusProducer.sendMessage(data);
    }

    public void syncResponseReceived(StatusMessageData data){
        this.statusMap = data.getFullSync();
    }
}
