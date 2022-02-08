import io.sparkwork.common.CustomUserDetailsService
import io.sparkwork.json.JsonRenderer
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import socket.config.WebSecurityConfig
import socket.config.WebSocketConfig
import socket.config.WebSocketLogInfo
import socket.event.StompConnectEvent

// Place your Spring DSL code here
beans = {
    webSocketConfig WebSocketConfig
    webSecurityConfig WebSecurityConfig
    stompConnectEvent StompConnectEvent
    webSocketLogInfo WebSocketLogInfo
    localeResolver(SessionLocaleResolver) {
        defaultLocale= new Locale('en-US');
    }

    corsFilter(CorsFilter)
    accessTokenJsonRenderer(JsonRenderer)
    userDetailsService(CustomUserDetailsService)
}
