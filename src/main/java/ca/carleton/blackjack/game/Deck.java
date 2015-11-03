package ca.carleton.blackjack.game;

import ca.carleton.blackjack.game.entity.card.Card;
import ca.carleton.blackjack.game.entity.card.Rank;
import ca.carleton.blackjack.game.entity.card.Suit;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.shuffle;

/**
 * Represents a card deck.
 * <p/>
 * Created by Mike on 11/3/2015.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Deck {

    // TODO what to do about ace high/low?
    private List<Card> cards;

    @PostConstruct
    public void init() {
        this.cards = new LinkedList<>();
        for (final Rank rank : Rank.values()) {
            for (final Suit suit : Suit.values()) {
                this.cards.add(new Card(rank, suit, false));
            }
        }
        shuffle(this.cards);
    }

    /**
     * Reset the deck.
     */
    public void reset() {
        this.init();
    }

    /**
     * Draw from the deck.
     *
     * @return the card, or null if no cards are left.
     */
    public Card draw() {
        return this.cards.size() >= 1 ? this.cards.remove(0) : null;
    }

}
