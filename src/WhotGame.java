import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * The logic of the Whot game starts here in the WhotGame class
 * drawPile: this is where computer and player can draw a card from when they run out of a fitting card or
 * when instructed to do so by the game rule such as the GENERAL MARKET.
 * computerCardPile: a list containing all the computer cards
 * playerDrawPile: a list containing all the player cards
 * computerCount and playerCount: keep record of the total face values of all the computer cards and the player
 * cards respectively, they are used when scoring by face to determine a winner.
 * boolean isThereWinner: used to check whether there is a winner or not
 */
public class WhotGame {
    private boolean computerTheWinner = false;
    private boolean humanTheWinner = false;
    private boolean isTie = false;
    private final SecureRandom rand = new SecureRandom();
    private ArrayList<Card> drawPile;//this is where computer and player can draw a card from when they run out of a fitting card or
    //when instructed to do so by the game rule such as the GENERAL MARKET.
    private ArrayList<Card> computerCardPile;//a list containing all the computer cards
    private ArrayList<Card> humanCardPile;//a list containing all the player cards
    private int computerCounter;//keeps record of the computer's face count when scoring by counting faces.
    private int playerCounter;//keeps record of the player's face count when scoring by counting faces.
    private ArrayList<Card> playedPile; //Holds the cards that are played during the course of the game
    private Card startCard;//This is first card that is displayed when the game starts

    public WhotGame()
    {
        NaijaWhots whots = new NaijaWhots();//creates a NaijaWhots object
        computerCardPile = new ArrayList<>();
        humanCardPile = new ArrayList<>();
        playedPile = new ArrayList<>();
        drawPile = new ArrayList<>();
        Card[] pack = new Card[54];
        pack = whots.getPack().toArray(pack);//gets the initialised and shuffled pack from the NaijaWhots class
        Collections.addAll(drawPile, pack);
        startCard = drawPile.get(0);//get the first card as the start card
    }

    /**
     *
     * @param number the total number of cards each player will receive at the start of the game, usually 6 but can
     * be any number as well.
     * @param mode the game mode, easy or difficult are available
     * @throws WhotGameException an exception that will be thrown when an invalid deal number is provided
     */
    public void deal(int number, String mode) throws WhotGameException
    {
        boolean isValidDeal = number >= 2 && number <= 27 && number % 2 == 0;
        if (isValidDeal)
        {
            for (int index = 0; index < number; index++)
            {
                String GAME_MODE_DIFFICULT = "Difficult";
                if(mode.equalsIgnoreCase(GAME_MODE_DIFFICULT))
                {
                    int probIndex = rand.nextInt(5);//Computer has 4/5 (80%) probability of getting special cards in
                    //difficult mode
                    if(probIndex != 3)
                    {
                        List<Card> specialCards = drawPile.stream().filter(card ->
                                !card.isNormalCard()).toList();
                        int randIndex = rand.nextInt(specialCards.size());
                        Card specialCard = specialCards.get(randIndex);
                        computerCardPile.add(specialCard);
                        drawPile.remove(specialCard);
                    }else
                    {
                        computerCardPile.add(drawPile.remove(0));//adds the first card in the drawPile to the computerPile
                        //and removes it afterward
                    }
                }else
                {
                    computerCardPile.add(drawPile.remove(0));//adds the first card in the drawPile to the computerPile
                    //and removes it afterward
                }
                humanCardPile.add(drawPile.remove(0));//adds the first card in the drawPile to the playerPile and
                //removes it afterward
            }

        } else if (number < 0)
        {
            throw new WhotGameException("You cannot deal a negative number of cards");
        } else if (number > 27)
        {
            throw new WhotGameException("You can only deal between 2 to 27 cards");
        } else if (number % 2 != 0)
        {
            throw new WhotGameException("You cannot deal an odd number of cards");
        } else
        {
            throw new WhotGameException("You can only deal 2 or more cards");
        }
    }

    /**
     * @param forceWinner if true, run the game till there is a winner
     * @param card the card object that is played, the method attempts to remove a played card from any of the card
     * piles it finds it.
     */
    public void play(Card card, boolean forceWinner)
    {
        computerCardPile.remove(card);
        humanCardPile.remove(card);
        drawPile.remove(card); //Needed only for the start card
        if (computerCardPile.size() == 1 || humanCardPile.size() == 1)
        {
            System.out.println("Last card!");
        }
        rule(card);
        checkWinner(forceWinner);
        playedPile.add(card);
    }

    /**
     * @param mode the game mode, easy or difficult are available
     * @param forceWinner if true, run the game till there is a winner
     * This draws a card from the draw pile and adds it to the computer pile if the draw contains at least one card
     * else there must have been a winner, it therefor sets the winner to true
     */
    public void computerDrawFromPile(boolean forceWinner, String mode)
    {
        if(!drawPile.isEmpty())
        {
            if(mode.equalsIgnoreCase("Difficult"))
            {
                int probIndex = rand.nextInt(5);//Computer has 4/5 (80%) probability of getting special cards in
                //difficult mode
                if(probIndex != 3)
                {
                    List<Card> specialCards = drawPile.stream().filter(card ->
                            !card.isNormalCard()).toList();
                    int randIndex = rand.nextInt(specialCards.size());
                    Card specialCard = specialCards.get(randIndex);
                    computerCardPile.add(specialCard);
                    drawPile.remove(specialCard);
                }else
                {
                    computerCardPile.add(drawPile.remove(0));//adds the first card in the drawPile to the computerPile
                    //and removes it afterward
                }
            }else
            {
                computerCardPile.add(drawPile.remove(0));
            }
        }
        checkWinner(forceWinner);
    }
    /**
     * @param forceWinner if true, run the game till there is a winner
     * This draws a card from the draw pile and adds it to the computer pile if the draw contains at least one card
     * else there must have been a winner, it therefor sets thereIsAWinner to true
     */
    public void humanDrawFromPile(boolean forceWinner)
    {
        if(!drawPile.isEmpty())
        {
            humanCardPile.add(drawPile.remove(0));
        }
        checkWinner(forceWinner);
    }

    /**
     *
     * @param card a card to be played or that has been played. The method checks whether the card is one of the six
     * special cards and issues the appropriate rule on the card
     */
    public void rule(Card card)
    {
        if (card.isHoldOn())
        {
            System.out.println("Hold on!");
        } else if (card.isPickTwo())
        {
            System.out.println("Pick two!");
        } else if (card.isPickThree())
        {
            System.out.println("Pick three!");
        } else if (card.isSuspension())
        {
            System.out.println("Suspension!");
        } else if (card.isWhot())
        {
            System.out.println("Whot! Select a card you need >> ");
        } else if (card.isGeneralMarket())
        {
            System.out.println("General market!");
        }
    }

    /**
     * @param forceWinner if true, run the game till there is a winner
     * The method checks for a winner and therefore should be called each time a card is played either by the computer
     * or by human. There is a winner when any of the following occurs during the game play:
     * 1. The drawPile runs out of cards (i.e. when it becomes empty).
     * 2. The computerDrawPile runs of cards or
     * 3. The playerDrawPile runs out of cards
     * If it is the drawPile that runs out of card, a winner is decided by counting the face values of each player's
     * cards. Any player that has the lowest count wins.
     */
    public void checkWinner(boolean forceWinner)
    {
        if(drawPile.isEmpty())
        {
            if (forceWinner)
            {
                ArrayList<Card> list = new ArrayList<>();
                for(int i =0; i < playedPile.size()-1; i++)
                {
                    //reset all cards but the last played card to their default values
                    Card card = playedPile.get(i);
                    card.setFirstCard(false);
                    card.setCardActionTaken(false);
                    card.setDefendCard(false);
                    list.add(card);
                }
                drawPile.addAll(list);
                Collections.shuffle(drawPile);
                playedPile.clear();
                list.clear();
            }else
            {
                countHumanCards();
                countComputerCards();
                if (computerCounter > playerCounter)
                {
                    humanTheWinner = true;
                } else if(playerCounter > computerCounter)
                {
                    computerTheWinner = true;
                }else
                {
                    isTie = true;
                }
            }
        }else
        {
            if(computerCardPile.isEmpty())
            {
                computerTheWinner = true;
            }else if(humanCardPile.isEmpty())
            {
                humanTheWinner = true;
            }
        }
    }
    /***
     * Count the face values of human cards after the draw pile has been exhausted.
     */
    public void countHumanCards()
    {
        playerCounter = 0;//reset counter each time it is called
        for (Card card : humanCardPile)
        {
            if (card.getSuit().equals(Suit.STAR))
            {
                playerCounter += card.getFace() * 2;
            } else
            {
                playerCounter += card.getFace();
            }
        }
    }

    /***
     * Count the face values of computer cards after the draw pile has been exhausted.
     */
    public void countComputerCards()
    {
        computerCounter = 0;//reset counter each time it is called
        for (Card card : computerCardPile)
        {
            if (card.getSuit().equals(Suit.STAR))
            {
                //face value of STAR suit is doubled during counting
                computerCounter += card.getFace() * 2;
            } else
            {
                computerCounter += card.getFace();
            }
        }
    }

    /**
     *
     * @return returns the draw pile
     */
    public ArrayList<Card> getDrawPile() {
        return drawPile;
    }

    /**
     *
     * @param drawPile draw pile where each player can pick cards from
     *
     */
    public void setDrawPile(ArrayList<Card> drawPile) {
        this.drawPile = drawPile;
    }

    /**
     *
     * @return the computer card pile
     */
    public ArrayList<Card> getComputerCardPile() {
        return computerCardPile;
    }

    /**
     *
     * @param computerCardPile a list of computer cards
     */
    public void setComputerCardPile(ArrayList<Card> computerCardPile) {
        this.computerCardPile = computerCardPile;
    }

    /**
     *
     * @return  a list containing the player's cards
     */
    public ArrayList<Card> getHumanCardPile() {
        return humanCardPile;
    }

    /**
     *
     * @param humanCardPile list containing player's cards
     */
    public void setHumanCardPile(ArrayList<Card> humanCardPile) {
        this.humanCardPile = humanCardPile;
    }

    /**
     *
     * @return Card the first card used at the start of the game
     */
    public Card getStartCard() {
        return startCard;
    }

    /**
     *
     * @param startCard may be used to set the start card
     */
    public void setStartCard(Card startCard) {
        this.startCard = startCard;
    }

    public ArrayList<Card> getPlayedPile() {
        return playedPile;
    }

    public void setPlayedPile(ArrayList<Card> playedPile) {
        this.playedPile = playedPile;
    }

    public boolean isComputerTheWinner() {
        return computerTheWinner;
    }

    public void setComputerTheWinner(boolean computerTheWinner) {
        this.computerTheWinner = computerTheWinner;
    }

    public boolean isHumanTheWinner() {
        return humanTheWinner;
    }

    public void setHumanTheWinner(boolean humanTheWinner) {
        this.humanTheWinner = humanTheWinner;
    }

    public boolean isTie() {
        return isTie;
    }

    public void setTie(boolean tie) {
        isTie = tie;
    }

    public int getComputerCounter() {
        return computerCounter;
    }

    public void setComputerCounter(int computerCounter) {
        this.computerCounter = computerCounter;
    }

    public int getPlayerCounter() {
        return playerCounter;
    }

    public void setPlayerCounter(int playerCounter) {
        this.playerCounter = playerCounter;
    }
}
