package ca.carleton.blackjack.game.entity.card;

import java.util.ArrayList;
import java.util.List;

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

    public List<Card> getCards() {
        return this.cards;
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

    private int handValue() {
        return this.cards.stream()
                .mapToInt(card -> card.getRank().getValue())
                .sum();
    }

    @Override
    public boolean equals(final Object rhs) {
        if (rhs instanceof Hand) {
            for (final Card otherCard : ((Hand) rhs).getCards()) {
                if (!this.cards.contains(otherCard)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
