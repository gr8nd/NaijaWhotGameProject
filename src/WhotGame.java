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
        computerCardPile = new ArrayList<>();
        playerCardPile = new ArrayList<>();
        drawPile = new ArrayList<>();
        cards = whots.getWhots();
        for (Card card : cards) {
            drawPile.add(card);
        }
    }

    public void deal(int number) {
        for (int index = 0; index < number; index++) {
            computerCardPile.add(drawPile.get(0));
            drawPile.remove(0);
            playerCardPile.add(drawPile.get(0));
            drawPile.remove(0);
        }
    }

    public void play() {
        ArrayList<Card> cards = new ArrayList<>(54);
        for (Card card : this.drawPile) {
            System.out.println(card.toString());
        }
        System.out.println(drawPile.size());
    }

    public void rule(Card card) {

    }

    public void checkWinner() {

    }

    public boolean isThereWinner() {
        return isThereWinner;
    }

    public void setThereWinner(boolean thereWinner) {
        isThereWinner = thereWinner;
    }

    public ArrayList<Card> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(ArrayList<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public ArrayList<Card> getComputerCardPile() {
        return computerCardPile;
    }

    public void setComputerCardPile(ArrayList<Card> computerCardPile) {
        this.computerCardPile = computerCardPile;
    }

    public ArrayList<Card> getPlayerCardPile() {
        return playerCardPile;
    }

    public void setPlayerCardPile(ArrayList<Card> playerCardPile) {
        this.playerCardPile = playerCardPile;
    }
}
