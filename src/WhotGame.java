import java.util.ArrayList;
import java.util.Collections;

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
    private boolean computerIsTheWinner = false;
    private boolean humanIsTheWinner = false;
    private boolean isTie = false;
    private ArrayList<Card> drawPile;//this is where computer and player can draw a card from when they run out of a fitting card or
    //when instructed to do so by the game rule such as the GENERAL MARKET.
    private ArrayList<Card> computerCardPile;//a list containing all the computer cards
    private ArrayList<Card> humanCardPile;//a list containing all the player cards
    private int computerCounter = 0;//keeps record of the computer's face count when scoring by counting faces.
    private int playerCounter = 0;//keeps record of the player's face count when scoring by counting faces.
    private ArrayList<Card> playedPile;
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
        startCard = drawPile.getFirst();//get the first card as the start card
    }

    /**
     *
     * @param number the total number of cards each player will receive at the start of the game, usually 6 but can
     * be any number as well.
     * @throws WhotGameException an exception that will be thrown when an invalid deal number is provided
     */
    public void deal(int number) throws WhotGameException
    {
        boolean isValidDeal = number >= 2 && number <= 27 && number % 2 == 0;
        if (isValidDeal)
        {
            for (int index = 0; index < number; index++)
            {
                computerCardPile.add(drawPile.removeFirst());//adds the first card in the drawPile to the computerPile
                //and removes it afterward
                humanCardPile.add(drawPile.removeFirst());//adds the first card in the drawPile to the playerPile and
                //removes it afterward
            }
        } else if (number < 0) {
            throw new WhotGameException("You cannot deal a negative number of cards");
        } else if (number > 27) {
            throw new WhotGameException("You can only deal between 2 to 27 cards");
        } else if (number % 2 != 0) {
            throw new WhotGameException("You cannot deal an odd number of cards");
        } else {
            throw new WhotGameException("You can only deal 2 or more cards");
        }
    }

    /**
     *
     * @param card the card object that is played, the method attempts to remove a played card from any of the card
     * piles it finds it.
     */
    public void play(Card card, boolean forceWinner)
    {
        playedPile.add(card);
        computerCardPile.remove(card);
        humanCardPile.remove(card);
        drawPile.remove(card); //Needed only for the start card
        if (computerCardPile.size() == 1 || humanCardPile.size() == 1)
        {
            System.out.println("Last card!");
        }
        rule(card);
        checkWinner(forceWinner);
    }

    /**
     * This draws a card from the draw pile and adds it to the computer pile if the draw contains at least one card
     * else there must have been a winner, it therefor sets thereIsAWinner to true
     */
    public void computerDraw(boolean forceWinner)
    {
        computerCardPile.add(drawPile.removeFirst());
        checkWinner(forceWinner);
    }
    /**
     * This draws a card from the draw pile and adds it to the computer pile if the draw contains at least one card
     * else there must have been a winner, it therefor sets thereIsAWinner to true
     */
    public void playerDraw(boolean forceWinner)
    {
        humanCardPile.add(drawPile.removeFirst());
        checkWinner(forceWinner);
    }

    /**
     *
     * @param card a card to be played or that has been played. The method checks whether the card is one of the six
     * special cards and issues the appropriate rule on the card
     */
    public void rule(Card card)
    {
        if (card.isHoldOn()) {
            System.out.println("Hold on!");
        } else if (card.isPickTwo()) {
            System.out.println("Pick two!");
        } else if (card.isPickThree()) {
            System.out.println("Pick three!");
        } else if (card.isSuspension()) {
            System.out.println("Suspension!");
        } else if (card.isWhot()) {
            System.out.println("Whot! Select a card you need >> ");
        } else if (card.isGeneralMarket()) {
            System.out.println("General market!");
        }
    }

    /**
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
                drawPile.addAll(playedPile);
                playedPile.clear();
            }else
            {
                countHumanCards();
                countComputerCards();
                if (computerCounter > playerCounter)
                {
                    humanIsTheWinner = true;
                } else if(playerCounter > computerCounter)
                {
                    computerIsTheWinner = true;
                }else
                {
                    isTie = true;
                }
            }
        }else
        {
            if(computerCardPile.isEmpty())
            {
                computerIsTheWinner = true;
            }else if(humanCardPile.isEmpty())
            {
                humanIsTheWinner = true;
            }
        }

    }
    /***
     * Count the face values of human cards after the draw pile has been exhausted.
     */
    public void countHumanCards()
    {
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
        for (Card card : computerCardPile)
        {
            if (card.getSuit().equals(Suit.STAR))
            {
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

    public boolean isComputerIsTheWinner() {
        return computerIsTheWinner;
    }

    public void setComputerIsTheWinner(boolean computerIsTheWinner) {
        this.computerIsTheWinner = computerIsTheWinner;
    }

    public boolean isHumanIsTheWinner() {
        return humanIsTheWinner;
    }

    public void setHumanIsTheWinner(boolean humanIsTheWinner) {
        this.humanIsTheWinner = humanIsTheWinner;
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
