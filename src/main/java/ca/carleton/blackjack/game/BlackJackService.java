package ca.carleton.blackjack.game;

import ca.carleton.blackjack.game.entity.AIPlayer;
import ca.carleton.blackjack.game.entity.Player;
import ca.carleton.blackjack.game.entity.card.Card;
import ca.carleton.blackjack.game.entity.card.Rank;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ca.carleton.blackjack.game.BlackJackGame.uniqueResult;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.containsAny;

/**
 * Service class implementing the logic of our program.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
@Service
public class BlackJackService {

    /**
     * Set the statuses of the given player hands.
     *
     * @param players the players.
     */
    public void setHandStatuses(final List<Player> players) {

    }

    /**
     * The action the dealer will take according to our game rules.
     *
     * @param dealer the dealer.
     * @return the option they will use for their next move.
     */
    public GameOption getDealerOption(final AIPlayer dealer) {
        final int handValue = (int) dealer.getHand().getHandValue();
        if (handValue < 17) {
            return GameOption.HIT;
        }
        final List<Card> cards = dealer.getHand().getCards();
        final List<Rank> cardRanks = cards.stream().map(Card::getRank).collect(toList());

        if (handValue == 17 && containsAny(cardRanks, asList(Rank.ACE_HIGH, Rank.ACE_LOW))) {
            return GameOption.HIT;
        } else if (handValue == 17) {
            return GameOption.STAY;
        }

        // TODO what about hand > 17? just keep hitting?
        return GameOption.HIT;
    }

    /**
     * The action the AI will take according to our game rules.
     *
     * @param player       the AI.
     * @param otherPlayers the other players.
     * @return the option they will use for their next move.
     */
    public GameOption getAIOption(final AIPlayer player, final List<Player> otherPlayers) {

        if (this.shouldAISplit(player.getHand().getCards())) {
            return GameOption.SPLIT;
        }

        final int handValue = (int) player.getHand().getHandValue();
        if (handValue == 21) {
            return GameOption.STAY;
        }

        for (final Player other : otherPlayers) {
            if (other.getLastOption() == GameOption.STAY) {
                if (other.getHand().getCards().size() == 2) {
                    // should only have 1 visible if their two initial cards.
                    final Card visibleCard = other.getHand()
                            .getCards()
                            .stream()
                            .filter(card -> !card.isHidden())
                            .collect(uniqueResult());
                    if (visibleCard.getRank().getValue() == 10
                            || visibleCard.getRank() == Rank.ACE_LOW
                            || visibleCard.getRank() == Rank.ACE_HIGH) {
                        return GameOption.HIT;
                    }
                }
            }
        }

        if (handValue >= 18 && handValue <= 20) {
            for (final Player other : otherPlayers) {
                final List<Card> visibleCards = other.getHand()
                        .getCards()
                        .stream()
                        .filter(card -> !card.isHidden())
                        .collect(Collectors.toList());
                final int valueOfVisibleCards = (int) (long) visibleCards.stream()
                        .mapToInt(card -> card.getRank().getValue())
                        .sum();
                if (valueOfVisibleCards > (handValue - 10)) {
                    return GameOption.HIT;
                }
            }
            return GameOption.STAY;
        }

        return GameOption.HIT;
    }

    /**
     * We only split if the initial cards are the same rank.
     */
    private boolean shouldAISplit(final List<Card> cards) {
        return cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }

}
