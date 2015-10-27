package ca.carleton.blackjack.game.entity.card;

/**
 * Represents a single playing card.
 * <p/>
 * Created by Mike on 10/27/2015.
 */
public class Card {

    private final Rank rank;

    private final Suit suit;

    public Card(final Rank rank, final Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return this.rank + " of " + this.suit;
    }

    /**
     * <div class="card rank-7 spades">
     * <span class="rank">7</span>
     * <span class="suit">&spades;</span>
     * </div>
     *
     * @return the HTML representation of this card.
     */
    public String toHTMLString() {
        return String.format("<div class=\"card rank-%s %s\">\n" +
                        "                        <span class=\"rank\">%d</span>\n" +
                        "                        <span class=\"suit\">&%s;</span>\n" +
                        "                    </div>",
                this.rank.getHtml(),
                this.suit.toString().toLowerCase(),
                this.rank.getValue(),
                this.suit.toString().toLowerCase());
    }

    public Rank getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }
}
