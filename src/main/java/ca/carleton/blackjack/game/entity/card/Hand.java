package ca.carleton.blackjack.game.entity.card;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Hand that a player has.
 * <p/>
 * Created by Mike on 10/27/2015.
 */
public class Hand {

    private final List<Card> cards = new ArrayList<>();

    public void addCard(final Card card) {
        this.cards.add(card);
    }

    public void clearHand() {
        this.cards.clear();
    }

    public long getHandValue() {
        return this.handValue();
    }

    public boolean isBust() {
        return this.handValue() > 21;
    }

    private long handValue() {
        return this.cards.stream()
                .map(card -> card.getRank().getValue())
                .collect(Collectors.counting());
    }
}
