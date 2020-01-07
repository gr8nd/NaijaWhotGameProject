import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GamePlay {
    private boolean isComputerTurn = true;
    private boolean isPlayerTurn = false;

    public static void main(String[] args) {
        WhotGame game = new WhotGame();
        game.deal(6);
        System.out.println();
        Scanner input = new Scanner(System.in);
        ArrayList<Card> playerCards = game.getPlayerCardPile();
        ArrayList<Card> computerCards = game.getComputerCardPile();
        try {
            int index;
            for (index = 0; index < playerCards.size(); index++) {
                System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
            }
            System.out.println("-1. to draw from the pile");
            System.out.print("Select a card to play >> ");
            int userInput = input.nextInt();
            game.rule(playerCards.get(index - 1));
        } catch (InputMismatchException err) {
            System.out.println("Select a valid card number");
        } catch (NumberFormatException err) {
            System.out.println("Select a valid card number");
        }

    }
}
