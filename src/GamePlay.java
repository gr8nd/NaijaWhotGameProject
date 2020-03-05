import java.util.ArrayList;
import java.util.InputMismatchException;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 * This class has the highest number of lines and also the most difficult to implement. It contains the whole logic of
 * the Whot game. Computer and human take turn to play, after playing computer relinquishes control to the human and like
 * wise human. This turn continues until the drawPile runs out or either the computerDrawPile or playerDrawPile run out
 * thereafter a winner will be decided.
 */
public class GamePlay {
    private boolean isComputerTurn = true;//Used for relinquishing control between computer and human, initially it is
    //computer turn
    private WhotGame game;
    private boolean validDeal = true;//Used in checking if a valid deal number is provided.
    private ArrayList<Card> playerCards;
    private ArrayList<Card> computerCards;
    private int index;
    private Card previousCard;//Initially the previousCard is the startCard in the WhotGame game class. Thereafter it takes
    //the most recent card played during the game.
    private SecureRandom rand = new SecureRandom();
    private Scanner input = new Scanner(System.in);
    private static int counter = 0;
    /**
     * The GamePlay has two overloaded constructors. This constructor without a parameter is used when no deal number
     * is provided, it therefore uses the default deal number of 6.
     */
    public GamePlay()
    {
        game = new WhotGame();
        playerCards = game.getPlayerCardPile();
        computerCards = game.getComputerCardPile();
        deal(6);
    }
    /**
     * @param number a deal number
     */
    public GamePlay(int number)
    {
        game = new WhotGame();
        playerCards = game.getPlayerCardPile();
        computerCards = game.getComputerCardPile();
        deal(number);
    }

    /**
     * The start method checks whether a valid deal was done and thereafter starts the game, the game runs until
     * there is a winner, rotating turn between computer and human.
     */
    public void start()
    {
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

    /**
     * computerPlay method handles the computer card play simulation, after finish playing computer should hand over
     * turn to the human.
     */
    public void computerPlay() {
        if(counter == 0)
        {
            System.out.println("Computer has played:");
            System.out.printf("*****%s*****%n", previousCard.toString());
            game.rule(previousCard);
        }
        if((previousCard.isNormalCard() && counter != 0) || previousCard.isPlayerWhot())
        {
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
            }
        }

        try {
            //Any of the following nested if-statements execute according to the nature of the previous card
            while (!previousCard.isNormalCard()) {
                //This statement executes when the previous card is HOLDON
                if (previousCard.isHoldOn()) {
                    for (Card card : computerCards) {
                        if (card.isHoldOn() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                            previousCard = card;
                            System.out.println("Computer has played:");
                            System.out.printf("*****%s*****%n", previousCard.toString());
                            game.rule(previousCard);
                            game.play(previousCard);
                            game.checkWinner();
                            break;
                        }
                    }
                    //This statement executes when the previous card is PICKTWO
                } else if (previousCard.isPickTwo() && !previousCard.isDefendCard()) {
                    System.out.print("Hit 'Enter' to see all cards");
                    String yes = input.nextLine();
                    playerSelectCard("-1. to pick two from the pile or a card to defend");
                    int userInput = input.nextInt();
                    outer:while (true) {
                        if (userInput == -1) {
                            game.playerDraw();
                            game.playerDraw();
                            System.out.println("You have picked two cards");
                            boolean isComputerDrawingFromPile = true;
                            for (Card card : computerCards) {
                                if (card.getFace() == previousCard.getFace() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                                    isComputerDrawingFromPile = false;
                                    previousCard = card;
                                    System.out.println("Computer has played:");
                                    System.out.printf("*****%s*****%n", previousCard.toString());
                                    game.rule(previousCard);
                                    game.play(previousCard);
                                    game.checkWinner();
                                    break outer;
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
                                previousCard = defendCard;
                                System.out.println("You defended the PICK TWO with: ");
                                previousCard.setDefendCard(true);
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(previousCard);
                                game.play(defendCard);
                                game.checkWinner();
                                break;
                            } else {
                                System.out.println("The card is not fit for defend, if you don't have a valid card pick from the draw pile");
                                playerSelectCard("-1. to pick two from the pile or a card to defend");
                                userInput = input.nextInt();
                            }
                        }
                    }
                    //This statement executes when the previous card is PICKTHREE
                } else if (previousCard.isPickThree() && !previousCard.isDefendCard()) {
                    System.out.print("Hit 'Enter' to see all cards");
                    String yes = input.nextLine();
                    playerSelectCard("-1. to pick three from the pile or a card to defend");
                    int userInput = input.nextInt();
                    outer:while (true) {
                        if (userInput == -1) {
                            game.playerDraw();
                            game.playerDraw();
                            game.playerDraw();
                            System.out.println("You have picked three cards");
                            boolean isComputerDrawingFromPile = true;
                            for (Card card : computerCards) {
                                if (card.isPickThree() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
                                    isComputerDrawingFromPile = false;
                                    previousCard = card;
                                    System.out.println("Computer has played:");
                                    System.out.printf("*****%s*****%n", previousCard.toString());
                                    game.rule(previousCard);
                                    game.play(previousCard);
                                    game.checkWinner();
                                    break outer;
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
                                previousCard = defendCard;
                                System.out.println("You defended the PICK THREE with: ");
                                previousCard.setDefendCard(true);
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(defendCard);
                                game.play(defendCard);
                                game.checkWinner();
                                break;
                            } else {
                                System.out.println("The card is not fit for defend, if you don't have a valid card pick from the draw pile");
                                playerSelectCard("-1. to pick three from the pile or a card to defend");
                                userInput = input.nextInt();
                            }

                        }
                    }
                    //This statement executes when the previous card is a GENERAL MARKET
                } else if (previousCard.isGeneralMarket()) {
                    for (index = 0; index < playerCards.size(); index++) {
                        System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
                    }
                    System.out.println("Hit 'Enter' to go to market");
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

                    //The statement will execute when the previous card is Whot and the Whot was played by
                    //the computer and not human
                } else if (previousCard.isWhot() || !previousCard.isPlayerWhot()) {
                    int randomIndex = rand.nextInt(computerCards.size());
                    Card wantedCard = computerCards.get(randomIndex);
                    while (!wantedCard.isWhot() && !wantedCard.getSuit().equals(previousCard.getSuit()))
                    {
                        randomIndex = rand.nextInt(computerCards.size());
                        wantedCard = computerCards.get(randomIndex);
                    }
                    System.out.printf("Computer needs *****%s*****%n", wantedCard.getSuit());
                    game.rule(wantedCard);
                    game.play(wantedCard);
                    game.checkWinner();
                    System.out.print("Hit 'Enter' to see all your cards");
                    String yes = input.nextLine();
                    playerSelectCard("-1. to draw from the pile");
                    int userInput = input.nextInt();
                    if (userInput == -1) {
                        boolean isComputerDrawingFromPile = true;
                        System.out.println("You have drawn from pile");
                        game.playerDraw();
                        for (Card card: computerCards)
                        {
                            if(card.getSuit().equals(wantedCard.getSuit()) || card.isWhot())
                            {
                                previousCard = card;
                                isComputerDrawingFromPile = false;
                                System.out.println("Computer has played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(card);
                                game.play(card);
                                game.checkWinner();
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
                        Card playedCard = playerCards.get(userInput - 1);
                        while (true) {
                            if (playedCard.getSuit().equals(wantedCard.getSuit()) || playedCard.isWhot()) {
                                previousCard = playedCard;
                                System.out.println("You played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(playedCard);
                                game.play(playedCard);
                                game.checkWinner();
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
                                playerSelectCard("-1. to draw from the pile");
                                userInput = input.nextInt();
                                if (userInput == -1) {
                                    System.out.printf("You have drawn from pile");
                                    game.playerDraw();
                                    break;
                                }
                            }
                        }
                    }
                    //This statement executes when the previous card is suspension card, that is a card with face
                    //value of 8
                } else if (previousCard.isSuspension()) {
                    boolean isComputerDrawingFromPile = false;
                    for (Card card : computerCards) {
                        if (card.isSuspension() || card.getSuit().equals(previousCard.getSuit()) || card.isWhot()) {
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
                    //This else statement executes when the previous card is normal card
                } else {
                        isComputerTurn = false;
                        for (Card card : computerCards)
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
                                break;
                            }
                        }
                }
                //This statement executes when the previous card is a defend card coming from the human player
                //the only two cards that can be defended are PICKTWO and PICKTHREE
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
    /**
     * playerPlay method handles the player card play simulation, after finish playing this method should hand over
     * turn to the computer.
     */
    public void playerPlay() {
        while (!isComputerTurn) {
            try {
                System.out.println("Hit 'Enter' to see all your cards");
                String yes = input.nextLine();
                playerSelectCard("-1. to draw from the pile");
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
                            System.out.println("You selected an invalid card, draw from pile or play a fitting card");
                            System.out.print("Hit 'Enter' to see all your cards");
                            yes = input.nextLine();
                            playerSelectCard("-1. to draw from the pile");
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
                    //This statement executes when the previous card is SUSPENSION
                    if (previousCard.isSuspension()) {

                        while (!validPlay) {
                            System.out.print("Hit 'Enter' to see all your cards");
                            yes = input.nextLine();
                            playerSelectCard("-1. to draw from the pile");
                            userInput = input.nextInt();
                            if (userInput == -1) {
                                game.playerDraw();
                                isComputerTurn = true;
                                validPlay = true;
                            } else {
                                playedCard = playerCards.get(userInput - 1);
                                if (playedCard.isSuspension() ||
                                        playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot()) {
                                    previousCard = playedCard;
                                    System.out.println("You played:");
                                    System.out.printf("*****%s*****%n", previousCard.toString());
                                    game.rule(previousCard);
                                    game.play(previousCard);
                                    game.checkWinner();
                                    isComputerTurn = false;
                                    validPlay = true;
                                }
                            }
                            if (!validPlay) {
                                System.out.println("Computer played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                System.out.println("Select a valid card to play or draw from pile");
                            }
                        }

                        //This statement executes when the previous card is HOLDON
                    } else if (previousCard.isHoldOn()) {
                        while (!validPlay) {
                            System.out.print("Hit 'Enter' to see all your cards");
                            yes = input.nextLine();
                            playerSelectCard("-1. to draw from the pile");
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
                                    isComputerTurn = false;
                                    validPlay = true;
                                }
                            }
                            if (!validPlay) {
                                System.out.println("Computer played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                System.out.println("Select a valid card to play or draw from pile");
                            }
                        }

                        //This statement executes when the previous card is PICKTWO
                    } else if (previousCard.isPickTwo()) {
                        while (previousCard.isPickTwo()) {
                            if (computerCards.size() >= 10) {
                                for (Card card : computerCards) {
                                    if (card.isPickTwo()) {
                                        Card computerDefendCard = card;
                                        previousCard = computerDefendCard;
                                        System.out.println("Computer has defended the PICK TWO with:");
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
                                System.out.println("computer has picked two cards");
                            }
                        }

                        //This statement executes when the previous card is PICKTHREE
                    } else if (previousCard.isPickThree()) {
                        while (previousCard.isPickThree()) {
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
                        }
                        //This statement executes when the previous card is GENERAL MARKET
                    } else if (previousCard.isGeneralMarket()) {
                        game.computerDraw();
                        game.play(previousCard);
                        game.checkWinner();
                        System.out.println("computer has gone to market");
                        isComputerTurn = false;
                    } else if (previousCard.isNormalCard()) {
                        game.rule(previousCard);
                        game.play(previousCard);
                        game.checkWinner();
                        isComputerTurn = true;
                    }

                    //This statement executes when the previous card is Whot
                    if (previousCard.isWhot()) {
                        previousCard.setPlayerWhot(true);
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
                        System.out.printf("You need %s%n", wantedSuit);
                        boolean isComputerDrawingFromPile = true;
                        for (Card card : computerCards) {
                            if (card.getSuit().equals(wantedSuit) || card.isWhot()) {
                                previousCard = card;
                                System.out.println("Computer played:");
                                System.out.printf("*****%s*****%n", previousCard.toString());
                                game.rule(previousCard);
                                game.play(card);
                                game.checkWinner();
                                isComputerDrawingFromPile = false;
                                break;
                            }
                        }
                        if (isComputerDrawingFromPile) {
                            game.computerDraw();
                            System.out.println("Computer has drawn from pile");
                            isComputerTurn = false;
                        }
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Select a valid card number");
            } catch (NumberFormatException e) {
                System.out.println("Select a valid card number");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("The selection is not in your card");
            }
        }
        }

    /**
     * previousCard is initially the startCard in the WhotGame
     * @param number a deal number that is provided, if no valid deal number was provided a WhotGameException is thrown
     * and appropriate message is issued to the user.
     */
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
    public void computerSelectCard(String message)
    {

    }
    public void playerSelectCard(String message)
    {
        for (index = 0; index < playerCards.size(); index++) {
            System.out.println(index + 1 + ".  " + playerCards.get(index).toString());
        }
        System.out.println(message);
        System.out.print("Select a card to play >> ");
    }
}
