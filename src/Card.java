
public class Card {
    private int face;
    private String suit;

    public Card(String suit, int face) {
        this.suit = suit;
        this.face = face;
    }

    public String toString() {
        return String.format("%d of %s", this.face, this.suit);
    }
}
