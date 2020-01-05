import java.util.ArrayList;

public class WhotGame {
    private boolean isThereWinner = false;
    private NaijaWhots whots;
    private Card[] cards;
    private ArrayList<Card> drawPile;
    private ArrayList<Card> computerCardPile;
    private ArrayList<Card> playerCardPile;

    public WhotGame() {
        whots = new NaijaWhots();
        cards = whots.getWhots();
        for (Card card : cards) {
            drawPile.add(card);
        }
    }

    public void deal(int number) {
        for (int index = 0; index < number * 2; index++) {
            computerCardPile.add(drawPile.get(0));
            drawPile.remove(0);
            playerCardPile.add(drawPile.get(0));
            drawPile.remove(0);
        }
    }

    public void play() {
        ArrayList<Card> cards = new ArrayList<>(54);
        NaijaWhots whots = new NaijaWhots();
        for (Card card : whots.getWhots()) {
            System.out.println(card.toString());
        }
        System.out.println(whots.getWhots().length);
    }

    public void rule(Card card) {

    }

    public void checkWinner() {

    }
}
