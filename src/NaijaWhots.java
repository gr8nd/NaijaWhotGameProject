import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In this NaijaWhots class, the initialization of the cards to form a pack and the shuffling
 * of the pack are done by the private methods initialise() and shuffle() respectively.
 * There is also a public shuffle method for shuffling the pack once more to avoid the need to instantiate NaijaWhot
 * object.
 * There is also the getPack method that returns an array containing 54 card objects -- the pack
 */
public class NaijaWhots
{
    private final List<Card> pack = new ArrayList<>(54);//The pack as an array containing 54 cards

    public NaijaWhots()
    {
        initializeCards();//The pack is initialised with 54 card objects at the  point of instantiation of NaijaWhot object
        shuffleCards();//After initialising the pack, this private method shuffles the pack to make it ready for use.
    }

    /**
     * The private method initialise() instantiates 54 card objects and adds them to the pack.
     * Each suit has an allowed faces on them as described in the README.md, for instance Whot card only
     * has one face value on it which is 20.
     */
    private void initializeCards()
    {
        int[] facesOfCirclesAndTriangles = {1, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14};
        for (int face : facesOfCirclesAndTriangles) {
            Card circle = new Card(Suit.CIRCLE, face);
            pack.add(circle);
            Card triangle = new Card(Suit.TRIANGLE, face);
            pack.add(triangle);
        }

        int[] facesOfCrossesAndSquares = {1, 2, 3, 5, 7, 10, 11, 13, 14};
        for (int face : facesOfCrossesAndSquares)
        {
            Card cross = new Card(Suit.CROSS, face);
            pack.add(cross);
            Card square = new Card(Suit.SQUARE, face);
            pack.add(square);
        }

        int[] facesOfStars = {1, 2, 3, 4, 5, 7, 8};
        for (int facesOfStar : facesOfStars)
        {
            Card card = new Card(Suit.STAR, facesOfStar);
            pack.add(card);
        }

        int NUM_OF_WHOT = 5; //There will be 5 Whots in our pack
        int WHOT_FACE = 20; //The number on the Whot card is usually 20
        for (int i = 0; i < NUM_OF_WHOT; i++)
        {
            Card card = new Card(Suit.WHOT, WHOT_FACE);
            pack.add(card);
        }
    }

    /**
     * This is a private method that shuffles the pack behind the scene when the NaijaWhot object is instantiated
     *
     */
    private void shuffleCards()
    {
        Collections.shuffle(pack);
    }

    /**
     *
     * @return pack, the initialised and shuffled arrays containing card objects
     */
    public List<Card>  getPack()
    {
        return this.pack;
    }
}