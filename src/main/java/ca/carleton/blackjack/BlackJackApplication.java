package ca.carleton.blackjack;

import ca.carleton.blackjack.game.BlackJackSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Main class - launch the application and register endpoint handlers.
 *
 * Created by Mike on 10/6/2015.
 */
@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@EnableAutoConfiguration
@EnableWebSocket
@ComponentScan(basePackages = "ca.carleton.blackjack")
public class BlackJackApplication extends SpringBootServletInitializer implements WebSocketConfigurer {

    @Autowired
    private BlackJackSocketHandler blackJackSocketHandler;

    public static void main(final String[] args) {
        SpringApplication.run(BlackJackApplication.class, args);
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this.blackJackSocketHandler, "/game")
                .withSockJS();
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder springApplicationBuilder) {
        return springApplicationBuilder.sources(BlackJackApplication.class);
    }

}
