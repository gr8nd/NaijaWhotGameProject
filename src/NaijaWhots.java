import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In this NaijaWhots class, the initialization of the cards to form a pack and the shuffling
 * of the pack are done by the private methods initialise() and shuffleCards() respectively.
 * There is also the getPack method that returns an array containing 54 card objects -- the pack
 */
public class NaijaWhots
{
    private final List<Card> deck = new ArrayList<>(54);//The deck as an array containing 54 cards

    protected NaijaWhots()
    {
        initializeCards();//The deck is initialised with 54 card objects at the  point of instantiation of NaijaWhot object
        shuffleCards();//After initialising the deck, this private method shuffles the deck to make it ready for use.
    }

    /**
     * The private method initialise() instantiates 54 card objects and adds them to the deck.
     * Each suit has an allowed faces on them as described in the README.md, for instance Whot card only
     * has one face value on it which is 20.
     */
    private void initializeCards()
    {
        //Initialise Circle and Triangle together because they have the same number of
        //faces
        int[] facesOfCircleAndTriangle = {1, 2, 3, 4, 5, 7, 8, 10, 11, 12, 13, 14};
        for (int face : facesOfCircleAndTriangle) {
            Card circle = new Card(Suit.CIRCLE, face);
            deck.add(circle);
            Card triangle = new Card(Suit.TRIANGLE, face);
            deck.add(triangle);
        }

        //Initialise Cross and Square together because they have the same number of
        //faces
        int[] facesOfCrosAndSquare = {1, 2, 3, 5, 7, 10, 11, 13, 14};
        for (int face : facesOfCrosAndSquare)
        {
            Card cross = new Card(Suit.CROSS, face);
            deck.add(cross);
            Card square = new Card(Suit.SQUARE, face);
            deck.add(square);
        }

        //Star is initialised alone because it has its own unique number of faces
        int[] facesOfStar = {1, 2, 3, 4, 5, 7, 8};
        for (int faceOfStar : facesOfStar)
        {
            Card star = new Card(Suit.STAR, faceOfStar);
            deck.add(star);
        }

        //Initialise the Whot! card alone just as Star
        int NUM_OF_WHOT = 5; //There will be 5 Whots in our deck
        int WHOT_FACE = 20; //The number on the Whot card is usually 20
        for (int i = 0; i < NUM_OF_WHOT; i++)
        {
            Card whot = new Card(Suit.WHOT, WHOT_FACE);
            deck.add(whot);
        }
    }

    /**
     * This is a private method that shuffles the pack behind the scene when the NaijaWhot object is instantiated
     *
     */
    private void shuffleCards()
    {
        Collections.shuffle(deck);
    }

    /**
     *
     * @return pack, the initialised and shuffled arrays containing card objects
     */
    protected List<Card> getDeck()
    {
        return this.deck;
    }
}