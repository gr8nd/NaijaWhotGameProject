import java.util.ArrayList;

public class WhotGame {
    private boolean isThereWinner = false;
    private NaijaWhots whots;
    private Card[] cards;
    private ArrayList<Card> drawPile;
    private ArrayList<Card> computerCardPile;
    private ArrayList<Card> playerCardPile;
    private int computerCount = 0;
    private int playerCount = 0;
    private boolean isHoldOn = false;
    private boolean isSuspension = false;
    private boolean isPickTwo = false;
    private boolean isPickThree = false;
    private boolean isWhot = false;

    public WhotGame() {
        whots = new NaijaWhots();
        computerCardPile = new ArrayList<>();
        playerCardPile = new ArrayList<>();
        drawPile = new ArrayList<>();
        cards = whots.getWhots();
        for (Card card : cards) {
            drawPile.add(card);
        }
    }

    public void deal(int number) {
        for (int index = 0; index < number; index++) {
            computerCardPile.add(drawPile.get(0));
            drawPile.remove(0);
            playerCardPile.add(drawPile.get(0));
            drawPile.remove(0);
        }
    }

    public void play() {
        ArrayList<Card> cards = new ArrayList<>(54);
        for (Card card : this.drawPile) {
            System.out.println(card.toString());
        }
        System.out.println(drawPile.size());
    }

    public void rule(Card card) {
        switch (card.getFace()) {
            case 1:
                isHoldOn = true;
                break;
            case 2:
                isPickTwo = true;
                break;
            case 5:
                isPickThree = true;
                break;
            case 8:
                isSuspension = true;
                break;
            case 20:
                isWhot = true;
                break;
        }
    }

    public String checkWinner() {
        if (drawPile.isEmpty() || computerCardPile.isEmpty() || playerCardPile.isEmpty()) {
            if (computerCardPile.isEmpty()) {
                isThereWinner = true;
                return "*****Game Over*****\n\nYou lost!";
            } else if (playerCardPile.isEmpty()) {
                isThereWinner = true;
                return "*****Game Over*****\n\nYou win!";
            } else {
                for (Card card : computerCardPile) {
                    if (card.getSuit().equals("Star")) {
                        computerCount += card.getFace() * 2;
                    } else {
                        computerCount += card.getFace();
                    }

                }
                for (Card card : playerCardPile) {
                    if (card.getSuit().equals("Star")) {
                        playerCount += card.getFace() * 2;
                    } else {
                        playerCount += card.getFace();
                    }

                }
                if (computerCount > playerCount) {
                    isThereWinner = true;
                    return "*****Game Over*****\n\nYou lost!";
                } else if (computerCount < playerCount) {
                    isThereWinner = true;
                    return "*****Game Over*****\n\nYou win!";
                } else {
                    isThereWinner = true;
                    return "*****Game Over*****\n\nA tie!";
                }
            }

        }
        return "No winner";
    }

    public boolean isThereWinner() {
        return isThereWinner;
    }

    public void setThereWinner(boolean thereWinner) {
        isThereWinner = thereWinner;
    }

    public ArrayList<Card> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(ArrayList<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public ArrayList<Card> getComputerCardPile() {
        return computerCardPile;
    }

    public void setComputerCardPile(ArrayList<Card> computerCardPile) {
        this.computerCardPile = computerCardPile;
    }

    public ArrayList<Card> getPlayerCardPile() {
        return playerCardPile;
    }

    public void setPlayerCardPile(ArrayList<Card> playerCardPile) {
        this.playerCardPile = playerCardPile;
    }

    public boolean isHoldOn() {
        return isHoldOn;
    }

    public void setHoldOn(boolean holdOn) {
        isHoldOn = holdOn;
    }

    public boolean isSuspension() {
        return isSuspension;
    }

    public void setSuspension(boolean suspension) {
        isSuspension = suspension;
    }

    public boolean isPickTwo() {
        return isPickTwo;
    }

    public void setPickTwo(boolean pickTwo) {
        isPickTwo = pickTwo;
    }

    public boolean isPickThree() {
        return isPickThree;
    }

    public void setPickThree(boolean pickThree) {
        isPickThree = pickThree;
    }

    public boolean isWhot() {
        return isWhot;
    }

    public void setWhot(boolean whot) {
        isWhot = whot;
    }
}
