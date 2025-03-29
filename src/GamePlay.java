import java.util.*;
import java.security.SecureRandom;
import java.util.stream.Stream;

/**
 * This class has the highest number of lines and also the most difficult to implement. It contains the whole logic of
 * the Whot game. Computer and human take turn to play, after playing computer relinquishes control to the human and like
 * wise human. This turn continues until the drawPile runs out or either the computerDrawPile or playerDrawPile runs out
 * thereafter a winner will be decided.
 */
public class GamePlay
{
    private boolean isComputerTurn = false;//Used for relinquishing control between computer and human, initially it is
    //human turn
    private final WhotGame game;
    private Suit wantedSuit;
    private boolean validDeal = true;//Used in checking if a valid deal number is provided.
    private final List<Card> humanCards;//A list containing all player's cards
    private final List<Card> computerCards;//A list containing all computer's cards
    private Card previousCard;//Initially the previousCard is the startCard in the WhotGame game class. Thereafter, it takes
    //the most recent card played during the game. Each player's play must be valid if the card played has the same
    // Suit or the face(number) as the previousCard. The Whot card can be played at any time except during HOLDON,
    //SUSPENSION, PICKTWO, PICKTHREE or GENERAL MARKET.
    private final SecureRandom rand = new SecureRandom();
    private final Scanner input = new Scanner(System.in);
    private final String mode;//the game mode, easy or difficult are available
    private final String GAME_MODE_EASY = "Easy";
    private final int DEFAULT_DEAL_NUMBER = 6;
    private final String GAME_MODE_DIFFICULT = "Difficult";
    private final boolean forceWinner; //If true, it will ensure that there is a winner in the game, draw will not be allowed
    //so the game will run indefinitely until there is a winner.

    /**
     * The GamePlay has two overloaded constructors. This constructor without
     * a parameter is used when no deal number
     * is provided, it therefore uses the default deal number of 6.
     * @param mode the game mode, easy or difficult are available
     * @param forceWinner if true, run the game till there is a winner
     */
    public GamePlay(boolean forceWinner, String mode) throws WhotGameException
    {
        if(!mode.equalsIgnoreCase(GAME_MODE_EASY) && !mode.equalsIgnoreCase(GAME_MODE_DIFFICULT))
        {
            throw new WhotGameException("Game mode can take only one of the two options: '" + GAME_MODE_EASY
                    + "' and '" + GAME_MODE_DIFFICULT + "' ");
        }
        this.forceWinner = forceWinner;
        this.mode = mode;
        this.game = new WhotGame();
        this.humanCards = game.getHumanCardPile();
        this.computerCards = game.getComputerCardPile();
        this.deal(DEFAULT_DEAL_NUMBER);
    }

    /**
     * @param number a deal number
     */
    public GamePlay(boolean forceWinner, String mode, int number) throws WhotGameException
    {
        if(!mode.equalsIgnoreCase(GAME_MODE_EASY) && !mode.equalsIgnoreCase(GAME_MODE_DIFFICULT))
        {
            throw new WhotGameException("Game mode can take only one of the two options: '" + GAME_MODE_EASY + "' and '" + GAME_MODE_DIFFICULT + "' ");
        }
        this.forceWinner = forceWinner;
        this.mode = mode;
        this.game = new WhotGame();
        this.humanCards = game.getHumanCardPile();
        this.computerCards = game.getComputerCardPile();
        this.deal(number);
    }

    /**
     * previousCard is initially the startCard in the WhotGame
     * @param number a deal number that is provided, if no valid deal number is provided a
     *  WhotGameException is thrown and appropriate message is issued to the user.
     */
    public void deal(int number)
    {
        try
        {
            game.deal(number, mode);
            previousCard = game.getStartCard();
            previousCard.setFirstCard(true);
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
     * The start method starts the game,
     * the game runs until
     * there is a winner, rotating turn between computer and human.
     */
    private void startGame()
    {
        while (!game.isHumanTheWinner() &&
                !game.isComputerTheWinner() &&
                !game.isTie())
        {
            if (isComputerTurn)
            {
                computerPlay();
            } else
            {
                System.out.println("The card on board is:");
                System.out.println(previousCard.toString());
                if(previousCard.isWhot() && wantedSuit != null)
                {
                    System.out.println("The wanted card is: *** " + wantedSuit + " ***");
                }
                humanPlay();
            }
        }

        System.out.println("Your number of cards' left: " + humanCards.size());
        System.out.println("Computer number of cards' left: " + computerCards.size());
        if(!forceWinner)
        {
            System.out.println("Your cards' face count: " + game.getPlayerCounter());
            System.out.println("Computer's cards' face count: " + game.getComputerCounter());
        }

        if(game.isHumanTheWinner())
        {
            System.out.println("Game Over!!!\nYou win!");
        }else if(game.isComputerTheWinner())
        {
            System.out.println("Game Over!!!\nYou lose!");
        }else
        {
            System.out.println("Game Over!!!\nIt is a tie!");
        }
    }

    /**
     * computerPlay method handles the computer card play simulation, after finish playing computer
     * should relinquish
     * turn to the human.
     */
    private void computerPlay()
    {
        if(computerCards.size() == 1)
        {
            //Specially handle the last card in the pile
            Card lastCard = computerCards.get(0);
            if(lastCard.isWhot() ||
                    lastCard.getSuit() == previousCard.getSuit() ||
                    lastCard.getFace() == previousCard.getFace()||
                    lastCard.getSuit() == wantedSuit)
            {
                game.play(lastCard, forceWinner);
                previousCard = lastCard;
                System.out.println(previousCard);
                System.out.println("Check!");
                return;
            }
        }
        if ((previousCard.isNormalCard()))
        {
            computerNormalPlay();
        } else if (previousCard.isPickTwo())
        {
            if(!previousCard.isCardActionTaken())
            {
                computerPickTwo();
            }else
            {
                computerNormalPlay();
            }
        } else if (previousCard.isPickThree())
        {
            if(!previousCard.isCardActionTaken())
            {
                computerPickThree();
            }else
            {
                computerNormalPlay();
            }
        } else if (previousCard.isHoldOn())
        {
            previousCard.setCardActionTaken(true);
            computerNormalPlay();
        }else if(previousCard.isSuspension())
        {
            previousCard.setCardActionTaken(true);
            computerNormalPlay();
        } else if (previousCard.isGeneralMarket())
        {
            if(!previousCard.isCardActionTaken())
            {
                computerGoMarket();
            }else
            {
                computerNormalPlay();
            }
        }else if(previousCard.isWhot())
        {
            if(!previousCard.isCardActionTaken())
            {
                computerPlaysWhot();
            }else
            {
                computerNormalPlay();
            }
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
        if(index == -1)
        {
            if(previousCard.isPickThree() &&
                    !previousCard.isCardActionTaken())
            {
                game.humanDrawFromPile(forceWinner);
                game.humanDrawFromPile(forceWinner);
                game.humanDrawFromPile(forceWinner);
                previousCard.setCardActionTaken(true);
                System.out.println("You have picked three.");
                isComputerTurn = true;
            }else if(previousCard.isPickTwo() &&
                    !previousCard.isCardActionTaken())
            {
                game.humanDrawFromPile(forceWinner);
                game.humanDrawFromPile(forceWinner);
                previousCard.setCardActionTaken(true);
                System.out.println("You have picked two.");
                isComputerTurn = true;
            }else if(previousCard.isGeneralMarket()
                    && !previousCard.isCardActionTaken())
            {
                game.humanDrawFromPile(forceWinner);
                previousCard.setCardActionTaken(true);
                System.out.println("You have gone to market.");
                isComputerTurn = true;
            }else if(previousCard.isHoldOn())
            {
                game.humanDrawFromPile(forceWinner);
                previousCard.setCardActionTaken(true);
                System.out.println("You have drawn from pile.");
                isComputerTurn = true;
            }else if(previousCard.isSuspension())
            {
                game.humanDrawFromPile(forceWinner);
                previousCard.setCardActionTaken(true);
                System.out.println("You have drawn from pile.");
                isComputerTurn = true;
            }else
            {
                game.humanDrawFromPile(forceWinner);
                System.out.println("You have drawn from pile.");
                previousCard.setCardActionTaken(true);
                isComputerTurn = true;
            }
        }else if(index == 0 &&
                previousCard.isWhot() &&
                previousCard.isFirstCard())
        {
            humanRequestsCard();
        }else if(index != -2)
        {
            if(humanCards.size() == 1)
            {
                //Specially handle the last card in the pile
                Card lastCard = humanCards.get(index-1);
                if(lastCard.isWhot() ||
                        lastCard.getSuit() == previousCard.getSuit() ||
                        lastCard.getFace() == previousCard.getFace() ||
                        lastCard.getSuit() == wantedSuit)
                {
                    game.play(lastCard, forceWinner);
                    previousCard = lastCard;
                    System.out.println(previousCard);
                    System.out.println("Check!");
                    return;
                }
            }

            humanNormalPlay(index-1);
        }else
        {
            isComputerTurn = false;
        }
    }

    private void humanRequestsCard()
    {
        try {
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
        }catch (InputMismatchException | NumberFormatException e)
        {
            input.nextLine();
            System.out.println("You made an invalid selection, please select a card you need.");
            return;
        }
        System.out.println("You need *** " + wantedSuit + " ***");
        isComputerTurn = true;
    }

    private int humanSelectCard()
    {
        try
        {
            System.out.println("You have " + (humanCards.size()) + " cards in your pile.");
            System.out.println("These are all your cards:");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < humanCards.size(); i++)
            {
                String s = ">> " + (i + 1) + " \n";
                String c = humanCards.get(i).toString();
                builder.append(s).append(c).append("\n");
            }
            System.out.println(builder);
            if(previousCard.isWhot() && previousCard.isFirstCard())
            {
                System.out.println("Enter 0 to request a card." );
            }else
            {
                System.out.println("Select the number to play or -1 to drawn from pile.");
            }
            return input.nextInt();
        }catch (IndexOutOfBoundsException e)
        {
            input.nextLine();
            System.out.println("The selection is not in your card.");
            return -2;
        }catch (InputMismatchException | NumberFormatException e)
        {
            input.nextLine();
            System.out.println("Select a valid card number.");
            return -2;
        }
    }

    private void humanNormalPlay(int index)
    {
        try
        {
            Card card = humanCards.get(index);
           if(previousCard.isWhot())
           {
               if(card.isWhot())
               {
                   game.play(card, forceWinner);
                   previousCard = card;
                   System.out.println("You played: ");
                   System.out.println(previousCard);
                   humanRequestsCard();
               }else
               {
                   if(card.getSuit() == wantedSuit)
                   {
                       game.play(card, forceWinner);
                       previousCard = card;
                       System.out.println("You played: ");
                       System.out.println(previousCard);
                       isComputerTurn = !card.isSuspension() && !card.isHoldOn();
                       wantedSuit = null;
                   }
               }
            }else if(previousCard.isPickTwo() &&
                   !previousCard.isCardActionTaken())
            {
                humanPickTwo(index);
            }else if(previousCard.isPickThree()&&
                   !previousCard.isCardActionTaken())
           {
               humanPickThree(index);
           } else if(previousCard.isGeneralMarket() &&
                   !previousCard.isCardActionTaken())
           {
               System.out.println("Enter -1 to go to market.");
           } else if(card.getSuit() == previousCard.getSuit() ||
                   card.getFace() == previousCard.getFace() ||
                   card.isWhot())
           {
               if(card.isWhot())
               {
                   game.play(card, forceWinner);
                   previousCard = card;
                   System.out.println("You played: ");
                   System.out.println(previousCard);
                   humanRequestsCard();
               }else
               {
                   game.play(card, forceWinner);
                   System.out.println("You played: ");
                   System.out.println(card);
                   isComputerTurn = !card.isSuspension() && !card.isHoldOn();
                   previousCard = card;
               }
           }
        } catch (IndexOutOfBoundsException e)
        {
            System.out.println("The selection is not in your card.");
        }catch (InputMismatchException | NumberFormatException e)
        {
            System.out.println("Select a valid card number.");
        }
    }

    private void humanPickTwo(int index)
    {
        Card card = humanCards.get(index);
        if(card.isPickTwo() &&
                !previousCard.isCardActionTaken())
        {
            game.play(card, forceWinner);
            previousCard = card;
            previousCard.setCardActionTaken(true);
            previousCard.setDefendCard(true);
            System.out.println("You have defended the PICKTWO with.");
            System.out.println(previousCard.toString());
            isComputerTurn = true;
        }else
        {
            System.out.println("You selected an invalid card, draw two cards " +
                    "from pile or play a fitting card to defend.");
        }
    }

    private void humanPickThree(int index)
    {
        Card card = humanCards.get(index);
        if(card.isPickThree() &&
                !previousCard.isCardActionTaken())
        {
            game.play(card, forceWinner);
            previousCard = card;
            previousCard.setCardActionTaken(true);
            previousCard.setDefendCard(true);
            System.out.println("You have defended the PICKTHREE with.");
            System.out.println(previousCard.toString());
            isComputerTurn = true;
        }else
        {
            System.out.println("You selected an invalid card, draw three cards " +
                    "from pile or play a fitting card to defend.");
        }
    }

    private void computerNormalPlay()
    {
        boolean computerDrawingFromPile = true;
        //In Difficult mode, and in the absence of force winner mode, when game is decided by the counts
        // of players cards
        //computer has to dispose cards with large face value(numbers) first before lower values.
        if(this.mode.equalsIgnoreCase(GAME_MODE_DIFFICULT) && !forceWinner)
        {
            computerCards.sort(Comparator.comparing(o -> String.valueOf(o.getFace())));
            //computerCards.forEach(System.out::println);
        }
        for (Card card : computerCards)
        {
            if (card.getSuit() == wantedSuit ||
                    card.getFace() == previousCard.getFace() ||
                    card.getSuit() == previousCard.getSuit())
            {
                game.play(card, forceWinner);
                System.out.println("Computer has played:");
                System.out.println(card);
                computerDrawingFromPile = false;
                isComputerTurn = card.isHoldOn() || card.isSuspension();
                previousCard = card;
                wantedSuit = null;
                break;
            }else if(card.isWhot() && wantedSuit == null)
            {
                game.play(card, forceWinner);
                previousCard = card;
                System.out.println("Computer has played:");
                System.out.println(previousCard);
                computerRequestsWhot();
                return;
            }
        }

        if(computerDrawingFromPile)
        {
            game.computerDrawFromPile(forceWinner, mode);
            System.out.println("Computer has drawn from pile.");
            isComputerTurn = false;
        }
    }

    private void computerPickTwo()
    {
        boolean twoPicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickTwo() &&
                    computerCards.size() > 7 &&
                    !previousCard.isCardActionTaken())
            {
                game.play(card, forceWinner);
                previousCard = card;
                twoPicked = false;
                System.out.println("Computer has defended the pick two with.");
                System.out.println(previousCard);
                previousCard.setCardActionTaken(true);
                previousCard.setDefendCard(true);
                break;
            }
        }
        if (twoPicked &&
                !previousCard.isDefendCard() &&
                !previousCard.isCardActionTaken())
        {
            game.computerDrawFromPile(forceWinner, mode);
            game.computerDrawFromPile(forceWinner, mode);
            previousCard.setCardActionTaken(true);
            System.out.println("Computer has picked pick two.");
        }
        isComputerTurn = false;
    }

    private void computerPickThree()
    {
        boolean threePicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickThree() &&
                    computerCards.size() > 7 &&
                    !previousCard.isCardActionTaken())
            {
                game.play(card, forceWinner);
                previousCard = card;
                threePicked = false;
                System.out.println("Computer has defended the pick three with:");
                System.out.println(previousCard);
                previousCard.setDefendCard(true);
                previousCard.setCardActionTaken(true);
                break;
            }
        }
        if (threePicked &&
                !previousCard.isDefendCard() &&
                !previousCard.isCardActionTaken())
        {
            game.computerDrawFromPile(forceWinner, mode);
            game.computerDrawFromPile(forceWinner, mode);
            game.computerDrawFromPile(forceWinner, mode);
            previousCard.setCardActionTaken(true);
            System.out.println("Computer has picked pick three.");
        }
        isComputerTurn = false;
    }

    private void computerRequestsWhot()
    {
        //In Difficult mode, and in the absence of force winner mode, when game is decided by the counts of players cards
        //computer has to request cards with large face value(numbers) first before lower values.
        if(this.mode.equalsIgnoreCase(GAME_MODE_DIFFICULT) && !forceWinner)
        {
            computerCards.sort(Comparator.comparing(o -> String.valueOf(o.getFace())));
        }
        ArrayList<Card> nonWhotCards = new ArrayList<>();
        ArrayList<Card> otherCards = new ArrayList<>();
        for (Card card : computerCards)
        {
            if (!card.isWhot() &&
                    card.getSuit() != previousCard.getSuit())
            {
                nonWhotCards.add(card);
            }
            if(!card.isWhot())
            {
                otherCards.add(card);
            }
        }

        if(otherCards.isEmpty())
        {
            //When computer has only Whot! cards in its pile, it requests any random suit
            //even though such a suit does not exist in its pile.
            Suit[] suits = {Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE};
            int randIndex = rand.nextInt(suits.length);
            wantedSuit = suits[randIndex];
            System.out.println("Computer needs *** " + wantedSuit + " ***");
            isComputerTurn = false;
            return;
        }

        //In difficult mode, when computer draw  pile is about to be exhausted, computer will have to
        //request special cards to increase its winning potential.
        if(this.mode.equalsIgnoreCase(GAME_MODE_DIFFICULT) &&
                computerCards.size() < 6)
        {
            Stream<Card> specialCards = computerCards.stream().filter(card ->
                    !card.isNormalCard()
            );
            long randomIndex = rand.nextLong(specialCards.count());
            Card neededCard = nonWhotCards.get((int) randomIndex);
            wantedSuit = neededCard.getSuit();
            System.out.println("Computer needs *** " + wantedSuit + " ***");
            isComputerTurn = false;
            return;
        }

        int randomIndex = rand.nextInt(nonWhotCards.size());
        Card neededCard = nonWhotCards.get(randomIndex);
        wantedSuit = neededCard.getSuit();
        System.out.println("Computer needs *** " + wantedSuit + " ***");
        isComputerTurn = false;
    }

    private void computerPlaysWhot()
    {
        ArrayList<Card> wantedSuits = new ArrayList<>();
        Card whotCard = null;
        boolean playedWhot = false;
        ArrayList<Card> nonWhotCards = new ArrayList<>();
        for (Card card : computerCards)
        {
            if (card.isWhot())
            {
                if (computerCards.size() > 7)
                {
                    whotCard = card;
                    playedWhot = true;
                    break;
                }
            } else if(card.getSuit() == wantedSuit)
            {
                wantedSuits.add(card);
            }

            if(!card.isWhot())
            {
                nonWhotCards.add(card);
            }
        }

        if (playedWhot)
        {
            game.play(whotCard, forceWinner);
            previousCard = whotCard;
            System.out.println("Computer has played: ");
            System.out.println(previousCard);
            computerRequestsWhot();
        }else
        {
            if(nonWhotCards.isEmpty())
            {
                //When computer has only Whot! cards in its pile, it requests any random suit
                //even though such a suit does not exist in its pile.
                Suit[] suits = {Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE};
                int randIndex = rand.nextInt(suits.length);
                wantedSuit = suits[randIndex];
                System.out.println("Computer needs *** " + wantedSuit + " ***");
                isComputerTurn = false;
            }else if(!wantedSuits.isEmpty())
            {
                int randomIndex = rand.nextInt(wantedSuits.size());
                Card neededCard = wantedSuits.get(randomIndex);
                game.play(neededCard, forceWinner);
                previousCard = neededCard;
                System.out.println("Computer has played:");
                System.out.println(neededCard.toString());
                wantedSuit = null;
                isComputerTurn = neededCard.isHoldOn() || neededCard.isSuspension();
            }else
            {
                game.computerDrawFromPile(forceWinner, mode);
                previousCard.setCardActionTaken(true);
                System.out.println("Computer has drawn from pile.");
                isComputerTurn = false;
            }
        }
    }

    private void computerGoMarket()
    {
        game.computerDrawFromPile(forceWinner, mode);
        previousCard.setCardActionTaken(true);
        System.out.println("Computer has gone to market.");
        isComputerTurn = false;
    }
}
