import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a shoe (multiple decks) of playing cards used in Baccarat.
 * Handles card shuffling and dealing.
 * 
 * @author sc23sss@leeds.ac.uk
 */
public class Shoe extends CardCollection {

    // private static final int STANDARD_DECKS = 6; // Common default
    private final int numDecks;

    /**
     * Creates a Shoe containing a specified number of decks.
     * 
     * @param numDecks The number of decks to include in the shoe
     * @throws CardException if the number of decks is invalid (not 6 or 8)
     */
    public Shoe(int numDecks) throws CardException {
        if (numDecks != 6 && numDecks != 8) {
            throw new CardException("Invalid number of decks for shoe");
        }
        this.numDecks = numDecks;
        initialize();
    }

    /**
     * Initializes the shoe with the correct number of decks, ordered by suit and
     * rank.
     */
    private void initialize() {
        cards = new ArrayList<>(); // Use ArrayList for efficiency
        for (int i = 0; i < numDecks; i++) {
            for (Card.Suit suit : Card.Suit.values()) {
                for (Card.Rank rank : Card.Rank.values()) {
                    cards.add(new BaccaratCard(rank, suit));
                }
            }
        }
    }

    /**
     * Shuffles the cards in the shoe randomly.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Inside your Shoe class
    public int size() {
        return cards.size(); // Assuming 'cards' is your internal card storage
    }

    /**
     * Deals a card from the shoe.
     * 
     * @return The top card from the shoe
     * @throws CardException if the shoe is empty
     */
    public BaccaratCard deal() throws CardException {
        if (isEmpty()) {
            throw new CardException("Shoe is empty");
        }
        return (BaccaratCard) cards.remove(0); // Remove and return the top card
    }
}
