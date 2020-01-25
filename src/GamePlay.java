import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class GamePlay {
    private boolean isComputerTurn = true;
    private WhotGame game;
    private boolean validDeal = true;
    private ArrayList<Card> playerCards;
    private ArrayList<Card> computerCards;
    private int index;
    private Card previousCard;
    private Random rand = new Random();
    private Scanner input = new Scanner(System.in);
    public GamePlay()
    {
        game = new WhotGame();
        playerCards = game.getPlayerCardPile();
        computerCards = game.getComputerCardPile();
    }
    public void start()
    {
        System.out.println();
       deal(6);
        if (validDeal)
        {
            while (!game.isThereWinner())
            {
                    if (isComputerTurn)
                    {
                        computer();
                    } else
                        {
                        player();
                    }
            }
        }
    }

    public void computer() {
        game.setComputerTurn(true);
        System.out.println("Computer has played:");
        System.out.printf("*****%s*****%n", previousCard.toString());
        game.rule(previousCard);
        while (!previousCard.isNormalCard()) {
            if (previousCard.isHoldOn()) {
                for (Card card : computerCards) {
                    if (card.getFace() == previousCard.getFace() || card.getSuit().equals(previousCard.getSuit())) {
                        previousCard = card;
                        System.out.printf("*****%s*****%n", previousCard.toString());
                        game.play(previousCard);
                        game.rule(previousCard);
                        game.checkWinner();
                        break;
                    }
                }
            } else if (previousCard.isPickTwo()) {
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
                        boolean isComputerDrawingFromPile = true;
                        for (Card card : computerCards) {
                            if (card.getFace() == previousCard.getFace() || card.getSuit().equals(previousCard.getSuit())) {
                                previousCard = card;
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.checkWinner();
                                game.rule(previousCard);
                                isComputerDrawingFromPile = false;
                                break;
                            }
                        }
                        if (isComputerDrawingFromPile)
                        {
                            game.computerDraw();
                            isComputerTurn = false;
                            game.setComputerTurn(false);
                            game.setPlayerTurn(true);
                            break;
                        }
                    } else {
                        Card defendCard = playerCards.get(userInput - 1);
                        if (defendCard.isPickTwo()) {
                            game.setComputerTurn(false);
                            game.setPlayerTurn(true);
                            game.play(defendCard);
                            game.setPlayerTurn(false);
                            game.setComputerTurn(true);
                            previousCard = defendCard;
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
            } else if (previousCard.isPickThree()) {
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
                        boolean isComputerDrawingFromPile = true;
                        for (Card card : computerCards) {
                            if (card.getFace() == previousCard.getFace() || card.getSuit().equals(previousCard.getSuit())) {
                                previousCard = card;
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.checkWinner();
                                game.rule(previousCard);
                                isComputerDrawingFromPile = false;
                                break;
                            }
                        }
                        if (isComputerDrawingFromPile)
                        {
                            game.computerDraw();
                            isComputerTurn = false;
                            game.setComputerTurn(false);
                            game.setPlayerTurn(true);
                            break;
                        }
                    } else {
                        Card defendCard = playerCards.get(userInput - 1);
                        if (defendCard.isPickThree()) {
                            game.setComputerTurn(false);
                            game.setPlayerTurn(true);
                            game.play(defendCard);
                            game.setPlayerTurn(false);
                            game.setComputerTurn(true);
                            previousCard = defendCard;
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


            } else if (previousCard.isGeneralMarket()) {
                for (index = 0; index < playerCards.size(); index++) {
                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                }
                System.out.print("Hit 'Enter' to go to market");
                String userInput = input.nextLine();
                game.playerDraw();
                boolean isComputerDrawingFromPile = true;
                for (Card card : computerCards) {
                    if (card.getFace() == previousCard.getFace() || card.getSuit().equals(previousCard.getSuit())) {
                        previousCard = card;
                        System.out.printf("*****%s*****%n", previousCard.toString());
                        game.checkWinner();
                        game.rule(previousCard);
                        isComputerDrawingFromPile = false;
                        break;
                    }
                }
                if (isComputerDrawingFromPile)
                {
                    game.computerDraw();
                    isComputerTurn = false;
                    game.setComputerTurn(false);
                    game.setPlayerTurn(true);
                }

            } else if (previousCard.isWhot()) {
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
            } else if (previousCard.isSuspension()) {
                for (Card card : computerCards) {
                    if (card.getFace() == previousCard.getFace() || card.getSuit().equals(previousCard.getSuit())) {
                        previousCard = card;
                        System.out.printf("*****%s*****%n", previousCard.toString());
                        game.checkWinner();
                        game.rule(previousCard);
                        break;
                    }
                }
                game.play(previousCard);
            }else
            {
                game.setComputerTurn(false);
                isComputerTurn = false;
                game.setPlayerTurn(true);
            }
        }
        game.setComputerTurn(false);
        isComputerTurn = false;
        game.setPlayerTurn(true);

    }

    public void player() {
        try {
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
                if (playedCard.getFace() == previousCard.getFace() || playedCard.getSuit().equals(previousCard.getSuit())) {
                    previousCard = playedCard;
                    System.out.printf("*****%s*****%n", previousCard.toString());
                    game.checkWinner();
                    game.rule(previousCard);
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
                            if (playedCard.getFace() == previousCard.getFace() || playedCard.getSuit().equals(previousCard.getSuit())) {
                                previousCard = playedCard;
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.checkWinner();
                                game.rule(previousCard);
                                validPlay = true;
                            }
                        }
                        if (!validPlay) {
                            System.out.println("Computer has played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                        }
                    }
                }
                if (previousCard.isSuspension()) {

                } else if (previousCard.isHoldOn()) {

                } else if (previousCard.isPickTwo()) {
                    if (computerCards.size() >= 10) {
                        for (Card card : computerCards) {
                            if (card.isPickTwo()) {
                                Card computerDefendCard = card;
                                previousCard = computerDefendCard;
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.play(computerDefendCard);
                                game.rule(computerDefendCard);
                                break;
                            }
                        }
                    } else {
                        game.computerDraw();
                        game.computerDraw();
                    }

                } else if (previousCard.isPickThree()) {
                    if (computerCards.size() >= 9) {
                        for (Card card : computerCards) {
                            if (card.isPickThree()) {
                                Card computerDefendCard = card;
                                previousCard = computerDefendCard;
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.play(computerDefendCard);
                                game.rule(computerDefendCard);
                                break;
                            }
                        }
                    } else {
                        game.computerDraw();
                        game.computerDraw();
                        game.computerDraw();
                    }
                } else if (previousCard.isWhot()) {

                } else if (previousCard.isGeneralMarket()) {
                    game.computerDraw();
                }
                game.setThereWinner(true);
            }

        } catch (InputMismatchException e) {
            System.out.println("Select a valid card number");
        } catch (NumberFormatException e) {
            System.out.println("Select a valid card number");
        }

    }
    public void deal(int number)
    {
        try {
            game.deal(number);
            previousCard = game.getStartCard();
        } catch (WhotGameException e) {
            validDeal = false;
            System.out.println(e.getMessage());
        }
    }
}
