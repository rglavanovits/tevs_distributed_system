package at.fh.restservice.service.data;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"timestamp", "origin"})
public class StatusDto {
    private String username;
    private String status;
    private Date timestamp;
    private String origin;

    public String getUsernameHash() {
        return String.valueOf(username.hashCode());
    }
}
