package socket.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer


@Configuration
class WebSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .nullDestMatcher().authenticated()
        .simpSubscribeDestMatchers('/user/queue/errors').permitAll()
        .simpDestMatchers('/app/**').hasAnyRole('ADMIN', 'AUTHOR', 'LEARNER', 'ACCOUNT_OWNER', 'HELPDESK', 'SUPER_ADMIN')
        .simpSubscribeDestMatchers('/user/**', '/topic/**').hasAnyRole('ADMIN', 'AUTHOR', 'LEARNER', 'ACCOUNT_OWNER', 'HELPDESK', 'SUPER_ADMIN')
        .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).hasAnyRole('ADMIN', 'AUTHOR', 'LEARNER', 'ACCOUNT_OWNER', 'HELPDESK', 'SUPER_ADMIN')
        .anyMessage().denyAll()

    }
    @Override
    protected boolean sameOriginDisabled() {
        return true
    }
}
