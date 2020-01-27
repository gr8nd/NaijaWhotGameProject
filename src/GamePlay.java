import java.util.ArrayList;
import java.util.InputMismatchException;
import java.security.SecureRandom;
import java.util.Scanner;

public class GamePlay {
    private boolean isComputerTurn = true;
    private WhotGame game;
    private boolean validDeal = true;
    private ArrayList<Card> playerCards;
    private ArrayList<Card> computerCards;
    private int index;
    private Card previousCard;
    private SecureRandom rand = new SecureRandom();
    private Scanner input = new Scanner(System.in);
    private static int counter = 0;
    public GamePlay()
    {
        game = new WhotGame();
        playerCards = game.getPlayerCardPile();
        computerCards = game.getComputerCardPile();
        deal(6);
    }
    public GamePlay(int number)
    {
        game = new WhotGame();
        playerCards = game.getPlayerCardPile();
        computerCards = game.getComputerCardPile();
        deal(number);
    }
    public void start()
    {
        System.out.println();
        if (validDeal)
        {
            while (!game.isThereWinner())
            {
                    if (isComputerTurn)
                    {
                        computerPlay();
                    } else
                        {
                        playerPlay();
                    }
            }
        }
    }

    public void computerPlay() {
        if(counter == 0)
        {
            System.out.println("Computer has played:");
            System.out.printf("*****%s*****%n", previousCard.toString());
            game.rule(previousCard);
        }
        if(previousCard.isNormalCard() && counter != 0)
        {
            boolean isComputerDrawingFromPile = true;
            for (Card card : computerCards) {
                if (card.isHoldOn() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                    previousCard = card;
                    System.out.println("Computer has played:");
                    System.out.printf("*****%s*****%n", previousCard.toString());
                    game.play(previousCard);
                    game.rule(previousCard);
                    game.checkWinner();
                    isComputerDrawingFromPile = false;
                    break;
                }
            }
            if (isComputerDrawingFromPile) {
                game.computerDraw();
                isComputerTurn = false;
                System.out.println("Computer has drawn from pile");
            }
        }

        try {
            while (!previousCard.isNormalCard()) {
                if (previousCard.isHoldOn()) {
                    for (Card card : computerCards) {
                        if (card.isHoldOn() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                            previousCard = card;
                            System.out.println("Computer has played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            game.play(previousCard);
                            game.rule(previousCard);
                            game.checkWinner();
                            break;
                        }
                    }
                } else if (previousCard.isPickTwo() && !previousCard.isDefendCard()) {
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
                            System.out.println("You have picked two cards");
                            boolean isComputerDrawingFromPile = true;
                            for (Card card : computerCards) {
                                if (card.getFace() == previousCard.getFace() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                                    previousCard = card;
                                    System.out.println("Computer has played:");
                                    System.out.printf("*****%s*****%n", previousCard.toString());
                                    game.rule(previousCard);
                                    game.play(previousCard);
                                    game.checkWinner();
                                    isComputerDrawingFromPile = false;
                                    break;
                                }
                            }
                            if (isComputerDrawingFromPile) {
                                game.computerDraw();
                                isComputerTurn = false;
                                System.out.println("Computer has drawn from pile");
                                break;
                            }
                        } else {
                            Card defendCard = playerCards.get(userInput - 1);
                            if (defendCard.isPickTwo()) {
                                game.play(defendCard);
                                previousCard = defendCard;
                                System.out.println("You defended the PICK TWO with: ");
                                previousCard.setDefendCard(true);
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.checkWinner();
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
                } else if (previousCard.isPickThree() && !previousCard.isDefendCard()) {
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
                            System.out.println("You have picked three cards");
                            boolean isComputerDrawingFromPile = true;
                            for (Card card : computerCards) {
                                if (card.isPickThree() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                                    previousCard = card;
                                    System.out.println("Computer has played:");
                                    System.out.printf("*****%s*****%n", previousCard.toString());
                                    game.rule(previousCard);
                                    game.play(previousCard);
                                    game.checkWinner();
                                    isComputerDrawingFromPile = false;
                                    break;
                                }
                            }
                            if (isComputerDrawingFromPile) {
                                game.computerDraw();
                                isComputerTurn = false;
                                System.out.println("Computer has drawn from pile");
                                break;
                            }
                        } else {
                            Card defendCard = playerCards.get(userInput - 1);
                            if (defendCard.isPickThree()) {
                                game.rule(defendCard);
                                game.play(defendCard);
                                previousCard = defendCard;
                                System.out.println("You defended the PICK THREE with: ");
                                previousCard.setDefendCard(true);
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.checkWinner();
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
                    System.out.println("You have gone to market");
                    boolean isComputerDrawingFromPile = true;
                    for (Card card : computerCards) {
                        if (card.isGeneralMarket() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                            previousCard = card;
                            System.out.println("Computer has played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            game.rule(previousCard);
                            game.play(previousCard);
                            game.checkWinner();
                            isComputerDrawingFromPile = false;
                            break;
                        }
                    }
                    if (isComputerDrawingFromPile) {
                        game.computerDraw();
                        isComputerTurn = false;
                        System.out.println("Computer has drawn from pile");
                        break;
                    }

                } else if (previousCard.isWhot()) {
                    int randomIndex = rand.nextInt(computerCards.size());
                    Card wantedCard = computerCards.get(randomIndex);
                    while (wantedCard.isWhot())
                    {
                        randomIndex = rand.nextInt(computerCards.size());
                        wantedCard = computerCards.get(randomIndex);
                    }
                    System.out.printf("Computer needs *****%s*****%n", wantedCard.getSuit());
                    System.out.print("Hit 'Enter' to see all your cards");
                    System.out.println();
                    String yes = input.nextLine();
                    for (index = 0; index < playerCards.size(); index++) {
                        System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                    }
                    System.out.println("-1. to draw from the pile");
                    System.out.print("Select a card to play >> ");
                    int userInput = input.nextInt();
                    if (userInput == -1) {
                        System.out.println("You have drawn from pile");
                        game.playerDraw();
                    } else {
                        Card playedCard = playerCards.get(userInput - 1);
                        while (true) {
                            if (playedCard.getSuit().equals(wantedCard.getSuit()) || playedCard.isWhot()) {
                                previousCard = playedCard;
                                System.out.println("You played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                if(playedCard.isWhot())
                                {
                                    isComputerTurn = false;
                                }
                                break;
                            } else {
                                System.out.printf("The card you played didn't fit computer request");
                                System.out.printf("Computer needs *****%s*****%n", wantedCard.getSuit());
                                System.out.print("Hit 'Enter' to see all your cards");
                                yes = input.nextLine();
                                for (index = 0; index < playerCards.size(); index++) {
                                    System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                                }
                                System.out.println("-1. to draw from the pile");
                                System.out.print("Select a card to play >> ");
                                userInput = input.nextInt();
                                if (userInput == -1) {
                                    System.out.printf("You have drawn from pile");
                                    game.playerDraw();
                                    break;
                                }
                            }
                        }
                    }
                } else if (previousCard.isSuspension()) {
                    boolean isComputerDrawingFromPile = false;
                    for (Card card : computerCards) {
                        if (card.isSuspension() || card.getSuit().equals(previousCard.getSuit())) {
                            previousCard = card;
                            System.out.println("Computer has played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            game.play(previousCard);
                            game.checkWinner();
                            game.rule(previousCard);
                            isComputerDrawingFromPile = true;
                            break;
                        }
                    }
                    if (!isComputerDrawingFromPile) {
                        game.computerDraw();
                        isComputerTurn = false;
                        System.out.println("Computer has drawn from pile");
                        break;
                    }
                } else {
                        isComputerTurn = false;
                        for (Card card : computerCards)
                        {
                            if(card.getFace() == previousCard.getFace() ||
                                    card.getSuit().equals(previousCard.getSuit()))
                            {
                                previousCard = card;
                                game.rule(previousCard);
                                game.play(previousCard);
                                System.out.println("Computer has played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                break;
                            }
                        }
                }
                if(previousCard.isDefendCard())
                {
                    boolean isComputerDrawingFromPile = false;
                    for(Card card:computerCards)
                    {
                        if(card.getFace() == previousCard.getFace() ||
                                card.getSuit().equals(previousCard.getSuit()) || card.isWhot())
                        {
                            previousCard = card;
                            System.out.println("Computer has played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            game.rule(previousCard);
                            game.play(previousCard);
                            game.checkWinner();
                            isComputerDrawingFromPile = true;
                            break;
                        }
                    }
                    if (!isComputerDrawingFromPile) {
                        game.computerDraw();
                        isComputerTurn = false;
                        System.out.println("Computer has drawn from pile");
                        break;
                    }
                    previousCard.setDefendCard(false);
                }
            }
        } catch(InputMismatchException e){
            System.out.println("Select a valid card number");
        } catch(NumberFormatException e){
            System.out.println("Select a valid card number");
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println("The selection is not in your card");
        }
            isComputerTurn = false;
        counter = 1;
    }

    public void playerPlay() {
        try {
            System.out.println("Hit 'Enter' to see all your cards");
            String yes = input.nextLine();
            for (index = 0; index < playerCards.size(); index++) {
                System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
            }
            System.out.println("-1. to draw from the pile");
            System.out.print("Select a card to play >> ");
            int userInput = input.nextInt();
            if (userInput == -1) {
                game.playerDraw();
                isComputerTurn = true;
                System.out.println("You have drawn from pile");
            } else {
                boolean validPlay = false;
                Card playedCard = playerCards.get(userInput - 1);
                if (playedCard.getFace() == previousCard.getFace() ||
                        playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot()) {
                    previousCard = playedCard;
                    System.out.println("You played:");
                    System.out.printf("*****%s*****%n", previousCard.toString());
                    game.rule(previousCard);
                    game.play(previousCard);
                    game.checkWinner();
                    isComputerTurn = true;
                } else {
                    while (!validPlay) {
                        System.out.println("You selected and invalid card, draw from pile or play a fitting card");
                        System.out.print("Hit 'Enter' to see all your cards");
                        yes = input.nextLine();
                        for (index = 0; index < playerCards.size(); index++) {
                            System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                        }
                        System.out.println("-1. to draw from the pile");
                        System.out.print("Select a card to play >> ");
                        userInput = input.nextInt();
                        if (userInput == -1) {
                            game.playerDraw();
                            isComputerTurn = true;
                            validPlay = true;
                        } else {
                            playedCard = playerCards.get(userInput - 1);
                            if (playedCard.getFace() == previousCard.getFace() ||
                                    playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot()) {
                                previousCard = playedCard;
                                System.out.println("You played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(previousCard);
                                game.play(previousCard);
                                game.checkWinner();
                                validPlay = true;
                            }
                        }
                        if (!validPlay) {
                            System.out.println("Computer played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            System.out.println("Select a valid card to play or draw from pile");
                        }
                    }
                }
                if (previousCard.isSuspension()) {

                    while (!validPlay) {
                        System.out.print("Hit 'Enter' to see all your cards");
                        yes = input.nextLine();
                        for (index = 0; index < playerCards.size(); index++) {
                            System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                        }
                        System.out.println("-1. to draw from the pile");
                        System.out.print("Select a card to play >> ");
                        userInput = input.nextInt();
                        if (userInput == -1) {
                            game.playerDraw();
                            isComputerTurn = true;
                            validPlay = true;
                        } else {
                            playedCard = playerCards.get(userInput - 1);
                            if (playedCard.getFace() == previousCard.getFace() ||
                                    playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot()) {
                                previousCard = playedCard;
                                System.out.println("You played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(previousCard);
                                game.play(previousCard);
                                game.checkWinner();
                                validPlay = true;
                            }
                        }
                        if (!validPlay) {
                            System.out.println("Computer played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            System.out.println("Select a valid card to play or draw from pile");
                        }
                    }

                } else if (previousCard.isHoldOn()) {
                    while (!validPlay) {
                        System.out.print("Hit 'Enter' to see all your cards");
                        yes = input.nextLine();
                        for (index = 0; index < playerCards.size(); index++) {
                            System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                        }
                        System.out.println("-1. to draw from the pile");
                        System.out.print("Select a card to play >> ");
                        userInput = input.nextInt();
                        if (userInput == -1) {
                            game.playerDraw();
                            isComputerTurn = true;
                            validPlay = true;
                        } else {
                            playedCard = playerCards.get(userInput - 1);
                            if (playedCard.getFace() == previousCard.getFace() ||
                                    playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot()) {
                                previousCard = playedCard;
                                System.out.println("You played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(previousCard);
                                game.play(previousCard);
                                game.checkWinner();
                                validPlay = true;
                            }
                        }
                        if (!validPlay) {
                            System.out.println("Computer played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            System.out.println("Select a valid card to play or draw from pile");
                        }
                    }

                } else if (previousCard.isPickTwo()) {
                    if (computerCards.size() >= 10) {
                        for (Card card : computerCards) {
                            if (card.isPickTwo()) {
                                Card computerDefendCard = card;
                                previousCard = computerDefendCard;
                                System.out.println("Computer has defended the PICK TWO with:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.play(computerDefendCard);
                                game.rule(computerDefendCard);
                                game.checkWinner();
                                break;
                            }
                        }
                    } else {
                        game.computerDraw();
                        game.computerDraw();
                        System.out.println("computer has picked two cards");
                    }

                } else if (previousCard.isPickThree()) {
                    if (computerCards.size() >= 9) {
                        for (Card card : computerCards) {
                            if (card.isPickThree()) {
                                Card computerDefendCard = card;
                                previousCard = computerDefendCard;
                                System.out.println("computer defended the PICK THREE with:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(computerDefendCard);
                                game.play(computerDefendCard);
                                game.checkWinner();
                                break;
                            }
                        }
                    } else {
                        game.computerDraw();
                        game.computerDraw();
                        game.computerDraw();
                        System.out.println("computer has picked three cards");
                    }
                } else if (previousCard.isGeneralMarket()) {
                    game.computerDraw();
                    game.play(previousCard);
                    game.checkWinner();
                    System.out.println("computer has gone to market");
                } else if (previousCard.isNormalCard()) {
                    game.play(previousCard);
                    game.checkWinner();
                    isComputerTurn = true;
                }

                if (previousCard.isWhot()) {
                    System.out.printf("1. %s%n2. %s%n3. %s%n4. %s%n5. %s%n",
                            Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE);
                    int playerInput = input.nextInt();
                    Suit wantedSuit = null;
                    switch (playerInput) {
                        case 1:
                            wantedSuit = Suit.CIRCLE;
                            break;
                        case 2:
                            wantedSuit = Suit.CROSS;
                            break;
                        case 3:
                            wantedSuit = Suit.TRIANGLE;
                            break;
                        case 4:
                            wantedSuit = Suit.STAR;
                            break;
                        case 5:
                            wantedSuit = Suit.SQUARE;
                            break;
                        default:
                            System.out.println("You made an invalid selection");
                    }
                    boolean isComputerDrawingFromPile = true;
                    for (Card card : computerCards) {
                        if (card.getSuit().equals(wantedSuit)) {
                            previousCard = card;
                            game.rule(previousCard);
                            game.play(card);
                            game.checkWinner();
                            System.out.println("Computer played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            isComputerDrawingFromPile = false;
                            break;
                        }
                    }
                    if (!isComputerDrawingFromPile) {
                        game.computerDraw();
                        isComputerTurn = false;
                        System.out.println("Computer has drawn from pile");
                    }

                }
            }
            } catch(InputMismatchException e){
                System.out.println("Select a valid card number");
            } catch(NumberFormatException e){
                System.out.println("Select a valid card number");
            }catch (IndexOutOfBoundsException e)
        {
            System.out.println("The selection is not in your card");
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
