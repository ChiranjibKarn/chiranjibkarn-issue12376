package socket.config;

/**
 * Created by ajay on 5/2/18.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;
import javax.annotation.PostConstruct;

public class WebSocketLogInfo {
    @Autowired
    WebSocketMessageBrokerStats webSocketMessageBrokerStats;
    @PostConstruct
    public void init() {
        webSocketMessageBrokerStats.setLoggingPeriod(600000);// desired time in millis
    }
}
