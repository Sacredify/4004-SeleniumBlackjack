Feature: AI
  To determine the option that the AI of the game will use when it is their turn.

  Scenario: AI should split given two initial cards of the same rank
    Given a card in the AI's hand with the rank 'QUEEN' and suit 'DIAMONDS' and visibility 'true'
    And another card in the AI's hand with the rank 'QUEEN' and suit 'HEARTS' and visibility 'false'
    When it is the AI's turn to make a move
    Then the AI should perform their turn
    And the AI's last move should be 'SPLIT'
    And the AI's hand size should remained unchanged

  Scenario: AI should stay with a card value of 21
    Given a card in the AI's hand with the rank 'QUEEN' and suit 'DIAMONDS' and visibility 'true'
    And another card in the AI's hand with the rank 'ACE_HIGH' and suit 'HEARTS' and visibility 'false'
    When it is the AI's turn to make a move
    Then the AI should perform their turn
    And the AI's last move should be 'STAY'
    And the AI's hand size should remained unchanged

  Scenario: AI should hit if another player has stayed with two cards with the visible one being a 10
    Given a card in the AI's hand with the rank 'QUEEN' and suit 'DIAMONDS' and visibility 'true'
    And another card in the AI's hand with the rank 'THREE' and suit 'HEARTS' and visibility 'false'
    And a player with two cards in their hand consisting of 'QUEEN' of 'SPADES', visibility 'true' and 'SEVEN' of 'CLUBS', visibility 'false'
    When it is the AI's turn to make a move
    Then the AI should perform their turn
    And the AI's last move should be 'HIT'
    And the other player's last move should be 'STAY'
    And the AI's hand should have one more card than before