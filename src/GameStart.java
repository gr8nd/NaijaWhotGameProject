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
        System.out.print("""
                Welcome to Whot! Game!!!
                The Nigerian national game.
                This console game is a fully functional Naija Whot! game \s
                played via CLI (Command Line Interface).
                This software is licensed by GNU GENERAL PUBLIC LICENSE.
                Hit Enter key to continue... >>""");
        input.nextLine();
        System.out.println("Do you want to see a simulated visual display of your cards Y/N?");
        String verbose = input.nextLine();
        System.out.println("Select game mode: Easy or Difficult?");
        String mode = input.nextLine();
        System.out.println("Play until there is a winner (i.e. no tie) Y/N?");
        String forceWinner = input.nextLine();
        System.out.println("Enter the deal number (between 2 and 26): ");
        int dealNum = input.nextInt();
        try
        {
            GamePlay game = new GamePlay(forceWinner.equalsIgnoreCase("Y"),
                    mode, dealNum, verbose.equalsIgnoreCase("Y"));
            game.start();
        } catch (WhotGameException e)
        {
            System.out.println(e.getMessage());
        }
    }
}