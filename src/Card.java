/**
 * This Card class bundles the suit and the face as a single object.
 * There are six special cards determined by their face viz: PICKTWO, PICKTHREE
 * HOLDON, SUSPENSION, GENERALMARKET AND WHOT.
 * Any card that is not a special card is a normal card, and there is no rule on it.
 * the toString method returns the String representation of the card object.
 */
public class Card
{
    private final int face;//an integer that represents the number on the card
    private final Suit suit;//an enumeration that represents the shape on the card
    private boolean isDefendCard;//Computer uses this to check if the PICK TWO/PICK THREE
    // are defended card from the player, which means it won't have to pick two or three for such card
    private boolean cardActionTaken; //True or false depending on whether action has been taken by of
    //the players on a special card.
    //private boolean isStartCard; //If the card is the start card of the game.
    private boolean callCard;//Holds the very first card of the game
    protected Card(Suit suit, int face)
    {
        this.suit = suit;
        this.face = face;
    }

    protected int getFace() {
        return this.face;
    }

    protected Suit getSuit() {
        return this.suit;
    }

    protected boolean isHoldOn() {
        return this.face == 1;
    }

    protected boolean isSuspension() {
        return this.face == 8;
    }

    protected boolean isPickTwo() {
        return this.face == 2;
    }

    protected boolean isGeneralMarket() {
        return this.face == 14;
    }

    protected boolean isPickThree() {
        return this.face == 5;
    }

    protected boolean isWhot() {
        return this.face == 20;
    }

    protected boolean isNormalCard() {
        return  this.face != 1 &&
                this.face != 2 &&
                this.face != 5 &&
                this.face != 8 &&
                this.face != 14 &&
                this.face != 20;
    }

    protected boolean isSpecialCard() {
        return  this.face == 1 ||
                this.face == 2 ||
                this.face == 5 ||
                this.face == 8 ||
                this.face == 14 ||
                this.face == 20;
    }

    protected boolean isDefendCard() {
        return isDefendCard;
    }

    protected void setDefendCard(boolean defendCard) {
        isDefendCard = defendCard;
    }

    protected boolean isCardActionTaken() {
        return cardActionTaken;
    }

    protected void setCardActionTaken(boolean cardActionTaken) {
        this.cardActionTaken = cardActionTaken;
    }

    protected boolean isCallCard() {
        return callCard;
    }

    protected void setCallCard(boolean callCard) {
        this.callCard = callCard;
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
