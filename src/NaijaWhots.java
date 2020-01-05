import java.util.Random;

public class NaijaWhots {
    private final int WHOT_NUMBER = 5;
    private final String WHOT_SUIT = "Whot";
    private final int WHOT_FACE = 20;
    private Random rand = new Random();
    private Card[] cards = new Card[54];

    public NaijaWhots() {
        initialize();
        shuffle();
    }

    private void initialize() {
        int index = 0;
        int[] facesOfCirclesAndTriangles = {1, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14};
        String[] suitsOfCirclesAndTriangles = {"Circle", "Triangle"};
        for (int i = 0; i < suitsOfCirclesAndTriangles.length; i++) {
            for (int j = 0; j < facesOfCirclesAndTriangles.length; j++) {
                Card card = new Card(suitsOfCirclesAndTriangles[i], facesOfCirclesAndTriangles[j]);
                cards[index] = card;
                index++;
            }
        }
        int[] facesOfStarsAndSquares = {1, 2, 3, 5, 7, 10, 11, 13, 14};
        String[] suitsOfStarsAndSquares = {"Star", "Square"};
        for (int i = 0; i < suitsOfStarsAndSquares.length; i++) {
            for (int j = 0; j < facesOfStarsAndSquares.length; j++) {
                Card card = new Card(suitsOfStarsAndSquares[i], facesOfStarsAndSquares[j]);
                cards[index] = card;
                index++;
            }
        }
        int[] facesOfStars = {1, 2, 3, 4, 5, 7, 8};
        for (int i = 0; i < facesOfStars.length; i++) {
            Card card = new Card("Star", facesOfStars[i]);
            cards[index] = card;
            index++;
        }
        for (int i = 0; i < WHOT_NUMBER; i++) {
            Card card = new Card(WHOT_SUIT, WHOT_FACE);
            cards[index] = card;
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
        if (this.cards.length > 1) {
            Card oldCard;
            for (int index = 0; index < this.cards.length; index++) {
                int randomIndex = rand.nextInt(this.cards.length);
                oldCard = this.cards[index];
                this.cards[index] = this.cards[randomIndex];
                this.cards[randomIndex] = oldCard;
            }
        }
    }

    public Card[] getWhots() {
        return this.cards;
    }
}