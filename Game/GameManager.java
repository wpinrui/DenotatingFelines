package Game;

import java.util.ArrayList;

/**
 * A class that handles the overall game, including the players, cards and
 * winning condition. Can be thought of as the person officiating the game.
 */
public class GameManager {
    /**
     * The deck that is in play.
     */
    private Deck deck;

    /**
     * The players participating in the game, including those who have exploded.
     * There can be 2 to 5 players.
     */
    private Player[] players;

    /**
     * A number to keep track of whose turn it is
     */
    private int turnIndex;

    /**
     * The collection of cards from which players draw during the game.
     */
    private Deck drawpile;

    /**
     * The collection of cards that is added to as players play actions.
     */
    private Deck discardPile;

    /**
     * Actions done to begin a game, which are as follows:
     * 1. Remove exploding kittens from the deck and set them aside
     * 2. Remove all defuse cards from the deck and deal 1 to each player.
     * 3. Insert the remaining defuse cards back into the deck
     *    If there are only 2 or 3 players, put only 2 defuse cards back
     *    into the deck.
     * 4. Shuffle the deck and deal 7 cards to each player.
     * 5. Insert n - 1 exploding kittens back into the deck, where n is
     *    the number of players.
     * 6. Shuffle the resulting deck, and this is the draw pile.
     * @param numPlayers The number of players in the game
     */
    public void startGame(int numPlayers) {
        if (numPlayers < 2 || numPlayers > 5) {
            throw new RuntimeException("Can only have 2 to 5 players.");
        }

        // Deal cards to players
        this.deck = Deck.initDeckNoExplodeOrDefuse();
        this.players = Player.init(numPlayers);
        for (Player player : this.players) {
            player.give(new DefuseCard());
        }
        this.deck.shuffle();
        for (int i = 0; i < 7; i++) {
            for (Player player : this.players) {
                player.give(this.deck.deal());
            }
        }

        // Insert remaining defuse cards into deck
        if (numPlayers < 4) {
            for (int i = 0; i < 1; i++) {
                this.deck.insert(new DefuseCard());
            }
        }
        else {
            for (int i = 0; i < 6 - numPlayers; i++) {
                this.deck.insert(new DefuseCard());
            }
        }

        // Insert exploding kittens into deck
        for (int i = 0; i < numPlayers - 1; i++) {
            this.deck.insert(new ExplodeCard());
        }

        this.deck.shuffle();
        this.drawpile = this.deck;
        this.discardPile = new Deck();

    }

    /**
     * Allows a Player to play any number of cards, before drawing a card to
     * end their turn. The drawing of a card can be prevented with certain
     * cards like Skip and Attack. Calls the next player recursively until
     * the game ends.
     * @param player The player whose turn it is
     */
    public void mainLoop(Player player) {
        Turn turn = player.passOrPlay();

        // Any player can play a nope card at any time, and in any order
        if (turn.isNopeable) {
            ArrayList<Player> doneNoping = {} // TODO: implement doneNoping logic
            while (doneNoping.size() < this.players.length) {
                for (Player player : this.players) {
                    if (player.passOrNope()) {
                        turn.nope();
                        break;
                    }
                }
            }
        }

    }

    /**
     * Declares the winner and performs any other endgame-related tasks like
     * scorekeeping, if applicable.
     */
    public void endGame();
}
