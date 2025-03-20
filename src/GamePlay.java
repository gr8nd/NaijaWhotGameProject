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
    private static int counter = 0;
    private boolean forceWinner; //If true, it will ensure that there is a winner in the game, draw will not be allowed
    //so the game will run indefinitely until there is a winner.
    /**
     * The GamePlay has two overloaded constructors. This constructor without a parameter is used when no deal number
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
     * @param number a deal number that is provided, if no valid deal number is provided a WhotGameException is thrown
     * and appropriate message is issued to the user.
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
     * The start method checks whether a valid deal was done and thereafter starts the game, the game runs until
     * there is a winner, rotating turn between computer and human.
     */
    public void start()
    {
        if (validDeal)
        {
            while ( !game.isHumanIsTheWinner() &&
                    !game.isComputerIsTheWinner() &&
                    !game.isTie() )
            {
                    if (isComputerTurn)
                    {
                        computerPlay();
                    }else
                    {
                        humanPlay();
                    }
            }
        }
    }

    /**
     * computerPlay method handles the computer card play simulation, after finish playing computer should relinquish
     * turn to the human.
     */
    public void computerPlay()
    {
        if(counter == 0)
        {
            System.out.println("Computer has played:");
            System.out.println(previousCard.toString());
            game.play(previousCard, forceWinner);
        }
        if((previousCard.isNormalCard() && counter != 0) || previousCard.isPlayerWhot())
        {
            if (computerSelectCardToPlay())
            {
                game.computerDraw(forceWinner);
                isComputerTurn = false;
                System.out.println("Computer has drawn from pile");
            }
        }

        try {
            //Any of the following nested if-statements execute according to the nature of the previous card
            while (!previousCard.isNormalCard()) {
                //This statement executes when the previous card is HOLDON
                if (previousCard.isHoldOn())
                {
                    computerSelectCardToPlay();
                    //This statement executes when the previous card is PICKTWO
                } else if (previousCard.isPickTwo() && !previousCard.isDefendCard())
                {
                    humanSelectCardToPlay("-1. to pick two from the pile or a card to defend");
                    int userInput = input.nextInt();
                    outer:while (true)
                    {
                        if (userInput == -1)
                        {
                            game.playerDraw(forceWinner);
                            game.playerDraw(forceWinner);
                            System.out.println("You have picked two cards");
                            if (computerSelectCardToPlay())
                            {
                                game.computerDraw(forceWinner);
                                isComputerTurn = false;
                                System.out.println("Computer has drawn from pile");
                                break;
                            }
                        } else
                        {
                            Card defendCard = playerCards.get(userInput - 1);
                            if (defendCard.isPickTwo())
                            {
                                previousCard = defendCard;
                                System.out.println("You defended the PICK TWO with: ");
                                previousCard.setDefendCard(true);
                                System.out.println(previousCard.toString());
                                game.play(defendCard, forceWinner);
                                break;
                            } else
                            {
                                System.out.println("The card is not fit for defend, if you don't have a valid card pick from the draw pile");
                                humanSelectCardToPlay("-1. to pick two from the pile or a card to defend");
                                userInput = input.nextInt();
                            }
                        }
                    }
                    //This statement executes when the previous card is PICKTHREE
                } else if (previousCard.isPickThree() && !previousCard.isDefendCard())
                {
                    humanSelectCardToPlay("-1. to pick three from the pile or a card to defend");
                    int userInput = input.nextInt();
                    outer:while (true)
                    {
                        if (userInput == -1)
                        {
                            game.playerDraw(forceWinner);
                            game.playerDraw(forceWinner);
                            game.playerDraw(forceWinner);
                            System.out.println("You have picked three cards");
                            if (computerSelectCardToPlay())
                            {
                                game.computerDraw(forceWinner);
                                isComputerTurn = false;
                                System.out.println("Computer has drawn from pile");
                                break;
                            }
                        } else
                        {
                            Card defendCard = playerCards.get(userInput - 1);
                            if (defendCard.isPickThree())
                            {
                                previousCard = defendCard;
                                System.out.println("You defended the PICK THREE with: ");
                                previousCard.setDefendCard(true);
                                System.out.printf(previousCard.toString());
                                game.play(defendCard, forceWinner);
                                break;
                            } else
                            {
                                System.out.println("The card is not fit for defend, if you don't have a valid card pick from the draw pile");
                                humanSelectCardToPlay("-1. to pick three from the pile or a card to defend");
                                userInput = input.nextInt();
                            }

                        }
                    }
                    //This statement executes when the previous card is a GENERAL MARKET
                } else if (previousCard.isGeneralMarket())
                {
                    humanSelectCardToPlay("Hit 'Enter' to go to market");
                    String userInput = input.nextLine();
                    game.playerDraw(forceWinner);
                    System.out.println("You have gone to market");
                    if (computerSelectCardToPlay())
                    {
                        game.computerDraw(forceWinner);
                        isComputerTurn = false;
                        System.out.println("Computer has drawn from pile");
                        break;
                    }

                    //The statement will execute when the previous card is Whot and the Whot was played by
                    //the computer and not human
                } else if (previousCard.isWhot() || !previousCard.isPlayerWhot())
                {
                    int randomIndex = rand.nextInt(computerCards.size());
                    Card wantedCard = computerCards.get(randomIndex);
                    while (!wantedCard.isWhot() && !wantedCard.getSuit().equals(previousCard.getSuit()))
                    {
                        randomIndex = rand.nextInt(computerCards.size());
                        wantedCard = computerCards.get(randomIndex);
                    }
                    System.out.printf("Computer needs *****%s*****%n", wantedCard.getSuit());
                    humanSelectCardToPlay("-1. to draw from the pile");
                    int userInput = input.nextInt();
                    if (userInput == -1)
                    {
                        if (computerSelectCardToPlay())
                        {
                            game.computerDraw(forceWinner);
                            isComputerTurn = false;
                            System.out.println("Computer has drawn from pile");
                            break;
                        }
                    } else
                    {
                        Card playedCard = playerCards.get(userInput - 1);
                        while (true)
                        {
                            if (playedCard.getSuit().equals(wantedCard.getSuit()) || playedCard.isWhot())
                            {
                                previousCard = playedCard;
                                System.out.println("You played:");
                                System.out.println(previousCard);
                                game.play(playedCard, forceWinner);
                                if(playedCard.isWhot())
                                {
                                    isComputerTurn = false;
                                }
                                break;
                            } else
                            {
                                System.out.print("The card you played didn't fit computer request");
                                System.out.printf("Computer needs *****%s*****%n", wantedCard.getSuit());
                                humanSelectCardToPlay("-1. to draw from the pile");
                                userInput = input.nextInt();
                                if (userInput == -1)
                                {
                                    System.out.print("You have drawn from pile");
                                    game.playerDraw(forceWinner);
                                    break;
                                }
                            }
                        }
                    }
                    //This statement executes when the previous card is suspension card, that is a card with face
                    //value of 8
                } else if (previousCard.isSuspension())
                {
                    if (!computerSelectCardToPlay())
                    {
                        game.computerDraw(forceWinner);
                        isComputerTurn = false;
                        System.out.println("Computer has drawn from pile");
                        break;
                    }
                    //This else statement executes when the previous card is normal card
                } else
                {
                    computerSelectCardToPlay();
                }
                //This statement executes when the previous card is a defend card coming from the human player
                //the only two cards that can be defended are PICKTWO and PICKTHREE
                if(previousCard.isDefendCard())
                {
                    if (!computerSelectCardToPlay())
                    {
                        game.computerDraw(forceWinner);
                        isComputerTurn = false;
                        System.out.println("Computer has drawn from pile");
                        break;
                    }
                    previousCard.setDefendCard(false);
                }
            }
        } catch(InputMismatchException | NumberFormatException e)
        {
            System.out.println("Select a valid card number");
        } catch (IndexOutOfBoundsException e)
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
    public void humanPlay()
    {
        while (!isComputerTurn)
        {
            try {
                humanSelectCardToPlay("-1. to draw from the pile");
                int userInput = input.nextInt();
                if (userInput == -1)
                {
                    game.playerDraw(forceWinner);
                    isComputerTurn = true;
                    System.out.println("You have drawn from pile");
                } else
                {
                    boolean validPlay = false;
                    Card playedCard = playerCards.get(userInput - 1);
                    if (playedCard.getFace() == previousCard.getFace() ||
                            playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot())
                    {
                        previousCard = playedCard;
                        System.out.println("You played:");
                        System.out.println(previousCard);
                        game.play(previousCard, forceWinner);
                        isComputerTurn = true;
                    } else
                    {
                        while (!validPlay)
                        {
                            System.out.println("You selected an invalid card, draw from pile or play a fitting card");
                            humanSelectCardToPlay("-1. to draw from the pile");
                            userInput = input.nextInt();
                            if (userInput == -1)
                            {
                                game.playerDraw(forceWinner);
                                isComputerTurn = true;
                                validPlay = true;
                            } else
                            {
                                playedCard = playerCards.get(userInput - 1);
                                if (playedCard.getFace() == previousCard.getFace() ||
                                        playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot())
                                {
                                    previousCard = playedCard;
                                    System.out.println("You played:");
                                    System.out.println(previousCard.toString());
                                    game.play(previousCard, forceWinner);
                                    validPlay = true;
                                }
                            }
                            if (!validPlay)
                            {
                                System.out.println("Computer played:");
                                System.out.println(previousCard.toString());
                                System.out.println("Select a valid card to play or draw from pile");
                            }
                        }
                    }
                    //This statement executes when the previous card is SUSPENSION
                    if (previousCard.isSuspension())
                    {

                        while (!validPlay)
                        {
                            humanSelectCardToPlay("-1. to draw from the pile");
                            userInput = input.nextInt();
                            if (userInput == -1)
                            {
                                game.playerDraw(forceWinner);
                                isComputerTurn = true;
                                validPlay = true;
                            } else
                            {
                                playedCard = playerCards.get(userInput - 1);
                                if (playedCard.isSuspension() ||
                                        playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot())
                                {
                                    previousCard = playedCard;
                                    System.out.println("You played:");
                                    System.out.println(previousCard);
                                    game.play(previousCard, forceWinner);
                                    isComputerTurn = false;
                                    validPlay = true;
                                }
                            }
                            if (!validPlay)
                            {
                                System.out.println("Computer played:");
                                System.out.println(previousCard.toString());
                                System.out.println("Select a valid card to play or draw from pile");
                            }
                        }

                        //This statement executes when the previous card is HOLDON
                    } else if (previousCard.isHoldOn())
                    {
                        while (!validPlay)
                        {
                            humanSelectCardToPlay("-1. to draw from the pile");
                            userInput = input.nextInt();
                            if (userInput == -1)
                            {
                                game.playerDraw(forceWinner);
                                isComputerTurn = true;
                                validPlay = true;
                            } else
                            {
                                playedCard = playerCards.get(userInput - 1);
                                if (playedCard.getFace() == previousCard.getFace() ||
                                        playedCard.getSuit().equals(previousCard.getSuit()) || playedCard.isWhot())
                                {
                                    previousCard = playedCard;
                                    System.out.println("You played:");
                                    System.out.println(previousCard.toString());
                                    game.play(previousCard, forceWinner);
                                    isComputerTurn = false;
                                    validPlay = true;
                                }
                            }
                            if (!validPlay)
                            {
                                System.out.println("Computer played:");
                                System.out.println(previousCard.toString());
                                System.out.println("Select a valid card to play or draw from pile");
                            }
                        }

                        //This statement executes when the previous card is PICKTWO
                    } else if (previousCard.isPickTwo())
                    {
                        while (previousCard.isPickTwo())
                        {
                            if (computerCards.size() >= 10)
                            {
                                for (Card card : computerCards)
                                {
                                    if (card.isPickTwo())
                                    {
                                        previousCard = card;
                                        System.out.println("Computer has defended the PICK TWO with:");
                                        System.out.println(previousCard.toString());
                                        game.play(card, forceWinner);
                                        break;
                                    }
                                }
                            } else
                            {
                                game.computerDraw(forceWinner);
                                game.computerDraw(forceWinner);
                                System.out.println("computer has picked two cards");
                            }
                        }

                        //This statement executes when the previous card is PICKTHREE
                    } else if (previousCard.isPickThree())
                    {
                        while (previousCard.isPickThree())
                        {
                            if (computerCards.size() >= 9)
                            {
                                for (Card card : computerCards)
                                {
                                    if (card.isPickThree())
                                    {
                                        previousCard = card;
                                        System.out.println("computer defended the PICK THREE with:");
                                        System.out.println(previousCard);
                                        game.play(card, forceWinner);
                                        break;
                                    }
                                }
                            } else
                            {
                                game.computerDraw(forceWinner);
                                game.computerDraw(forceWinner);
                                game.computerDraw(forceWinner);
                                System.out.println("computer has picked three cards");
                            }
                        }
                        //This statement executes when the previous card is GENERAL MARKET
                    } else if (previousCard.isGeneralMarket())
                    {
                        game.computerDraw(forceWinner);
                        game.play(previousCard, forceWinner);
                        System.out.println("computer has gone to market");
                        isComputerTurn = false;
                    } else if (previousCard.isNormalCard())
                    {
                        game.play(previousCard, forceWinner);
                        isComputerTurn = true;
                    }

                    //This statement executes when the previous card is Whot
                    if (previousCard.isWhot())
                    {
                        previousCard.setPlayerWhot(true);
                        System.out.printf("1. %s%n2. %s%n3. %s%n4. %s%n5. %s%n",
                                Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE);
                        int playerInput = input.nextInt();
                        Suit wantedSuit = null;
                        switch (playerInput)
                        {
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
                        boolean computerPlayed = false;
                       for(Card card : computerCards)
                       {
                           if(card.getSuit().equals(wantedSuit) || card.isWhot())
                           {
                               previousCard = card;
                               System.out.println("Computer has played:");
                               System.out.println(previousCard.toString());
                               game.play(previousCard, forceWinner);
                               computerPlayed = true;
                           }
                       }
                       if(!computerPlayed)
                       {
                           game.computerDraw(forceWinner);
                           System.out.println("Computer has drawn from pile");
                       }
                    }
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

    public boolean computerSelectCardToPlay()
    {
        boolean isComputerDrawingFromPile = true;
        for(Card card:computerCards)
        {
            if(card.getFace() == previousCard.getFace() ||
                    card.getSuit().equals(previousCard.getSuit()) ||
                    card.isWhot())
            {
                previousCard = card;
                System.out.println("Computer has played:");
                System.out.println(previousCard);
                game.play(previousCard, forceWinner);
                isComputerDrawingFromPile = false;
                break;
            }
        }
        return  isComputerDrawingFromPile;
    }

    public void humanSelectCardToPlay(String message)
    {
        System.out.print("Hit 'Enter' to see all your cards: >> ");
        String yes = input.nextLine();
        int index;
        for (index = 0; index < playerCards.size(); index++)
        {
            System.out.println(">> " + (index + 1) + " ");
            System.out.print(playerCards.get(index).toString() + " ");
        }
        System.out.println();
        System.out.println(message);
        System.out.print("Select a card to play: >> ");
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
