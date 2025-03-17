import java.security.SecureRandom;

/**
 * In this NaijaWhots class, the initialization of the cards to form a pack and the shuffling
 * of the pack are done by the private methods initialise() and shuffle() respectively.
 * There is also a public shuffle method for shuffling the pack once more to avoid the need to instantiate NaijaWhot
 * object.
 * There is also the getPack method that returns an array containing 54 card objects -- the pack
 */
public class NaijaWhots {
    private final SecureRandom rand = new SecureRandom();//Ensures that the random number generated is truly unpredictable
    private final Card[] pack = new Card[54];//The pack as an array containing 54 cards

    public NaijaWhots() {
        initializeCards();//The pack is initialised with 54 card objects at the  point of instantiation of NaijaWhot object
        shuffleCards();//After initialising the pack, this private method shuffles the pack to make it ready for use.
    }

    /**
     * The private method initialise() instantiates 54 card objects and adds them to the pack.
     * Each suit has an allowed faces on them as described in the README.md, for instance Whot card only
     * has one face value on it which is 20.
     */
    private void initializeCards() {
        int index = 0;
        int[] facesOfCirclesAndTriangles = {1, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14};
        for (int face : facesOfCirclesAndTriangles) {
            Card circle = new Card(Suit.CIRCLE, face);
            index++;
            pack[index] = circle;
            Card triangle = new Card(Suit.TRIANGLE, face);
            pack[index] = triangle;
            index++;
        }

        int[] facesOfCrossesAndSquares = {1, 2, 3, 5, 7, 10, 11, 13, 14};
        for (int face : facesOfCrossesAndSquares) {
            Card cross = new Card(Suit.CROSS, face);
            index++;
            pack[index] = cross;
            Card square = new Card(Suit.SQUARE, face);
            pack[index] = square;
            index++;
        }

        int[] facesOfStars = {1, 2, 3, 4, 5, 7, 8};
        for (int facesOfStar : facesOfStars) {
            Card card = new Card(Suit.STAR, facesOfStar);
            pack[index] = card;
            index++;
        }

        int NUM_OF_WHOT = 5; //There will be 5 Whots in our pack
        int WHOT_FACE = 20; //The number on the Whot card is usually 20
        for (int i = 0; i < NUM_OF_WHOT; i++) {
            Card card = new Card(Suit.WHOT, WHOT_FACE);
            pack[index] = card;
            index++;
        }
    }

    /**
     * This is a private method that shuffles the pack behind the scene when the NaijaWhot object is instantiated
     *
     */
    private void shuffleCards() {
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
     * @return pack, the initialised and shuffled arrays containing card objects
     */
    public Card[] getPack() {
        return this.pack;
    }
}