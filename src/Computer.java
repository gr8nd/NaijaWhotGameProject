import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Computer
{
    private final boolean forceWinner;
    private final String mode;
    private final WhotGame whotGame;
    private final SecureRandom rand = new SecureRandom();
    private final String GAME_MODE_DIFFICULT = "Difficult";
    private final List<Card> computerCards;
    private final GamePlay gamePlay;
    protected Computer(boolean forceWinner, String mode, WhotGame whotGame,
                    List<Card> computerCards, GamePlay gamePlay)
    {
        this.forceWinner = forceWinner;
        this.mode = mode;
        this.whotGame = whotGame;
        this.computerCards = computerCards;
        this.gamePlay = gamePlay;
    }

    /**
     * computerPlay method handles the computer card play simulation, after finish playing computer
     * should relinquish
     * turn to the human.
     */
    protected void play()
    {
        //Specially handle the last card in the pile
        Card lastCard = computerCards.get(0);
        if(computerCards.size() == 1 && (lastCard.isWhot() ||
                lastCard.getSuit() == gamePlay.getPreviousCard().getSuit() ||
                lastCard.getFace() == gamePlay.getPreviousCard().getFace()||
                lastCard.getSuit() == gamePlay.getWantedSuit()))
        {
            whotGame.play(lastCard, forceWinner);
            System.out.println(lastCard);
            System.out.println("Check!");
            gamePlay.setPreviousCard(lastCard);
            return;
        }
        if ((gamePlay.getPreviousCard().isNormalCard()))
        {
            computerNormalPlay();
        } else if (gamePlay.getPreviousCard().isPickTwo())
        {
            if(!gamePlay.getPreviousCard().isCardActionTaken())
            {
                computerPickTwo();
            }else
            {
                computerNormalPlay();
            }
        } else if (gamePlay.getPreviousCard().isPickThree())
        {
            if(!gamePlay.getPreviousCard().isCardActionTaken())
            {
                computerPickThree();
            }else
            {
                computerNormalPlay();
            }
        } else if (gamePlay.getPreviousCard().isHoldOn())
        {
            gamePlay.getPreviousCard().setCardActionTaken(true);
            computerNormalPlay();
        }else if(gamePlay.getPreviousCard().isSuspension())
        {
            gamePlay.getPreviousCard().setCardActionTaken(true);
            computerNormalPlay();
        } else if (gamePlay.getPreviousCard().isGeneralMarket())
        {
            if(!gamePlay.getPreviousCard().isCardActionTaken())
            {
                computerGoMarket();
            }else
            {
                computerNormalPlay();
            }
        }else if(gamePlay.getPreviousCard().isWhot())
        {
            if(!gamePlay.getPreviousCard().isCardActionTaken())
            {
                computerPlaysWhot();
            }else
            {
                computerNormalPlay();
            }
        }
    }

    /**
     * Method that handles computer normal play i.e. the play of cards that
     * are not special cards
     */
    private void computerNormalPlay()
    {
        if(this.mode.equalsIgnoreCase(GAME_MODE_DIFFICULT))
        {
            playDifficultMode();
        }else
        {
            playEasyMode();
        }
    }

    private void playDifficultMode()
    {
        //In Difficult mode, and in the absence of force winner mode, when game is decided by the counts
        // of players cards
        //computer has to dispose cards with large face value(numbers) first before lower values.
        if(!forceWinner)
        {
            computerCards.sort(Comparator.comparing(o -> String.valueOf(o.getFace())));
        }
        Card nonNextTurnCard = null;
        List<Card> longestSequentialList = findLongestSequentialPlayList(gamePlay.getPreviousCard());
        for (Card card : longestSequentialList)
        {
            if ((card.getSuit() == gamePlay.getWantedSuit() ||
                    card.getFace() == gamePlay.getPreviousCard().getFace() ||
                    card.getSuit() == gamePlay.getPreviousCard().getSuit()) &&
                    !card.isWhot())
            {
                //For each card in computer's pile, check whether there is another card with the
                //same face or suit, so computer will play next turn without being forced to
                //draw from pile.
                if(findNextTurnCards(longestSequentialList, card))
                {
                    System.out.println("Computer has played:");
                    System.out.println(card);
                    gamePlay.setIsComputerTurn(card.isHoldOn() || card.isSuspension());
                    whotGame.play(card, forceWinner);
                    gamePlay.setPreviousCard(card);
                    gamePlay.setWantedSuit(null);
                    return;
                }
                nonNextTurnCard = card;
            }else if(card.isWhot() &&
                    gamePlay.getWantedSuit() == null &&
                    computerCards.size() > 6)
            {
                computerRequestsWhot(card);
                return;
            }
        }

        if(nonNextTurnCard != null)
        {
            System.out.println("Computer has played:");
            System.out.println(nonNextTurnCard);
            gamePlay.setIsComputerTurn(nonNextTurnCard.isHoldOn() || nonNextTurnCard.isSuspension());
            whotGame.play(nonNextTurnCard, forceWinner);
            gamePlay.setPreviousCard(nonNextTurnCard);
            gamePlay.setWantedSuit(null);
            return;
        }

        draw();
    }

    private void playEasyMode()
    {
        for (Card card : computerCards)
        {
            if ((card.getSuit() == gamePlay.getWantedSuit() ||
                    card.getFace() == gamePlay.getPreviousCard().getFace() ||
                    card.getSuit() == gamePlay.getPreviousCard().getSuit()) &&
                    !card.isWhot())
            {
                System.out.println("Computer has played:");
                System.out.println(card);
                gamePlay.setIsComputerTurn(card.isHoldOn() || card.isSuspension());
                whotGame.play(card, forceWinner);
                gamePlay.setPreviousCard(card);
                gamePlay.setWantedSuit(null);
                return;
            }else if(card.isWhot() &&
                    gamePlay.getWantedSuit() == null &&
                    computerCards.size() > 6)
            {
                computerRequestsWhot(card);
                return;
            }
        }

        draw();
    }

    private void computerPickTwo()
    {
        boolean twoPicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickTwo() &&
                    computerCards.size() > 6 &&
                    !gamePlay.getPreviousCard().isCardActionTaken())
            {
                whotGame.play(card, forceWinner);
                twoPicked = false;
                System.out.println("Computer has defended the PICK TWO with.");
                System.out.println(card);
                gamePlay.getPreviousCard().setCardActionTaken(true);
                card.setDefendCard(true);
                gamePlay.setPreviousCard(card);
                break;
            }
        }
        if (twoPicked &&
                !gamePlay.getPreviousCard().isDefendCard() &&
                !gamePlay.getPreviousCard().isCardActionTaken())
        {
            whotGame.computerDrawFromPile(forceWinner, mode);
            whotGame.computerDrawFromPile(forceWinner, mode);
            gamePlay.getPreviousCard().setCardActionTaken(true);
            System.out.println("Computer has picked pick two.");
        }
        gamePlay.setIsComputerTurn(false);
    }

    private void computerPickThree()
    {
        boolean threePicked = true;
        for (Card card : computerCards)
        {
            if (card.isPickThree() &&
                    computerCards.size() > 6 &&
                    !gamePlay.getPreviousCard().isCardActionTaken())
            {
                whotGame.play(card, forceWinner);
                threePicked = false;
                System.out.println("Computer has defended the PICK THREE with:");
                System.out.println(card);
                gamePlay.getPreviousCard().setCardActionTaken(true);
                card.setDefendCard(true);
                gamePlay.setPreviousCard(card);
                break;
            }
        }
        if (threePicked &&
                !gamePlay.getPreviousCard().isDefendCard() &&
                !gamePlay.getPreviousCard().isCardActionTaken())
        {
            whotGame.computerDrawFromPile(forceWinner, mode);
            whotGame.computerDrawFromPile(forceWinner, mode);
            whotGame.computerDrawFromPile(forceWinner, mode);
            gamePlay.getPreviousCard().setCardActionTaken(true);
            System.out.println("Computer has picked pick three.");
        }
        gamePlay.setIsComputerTurn(false);
    }

    /**
     * Play the Whot! the user requests
     */
    private void computerPlaysWhot()
    {
        ArrayList<Card> wantedSuits = new ArrayList<>();
        Card whotCard = null;
        boolean playedWhot = false;
        ArrayList<Card> nonWhotCards = new ArrayList<>();
        for (Card card : computerCards)
        {
            if (card.isWhot() && computerCards.size() > 5)
            {
                whotCard = card;
                playedWhot = true;
            } else if(card.getSuit() == gamePlay.getWantedSuit())
            {
                wantedSuits.add(card);
            }

            if(!card.isWhot())
            {
                nonWhotCards.add(card);
            }
        }

        if(nonWhotCards.isEmpty())
        {
            //When computer has only Whot! cards in its pile, it requests any random suit
            //even though a card with such a suit does not exist in its pile.
            Suit[] suits = {Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE};
            int randIndex = rand.nextInt(suits.length);
            gamePlay.setWantedSuit(suits[randIndex]);
            whotGame.play(nonWhotCards.get(0), forceWinner);
            System.out.println("Computer needs *** " + gamePlay.getWantedSuit() + " ***");
            gamePlay.setIsComputerTurn(false);
        }else if(!wantedSuits.isEmpty())
        {
            int randomIndex = rand.nextInt(wantedSuits.size());
            Card neededCard = wantedSuits.get(randomIndex);
            System.out.println("Computer has played:");
            System.out.println(neededCard.toString());
            whotGame.play(neededCard, forceWinner);
            gamePlay.setWantedSuit(null);
            gamePlay.setIsComputerTurn(neededCard.isHoldOn() || neededCard.isSuspension());
            gamePlay.setPreviousCard(neededCard);
        }else if(playedWhot)
        {
            //If computer does not find any reason to request a card
            //with Whot card, it will just draw from pile
            if(!computerRequestsWhot(whotCard))
            {
                draw();
            }
        }
    }

    private void draw()
    {
        whotGame.computerDrawFromPile(forceWinner, mode);
        gamePlay.getPreviousCard().setCardActionTaken(true);
        System.out.println("Computer has drawn from pile.");
        gamePlay.setIsComputerTurn(false);
    }

    /**
     * Requests a card for human player to play
     * @param whotCard a Whot card
     * @return true if it requests a card, false otherwise
     */
    private boolean computerRequestsWhot(Card whotCard)
    {
        //In Difficult mode, and in the absence of force winner mode, when game is decided by the counts of players cards
        //computer has to request cards with large face value(numbers) first before lower values.
        if(this.mode.equalsIgnoreCase(GAME_MODE_DIFFICULT) && !forceWinner)
        {
            computerCards.sort(Comparator.comparing(o -> String.valueOf(o.getFace())));
        }
        ArrayList<Card> cardArrayList = new ArrayList<>();//non-whot cards whose suits are not the same
        // as a card currently on board i.e. cards whose suits are not the same as the suit of previousCard
        ArrayList<Card> otherCards = new ArrayList<>();
        computerCards.forEach(card ->{
            if (!card.isWhot() &&
                    card.getSuit() != gamePlay.getPreviousCard().getSuit())
            {
                cardArrayList.add(card);
            }
            if(!card.isWhot())
            {
                otherCards.add(card);
            }
        });

        if(otherCards.isEmpty())
        {
            //When computer has only Whot! cards in its pile, it requests any random suit
            //even though a card with such a suit does not exist in its pile.
            Suit[] suits = {Suit.CIRCLE, Suit.CROSS, Suit.TRIANGLE, Suit.STAR, Suit.SQUARE};
            int randIndex = rand.nextInt(suits.length);
            Suit wantedSuit = suits[randIndex];
            System.out.println("Computer has played:");
            System.out.println(whotCard);
            whotGame.play(whotCard, forceWinner);
            System.out.println("Computer needs *** " + wantedSuit + " ***");
            gamePlay.setPreviousCard(whotCard);
            gamePlay.setWantedSuit(wantedSuit);
            gamePlay.setIsComputerTurn(false);
            return true;
        }

        //In difficult mode, when computer draw  pile is about to be exhausted, computer will have to
        //request special cards to increase its winning potential and find a card to start with to
        // give the longest sequential play.
        if(this.mode.equalsIgnoreCase(GAME_MODE_DIFFICULT)  && !cardArrayList.isEmpty())
        {
            List<Card> specialCards = cardArrayList.stream().filter(Card::isSpecialCard).toList();
            if(!specialCards.isEmpty() &&
                    computerCards.size() < 7)
            {
                List<Card> longestList = new ArrayList<>();
                List<Card> nonSeqList = new ArrayList<>();
                List<Card> list = findLongestSequentialPlayList(specialCards,
                        0, longestList, nonSeqList);
                gamePlay.setWantedSuit(list.get(0).getSuit());
                gamePlay.setIsComputerTurn(false);
                displayComputerWantedCard(whotCard);
                return true;
            }
            List<Card> longestList = new ArrayList<>();
            List<Card> nonSeqList = new ArrayList<>();
            List<Card> list = findLongestSequentialPlayList(cardArrayList,
                    0, longestList, nonSeqList);
            gamePlay.setWantedSuit(list.get(0).getSuit());
            gamePlay.setIsComputerTurn(false);
            displayComputerWantedCard(whotCard);
            return true;
        }

        if(!cardArrayList.isEmpty())
        {
            gamePlay.setWantedSuit(cardArrayList.get(0).getSuit());
            gamePlay.setIsComputerTurn(false);
            displayComputerWantedCard(whotCard);
            return true;
        }

        return false;
    }

    private void displayComputerWantedCard(Card whotCard)
    {
        System.out.println("Computer has played:");
        System.out.println(whotCard);
        whotGame.play(whotCard, forceWinner);
        System.out.println("Computer needs *** " + gamePlay.getWantedSuit() + " ***");
        gamePlay.setPreviousCard(whotCard);
    }

    private void computerGoMarket()
    {
        whotGame.computerDrawFromPile(forceWinner, mode);
        gamePlay.getPreviousCard().setCardActionTaken(true);
        System.out.println("Computer has gone to market.");
        gamePlay.setIsComputerTurn(false);
    }

    /**
     * Computer needs to find the card it will play to get the longest
     * sequential play. This is necessary to maximize its winning
     * potential.
     * @param longestList the longest list of such cards to be returned, initially empty
     * @param currentIndex the index of the current card to check if it is eligible to join the
     * longest list
     * @param nonSeqList the list that holds all disqualified cards
     * @param wantedCardList a list of cards that computer wants to choose one from to request with Whot card,
     * any card in this list that gives the longest sequential play will be requested by computer
     * @return a list with first part or all, containing cards that should be played in the list
     * order (from first element to last element) to give the longest sequential play, any
     * 'non-sequential' cards are also appended to the end of the list.
     */
    private List<Card> findLongestSequentialPlayList(List<Card> wantedCardList,
                                                     int currentIndex, List<Card> longestList,
                                                     List<Card> nonSeqList)
    {
        List<Card> currentLongestList = new ArrayList<>();
        List<Card> currentList = new ArrayList<>(wantedCardList);
        Card currentCard = wantedCardList.get(currentIndex);
        currentLongestList.add(currentCard);
        currentList.remove(currentCard);
        for (int index = 0; index < currentList.size(); index++)
        {
            Card card = currentList.get(index);
            if (card.getFace() == currentCard.getFace() ||
                    card.getSuit() == currentCard.getSuit())
            {
                currentLongestList.add(card);
                currentCard = card;
                currentList.remove(currentCard);
                index = 0;//move the cursor to the first index (start of the list)
            }
        }

        if(currentIndex == wantedCardList.size()-1)
        {
            longestList.addAll(nonSeqList);
            return longestList;
        }
        currentIndex += 1;
        if(currentLongestList.size() > longestList.size())
        {
            longestList = currentLongestList;
            nonSeqList = currentList;
        }
        return findLongestSequentialPlayList(wantedCardList, currentIndex, longestList, nonSeqList);
    }

    /**
     * Computer needs to find the card it will play to get the longest
     * sequential play. This is necessary to maximize its winning
     * potential.
     * @param previousCard the current card to start the sequential run from
     * @return a list with first part or all, containing cards that should be played in the list
     * order (from first element to last element) to give the longest sequential play, any
     * 'non-sequential' cards are also appended to the end of the list.
     */
    private List<Card> findLongestSequentialPlayList(Card previousCard)
    {
        List<Card> longestList = new ArrayList<>();
        List<Card> currentList = new ArrayList<>(computerCards);
        longestList.add(previousCard);
        currentList.remove(previousCard);//This may be redundant as previousCard may not
        //actually be in the currentList list since the very first previousCard is the most
        //recently played card(i.e. it might have been removed from computerCards or humanCards)
        for (int index = 0; index < currentList.size(); index++)
        {
            Card card = currentList.get(index);
            if (!card.isWhot() && (card.getFace() == previousCard.getFace() ||
                    card.getSuit() == previousCard.getSuit()))
            {
                longestList.add(card);
                previousCard = card;
                currentList.remove(previousCard);
                index = 0;//move the cursor to the first index (start of the list)
            }
        }

        longestList.remove(0);//remove the first element because it is the 'previousCard' played
        //card of the game
        longestList.addAll(currentList);
        return longestList;
    }

    /**
     * Before playing a card, computer wants to find a card it will play
     * so that it will be able to play next turn without being forced to draw from pile
     * so it will need to play a card whose face or suit it still has in its pile.
     * @param cardToPlay a card computer wants to check if it still has another card with
     * the same suit or face before playing it.
     * @param list computer card list to check for the other cards.
     * @return true if such a card exists, false otherwise.
     */
    private boolean findNextTurnCards(List<Card> list, Card  cardToPlay)
    {
        List<Card> similarCards = list.stream().filter(card ->
            card.getFace()==cardToPlay.getFace() || card.getSuit()==cardToPlay.getSuit()
        ).toList();
        return !similarCards.isEmpty();
    }
}
