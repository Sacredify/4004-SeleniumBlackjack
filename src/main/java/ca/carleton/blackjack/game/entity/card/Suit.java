package ca.carleton.blackjack.game.entity.card;

import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * The suit of a card.
 * <p/>
 * Created by Mike on 10/27/2015.
 */
public enum Suit {
    HEARTS,
    CLUBS,
    DIAMONDS,
    SPADES;

    @Override
    public String toString() {
        return capitalize(this.name().toLowerCase());
    }
}
