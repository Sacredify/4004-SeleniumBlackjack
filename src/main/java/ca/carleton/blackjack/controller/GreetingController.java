package ca.carleton.blackjack.controller;

import ca.carleton.blackjack.message.GreetingMessage;
import ca.carleton.blackjack.message.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Controller for the main greeting page.
 *
 * Created by Mike on 10/6/2015.
 */
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GreetingMessage greeting(final HelloMessage message) {
        return new GreetingMessage(String.format("Hello %s! This is spring boot!", message.getName()));
    }
}
