package socket.event

import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import org.springframework.web.socket.messaging.SessionSubscribeEvent
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent

class StompConnectEvent  {
@EventListener
    void handleSessionConnect(SessionConnectEvent event) {
         StompHeaderAccessor.wrap(event.message)
    }
    @EventListener
    void handelSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor.wrap(event.message)
    }

    @EventListener
    void handleSessionSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor.wrap(event.message)
    }

    @EventListener
    void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor.wrap(event.getMessage())

    }

    @EventListener
    void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor.wrap(event.message)
    }
}

