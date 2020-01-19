
public class Card {
    private int face;
    private Suit suit;
    private boolean isHoldOn;
    private boolean isSuspension;
    private boolean isPickTwo;
    private boolean isGeneralMarket;
    private boolean isPickThree;
    private boolean isWhot;
    private boolean isNormalCard;

    public Card(Suit suit, int face) {
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
                this.face != 20 &&
                this.face != 14;
    }

    public String toString() {
        return String.format("%d of %s", this.face, this.suit);
    }
}
