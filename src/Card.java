
public class Card {
    private int face;
    private String suit;

    public Card(String suit, int face) {
        this.suit = suit;
        this.face = face;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String toString() {
        return String.format("%d of %s", this.face, this.suit);
    }
}
