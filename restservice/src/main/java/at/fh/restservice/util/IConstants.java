package at.fh.restservice.util;

import java.util.UUID;

public interface IConstants {
    String STATUS_EXCHANGE = "exchange.status";
    String STATUS_QUEUE = "queue.status." + UUID.randomUUID();
}
