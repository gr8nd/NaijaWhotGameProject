import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Human
{
    private final boolean forceWinner;
    private final boolean verbose;
    private final int EXIT_CODE = 99;
    private final WhotGame whotGame;
    private final List<Card> humanCards;
    private final GamePlay gamePlay;
    private final Scanner input = new Scanner(System.in);
    protected Human(boolean forceWinner, boolean verbose,
                 WhotGame whotGame, List<Card> humanCards, GamePlay gamePlay)
    {
        this.forceWinner = forceWinner;
        this.verbose = verbose;
        this.whotGame = whotGame;
        this.humanCards = humanCards;
        this.gamePlay = gamePlay;
    }

    /**
     * playerPlay method handles the player card play simulation, after
     * finish playing this method should hand over
     * turn to the computer.
     */
    protected void play()
    {
        showStats();
        int index = humanSelectCard();
        if(index == -1)
        {
            if(gamePlay.getCallCard().isPickThree() &&
                    !gamePlay.getCallCard().isCardActionTaken() &&
                    !gamePlay.getCallCard().isDefendCard())
            {
                whotGame.humanDrawFromPile(forceWinner);
                whotGame.humanDrawFromPile(forceWinner);
                whotGame.humanDrawFromPile(forceWinner);
                gamePlay.getCallCard().setCardActionTaken(true);
                System.out.println("You have picked three.");
                gamePlay.setIsComputerTurn(true);
            }else if(gamePlay.getCallCard().isPickTwo() &&
                    !gamePlay.getCallCard().isCardActionTaken() &&
                    !gamePlay.getCallCard().isDefendCard())
            {
                whotGame.humanDrawFromPile(forceWinner);
                whotGame.humanDrawFromPile(forceWinner);
                gamePlay.getCallCard().setCardActionTaken(true);
                System.out.println("You have picked two.");
                gamePlay.setIsComputerTurn(true);
            }else if(gamePlay.getCallCard().isGeneralMarket()
                    && !gamePlay.getCallCard().isCardActionTaken())
            {
                whotGame.humanDrawFromPile(forceWinner);
                gamePlay.getCallCard().setCardActionTaken(true);
                System.out.println("You have gone to market.");
                gamePlay.setIsComputerTurn(true);
            }else
            {
                whotGame.humanDrawFromPile(forceWinner);
                System.out.println("You have drawn from pile.");
                gamePlay.getCallCard().setCardActionTaken(true);
                gamePlay.setIsComputerTurn(true);
            }
        }else if(index == 0 &&
                gamePlay.getCallCard().isWhot() &&
                gamePlay.getCallCard().isCallCard())
        {
            humanRequestsCard(gamePlay.getCallCard());
        }else if(index == EXIT_CODE)
        {
            System.out.println("You ended the game.");
            System.exit(0);
        }else if(index != -2)
        {
           playCard(index);
        }else
        {
            gamePlay.setIsComputerTurn(false);
        }
    }

    private void showStats()
    {
        System.out.println("The current Draw Pile count: " + whotGame.getDrawPile().size());
        System.out.println("The call card is:");
        System.out.println(gamePlay.getCallCard().toString());
        if(gamePlay.getCallCard().isWhot() && gamePlay.getWantedSuit() != null)
        {
            System.out.println("The wanted card is: *** " + gamePlay.getWantedSuit() + " ***");
        }
    }

    private void playCard(int index)
    {
        try
        {
            //Specifically handle the last card in the pile
            Card lastCard = humanCards.get(index-1);
            if(humanCards.size() == 1 && (lastCard.isWhot() ||
                    lastCard.getSuit() == gamePlay.getCallCard().getSuit() ||
                    lastCard.getFace() == gamePlay.getCallCard().getFace() ||
                    lastCard.getSuit() == gamePlay.getWantedSuit()))
            {
                whotGame.play(lastCard, forceWinner);
                System.out.println(lastCard);
                System.out.println("Check up");
                gamePlay.setCallCard(lastCard);
                return;
            }
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println("The card number you selected is invalid.");
        }
        humanNormalPlay(index-1);
    }

    /**
     * prompt user to type what to play during the game
     * @return int an index of the card he wants to play or an instruction of the game
     */
    private int humanSelectCard()
    {
        try
        {
            System.out.println(displayHumanCards());
            if(gamePlay.getCallCard().isWhot() && gamePlay.getCallCard().isCallCard())
            {
                System.out.print("Enter 0 to request a card or 99 to end the game. >> " );
            }else
            {
                System.out.print("Select the number to play or -1 to pick from pile or 99 to end the game. >> ");
            }
            return input.nextInt();
        }catch (IndexOutOfBoundsException e)
        {
            System.out.println("The selection is not in your card.");
            return -2;
        }catch (InputMismatchException | NumberFormatException e)
        {
            System.out.println("Select a valid card number.");
            return -2;
        }
    }

    private String displayHumanCards()
    {
        System.out.println("You have " + (humanCards.size()>1?humanCards.size()+
                " cards":humanCards.size() + " card") + " in your pile.");
        System.out.println("This is your card list:");
        StringBuilder builder = new StringBuilder();
        if(verbose)
        {
            for (int i = 0; i < humanCards.size(); i++)
            {
                String s = ">> " + (i + 1) + " \n";
                String c = humanCards.get(i).toString();
                builder.append(s).append(c).append("\n");
            }
        }else
        {
            for (int i = 0; i < humanCards.size(); i++)
            {
                String s = "(" + (i + 1) + ") " +
                        humanCards.get(i).getFace() + " " + humanCards.get(i).getSuit();
                builder.append(s).append(" ");
            }
        }
        return builder.toString();
    }

    private void humanRequestsCard(Card card)
    {
        try
        {
            System.out.printf("Select Suit number to request:\n1. %s%n2. %s%n3. %s%n4. %s%n5. %s%n",
                    Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE);
            int want = input.nextInt();
            switch (want)
            {
                case 1:
                    gamePlay.setWantedSuit(Suit.CIRCLE);
                    break;
                case 2:
                    gamePlay.setWantedSuit(Suit.CROSS);
                    break;
                case 3:
                    gamePlay.setWantedSuit(Suit.TRIANGLE);
                    break;
                case 4:
                    gamePlay.setWantedSuit(Suit.STAR);
                    break;
                case 5:
                    gamePlay.setWantedSuit(Suit.SQUARE);
                    break;
                default:
                    System.out.println("You made an invalid selection, please select a card you need.");
                    return;
            }
        }catch (InputMismatchException | NumberFormatException e)
        {
            System.out.println("You made an invalid selection, please select a card you need.");
            return;
        }
        System.out.println("You played: ");
        System.out.println(card);
        whotGame.play(card, forceWinner);
        System.out.println("You need *** " + gamePlay.getWantedSuit() + " ***");
        gamePlay.setCallCard(card);
        gamePlay.setIsComputerTurn(true);
    }

    /**
     * Called when the callCard is normal card
     * @param index the index of the card user wants to play
     */
    private void humanNormalPlay(int index)
    {
        try
        {
            Card card = humanCards.get(index);
            if(gamePlay.getCallCard().isWhot())
            {
               humanPlaysWhot(card);
            }else if(gamePlay.getCallCard().isPickTwo() &&
                    !gamePlay.getCallCard().isCardActionTaken() &&
                    !gamePlay.getCallCard().isDefendCard())
            {
                humanPickTwo(index);
            }else if(gamePlay.getCallCard().isPickThree() &&
                    !gamePlay.getCallCard().isCardActionTaken() &&
                    !gamePlay.getCallCard().isDefendCard())
            {
                humanPickThree(index);
            } else if(gamePlay.getCallCard().isGeneralMarket() &&
                    !gamePlay.getCallCard().isCardActionTaken())
            {
                System.out.println("Enter -1 to go to market.");
            } else if(card.getSuit() == gamePlay.getCallCard().getSuit() ||
                    card.getFace() == gamePlay.getCallCard().getFace() ||
                    card.isWhot())
            {
               humanPlayCard(card);
            }
        } catch (IndexOutOfBoundsException e)
        {
            System.out.println("The selection is not in your card.");
        }catch (InputMismatchException | NumberFormatException e)
        {
            System.out.println("Select a valid card number.");
        }
    }

    private void humanPlayCard(Card card)
    {
        if(card.isWhot())
        {
            humanRequestsCard(card);
            return;
        }
        System.out.println("You played: ");
        System.out.println(card);
        whotGame.play(card, forceWinner);
        gamePlay.setIsComputerTurn(!card.isSuspension() && !card.isHoldOn());
        gamePlay.setCallCard(card);
    }

    private void humanPlaysWhot(Card card)
    {
        if(card.isWhot())
        {
            humanRequestsCard(card);
        }else if(card.getSuit() == gamePlay.getWantedSuit())
        {
            System.out.println("You played: ");
            System.out.println(card);
            whotGame.play(card, forceWinner);
            gamePlay.setCallCard(card);
            gamePlay.setIsComputerTurn(!card.isSuspension() && !card.isHoldOn());
            gamePlay.setWantedSuit(null);
        }
    }

    private void humanPickTwo(int index)
    {
        Card card = humanCards.get(index);
        if(card.isPickTwo() &&
                !gamePlay.getCallCard().isCardActionTaken())
        {
            whotGame.play(card, forceWinner);
            gamePlay.getCallCard().setCardActionTaken(true);
            card.setDefendCard(true);
            System.out.println("You have defended the PICK TWO with.");
            System.out.println(card);
            gamePlay.setCallCard(card);
            gamePlay.setIsComputerTurn(true);
            return;
        }
        System.out.println("You selected an invalid card, enter -1 to draw two cards " +
                    "from pile or play a fitting card to defend.");
    }

    private void humanPickThree(int index)
    {
        Card card = humanCards.get(index);
        if(card.isPickThree() &&
                !gamePlay.getCallCard().isCardActionTaken())
        {
            whotGame.play(card, forceWinner);
            gamePlay.getCallCard().setCardActionTaken(true);
            card.setDefendCard(true);
            System.out.println("You have defended the PICK THREE with.");
            System.out.println(card);
            gamePlay.setCallCard(card);
            gamePlay.setIsComputerTurn(true);
            return;
        }
        System.out.println("You selected an invalid card, enter -1 to draw three cards " +
                    "from pile or play a fitting card to defend.");
    }
}
