import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.security.SecureRandom;

/**
 * This class has the highest number of lines and also the most difficult to implement. It contains the whole logic of
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
    private boolean validDeal = true;//Used in checking if a valid deal number is provided.
    private final List<Card> humanCards;//A list containing all player's cards
    private final List<Card> computerCards;//A list containing all computer's cards
    private Card previousCard;//Initially the previousCard is the startCard in the WhotGame game class. Thereafter, it takes
    //the most recent card played during the game. Each player's play must be valid if the card played has the same
    // Suit or the face(number) as the previousCard. The Whot card can be played at any time except during HOLDON,
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
     * a parameter is used when no deal number
     * is provided, it therefore uses the default deal number of 6.
     * @param mode the game mode, easy or difficult are available
     * @param forceWinner if true, run the game till there is a winner
     */
    public GamePlay(boolean forceWinner, String mode, boolean verbose) throws WhotGameException
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
        this.human = new Human(forceWinner, verbose, this.game, this.humanCards, this);
        this.computer = new Computer(forceWinner, mode, this.game, this.computerCards, this);
        int DEFAULT_DEAL_NUMBER = 6;
        this.deal(DEFAULT_DEAL_NUMBER);
    }

    /**
     * @param number a deal number
     */
    public GamePlay(boolean forceWinner, String mode, int number, boolean verbose) throws WhotGameException
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
        this.human = new Human(forceWinner, verbose, this.game, this.humanCards, this);
        this.computer = new Computer(forceWinner, mode, this.game, this.computerCards, this);
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
            previousCard = game.getStartCard();
            previousCard.setFirstCard(true);
            System.out.println("Your initial Pile count: " + game.getHumanCardPile().size());
            System.out.println("Computer's initial Pile count: " + game.getComputerCardPile().size());
            System.out.println("Initial Draw Pile count: " + game.getDrawPile().size());
        } catch (WhotGameException e)
        {
            validDeal = false;
            System.out.println(e.getMessage());
        }
    }

    public void start()
    {
        if (validDeal)
        {
            startCardPlay();
        }
    }

    private void startCardPlay()
    {
        System.out.println("The start card is:");
        System.out.println(previousCard.toString());
        game.play(previousCard, forceWinner);

        startGame();
    }

    /**
     * The start method starts the game,
     * the game runs until
     * there is a winner, rotating turn between computer and human.
     */
    private void startGame()
    {
        while (!game.isHumanTheWinner() &&
                !game.isComputerTheWinner() &&
                !game.isTie())
        {
            if (isComputerTurn)
            {
                computer.play();
            } else
            {
                human.play();
            }
        }

        System.out.println("Your number of cards' left: " + humanCards.size());
        System.out.println("Computer's number of cards' left: " + computerCards.size());
        if(!forceWinner)
        {
            game.countComputerCards();
            game.countHumanCards();
            System.out.println("Your cards' face count: " + game.getPlayerCounter());
            System.out.println("Computer's cards' face count: " + game.getComputerCounter());
        }

        if(game.isHumanTheWinner())
        {
            System.out.println("Game Over!!!\nYou win!");
        }else if(game.isComputerTheWinner())
        {
            System.out.println("Game Over!!!\nYou lose!");
        }else
        {
            System.out.println("Game Over!!!\nIt is a tie!");
        }
        this.writeToCSV(game.getHumanPlayedPile(), game.getComputerPlayedPile());
    }

    /**
     * Write the current statistics of the game to file as comma-separated values (.csv)
     * @param humanPlayedPile a list containing all the computer cards computer has played
     *     //during the course of the game.
     * @param computerPlayedPile a list containing all the player cards the human player
     *     //has played during the course of the game.
     */
    private void writeToCSV(List<Card> humanPlayedPile, List<Card> computerPlayedPile)
    {
        String id = String.valueOf(System.currentTimeMillis());
        String name = "whot" + id + ".csv";
        String fileName = System.getProperty("user.dir") + File.separator + name;
        File file = new File(fileName);
        if(!file.exists())
        {
            file.mkdir();
        }
        String s = "id,computersuit,humansuit,computerface,humanface,computertype," +
                "humantype,computeraction,humanaction,computerdefendcard,humandefendcard," +
                "computeractiontaken,humanactiontaken,mode,forcewinner,humanwinner,computerwinner,tie";
        StringBuilder builder = new StringBuilder();
        builder.append(s);
        builder.append("\n");
        long maximum = (Math.max(humanPlayedPile.size(), computerPlayedPile.size()));
        for (int i = 0; i < maximum; i++)
        {
            builder.append(id);
            String CSV_SEPARATOR = ",";
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
        try
        {
            OutputStream fileOutputStream = new FileOutputStream(new
                    File(String.valueOf(file), name));
            fileOutputStream.write(builder.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
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
            return "card request";
        }else if(card.isPickTwo())
        {
            return  "pick two";
        }else if(card.isPickThree())
        {
            return "pick three";
        }else if(card.isGeneralMarket())
        {
            return "general market";
        }else if(card.isHoldOn())
        {
            return "hold on";
        }else if(card.isSuspension())
        {
            return "suspension";
        }
        return "none";
    }

    public Card getPreviousCard() {
        return previousCard;
    }

    public void setPreviousCard(Card previousCard) {
        this.previousCard = previousCard;
    }

    public Suit getWantedSuit() {
        return wantedSuit;
    }

    public void setWantedSuit(Suit wantedSuit) {
        this.wantedSuit = wantedSuit;
    }

    public boolean isIsComputerTurn() {
        return isComputerTurn;
    }

    public void setIsComputerTurn(boolean isComputerTurn) {
        this.isComputerTurn = isComputerTurn;
    }

}
