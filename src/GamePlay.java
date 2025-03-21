import java.util.ArrayList;
import java.util.InputMismatchException;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 * This class has the highest number of lines and also the most difficult to implement. It contains the whole logic of
 * the Whot game. Computer and human take turn to play, after playing computer relinquishes control to the human and like
 * wise human. This turn continues until the drawPile runs out or either the computerDrawPile or playerDrawPile runs out
 * thereafter a winner will be decided.
 */
public class GamePlay {
    private boolean isComputerTurn = true;//Used for relinquishing control between computer and human, initially it is
    //computer turn
    private final WhotGame game;
    private boolean validDeal = true;//Used in checking if a valid deal number is provided.
    private final ArrayList<Card> playerCards;//A list containing all player's cards
    private final ArrayList<Card> computerCards;//A list containing all computer's cards
    private Card previousCard;//Initially the previousCard is the startCard in the WhotGame game class. Thereafter, it takes
    //the most recent card played during the game. Each player's play must be valid if the card played has the same
    // Suit or the face(number) as the previousCard. The Whot card can be played at any time except during HOLDON,
    //SUSPENSION, PICKTWO, PICKTHREE or GENERAL MARKET.
    private final SecureRandom rand = new SecureRandom();
    private final Scanner input = new Scanner(System.in);
    private boolean forceWinner; //If true, it will ensure that there is a winner in the game, draw will not be allowed
    //so the game will run indefinitely until there is a winner.

    /**
     * The GamePlay has two overloaded constructors. This constructor without
     * a parameter is used when no deal number
     * is provided, it therefore uses the default deal number of 6.
     */
    public GamePlay(boolean forceWinner)
    {
        this.forceWinner = forceWinner;
        game = new WhotGame();
        playerCards = game.getHumanCardPile();
        computerCards = game.getComputerCardPile();
        this.deal(6);
    }

    /**
     * @param number a deal number
     */
    public GamePlay(int number, boolean forceWinner)
    {
        this.forceWinner = forceWinner;
        game = new WhotGame();
        playerCards = game.getHumanCardPile();
        computerCards = game.getComputerCardPile();
        this.deal(number);
    }

    /**
     * previousCard is initially the startCard in the WhotGame
     *
     * @param number a deal number that is provided, if no valid deal number is provided a
     *               WhotGameException is thrown
     *               and appropriate message is issued to the user.
     */
    public void deal(int number)
    {
        try
        {
            game.deal(number);
            previousCard = game.getStartCard();
        } catch (WhotGameException e)
        {
            validDeal = false;
            System.out.println(e.getMessage());
        }
    }

    /**
     * The start method checks whether a valid deal was done and thereafter starts the game,
     * the game runs until
     * there is a winner, rotating turn between computer and human.
     */
    public void start()
    {
        if (validDeal)
        {
            while (!game.isHumanIsTheWinner() &&
                    !game.isComputerIsTheWinner() &&
                    !game.isTie())
            {
                if (isComputerTurn)
                {
                    computerPlay();
                } else
                {
                    humanPlay();
                }
            }
        }
    }

    /**
     * computerPlay method handles the computer card play simulation, after finish playing computer
     * should relinquish
     * turn to the human.
     */
    public void computerPlay()
    {
        if ((previousCard.isNormalCard()))
        {
            if (!computerGetCardToPlay())
            {
                isComputerTurn = false;
            }
        } else if (previousCard.isPickThree())
        {
            //TODO
        } else if (previousCard.isPickThree())
        {
            //TODO
        } else if (previousCard.isHoldOn() | previousCard.isSuspension())
        {
            //TODO
        } else if (previousCard.isGeneralMarket())
        {
            //TODO
        }

        try {
            while (!previousCard.isNormalCard())
            {

            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Select a valid card number");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("The selection is not in your card");
        }
    }

    /**
     * playerPlay method handles the player card play simulation, after
     * finish playing this method should hand over
     * turn to the computer.
     */
    public void humanPlay()
    {
        if ((previousCard.isNormalCard()))
        {
            if (!computerGetCardToPlay())
            {
                isComputerTurn = false;
            }
        } else if (previousCard.isPickThree())
        {
            //TODO
        } else if (previousCard.isPickThree())
        {
            //TODO
        } else if (previousCard.isHoldOn() | previousCard.isSuspension())
        {
            //TODO
        } else if (previousCard.isGeneralMarket())
        {
            //TODO
        }
        while (!isComputerTurn)
        {
            try
            {
                int index = humanSelectCardToPlay();
                int userInput = input.nextInt();
                if (userInput == -1)
                {
                    game.humanDrawFromPile(forceWinner);
                    isComputerTurn = true;
                    System.out.println("You have drawn from pile");
                }
            } catch (InputMismatchException | NumberFormatException e)
            {
                System.out.println("Select a valid card number");
            } catch (IndexOutOfBoundsException e)
            {
                System.out.println("The selection is not in your card");
            }
        }
    }

    public boolean computerGetCardToPlay()
    {
        boolean isComputerDrawingFromPile = false;
        for (Card card : computerCards)
        {
            if (card.getFace() == previousCard.getFace() ||
                    card.getSuit().equals(previousCard.getSuit()) ||
                    card.isWhot())
            {
                previousCard = card;
                System.out.println("Computer has played:");
                System.out.println(previousCard);
                game.play(previousCard, forceWinner);
                isComputerDrawingFromPile = true;
                break;
            }
        }
        return isComputerDrawingFromPile;
    }

    private void humanWantsCard(int want)
    {
        previousCard.setPlayerWhot(true);
        System.out.printf("1. %s%n2. %s%n3. %s%n4. %s%n5. %s%n",
                Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE);
        int playerInput = input.nextInt();
        Suit wantedSuit = null;
        switch (want) {
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
    }

    public int humanSelectCardToPlay()
    {
        System.out.print("Hit 'Enter' to see all your cards: >> ");
        String yes = input.nextLine();
        for (int i = 0; i < playerCards.size(); i++)
        {
            System.out.println(">> " + (i + 1) + " ");
            System.out.println(playerCards.get(i).toString());
        }
        System.out.println("Select the card to play or -1 to drawn from pile:");
        return input.nextInt();
    }

    private boolean computerPickTwo()
    {
        boolean twoPicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickTwo() && computerCards.size() > 10)
            {
                game.play(card, forceWinner);
                twoPicked = false;
                System.out.println("Computer has played:");
                System.out.println(card);
            }
        }
        if (twoPicked)
        {
            game.computerDrawFromPile(forceWinner);
            game.computerDrawFromPile(forceWinner);
        }
        return twoPicked;
    }

    private boolean computerPickThree()
    {
        boolean threePicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickThree() && computerCards.size() > 10) {
                game.play(card, forceWinner);
                threePicked = false;
                System.out.println("Computer has played:");
                System.out.println(card);
            }
        }
        if (threePicked)
        {
            game.computerDrawFromPile(forceWinner);
            game.computerDrawFromPile(forceWinner);
            game.computerDrawFromPile(forceWinner);
        }
        return threePicked;
    }

    private void computerWhot(Card whot)
    {
        Card whotCard = null;
        boolean playedWhot = false;
        ArrayList<Card> nonWhotCards = new ArrayList<>();
        for (Card card : computerCards)
        {
            if (card.isWhot())
            {
                if (computerCards.size() > 10)
                {
                    whotCard = card;
                    playedWhot = true;
                }
            } else
            {
                nonWhotCards.add(card);
            }
        }
        if (playedWhot)
        {
            game.play(whotCard, forceWinner);
            System.out.println("Computer has played:");
            System.out.println(whotCard);
        }
        int randomIndex = rand.nextInt(nonWhotCards.size());
    }

    private void humanPickTwo(int requestCode)
    {
        if(requestCode == -1)
        {
            game.humanDrawFromPile(forceWinner);
            game.humanDrawFromPile(forceWinner);
            System.out.println("You have picked two.");
            isComputerTurn = true;
        }else if(requestCode == 1)
        {
            Card pickTwoCard = playerCards.get(requestCode);
            game.play(pickTwoCard, forceWinner);
            isComputerTurn = true;
            pickTwoCard.setDefendCard(true);
            System.out.println("You have defended the PICKTWO.");
        }else
        {
            System.out.println("You selected an invalid card, draw two cards " +
                    "from pile or play a fitting card to defend.");
        }
    }

    private void humanPickThree(int requestCode)
    {
        if(requestCode == -1)
        {
            game.humanDrawFromPile(forceWinner);
            game.humanDrawFromPile(forceWinner);
            game.humanDrawFromPile(forceWinner);
            System.out.println("You have picked three.");
            isComputerTurn = true;
        }else if(requestCode == 1)
        {
            Card pickTwoCard = playerCards.get(requestCode);
            game.play(pickTwoCard, forceWinner);
            isComputerTurn = true;
            pickTwoCard.setDefendCard(true);
            System.out.println("You have defended the PICKTHREE.");
        }else
        {
            System.out.println("You selected an invalid card, draw three cards " +
                    "from pile or play a fitting card to defend.");
        }
    }

    private void humanGoMarket()
    {
        game.humanDrawFromPile(forceWinner);
        System.out.println("You have gone to market.");
        isComputerTurn = true;
    }

    private void computerHoldOnAndSuspension()
    {
        isComputerTurn = false;
    }

    private void humanHoldOnAndSuspension()
    {
        isComputerTurn = true;
    }

    public boolean isForceWinner()
    {
        return forceWinner;
    }

    public void setForceWinner(boolean forceWinner)
    {
        this.forceWinner = forceWinner;
    }
}
