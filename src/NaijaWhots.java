import java.security.SecureRandom;

/**
 * In this NaijaWhots class, the initialization of the cards to form a pack and the shuffling
 * of the pack are done by the private methods initialise() and shuffle() respectively.
 * There is also a public shuffle method for shuffling the pack once more to avoid the need to instantiate NaijaWhot
 * object.
 * There is also the getPack method that returns an array containing 54 card objects -- the pack
 */
public class NaijaWhots {
    private final int WHOT_NUMBER = 5;//There will be 5 Whots in our pack
    private final int WHOT_FACE = 20;//The number on the Whot card is usually 20
    private SecureRandom rand = new SecureRandom();//Ensures that the random number generated is truly unpredictable
    private Card[] pack = new Card[54];//The pack as an array containing 54 cards

    public NaijaWhots() {
        initialize();//The pack is initialise with 54 card objects at the  point of instantiation of NaijaWhot object
        shuffle();//After initialising the pack, this private method shuffle the pack to make it ready for use.
    }

    /**
     * The private method initialise() instantiates 54 card objects and adds them to the pack.
     * Each suit has an allowed faces on them as described in the ReadMe.md, for instance Whot card only
     * has one face value on it which is 20.
     */
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

    /**
     * This is a private method that shuffles the pack behind the scene when the NaijaWhot object is instantiated
     * @return return the shuffled pack
     */
    private void shuffle() {
        if (this.pack.length > 1) {
            Card oldCard;//holds a temporary card object used for swapping
            for (int index = 0; index < this.pack.length; index++) {
                int randomIndex = rand.nextInt(this.pack.length);//generates a random integer in the range 0 - 53
                oldCard = this.pack[index];//oldCard holds the card at the index
                //Then swap the card at the index with the card at the randomly generated integer index
                this.pack[index] = this.pack[randomIndex];
                this.pack[randomIndex] = oldCard;
            }
        }
    }

    /**
     *
     * @param cards the pack to be shuffled, an array containing card objects
     * @return return the shuffled pack
     */
    public Card[] shuffle(Card[] cards) {
        if (cards.length > 1) {
            Card oldCard;//holds a temporary card object used for swapping
            for (int index = 0; index < cards.length; index++) {
                int randomIndex = rand.nextInt(cards.length + 1);//generates a random integer in the range 0 - 53
                oldCard = cards[index];//oldCard holds the card at the index
                //Then swap the card at the index with the card at the randomly generated integer index
                cards[index] = cards[randomIndex];
                cards[randomIndex] = oldCard;
            }
        }
        return cards;
    }

    /**
     *
     * @return pack, the initialised and shuffled arrays containing card objects
     */
    public Card[] getPack() {
        return this.pack;
    }
}