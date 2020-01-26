import java.security.SecureRandom;

public class NaijaWhots {
    private final int WHOT_NUMBER = 5;
    private final int WHOT_FACE = 20;
    private SecureRandom rand = new SecureRandom();
    private Card[] pack = new Card[54];

    public NaijaWhots() {
        initialize();
        shuffle();
    }

    private void initialize() {
        int index = 0;
        int[] facesOfCirclesAndTriangles = {1, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14};
        Suit[] suitsOfCirclesAndTriangles = {Suit.CIRCLE, Suit.TRIANGLE};
        for (int i = 0; i < suitsOfCirclesAndTriangles.length; i++) {
            for (int j = 0; j < facesOfCirclesAndTriangles.length; j++) {
                Card card = new Card(suitsOfCirclesAndTriangles[i], facesOfCirclesAndTriangles[j]);
                pack[index] = card;
                index++;
            }
        }
        int[] facesOfCrossesAndSquares = {1, 2, 3, 5, 7, 10, 11, 13, 14};
        Suit[] suitsOfCrossesAndSquares = {Suit.CROSS, Suit.SQUARE};
        for (int i = 0; i < suitsOfCrossesAndSquares.length; i++) {
            for (int j = 0; j < facesOfCrossesAndSquares.length; j++) {
                Card card = new Card(suitsOfCrossesAndSquares[i], facesOfCrossesAndSquares[j]);
                pack[index] = card;
                index++;
            }
        }
        int[] facesOfStars = {1, 2, 3, 4, 5, 7, 8};
        for (int i = 0; i < facesOfStars.length; i++) {
            Card card = new Card(Suit.STAR, facesOfStars[i]);
            pack[index] = card;
            index++;
        }
        for (int i = 0; i < WHOT_NUMBER; i++) {
            Card card = new Card(Suit.WHOT, WHOT_FACE);
            pack[index] = card;
            index++;
        }
    }

    public Card[] shuffle(Card[] cards) {
        if (cards.length > 1) {
            Card oldCard;
            for (int index = 0; index < cards.length; index++) {
                int randomIndex = rand.nextInt(cards.length + 1);
                oldCard = cards[index];
                cards[index] = cards[randomIndex];
                cards[randomIndex] = oldCard;
            }
        }
        return cards;
    }

    private void shuffle() {
        if (this.pack.length > 1) {
            Card oldCard;
            for (int index = 0; index < this.pack.length; index++) {
                int randomIndex = rand.nextInt(this.pack.length);
                oldCard = this.pack[index];
                this.pack[index] = this.pack[randomIndex];
                this.pack[randomIndex] = oldCard;
            }
        }
    }

    public Card[] getPack() {
        return this.pack;
    }
}