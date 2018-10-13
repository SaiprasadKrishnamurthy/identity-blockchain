package web;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@SpringBootApplication
@EnableWebSocketMessageBroker
public class Starter {

    @Configuration
    class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/stomp").withSockJS();
        }
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Starter.class);
        springApplication.setWebEnvironment(true);
        springApplication.run(args);
    }
}
