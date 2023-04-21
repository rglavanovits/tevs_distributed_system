package at.fh.restservice.api;

import at.fh.restservice.service.StatusService;
import at.fh.restservice.service.data.StatusDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(path = "/status")
public class StatusController {

    @Autowired
    private ServletWebServerApplicationContext context;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private StatusService statusService;

    @GetMapping
    public ResponseEntity<Map<String, StatusDto>> getStatus() {
        setResponseHeader();
        return ResponseEntity.ok(this.statusService.getStatus());
    }

    @PostMapping
    public ResponseEntity<?> addStatus(@RequestBody StatusDto statusDto) {
        setResponseHeader();

        statusDto.setTimestamp(new Date());
        this.statusService.addStatus(statusDto, true);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> updateStatus(@RequestBody StatusDto statusDto) {
        setResponseHeader();

        statusDto.setTimestamp(new Date());
        this.statusService.updateStatus(statusDto, true);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteStatus(@RequestBody StatusDto statusDto) {
        setResponseHeader();

        this.statusService.deleteStatus(statusDto, true);
        return ResponseEntity.ok().build();
    }

    private void setResponseHeader() {
        String host = "localhost";
        String port = String.valueOf(context.getWebServer().getPort());
        String hostname = host + ":" + port;

        try {
            hostname = System.getenv().get("HOSTNAME");
        }catch (Exception ignored){}

        httpServletResponse.setHeader("x-origin", hostname);
    }
}
