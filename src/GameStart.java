/**
 * The GameStart class is where the main method resides, the game is started from this class by calling the start method of
 *  GamePlay class.
 */
public class GameStart
{
    public static void main(String[] args)
    {
        GamePlay game = new GamePlay(false);
        game.start();
    }
}