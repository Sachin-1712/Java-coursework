import java.util.Scanner;

/**
 * The main class for the Baccarat game. Handles command-line arguments
 * for interactive mode and starts the game.
 * 
 * @author sc23sss@leeds.ac.uk
 */
public class Baccarat {
  public static void main(String[] args) {
    // Check if the program was started in interactive mode
    boolean interactive = args.length > 0 && (args[0].equals("-i") || args[0].equals("--interactive"));
    // Create a new game
    BaccaratGame game = new BaccaratGame(interactive);
    // Start the game
    game.play();
  }
}

class BaccaratGame {
  // Define constants and variables
  private static final int MIN_CARDS_IN_SHOE = 6;
  private static final Scanner SCANNER = new Scanner(System.in);
  private final boolean interactive;
  private Shoe shoe;
  private int numRoundsPlayed;
  private int playerWins;
  private int bankerWins;
  private int ties;

  /**
   * Constructs a new BaccaratGame object and initializes game variables.
   * 
   * @param interactive true if the game should be played in interactive mode,
   *                    false otherwise
   */
  public BaccaratGame(boolean interactive) {
    // Initialize game variables
    this.interactive = interactive;
    reset();
  }

  public void play() {
    // Play rounds until there are not enough cards left in the shoe
    while (shoe.size() >= MIN_CARDS_IN_SHOE) {
      playRound();
      // If the game is in interactive mode, ask the user if they want to play another
      // round
      if (interactive && !playAnotherRound()) {
        break;
      }
    }
    displayEndOfGameSummary();
  }

  private void playRound() {
    // Create hands for the player and the banker
    BaccaratHand playerHand = new BaccaratHand();
    BaccaratHand bankerHand = new BaccaratHand();
    // Deal cards to the player and the banker
    dealCards(shoe, playerHand, bankerHand);
    // Determine the winner
    String winner = determineWinner(playerHand, bankerHand, shoe);
    // Display information about the round
    displayRoundInfo(++numRoundsPlayed, playerHand, bankerHand, winner);
    // Update the game statistics
    updateStatistics(winner);
  }

  private static void displayRoundInfo(int round, BaccaratHand playerHand, BaccaratHand bankerHand, String winner) {
    System.out.println("\nRound " + round);

    // Calculate and print scores for the first two cards
    int playerHandScore = playerHand.calculateScore(playerHand.getCards().subList(0, 2));
    int bankerHandScore = bankerHand.calculateScore(bankerHand.getCards().subList(0, 2));

    System.out.println("Player Hand: " + playerHand.getCards().subList(0, 2) + " = " + playerHandScore);
    System.out.println("Banker Hand: " + bankerHand.getCards().subList(0, 2) + " = " + bankerHandScore);

    // Conditional printing of third card messages
    if (playerHand.size() == 3 && playerHandScore <= 5) {
      System.out.println("Dealing third card to player...");
    }
    if (bankerHand.size() == 3 && bankerHandScore <= 5) {
      System.out.println("Dealing third card to banker...");
    }

    // Print updated hands if third cards were dealt (with scores)
    if (playerHand.size() == 3) {
      int playerScore = playerHand.value(); // Recalculate score
      System.out.println("Player Hand: " + playerHand + " = " + playerScore);
    }
    if (bankerHand.size() == 3) {
      int bankerScore = bankerHand.value(); // Recalculate score
      System.out.println("Banker Hand: " + bankerHand + " = " + bankerScore);
    }

    // 4. Print the winner
    if (winner.equals("Tie")) {
      System.out.println("Tie\n");
    } else {
      System.out.println(winner + " wins!\n");
    }
  }

  private static void dealCards(Shoe shoe, BaccaratHand playerHand, BaccaratHand bankerHand) throws CardException {
    for (int i = 0; i < 2; i++) {
      // Deal two cards each to the player and the banker
      playerHand.add(shoe.deal());
      bankerHand.add(shoe.deal());
    }
  }

  /**
   * Determines the winner of a Baccarat round based on player and banker hand
   * scores.
   * Implements the rules of Baccarat (punto banco) for when a player or banker should
   * draw
   * a third card.
   * 
   * @param playerHand the player's hand
   * @param bankerHand the banker's hand
   * @param shoe       the shoe (deck of cards) used for dealing
   * @return "Player" if the player wins, "Banker" if the banker wins, or "Tie"
   */
  private static String determineWinner(BaccaratHand playerHand, BaccaratHand bankerHand, Shoe shoe) {
    int playerScore = playerHand.value();
    int bankerScore = bankerHand.value();

    // 1. Check for Natural Win (Player or Banker)
    if (playerScore >= 8 || bankerScore >= 8) {
      if (playerScore > bankerScore) {
        return "Player";
      } else if (bankerScore > playerScore) {
        return "Banker";
      } else {
        return "Tie";
      }
    }

    // 2. Player Stand or Draw
    if (playerScore <= 5) { // Player Draws a Third Card
      try {

        playerHand.add(shoe.deal());
        playerScore = playerHand.value(); // Recalculate score
      } catch (IllegalArgumentException e) {
        // Handle the case where the player doesn't get a third card
        System.err.println("Error getting third card for player: " + e.getMessage());
      }
    }

    // 3. Banker Draw Rules
    boolean bankerDraws = false;

    if (bankerScore <= 2) {
      bankerDraws = true;
    } else if (bankerScore == 3) {
      if (playerHand.size() >= 3) { // Check if the player has a third card
        bankerDraws = playerHand.getCard(2).value() != 8;
      }
    } else if (bankerScore == 4) {
      if (playerHand.size() >= 3) {
        bankerDraws = 2 <= playerHand.getCard(2).value() && playerHand.getCard(2).value() <= 7;
      }
    } else if (bankerScore == 5) {
      if (playerHand.size() >= 3) {
        bankerDraws = 4 <= playerHand.getCard(2).value() && playerHand.getCard(2).value() <= 7;
      }
    } else if (bankerScore == 6) {
      if (playerHand.size() >= 3) {
        bankerDraws = 6 <= playerHand.getCard(2).value() && playerHand.getCard(2).value() <= 7;
      }
    } // Banker stands on 7

    if (bankerDraws) {

      bankerHand.add(shoe.deal());
      bankerScore = bankerHand.value();
    }

    if (playerHand.size() == 3) { // Player got a third card
      playerScore = playerHand.value();
    }
    if (bankerHand.size() == 3) { // Banker got a third card
      bankerScore = bankerHand.value();
    }
    // 4. Final Comparison
    if (playerScore > bankerScore) {
      return "Player";
    } else if (bankerScore > playerScore) {
      return "Banker";
    } else {
      return "Tie";
    }
  }

  private boolean playAnotherRound() {
    if (!interactive)
      return true; // Always continue in non-interactive mode

    System.out.print("Play another round (y/n)? ");
    String answer = SCANNER.nextLine().trim().toUpperCase();
    return answer.startsWith("Y");
  }

  private void updateStatistics(String winner) {
    if (winner.equals("Player")) {
      playerWins++;
    } else if (winner.equals("Banker")) {
      bankerWins++;
    } else {
      ties++;
    }
  }

  private void displayEndOfGameSummary() {
    System.out.println("\nGame Summary:");
    System.out.println("Rounds Played: " + numRoundsPlayed);
    System.out.println("Player Wins: " + playerWins);
    System.out.println("Banker Wins: " + bankerWins);
    System.out.println("Ties: " + ties);
  }

  private void reset() {
    shoe = new Shoe(6);
    shoe.shuffle();
    numRoundsPlayed = 0;
    playerWins = 0;
    bankerWins = 0;
    ties = 0;
  }
}
