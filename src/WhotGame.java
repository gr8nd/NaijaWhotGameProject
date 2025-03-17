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
    private boolean thereIsAWinner = false;
    private ArrayList<Card> drawPile;//this is where computer and player can draw a card from when they run out of a fitting card or
    //when instructed to do so by the game rule such as the GENERAL MARKET.
    private ArrayList<Card> computerCardPile;//a list containing all the computer cards
    private ArrayList<Card> playerCardPile;//a list containing all the player cards
    private int computerCounter = 0;//keeps record of the computer's face count when scoring by counting faces.
    private int playerCounter = 0;//keeps record of the player's face count when scoring by counting faces.

    private Card startCard;//This is first card that is displayed when the game starts

    public WhotGame() {
        NaijaWhots whots = new NaijaWhots();//creates a NaijaWhots object
        computerCardPile = new ArrayList<>();
        playerCardPile = new ArrayList<>();
        drawPile = new ArrayList<>();
        Card[] pack = new Card[54];
        pack = whots.getPack().toArray(pack);//gets the initialised and shuffled pack from the NaijaWhots class
        Collections.addAll(drawPile, pack);
        startCard = drawPile.remove(0);//get the first card as the start card and remove it thereafter
    }

    /**
     *
     * @param number the total number of cards each player will receive at the start of the game, usually 6 but can
     * be any number as well.
     * @throws WhotGameException an exception that will be thrown when an invalid deal number is provided
     */
    public void deal(int number) throws WhotGameException {
        boolean isValidDeal = number >= 2 && number <= 27 && number % 2 == 0;
        if (isValidDeal) {
            for (int index = 0; index < number; index++) {
                computerCardPile.add(drawPile.remove(0));//adds the first card in the drawPile to the computerPile
                //and removes it afterward
                playerCardPile.add(drawPile.remove(0));//adds the first card in the drawPile to the playerPile and
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
    public void play(Card card) {
        computerCardPile.remove(card);
        playerCardPile.remove(card);
    }

    /**
     * This draws a card from the draw pile and adds it to the computer pile if the draw contains at least one card
     * else there must have been a winner, it therefor sets thereIsAWinner to true
     */
    public void computerDraw() {
        thereIsAWinner = drawPile.isEmpty() || !computerCardPile.add(drawPile.remove(0));
    }
    /**
     * This draws a card from the draw pile and adds it to the computer pile if the draw contains at least one card
     * else there must have been a winner, it therefor sets thereIsAWinner to true
     */
    public void playerDraw() {
        thereIsAWinner = drawPile.isEmpty() || !playerCardPile.add(drawPile.remove(0));
    }

    /**
     *
     * @param card a card to be played or that has been played. The method checks whether the card is one of the six
     * special cards and issues the appropriate rule on the card
     */
    public void rule(Card card) {
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
    public void checkWinner(boolean forceWinner) {
        if ((!forceWinner && drawPile.isEmpty()) || computerCardPile.isEmpty() || playerCardPile.isEmpty()) {
            if (computerCardPile.isEmpty()) {
                thereIsAWinner = true;
            } else if (playerCardPile.isEmpty()) {
                thereIsAWinner = true;
            } else {
                for (Card card : computerCardPile) {
                    if (card.getSuit().equals(Suit.STAR)) {
                        computerCounter += card.getFace() * 2;
                    } else {
                        computerCounter += card.getFace();
                    }

                }
                for (Card card : playerCardPile) {
                    if (card.getSuit().equals(Suit.STAR)) {
                        playerCounter += card.getFace() * 2;
                    } else {
                        playerCounter += card.getFace();
                    }

                }
                if (computerCounter > playerCounter) {
                    thereIsAWinner = true;
                } else if (computerCounter < playerCounter) {
                    thereIsAWinner = true;
                } else {
                    thereIsAWinner = true;
                }
            }

        }
    }

    /**
     *
     * @return boolean that indicates whether there is a winner or not
     */
    public boolean thereIsAWinner() {
        return thereIsAWinner;
    }

    /**
     *
     * @param thereIsAWinner boolean used to indicate whether there is winner or not
     */
    public void setThereIsAWinner(boolean thereIsAWinner) {
        this.thereIsAWinner = thereIsAWinner;
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
    public ArrayList<Card> getPlayerCardPile() {
        return playerCardPile;
    }

    /**
     *
     * @param playerCardPile list containing player's cards
     */
    public void setPlayerCardPile(ArrayList<Card> playerCardPile) {
        this.playerCardPile = playerCardPile;
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
}
