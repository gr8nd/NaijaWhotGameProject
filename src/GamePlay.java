import java.util.ArrayList;
import java.security.SecureRandom;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class has the highest number of lines and also the most difficult to implement. It contains the whole logic of
 * the Whot game. Computer and human take turn to play, after playing computer relinquishes control to the human and like
 * wise human. This turn continues until the drawPile runs out or either the computerDrawPile or playerDrawPile runs out
 * thereafter a winner will be decided.
 */
public class GamePlay
{
    private boolean isComputerTurn = true;//Used for relinquishing control between computer and human, initially it is
    //computer turn
    private final WhotGame game;
    private Suit wantedSuit;
    private boolean validDeal = true;//Used in checking if a valid deal number is provided.
    private final ArrayList<Card> humanCards;//A list containing all player's cards
    private final ArrayList<Card> computerCards;//A list containing all computer's cards
    private Card previousCard;//Initially the previousCard is the startCard in the WhotGame game class. Thereafter, it takes
    //the most recent card played during the game. Each player's play must be valid if the card played has the same
    // Suit or the face(number) as the previousCard. The Whot card can be played at any time except during HOLDON,
    //SUSPENSION, PICKTWO, PICKTHREE or GENERAL MARKET.
    private final SecureRandom rand = new SecureRandom();
    private final Scanner input = new Scanner(System.in);
    private final boolean forceWinner; //If true, it will ensure that there is a winner in the game, draw will not be allowed
    //so the game will run indefinitely until there is a winner.

    /**
     * The GamePlay has two overloaded constructors. This constructor without
     * a parameter is used when no deal number
     * is provided, it therefore uses the default deal number of 6.
     */
    public GamePlay(boolean forceWinner, boolean isComputerTurn)
    {
        this.forceWinner = forceWinner;
        this.isComputerTurn = isComputerTurn;
        game = new WhotGame();
        humanCards = game.getHumanCardPile();
        computerCards = game.getComputerCardPile();
        this.deal(6);
    }

    /**
     * @param number a deal number
     */
    public GamePlay(int number, boolean forceWinner, boolean isComputerTurn)
    {
        this.forceWinner = forceWinner;
        this.isComputerTurn = isComputerTurn;
        game = new WhotGame();
        humanCards = game.getHumanCardPile();
        computerCards = game.getComputerCardPile();
        this.deal(number);
    }

    /**
     * previousCard is initially the startCard in the WhotGame
     *
     * @param number a deal number that is provided, if no valid deal number is provided a
     *  WhotGameException is thrown and appropriate message is issued to the user.
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

    public void start()
    {
        if (validDeal)
        {
            startCardPlay();
        }
    }

    private void startCardPlay()
    {
        System.out.println("The start card is:");
        System.out.println(previousCard.toString());
        game.play(previousCard, forceWinner);

        startGame();

    }

    /**
     * The start method checks whether a valid deal was done and thereafter starts the game,
     * the game runs until
     * there is a winner, rotating turn between computer and human.
     */
    private void startGame()
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

    /**
     * computerPlay method handles the computer card play simulation, after finish playing computer
     * should relinquish
     * turn to the human.
     */
    private void computerPlay()
    {
        if ((previousCard.isNormalCard()))
        {
            computerNormalPlay();
        } else if (previousCard.isPickTwo())
        {
            computerPickTwo();
        } else if (previousCard.isPickThree())
        {
            computerPickThree();
        } else if (previousCard.isHoldOn())
        {
            computerHoldOn();
        }else if(previousCard.isSuspension())
        {
            computerSuspension();
        } else if (previousCard.isGeneralMarket())
        {
            computerGoMarket();
        }else if(previousCard.isWhot())
        {
            computerPlaysWhot();
            isComputerTurn = false;
        }
    }

    /**
     * playerPlay method handles the player card play simulation, after
     * finish playing this method should hand over
     * turn to the computer.
     */
    private void humanPlay()
    {
        int index = humanSelectCard();
        if(index != -2)
        {
            if ((previousCard.isNormalCard()))
            {
                if(humanNormalPlay(index))
                {
                    isComputerTurn = true;
                }
            } else if (previousCard.isPickTwo())
            {
                humanPickTwo(index);
            } else if (previousCard.isPickThree())
            {
                humanPickThree(index);
            } else if (previousCard.isGeneralMarket())
            {
                humanGoMarket(index);
            } else if(previousCard.isHoldOn())
            {
               humanNormalPlay(index);
            }else if(previousCard.isSuspension())
            {
                humanNormalPlay(index);
            } else if(previousCard.isWhot())
            {
                humanPlaysWhot(index);
            }
        }
    }

    private void humanPlaysWhot(int index)
    {
        if(index == -1)
        {
            game.humanDrawFromPile(forceWinner);
            System.out.println("You have drawn from pile.");
            isComputerTurn = true;
        }else
        {
            Card card = humanCards.get(index-1);
            if(card.isWhot())
            {
                game.play(card, forceWinner);
                previousCard = card;
                humanRequestsCard();
            }else
            {
                System.out.println("Play a Whot card or pick from pile.");
            }
        }
    }

    private void computerNormalPlay()
    {
        boolean computerDrawingFromPile = true;
        for (Card card : computerCards)
        {
            if (card.getFace() == previousCard.getFace() ||
                    card.getSuit().equals(previousCard.getSuit()))
            {
                game.play(previousCard, forceWinner);
                previousCard = card;
                System.out.println("Computer has played:");
                System.out.println(previousCard.toString());
                computerDrawingFromPile = false;
                break;
            }
        }
        if(computerDrawingFromPile)
        {
            game.computerDrawFromPile(forceWinner);
            System.out.println("Computer has drawn from pile.");
        }
        isComputerTurn = false;
    }

    private void humanRequestsCard()
    {
        System.out.printf("1. %s%n2. %s%n3. %s%n4. %s%n5. %s%n",
                Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE);
        int want = input.nextInt();
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
                System.out.println("You made an invalid selection, please select a card you need.");
                return;
        }
        System.out.println("You need ***" + wantedSuit + "***");
    }

    private int humanSelectCard()
    {
        System.out.println("Hit 'Enter' to see all your cards: >> ");
        input.nextLine();
        for (int i = 0; i < humanCards.size(); i++)
        {
            System.out.println(">> " + (i + 1) + " ");
            System.out.println(humanCards.get(i).toString());
        }
        System.out.println("Select the card to play or -1 to drawn from pile:");
        String userInput = input.nextLine();
        return convertUserInput(userInput);
    }

    private  int convertUserInput(String userInput)
    {
        try
        {
            return Integer.parseInt(userInput);
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println("The selection is not in your card.");
        }catch (InputMismatchException | NumberFormatException e)
        {
            System.out.println("Select a valid card number.");
        }
        return -2;
    }

    private boolean humanNormalPlay(int index)
    {
        if(index == -1)
        {
            game.humanDrawFromPile(forceWinner);
            System.out.println("You have drawn from pile.");
            isComputerTurn = true;
            return true;
        }else
        {
            try
            {
                Card card = humanCards.get(index);
                if(card.getSuit() == previousCard.getSuit() ||
                        card.getFace() == previousCard.getFace())
                {
                    game.play(card, forceWinner);
                    previousCard = card;
                    System.out.println("You played: ");
                    System.out.println(previousCard.toString());
                    return true;
                }else
                {
                    System.out.println("Select a valid card number.");
                }
            } catch (IndexOutOfBoundsException e)
            {
                System.out.println("The selection is not in your card.");
            }catch (InputMismatchException | NumberFormatException e)
            {
                System.out.println("Select a valid card number.");
            }
        }
        return false;
    }

    private void computerPickTwo()
    {
        if(previousCard.isCardActionTaken())
        {
            computerNormalPlay();
            return;
        }
        boolean twoPicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickTwo() &&
                    computerCards.size() > 10 &&
                    !previousCard.isCardActionTaken())
            {
                game.play(card, forceWinner);
                previousCard.setCardActionTaken(true);
                previousCard = card;
                twoPicked = false;
                System.out.println("Computer has defended the pick two with.");
                System.out.println(previousCard.toString());
                break;
            }
        }
        if (twoPicked &&
                !previousCard.isDefendCard() &&
                !previousCard.isCardActionTaken())
        {
            game.computerDrawFromPile(forceWinner);
            game.computerDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("Computer has picked pick two.");
        }
        isComputerTurn = false;
    }

    private void computerPickThree()
    {
        if(previousCard.isCardActionTaken())
        {
            computerNormalPlay();
            return;
        }
        boolean threePicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickThree() &&
                    computerCards.size() > 10 &&
                    !previousCard.isCardActionTaken()) {
                game.play(card, forceWinner);
                previousCard.setCardActionTaken(true);
                previousCard = card;
                threePicked = false;
                System.out.println("Computer has defended the pick three with:");
                System.out.println(previousCard.toString());
                break;
            }
        }
        if (threePicked &&
                !previousCard.isDefendCard() &&
                !previousCard.isCardActionTaken())
        {
            game.computerDrawFromPile(forceWinner);
            game.computerDrawFromPile(forceWinner);
            game.computerDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("Computer has picked pick three.");
        }
        isComputerTurn = false;
    }

    private void computerRequestsWhot()
    {
        if(previousCard.isCardActionTaken())
        {
            computerNormalPlay();
            return;
        }
        ArrayList<Card> nonWhotCards = new ArrayList<>();
        for (Card card : computerCards)
        {
            if (!card.isWhot())
            {
                nonWhotCards.add(card);
            }
        }
        int randomIndex = rand.nextInt(nonWhotCards.size());
        Card neededCard = nonWhotCards.get(randomIndex);
        wantedSuit = neededCard.getSuit();
        System.out.println("Computer needs ***" + wantedSuit + "***");
        isComputerTurn = false;
    }

    private void computerPlaysWhot()
    {
        ArrayList<Card> nonWhotCards = new ArrayList<>();
        Card whotCard = null;
        boolean playedWhot = false;
        for (Card card : computerCards)
        {
            if (card.isWhot())
            {
                if (computerCards.size() > 10)
                {
                    whotCard = card;
                    playedWhot = true;
                    break;
                }
            } else if(card.getSuit() == wantedSuit)
            {
                nonWhotCards.add(card);
            }
        }

        if (playedWhot)
        {
            game.play(whotCard, forceWinner);
            previousCard = whotCard;
            computerRequestsWhot();
        }else
        {
            if(nonWhotCards.isEmpty())
            {
                game.computerDrawFromPile(forceWinner);
                System.out.println("Computer has drawn from pile.");
            }else
            {
                int randomIndex = rand.nextInt(nonWhotCards.size());
                Card neededCard = nonWhotCards.get(randomIndex);
                game.play(neededCard, forceWinner);
                previousCard = neededCard;
                System.out.println("Computer has played your requested card.");
                System.out.println(neededCard.toString());
            }
            wantedSuit = null;
        }
    }

    private void humanPickTwo(int index)
    {
        if(index == -1 &&
                !previousCard.isCardActionTaken())
        {
            game.humanDrawFromPile(forceWinner);
            game.humanDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("You have picked two.");
        }else
        {
            Card pickTwoCard = humanCards.get(index);
            if(pickTwoCard.isPickTwo() &&
                    !previousCard.isCardActionTaken())
            {
                game.play(pickTwoCard, forceWinner);
                previousCard.setCardActionTaken(true);
                previousCard = pickTwoCard;
                pickTwoCard.setDefendCard(true);
                System.out.println("You have defended the PICKTWO with.");
                System.out.println(previousCard.toString());
            }else
            {
                System.out.println("You selected an invalid card, draw two cards " +
                        "from pile or play a fitting card to defend.");
            }
        }
    }

    private void humanPickThree(int index)
    {
        if(index == -1 &&
                !previousCard.isCardActionTaken())
        {
            game.humanDrawFromPile(forceWinner);
            game.humanDrawFromPile(forceWinner);
            game.humanDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("You have picked three.");
        }else
        {
            Card pickThreeCard = humanCards.get(index);
            if(pickThreeCard.isPickThree() &&
                    !previousCard.isCardActionTaken())
            {
                game.play(pickThreeCard, forceWinner);
                previousCard.setCardActionTaken(true);
                previousCard = pickThreeCard;
                pickThreeCard.setDefendCard(true);
                System.out.println("You have defended the PICKTHREE with.");
                System.out.println(previousCard.toString());
            }else
            {
                System.out.println("You selected an invalid card, draw two cards " +
                        "from pile or play a fitting card to defend.");
            }
        }
    }

    private void humanGoMarket(int index)
    {
        if(previousCard.isCardActionTaken())
        {
            humanNormalPlay(index);
            return;
        }
        if(index == -1 &&
                !previousCard.isCardActionTaken())
        {
            game.humanDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("You have gone to market.");
        }else
        {
            System.out.println("Select -1 to go to market.");
        }
    }

    private void computerGoMarket()
    {
        if(previousCard.isCardActionTaken())
        {
            computerNormalPlay();
            return;
        }
        if(!previousCard.isCardActionTaken())
        {
            game.computerDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("Computer has gone to market.");
            isComputerTurn = false;
        }
    }

    private void computerSuspension()
    {
        if(previousCard.isCardActionTaken())
        {
            computerNormalPlay();
            return;
        }
        boolean pickFromPile = true;
        boolean playOtherCard = false;
        Card otherCard = null;
        for (Card card : computerCards)
        {
            if (card.isSuspension())
            {
                game.play(card, forceWinner);
                previousCard.setCardActionTaken(true);
                previousCard = card;
                System.out.println("Computer has played: ");
                System.out.println(previousCard.toString());
                pickFromPile = false;
                isComputerTurn = true;
                break;
            }else if(card.getSuit() == previousCard.getSuit())
            {
                playOtherCard = true;
                otherCard = card;
            }
        }

        if(pickFromPile)
        {
            game.computerDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("Computer has drawn from pile.");
            isComputerTurn = false;
        }else if(playOtherCard)
        {
            game.play(otherCard, forceWinner);
            previousCard.setCardActionTaken(true);
            previousCard = otherCard;
            System.out.println("Computer has played: ");
            System.out.println(previousCard.toString());
            isComputerTurn = false;
        }
    }

    private void computerHoldOn()
    {
        if(previousCard.isCardActionTaken())
        {
            computerNormalPlay();
            return;
        }
        boolean pickFromPile = true;
        boolean playOtherCard = false;
        Card otherCard = null;
        for (Card card : computerCards)
        {
            if (card.isHoldOn())
            {
                game.play(card, forceWinner);
                previousCard.setCardActionTaken(true);
                previousCard = card;
                System.out.println("Computer has played: ");
                System.out.println(previousCard.toString());
                pickFromPile = false;
                isComputerTurn = true;
                break;
            }else if(card.getSuit() == previousCard.getSuit())
            {
                playOtherCard = true;
                otherCard = card;
            }
        }

        if(pickFromPile)
        {
            game.computerDrawFromPile(forceWinner);
            previousCard.setCardActionTaken(true);
            System.out.println("Computer has drawn from pile.");
            isComputerTurn = false;
        }else if(playOtherCard)
        {
            game.play(otherCard, forceWinner);
            previousCard.setCardActionTaken(true);
            previousCard = otherCard;
            System.out.println("Computer has played: ");
            System.out.println(previousCard.toString());
            isComputerTurn = false;
        }
    }

    private void humanHoldOn(int index)
    {
        if(previousCard.isCardActionTaken())
        {
            humanNormalPlay(index);
            return;
        }
        Card card = humanCards.get(index);
        if(card.isHoldOn())
        {
            game.play(card, forceWinner);
            previousCard.setCardActionTaken(true);
            previousCard = card;
            System.out.println("You have played: ");
            System.out.println(previousCard.toString());
            isComputerTurn = false;
        }else
        {
            System.out.println("Play a valid card or pick from pile.");
        }
    }

    private void humanSuspension(int index)
    {
        if(previousCard.isCardActionTaken())
        {
            humanNormalPlay(index);
            return;
        }
        Card card = humanCards.get(index);
        if(card.isSuspension())
        {
            game.play(card, forceWinner);
            previousCard.setCardActionTaken(true);
            previousCard = card;
            System.out.println("You have played: ");
            System.out.println(previousCard.toString());
            isComputerTurn = false;
        }else
        {
            System.out.println("Play a valid card or pick from pile.");
        }
    }
}
