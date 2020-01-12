import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class GamePlay {
    private static boolean isComputerTurn = true;
    private static boolean isPlayerTurn = false;

    public static void main(String[] args) {
        WhotGame game = new WhotGame();
        boolean validDeal = true;
        int index;
        Random rand = new Random();
        System.out.println();
        Scanner input = new Scanner(System.in);
        try {
            game.deal(0);
        } catch (WhotGameException e) {
            validDeal = false;
            System.out.println(e.getMessage());
        }
        ArrayList<Card> playerCards = game.getPlayerCardPile();
        ArrayList<Card> computerCards = game.getComputerCardPile();
        Card startCard = game.getStartCard();
        if (validDeal) {
            while (!game.isThereWinner()) {
                try {
                    if (isComputerTurn) {
                        game.setComputerTurn(true);
                        System.out.printf("*****%s*****%n", startCard.toString());
                        game.rule(startCard);
                        while (!game.isNormalCard()) {
                            if (game.isHoldOn()) {
                                game.setHoldOn(false);
                                for (Card card : computerCards) {
                                    if (card.getFace() == startCard.getFace() || card.getSuit().equals(startCard.getSuit()))
                                        startCard = card;
                                    System.out.printf("*****%s*****%n", startCard.toString());
                                    {
                                        game.rule(startCard);
                                        game.play(startCard);
                                    }
                                }
                                game.setHoldOn(false);
                            } else if (game.isPickTwo()) {
                                System.out.print("Hit 'Enter' to see all cards");
                                String yes = input.nextLine();
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.println("-1. to pick two from the pile or a card to defend");
                                System.out.print("Select a card to play >> ");
                                int userInput = input.nextInt();
                                if (userInput == -1) {
                                    game.setComputerTurn(false);
                                    game.setPlayerTurn(true);
                                    game.draw();
                                    game.draw();
                                    game.setPlayerTurn(false);
                                    game.setComputerTurn(true);
                                }
                                game.setPickTwo(false);
                            } else if (game.isPickThree()) {
                                System.out.print("Hit 'Enter' to see all cards");
                                String yes = input.nextLine();
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.println("-1. to pick three from the pile or a card to defend");
                                System.out.print("Select a number to play >> ");
                                int userInput = input.nextInt();
                                if (userInput == -1) {
                                    game.setComputerTurn(false);
                                    game.setPlayerTurn(true);
                                    game.draw();
                                    game.draw();
                                    game.draw();
                                    game.setPlayerTurn(false);
                                    game.setComputerTurn(true);
                                }
                                game.setPickThree(false);

                            } else if (game.isGeneralMarket()) {
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.print("Hit 'Enter' to go to market");
                                String userInput = input.nextLine();
                                game.setComputerTurn(false);
                                game.setPlayerTurn(true);
                                game.draw();
                                game.setPlayerTurn(false);
                                game.setComputerTurn(true);
                                game.setGeneralMarket(false);

                            } else if (game.isWhot()) {
                                int randomIndex = rand.nextInt(computerCards.size());
                                Card wantedCard = computerCards.get(randomIndex);
                                System.out.printf("Computer needs *****%s*****%n", wantedCard.getSuit());
                                System.out.print("Hit 'Enter' to see all cards");
                                String yes = input.nextLine();
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.println("-1. to draw from the pile");
                                System.out.print("Select a card to play >> ");
                                int userInput = input.nextInt();
                                game.setWhot(false);
                            } else if (game.isSuspension()) {
                                game.setSuspension(false);
                                for (Card card : computerCards) {
                                    if (card.getFace() == startCard.getFace() || card.getSuit().equals(startCard.getSuit()))
                                        startCard = card;
                                    System.out.printf("*****%s*****%n", startCard.toString());
                                    {
                                        game.rule(startCard);
                                        game.play(startCard);
                                    }
                                }
                                game.setSuspension(false);
                            }
                        }

                        game.setHoldOn(false);
                        game.setNormalCard(false);
                        game.setPickTwo(false);
                        game.setPickThree(false);
                        game.setSuspension(false);
                        game.setGeneralMarket(false);
                        game.setComputerTurn(false);
                        game.setWhot(false);

                        isComputerTurn = false;
                    }

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
                } catch (InputMismatchException e) {
                    System.out.println("Select a valid card number");
                } catch (NumberFormatException e) {
                    System.out.println("Select a valid card number");
                }
            }
        }
    }

    public static void computer(Card startCard) {

    }

    public static void player(Card startCard) {

    }
}
