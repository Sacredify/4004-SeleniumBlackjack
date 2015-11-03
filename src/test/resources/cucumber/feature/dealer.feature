Feature: Dealer
  To determine the option that the dealer of the game will use when it is their turn.

  Scenario: Dealer should always hit if his hand value is less than 17
    Given a card in the dealer's hand with the rank 'QUEEN' and suit 'DIAMONDS' and visibility 'true'
    And another card in the dealer's hand with the rank 'THREE' and suit 'HEARTS' and visibility 'false'
    When it is the dealer's turn to make a move
    Then the dealer should perform their turn
    And the dealer's last move should be 'HIT'
    And the dealer's hand should have one more card than before

  Scenario: Dealer should stay if their hand value totals 17 and they do not have an ace
    Given a card in the dealer's hand with the rank 'QUEEN' and suit 'DIAMONDS' and visibility 'true'
    And another card in the dealer's hand with the rank 'SEVEN' and suit 'HEARTS' and visibility 'false'
    When it is the dealer's turn to make a move
    Then the dealer should perform their turn
    And the dealer's last move should be 'STAY'
    And the dealer's hand should have the same number of cards