package at.fh.restservice.messaging;

import at.fh.restservice.service.data.ChangeType;
import at.fh.restservice.service.data.StatusDto;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatusMessageData {
    private ChangeType changeType;
    private StatusDto statusDto;
    private String origin;
    private Map<String, StatusDto> fullSync;
}
