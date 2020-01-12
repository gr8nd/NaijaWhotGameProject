
public class Card {
    private int face;
    private Suit suit;

    public Card(Suit suit, int face) {
        this.suit = suit;
        this.face = face;
    }
    public int getFace() {
        return face;
    }
    public Suit getSuit() {
        return suit;
    }

    public String toString() {
        return String.format("%d of %s", this.face, this.suit);
    }
}
