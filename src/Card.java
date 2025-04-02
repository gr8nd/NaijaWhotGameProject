/**
 * This Card class bundles the suit and the face as a single object.
 * There are six special cards determined by their face viz: PICKTWO, PICKTHREE
 * HOLDON, SUSPENSION, GENERALMARKET AND WHOT.
 * Any card that is not a special card is a normal card, and there is no rule on it.
 * the toString method returns the String representation of the card object.
 */
public class Card {
    private final int face;//an integer that represents the number on the card
    private final Suit suit;//an enumeration that represents the shape on the card
    private boolean isDefendCard;//Computer uses this to check if the PICK TWO/PICK THREE
    // are defended card from the player, which means it won't have to pick two or three for such card
    private boolean cardActionTaken; //True or false depending on whether action has been taken by of
    //the players on a special card.
    //private boolean isStartCard; //If the card is the start card of the game.
    private boolean isFirstCard;//Holds the very first card of the game
    protected Card(Suit suit, int face)
    {
        this.suit = suit;
        this.face = face;
    }
    public int getFace() {
        return this.face;
    }
    public Suit getSuit() {
        return this.suit;
    }

    public boolean isHoldOn() {
        return this.face == 1;
    }

    public boolean isSuspension() {
        return this.face == 8;
    }

    public boolean isPickTwo() {
        return this.face == 2;
    }

    public boolean isGeneralMarket() {
        return this.face == 14;
    }

    public boolean isPickThree() {
        return this.face == 5;
    }

    public boolean isWhot() {
        return this.face == 20;
    }

    public boolean isNormalCard() {
        return  this.face != 1 &&
                this.face != 2 &&
                this.face != 5 &&
                this.face != 8 &&
                this.face != 14 &&
                this.face != 20;
    }

    public boolean isSpecialCard() {
        return  this.face == 1 ||
                this.face == 2 ||
                this.face == 5 ||
                this.face == 8 ||
                this.face == 14 ||
                this.face == 20;
    }

    public boolean isDefendCard() {
        return isDefendCard;
    }

    public void setDefendCard(boolean defendCard) {
        isDefendCard = defendCard;
    }

    public boolean isCardActionTaken() {
        return cardActionTaken;
    }

    public void setCardActionTaken(boolean cardActionTaken) {
        this.cardActionTaken = cardActionTaken;
    }

    public boolean isFirstCard() {
        return isFirstCard;
    }

    public void setFirstCard(boolean firstCard) {
        isFirstCard = firstCard;
    }

    @Override
    public String toString()
    {

        return           ". . . . . . . . .\n" +
                         ".               .\n" +
                         ".               .\n" +
        String.format("  %d %s          \n", this.face, this.suit) +
                         ".               .\n" +
                         ".               .\n" +
                         ". . . . . . . . .\n";
    }
}
