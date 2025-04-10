import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.security.SecureRandom;

/**
 * This class contains the whole logic of
 * the Whot game. Computer and human take turn to play, after playing computer relinquishes control to the human and like
 * wise human. This turn continues until the drawPile runs out or either the computerDrawPile or playerDrawPile runs out
 * thereafter a winner will be decided.
 */
public class GamePlay
{
    private boolean isComputerTurn = false;//Used for relinquishing control between computer and human, initially it is
    //human turn
    private final WhotGame game;
    private Suit wantedSuit;
    private final String CSV_SEPARATOR = ",";
    private final int DEFAULT_DEAL_NUMBER = 6;
    private boolean validDeal = true;//Used in checking if a valid deal number is provided.
    private final List<Card> humanCards;//A list containing all player's cards
    private final List<Card> computerCards;//A list containing all computer's cards
    private Card callCard;//Initially the callCard is the callCard in the WhotGame game class. Thereafter, it takes
    //the most recent card played during the game. Each player's play must be valid if the card played has the same
    // Suit or the face(number) as the callCard. The Whot card can be played at any time except during HOLDON,
    //SUSPENSION, PICKTWO, PICKTHREE or GENERAL MARKET.
    private final SecureRandom rand = new SecureRandom();
    private final String mode;//the game mode, easy or difficult are available
    private final String GAME_MODE_EASY = "Easy";
    private final Human human;
    private final Computer computer;
    private final String GAME_MODE_DIFFICULT = "Difficult";
    private final boolean forceWinner; //If true, it will ensure that there is a winner in the game, draw will not be allowed
    //so the game will run indefinitely until there is a winner.

    /**
     * The GamePlay has two overloaded constructors. This constructor without
     * a 'number' parameter is used when no deal number
     * is provided, it therefore uses the default deal number of 6.
     * @param mode the game mode, easy or difficult are available
     * @param forceWinner if true, run the game till there is a winner
     * @param verbose if true, show a simulated visual display of
     * human cards, but can make the game hard to play when card list
     * is much.
     */
    protected GamePlay(boolean forceWinner, String mode, boolean verbose) throws WhotGameException
    {
        if(!mode.equalsIgnoreCase(GAME_MODE_EASY) && !mode.equalsIgnoreCase(GAME_MODE_DIFFICULT))
        {
            throw new WhotGameException("Game mode can take only one of the two options: '" + GAME_MODE_EASY
                    + "' and '" + GAME_MODE_DIFFICULT + "' ");
        }
        this.forceWinner = forceWinner;
        this.mode = mode;
        this.game = new WhotGame();
        this.humanCards = game.getHumanCardPile();
        this.computerCards = game.getComputerCardPile();
        this.human = new Human(forceWinner, verbose, game, humanCards, this);
        this.computer = new Computer(forceWinner, mode, game, computerCards, this);
        this.deal(DEFAULT_DEAL_NUMBER);
    }

    /**
     * @param mode the game mode, easy or difficult are available
     * @param forceWinner if true, run the game till there is a winner
     * @param verbose if true, show a simulated visual display of
     * human cards, but can make the game hard to play when card list
     * is much.
     * @param number a deal number
     */
    protected GamePlay(boolean forceWinner, String mode, int number, boolean verbose) throws WhotGameException
    {
        if(!mode.equalsIgnoreCase(GAME_MODE_EASY) && !mode.equalsIgnoreCase(GAME_MODE_DIFFICULT))
        {
            throw new WhotGameException("Game mode can take only one of the two options: '" + GAME_MODE_EASY + "' and '" + GAME_MODE_DIFFICULT + "' ");
        }
        this.forceWinner = forceWinner;
        this.mode = mode;
        this.game = new WhotGame();
        this.humanCards = game.getHumanCardPile();
        this.computerCards = game.getComputerCardPile();
        this.human = new Human(forceWinner, verbose, game, humanCards, this);
        this.computer = new Computer(forceWinner, mode, game, computerCards, this);
        this.deal(number);
    }

    /**
     * previousCard is initially the startCard in the WhotGame
     * @param number a deal number that is provided, if no valid deal number is provided a
     *  WhotGameException is thrown and appropriate message is issued to the user.
     */
    private void deal(int number)
    {
        try
        {
            game.deal(number, mode);
            callCard = game.getCallCard();
            callCard.setCallCard(true);
            System.out.println("Your initial Pile count: " + game.getHumanCardPile().size());
            System.out.println("Computer's initial Pile count: " + game.getComputerCardPile().size());
            System.out.println("Initial Draw Pile count: " + game.getDrawPile().size());
        } catch (WhotGameException e)
        {
            validDeal = false;
            System.out.println(e.getMessage());
        }
    }

    //An API that should be called to start the game
    protected void start()
    {
        if (validDeal)
        {
            startCardPlay();
        }
    }

    private void startCardPlay()
    {
        System.out.println("The call card is:");
        System.out.println(callCard.toString());
        game.play(callCard, forceWinner);

        startGame();
    }

    /**
     * The start method starts the game,
     * the game runs until
     * there is a winner or a tie, rotating turn between computer and human.
     */
    private void startGame()
    {
        while (!game.isHumanTheWinner() &&
                !game.isComputerTheWinner() &&
                !game.isTie())
        {
            int totalCards = game.getComputerCardPile().size() + game.getHumanCardPile().size() +
                    game.getDrawPile().size() + game.getPlayedPile().size();
            //make sure total number of cards remain 54 and none is lost
            //due to bugs during game play
            assert totalCards == 54;
            if (isComputerTurn)
            {
                computer.play();
            } else
            {
                human.play();
            }
        }
        int totalCards = game.getComputerCardPile().size() + game.getHumanCardPile().size() +
                game.getDrawPile().size() + game.getPlayedPile().size();
        //make sure total number of cards remain 54 and none is lost
        //due to bugs at the end of the game play
        assert totalCards == 54;
        displayWinner();
        String csv =  buildCSV(game.getHumanPlayedPile(), game.getComputerPlayedPile());
        writeToCSV(csv);
    }

    private void displayWinner()
    {
        System.out.println("Your number of cards' left: " + humanCards.size());
        System.out.println("Computer's number of cards' left: " + computerCards.size());
        game.countHumanCards();
        game.countComputerCards();
        int a = Math.min(game.getHumanCardsCount(), game.getComputerCardsCount());
        if(!forceWinner && a != 0)
        {
            System.out.println("Your cards' face count: " + game.getHumanCardsCount());
            System.out.println("Computer's cards' face count: " + game.getComputerCardsCount());
        }

        if(game.isHumanTheWinner())
        {
            System.out.println("Game Over!!!\nYou win!");
        }else if(game.isComputerTheWinner())
        {
            System.out.println("Game Over!!!\nYou lose!");
        }else
        {
            System.out.println("Game Over!!!\nIt's a tie!");
        }
    }

    /**
     * Build the current statistics of the game as comma-separated values (.csv)
     * @param humanPlayedPile a list containing all the computer cards computer has played
     *     //during the course of the game.
     * @param computerPlayedPile a list containing all the player cards the human player
     *     //has played during the course of the game.
     */
    private String buildCSV(List<Card> humanPlayedPile, List<Card> computerPlayedPile)
    {
        String s = "id,humansuit,humanface,humantype,humanaction,humandefendcard,humanactiontaken," +
                "computersuit,computerface,computertype," +
                "computeraction,computerdefendcard," +
                "computeractiontaken,mode,forcewinner,humanwinner,computerwinner,tie";
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        builder.append("\n");
        long maximum = (Math.max(humanPlayedPile.size(), computerPlayedPile.size()));
        for (int i = 0; i < maximum; i++)
        {
            builder.append((i + 1));
            if(i < humanPlayedPile.size())
            {
                Card humanCard = humanPlayedPile.get(i);
                builder.append(CSV_SEPARATOR);
                builder.append(humanCard.getSuit());
                builder.append(CSV_SEPARATOR);
                builder.append(humanCard.getFace());
                builder.append(CSV_SEPARATOR);
                builder.append(humanCard.isNormalCard());
                builder.append(CSV_SEPARATOR);
                builder.append(getAction(humanCard));
                builder.append(CSV_SEPARATOR);
                builder.append(humanCard.isDefendCard());
                builder.append(CSV_SEPARATOR);
                builder.append(humanCard.isCardActionTaken());
            }else
            {
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append(0);
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append("None");
            }
            if(i < computerPlayedPile.size())
            {
                Card computerCard = computerPlayedPile.get(i);
                builder.append(CSV_SEPARATOR);
                builder.append(computerCard.getSuit());
                builder.append(CSV_SEPARATOR);
                builder.append(computerCard.getFace());
                builder.append(CSV_SEPARATOR);
                builder.append(computerCard.isNormalCard());
                builder.append(CSV_SEPARATOR);
                builder.append(getAction(computerCard));
                builder.append(CSV_SEPARATOR);
                builder.append(computerCard.isDefendCard());
                builder.append(CSV_SEPARATOR);
                builder.append(computerCard.isCardActionTaken());
            }else
            {
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append(0);
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append("None");
                builder.append(CSV_SEPARATOR);
                builder.append("None");
            }

            builder.append(CSV_SEPARATOR);
            builder.append(mode);
            builder.append(CSV_SEPARATOR);
            builder.append(forceWinner);
            builder.append(CSV_SEPARATOR);
            builder.append(game.isHumanTheWinner());
            builder.append(CSV_SEPARATOR);
            builder.append(game.isComputerTheWinner());
            builder.append(CSV_SEPARATOR);
            builder.append(game.isTie());
            builder.append("\n\n");
        }
        return builder.toString();
    }

    /**
     * Write the current statistics of the game to file as comma-separated values (.csv)
     * @param data the csv file to write to file
     */
    private void writeToCSV(String data)
    {
        long id = System.currentTimeMillis();
        String name = "whot" + id + ".csv";
        String fileName = System.getProperty("user.dir") + File.separator + "whots";
        File file = new File(fileName);
        if(!file.exists())
        {
            file.mkdir();
        }
        try
        {
            OutputStream fileOutputStream = new FileOutputStream(new
                    File(String.valueOf(file), name));
            fileOutputStream.write(data.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("Game statistics saved at: " + file.getAbsolutePath());
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /***
     * Get the action which a card stands for
     * @param card the card to find its action
     * @return a string representing the card's action.
     */
    private String getAction(Card card)
    {
        if(card.isWhot())
        {
            return "Whot";
        }else if(card.isPickTwo())
        {
            return  "Pick two";
        }else if(card.isPickThree())
        {
            return "Pick three";
        }else if(card.isGeneralMarket())
        {
            return "General market";
        }else if(card.isHoldOn())
        {
            return "Hold on";
        }else if(card.isSuspension())
        {
            return "Suspension";
        }
        return "None";
    }

    protected Card getCallCard() {
        return callCard;
    }

    protected void setCallCard(Card callCard) {
        this.callCard = callCard;
    }

    protected Suit getWantedSuit() {
        return wantedSuit;
    }

    protected void setWantedSuit(Suit wantedSuit) {
        this.wantedSuit = wantedSuit;
    }

    protected boolean isIsComputerTurn() {
        return isComputerTurn;
    }

    protected void setIsComputerTurn(boolean isComputerTurn) {
        this.isComputerTurn = isComputerTurn;
    }

}
