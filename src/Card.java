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
    private boolean isDefendCard;//Computer uses this to check if the PICK TWO/PICK THREE are defend card from the player
    private boolean isPlayerWhot;//Computer uses this to check if the whot card is player's.
    public Card(Suit suit, int face)
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
        return this.face != 1 &&
                this.face != 2 &&
                this.face != 5 &&
                this.face != 8 &&
                this.face != 14 &&
                this.face != 20;
    }

    public boolean isDefendCard() {
        return isDefendCard;
    }

    public void setDefendCard(boolean defendCard) {
        isDefendCard = defendCard;
    }

    public boolean isPlayerWhot() {
        return isPlayerWhot;
    }

    public void setPlayerWhot(boolean playerWhot) {
        isPlayerWhot = playerWhot;
    }

    public String toString()
    {
        String display = ". . . . . . . . .\n" +
                         ".               .\n" +
                         ".               .\n" +
        String.format("  %d %s          \n", this.face, this.suit) +
                         ".               .\n" +
                         ".               .\n" +
                         ". . . . . . . . .\n";

        return display;
    }
}
