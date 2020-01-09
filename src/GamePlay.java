import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GamePlay {
    private static boolean isComputerTurn = true;
    private static boolean isPlayerTurn = false;

    public static void main(String[] args) {
        WhotGame game = new WhotGame();
        game.deal(6);
        System.out.println();
        Scanner input = new Scanner(System.in);
        ArrayList<Card> playerCards = game.getPlayerCardPile();
        ArrayList<Card> computerCards = game.getComputerCardPile();
        Card startcard = game.getStartCard();
        while (!game.isThereWinner()) {
            try {
                if (isComputerTurn) {
                    game.setComputerTurn(true);
                    System.out.printf("*****%s*****%n", startcard.toString());
                    game.rule(startcard);
                    while (!game.isNormalCard()) {
                        if (game.isHoldOn() || game.isSuspension()) {
                            for (Card card : computerCards) {
                                if (card.getFace() == startcard.getFace() || card.getSuit().equals(startcard.getSuit()))
                                    startcard = card;
                                System.out.printf("*****%s*****%n", startcard.toString());
                                {
                                    game.rule(startcard);
                                }
                            }
                        } else if (game.isPickTwo()) {

                        } else if (game.isPickThree()) {

                        } else if (game.isGeneralMarket()) {

                        } else if (game.isWhot()) {

                        }
                    }

                    game.setHoldOn(false);
                    game.setNormalCard(false);
                    game.setPickTwo(false);
                    game.setPickThree(false);
                    game.isSuspension();
                    game.setGeneralMarket(false);
                    game.setComputerTurn(false);

                    isComputerTurn = false;
                }
                int index;
                System.out.print("Hit 'Enter' to see all cards");
                String yes = input.nextLine();
                for (index = 0; index < playerCards.size(); index++) {
                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                }
                System.out.println("-1. to draw from the pile");
                System.out.print("Select a card to play >> ");
                int userInput = input.nextInt();
                game.rule(playerCards.get(userInput - 1));
                game.setThereWinner(true);
            } catch (InputMismatchException err) {
                System.out.println("Select a valid card number");
            } catch (NumberFormatException err) {
                System.out.println("Select a valid card number");
            }
        }
    }
}
