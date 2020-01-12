import java.util.ArrayList;

public class WhotGame {
    private boolean isThereWinner = false;
    private NaijaWhots whots;
    private Card[] pack;
    private ArrayList<Card> drawPile;
    private ArrayList<Card> computerCardPile;
    private ArrayList<Card> playerCardPile;
    private int computerCount = 0;
    private int playerCount = 0;
    private boolean isHoldOn = false;
    private boolean isSuspension = false;
    private boolean isPickTwo = false;
    private boolean isGeneralMarket = false;
    private boolean isPickThree = false;
    private boolean isWhot = false;
    private boolean isNormalCard = false;
    private boolean isComputerTurn = false;
    private boolean isPlayerTurn = false;


    private Card startCard;

    public WhotGame() {
        whots = new NaijaWhots();
        computerCardPile = new ArrayList<>();
        playerCardPile = new ArrayList<>();
        drawPile = new ArrayList<>();
        pack = whots.getPack();
        for (Card card : pack) {
            drawPile.add(card);
        }
    }

    public void deal(int number) throws WhotGameException {
        if (number >= 2 && number <= 27 && number % 2 == 0) {
            for (int index = 0; index < number; index++) {
                computerCardPile.add(drawPile.get(0));
                drawPile.remove(0);
                playerCardPile.add(drawPile.get(0));
                drawPile.remove(0);
            }
            startCard = drawPile.remove(0);
        } else if (number < 0) {
            throw new WhotGameException("You cannot deal a negative number of cards");
        } else if (number > 27) {
            throw new WhotGameException("You can only deal between 2 to 27 cards");
        } else if (number % 2 != 0) {
            throw new WhotGameException("You cannot deal an odd number of cards");
        } else if (number >= 0 && number < 2) {
            throw new WhotGameException("You can only deal 2 or more cards");
        }
    }

    public void play(Card card) {
        if (computerCardPile.contains(card)) {
            computerCardPile.remove(card);
        } else if (playerCardPile.contains(card)) {
            playerCardPile.remove(card);
        }
    }

    public void draw() {
        if (isComputerTurn) {
            computerCardPile.add(drawPile.remove(0));
        } else {
            playerCardPile.add(drawPile.remove(0));
        }
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
            case 14:
                isGeneralMarket = true;
                break;
            case 20:
                isWhot = true;
                break;
            default:
                isNormalCard = true;
                break;
        }
        if (isHoldOn) {
            System.out.println("Hold on!");
        } else if (isPickTwo) {
            System.out.println("Pick two!");
        } else if (isPickThree) {
            System.out.println("Pick three!");
        } else if (isSuspension) {
            System.out.println("Suspension!");
        } else if (isWhot) {
            System.out.println("Whot! Select a card you need >> ");
        } else if (isGeneralMarket) {
            System.out.println("General market!");
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
                    if (card.getSuit().equals(Suit.STAR)) {
                        computerCount += card.getFace() * 2;
                    } else {
                        computerCount += card.getFace();
                    }

                }
                for (Card card : playerCardPile) {
                    if (card.getSuit().equals(Suit.STAR)) {
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

    public boolean isNormalCard() {
        return isNormalCard;
    }

    public void setNormalCard(boolean normalCard) {
        isNormalCard = normalCard;
    }

    public boolean isComputerTurn() {
        return isComputerTurn;
    }

    public void setComputerTurn(boolean computerTurn) {
        isComputerTurn = computerTurn;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public Card getStartCard() {
        return startCard;
    }

    public void setStartCard(Card startCard) {
        this.startCard = startCard;
    }

    public boolean isGeneralMarket() {
        return isGeneralMarket;
    }

    public void setGeneralMarket(boolean generalMarket) {
        isGeneralMarket = generalMarket;
    }
}
