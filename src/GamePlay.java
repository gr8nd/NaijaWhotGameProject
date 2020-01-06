import java.util.ArrayList;

public class GamePlay {
    private boolean isComputerTurn = true;
    private boolean isPlayerTurn = false;

    public static void main(String[] args) {
        WhotGame game = new WhotGame();
        game.deal(6);
        System.out.println();
        ArrayList<Card> playerCards = game.getPlayerCardPile();
        for (int index = 0; index < playerCards.size(); index++) {
            System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
        }
        System.out.println();
    }
}
