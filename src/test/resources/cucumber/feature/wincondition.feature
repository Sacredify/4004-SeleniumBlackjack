Feature: Win Conditions
  To outline the winning conditions of the game.

  Scenario: Player immediately wins if they have 7 cards without busting (7 card charlie)
    Given a card in the player's hand with the rank 'THREE' and suit 'DIAMONDS' and hidden 'true'
    And another card in the player's hand with the rank 'THREE' and suit 'HEARTS' and hidden 'false'
    And another card in the player's hand with the rank 'TWO' and suit 'DIAMONDS' and hidden 'false'
    And another card in the player's hand with the rank 'TWO' and suit 'SPADES' and hidden 'false'
    And another card in the player's hand with the rank 'TWO' and suit 'CLUBS' and hidden 'false'
    And another card in the player's hand with the rank 'TWO' and suit 'HEARTS' and hidden 'false'
    When the player draws his seventh card with the rank 'ACE_LOW' and suit 'HEARTS' and hidden 'false'
    And the player's hand value doesn't exceed 21
    Then the player immediately wins with a seven card charlie
    And the player's status should be set
    And the other player's statuses should be set