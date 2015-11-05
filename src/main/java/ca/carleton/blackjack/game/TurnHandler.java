package ca.carleton.blackjack.game;

import ca.carleton.blackjack.game.entity.AIPlayer;
import ca.carleton.blackjack.game.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.carleton.blackjack.game.BlackJackGame.uniqueResult;
import static java.util.Collections.shuffle;

/**
 * Handle the order of the turns.
 * <p/>
 * Created by Mike on 11/4/2015.
 */
@Service
public class TurnHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TurnHandler.class);

    private List<Player> ordering;

    /**
     * Initialize a new round with the given players.
     *
     * @param players the players.
     */
    public void initiliazeNewRound(final List<Player> players) {

        this.ordering = new ArrayList<>();

        // Add real players first - excluding the ones that STAYED.
        final List<Player> realPlayers = players.stream()
                .filter(Player::isReal)
                .filter(player -> player.getLastOption() == null || player.getLastOption() != GameOption.STAY)
                .collect(Collectors.toList());
        shuffle(realPlayers);
        this.ordering.addAll(realPlayers);

        // Add ai players next
        final List<Player> aiPlayers = players.stream()
                .filter(player -> player instanceof AIPlayer && !((AIPlayer) player).isDealer())
                .collect(Collectors.toList());
        shuffle(aiPlayers);
        this.ordering.addAll(aiPlayers);

        // Add dealer
        final Player dealer = players.stream()
                .filter(player -> player instanceof AIPlayer && ((AIPlayer) player).isDealer())
                .collect(uniqueResult());
        this.ordering.add(dealer);
        LOG.info("New ordering: {}", this.ordering);
    }

    /**
     * Get the next player to go.
     *
     * @return the next player.
     */
    public Player getNextPlayer() {
        if (this.ordering.size() == 0) {
            throw new IllegalStateException("No players remaining!");
        }
        return this.ordering.remove(0);
    }

    /**
     * true if we need to re-initialize the ordering.
     */
    public boolean requiresReInitialization() {
        return this.ordering == null || this.ordering.size() == 0;
    }
}

