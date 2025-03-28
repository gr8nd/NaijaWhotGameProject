import java.util.Scanner;

/**
 * The GameStart class is where the main method resides, the game is started from this class by calling the start method of
 *  GamePlay class.
 */
public class GameStart
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Select game mode: Easy or Difficult?");
        String mode = input.nextLine();
        System.out.println("Play until there is a winner (i.e. no tie) Y/N?");
        String forceWinner = input.nextLine();
        GamePlay game;
        try {
            game = new GamePlay(forceWinner.equalsIgnoreCase("Y"),  mode);
            game.start();
        } catch (WhotGameException e) {
            System.out.println(e.getMessage());
        }
    }
}