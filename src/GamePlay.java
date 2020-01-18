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
            game.deal(6);
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
                        System.out.println("Computer has played:");
                        System.out.printf("*****%s*****%n", startCard.toString());
                        game.rule(startCard);
                        while (!startCard.isNormalCard()) {
                            if (startCard.isHoldOn()) {
                                for (Card card : computerCards) {
                                    if (card.getFace() == startCard.getFace() || card.getSuit().equals(startCard.getSuit())) {
                                        startCard = card;
                                    System.out.printf("*****%s*****%n", startCard.toString());
                                        game.checkWinner();
                                        game.rule(startCard);
                                        break;
                                    }
                                }
                                game.play(startCard);
                            } else if (startCard.isPickTwo()) {
                                System.out.print("Hit 'Enter' to see all cards");
                                String yes = input.nextLine();
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.println("-1. to pick two from the pile or a card to defend");
                                System.out.print("Select a card to play >> ");
                                int userInput = input.nextInt();
                                while (true) {
                                    if (userInput == -1) {
                                        game.playerDraw();
                                        game.playerDraw();
                                        game.playerDraw();
                                        break;
                                    } else {
                                        Card defendCard = playerCards.get(userInput - 1);
                                        if (defendCard.isPickTwo()) {
                                            game.setComputerTurn(false);
                                            game.setPlayerTurn(true);
                                            game.play(defendCard);
                                            game.setPlayerTurn(false);
                                            game.setComputerTurn(true);
                                            startCard = defendCard;
                                            break;
                                        } else {
                                            System.out.println("The card is not fit for defend, if you don't have a valid card pick from the draw pile");
                                            for (index = 0; index < playerCards.size(); index++) {
                                                System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                            }
                                            System.out.println("-1. to pick two from the pile or a card to defend");
                                            System.out.print("Select a number to play >> ");
                                            userInput = input.nextInt();
                                        }

                                    }
                                }
                            } else if (startCard.isPickThree()) {
                                System.out.print("Hit 'Enter' to see all cards");
                                String yes = input.nextLine();
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.println("-1. to pick three from the pile or a card to defend");
                                System.out.print("Select a number to play >> ");
                                int userInput = input.nextInt();
                                while (true) {
                                    if (userInput == -1) {
                                        game.playerDraw();
                                        game.playerDraw();
                                        game.playerDraw();
                                        break;
                                    } else {
                                        Card defendCard = playerCards.get(userInput - 1);
                                        if (defendCard.isPickThree()) {
                                            game.setComputerTurn(false);
                                            game.setPlayerTurn(true);
                                            game.play(defendCard);
                                            game.setPlayerTurn(false);
                                            game.setComputerTurn(true);
                                            startCard = defendCard;
                                            break;
                                        } else {
                                            System.out.println("The card is not fit for defend, if you don't have a valid card pick from the draw pile");
                                            for (index = 0; index < playerCards.size(); index++) {
                                                System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                            }
                                            System.out.println("-1. to pick three from the pile or a card to defend");
                                            System.out.print("Select a number to play >> ");
                                            userInput = input.nextInt();
                                        }

                                    }
                                }


                            } else if (startCard.isGeneralMarket()) {
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.print("Hit 'Enter' to go to market");
                                String userInput = input.nextLine();
                                game.playerDraw();

                            } else if (startCard.isWhot()) {
                                int randomIndex = rand.nextInt(computerCards.size());
                                Card wantedCard = computerCards.get(randomIndex);
                                System.out.printf("Computer needs *****%s*****%n", wantedCard.getSuit());
                                System.out.print("Hit 'Enter' to see all cards");
                                String yes = input.nextLine();
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.println("-1. to draw from the pile");
                                System.out.println("Select a card to play >> ");
                                int userInput = input.nextInt();
                            } else if (startCard.isSuspension()) {
                                for (Card card : computerCards) {
                                    if (card.getFace() == startCard.getFace() || card.getSuit().equals(startCard.getSuit())) {
                                        startCard = card;
                                    System.out.printf("*****%s*****%n", startCard.toString());
                                        game.checkWinner();
                                        game.rule(startCard);
                                        break;
                                    }
                                }
                                game.play(startCard);
                            }
                        }

                        game.setComputerTurn(false);

                        isComputerTurn = false;
                        game.setPlayerTurn(true);
                    } else {
                        System.out.print("Hit 'Enter' to see all your cards");
                        String yes = input.nextLine();
                        for (index = 0; index < playerCards.size(); index++) {
                            System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                        }
                        System.out.println("-1. to draw from the pile");
                        System.out.println("Select a card to play >> ");
                        int userInput = input.nextInt();
                        if (userInput == -1) {
                            game.playerDraw();
                            isComputerTurn = true;
                            game.setComputerTurn(true);
                            game.setPlayerTurn(false);
                        } else {
                            boolean validPlay = false;
                            Card playedCard = playerCards.get(userInput - 1);
                            if (playedCard.getFace() == startCard.getFace() || playedCard.getSuit().equals(startCard.getSuit())) {
                                startCard = playedCard;
                                System.out.printf("*****%s*****%n", startCard.toString());
                                game.checkWinner();
                                game.rule(startCard);
                                game.play(playedCard);
                            } else {
                                while (!validPlay) {
                                    System.out.print("Hit 'Enter' to see all your cards");
                                    yes = input.nextLine();
                                    for (index = 0; index < playerCards.size(); index++) {
                                        System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                    }
                                    System.out.println("-1. to draw from the pile");
                                    System.out.println("Select a card to play >> ");
                                    userInput = input.nextInt();
                                    if (userInput == -1) {
                                        game.playerDraw();
                                        isComputerTurn = true;
                                        game.setComputerTurn(true);
                                        validPlay = true;
                                    } else {
                                        playedCard = playerCards.get(userInput - 1);
                                        if (playedCard.getFace() == startCard.getFace() || playedCard.getSuit().equals(startCard.getSuit())) {
                                            startCard = playedCard;
                                            System.out.printf("*****%s*****%n", startCard.toString());
                                            game.checkWinner();
                                            game.rule(startCard);
                                            validPlay = true;
                                        }
                                    }
                                    if (!validPlay) {
                                        System.out.println("Computer has played:");
                                        System.out.printf("*****%s*****%n", startCard.toString());
                                    }
                                }
                            }
                            if (startCard.isSuspension()) {

                            } else if (startCard.isHoldOn()) {

                            } else if (startCard.isPickTwo()) {
                                if (computerCards.size() >= 10) {
                                    for (Card card : computerCards) {
                                        if (card.isPickTwo()) {
                                            Card computerDefendCard = card;
                                            startCard = computerDefendCard;
                                            System.out.printf("*****%s*****%n", startCard.toString());
                                            game.play(computerDefendCard);
                                        }
                                    }
                                } else {
                                    game.computerDraw();
                                    game.computerDraw();
                                }

                            } else if (startCard.isPickThree()) {
                                if (computerCards.size() >= 9) {
                                    for (Card card : computerCards) {
                                        if (card.isPickThree()) {
                                            Card computerDefendCard = card;
                                            startCard = computerDefendCard;
                                            System.out.printf("*****%s*****%n", startCard.toString());
                                            game.play(computerDefendCard);
                                        }
                                    }
                                } else {
                                    game.computerDraw();
                                    game.computerDraw();
                                    game.computerDraw();
                                }
                            } else if (startCard.isWhot()) {

                            } else if (startCard.isGeneralMarket()) {
                                game.computerDraw();
                            }
                            game.setThereWinner(true);
                        }
                    }
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
