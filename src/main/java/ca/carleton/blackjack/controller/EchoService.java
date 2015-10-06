package ca.carleton.blackjack.controller;

import org.springframework.stereotype.Component;

/**
 * Test service.
 *
 * Created by Mike on 10/6/2015.
 */
@Component
public class EchoService {

    public String getMessage(final String message) {
        return String.format("Received message: %s!", message);
    }

}
