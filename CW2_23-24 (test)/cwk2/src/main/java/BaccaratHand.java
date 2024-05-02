import java.util.List;

/**
 * Represents a hand of cards in the game of Baccarat. Provides methods for
 * managing cards within the hand and calculating its value.
 * 
 * @author sc23sss@leeds.ac.uk
 */
public class BaccaratHand extends CardCollection {

    /**
     * Calculates the hand's value based on Baccarat rules.
     * 
     * @return the value of the hand
     */
    public int value() {
        int totalValue = 0;
        for (Card card : cards) {
            totalValue += calculateCardValue(card);
        }
        return totalValue % 10;
    }

    private int calculateCardValue(Card card) {
        int rankValue = Math.min(card.getRank().ordinal() + 1, 10);
        return rankValue;
    }

    /**
     * Returns a list of all the cards in the hand.
     * 
     * @return a list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Calculates the value of a given set of cards.
     * 
     * @param cards the list of cards
     * @return the value of the hand
     */
    public int calculateScore(List<Card> cards) {
        int totalValue = 0;

        for (Card card : cards) {
            int rankValue = Math.min(card.getRank().ordinal() + 1, 10);
            totalValue += rankValue;
        }

        return totalValue % 10;
    }

    /**
     * This method is used to get a card from the deck at a specific index.
     *
     * @param index This is the position of the card in the deck. It should be a
     *              non-negative integer and less than the size of the deck.
     * @return Card This returns the card at the specified index in the deck.
     * @exception IllegalArgumentException On input index that is out of range
     *                                     (negative or greater than or equal to the
     *                                     size of the deck).
     */
    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        } else {
            throw new IllegalArgumentException("Invalid card index: " + index);
            // return null;
        }
    }

    /**
     * Determines if the hand is a natural (a two-card hand with a value of 8 or 9).
     * 
     * @return true if the hand is a natural, false otherwise
     */
    public boolean isNatural() {
        return size() == 2 && (value() == 8 || value() == 9);
    }

    /**
     * Provides a string representation of the cards in the hand.
     * 
     * @return a string representation of the hand
     */
    @Override
    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card.toString()).append(" ");
        }

        return handString.toString().trim(); // Trim any trailing space
    }

}
