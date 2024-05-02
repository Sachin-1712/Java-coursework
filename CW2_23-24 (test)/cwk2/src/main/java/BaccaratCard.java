/**
 * Represents a single playing card with a suit and a rank.
 * 
 * @author sc23sss@leeds.ac.uk
 */
public class BaccaratCard extends Card {

    // Explicitly calls the superclass (Card) constructor
    public BaccaratCard(Rank rank, Suit suit) {
        super(rank, suit);
    }

    // Calculates the card's value using Baccarat rules
    public int value() {
        int cardValue = super.value();
        return cardValue == 10 ? 0 : cardValue;
    }
}
