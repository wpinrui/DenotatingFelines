package Game;

import java.util.ArrayList;

public class Deck {
    ArrayList<Card> cards;

    public static Deck initDeckNoExplodeOrDefuse() {
    }

    void shuffle();
    Card draw();
}
