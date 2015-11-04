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

    private List<Card> splitCards;

    private boolean splitHand;

    private HandStatus handStatus;

    /**
     * Split the hand into two...dear lord.
     */
    public void splitHand() {
        this.splitCards = new ArrayList<>();
        this.splitHand = true;
    }

    public void addCard(final Card card) {
        this.cards.add(card);
    }

    public void addSplitCard(final Card card) {
        if (!this.splitHand) {
            throw new IllegalStateException("can't add to split hand! We didn't split yet!");
        }
        this.splitCards.add(card);
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public List<Card> getSplitCards() {
        return this.splitCards;
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

    public boolean isSplitHand() {
        return this.splitHand;
    }

    public HandStatus getHandStatus() {
        return this.handStatus;
    }

    public void setHandStatus(final HandStatus handStatus) {
        this.handStatus = handStatus;
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
