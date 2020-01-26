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

    public void computerDraw() {
        if(drawPile.size() > 0)
        computerCardPile.add(drawPile.remove(0));
        else
            isThereWinner = true;
    }

    public void playerDraw() {
        if(drawPile.size() > 0)
        playerCardPile.add(drawPile.remove(0));
        else
            isThereWinner = true;
    }

    public void rule(Card card) {
        if (card.isHoldOn()) {
            System.out.println("Hold on!");
        } else if (card.isPickTwo()) {
            System.out.println("Pick two!");
        } else if (card.isPickThree()) {
            System.out.println("Pick three!");
        } else if (card.isSuspension()) {
            System.out.println("Suspension!");
        } else if (card.isWhot()) {
            System.out.println("Whot! Select a card you need >> ");
        } else if (card.isGeneralMarket()) {
            System.out.println("General market!");
        }
    }

    public String checkWinner() {
        if (drawPile.isEmpty() || computerCardPile.isEmpty() || playerCardPile.isEmpty()) {
            if (computerCardPile.isEmpty()) {
                isThereWinner = true;
                return "*****Game Over*****\n\nYou lose!";
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
                    return "*****Game Over*****\n\nYou lose!";
                } else if (computerCount < playerCount) {
                    isThereWinner = true;
                    return "*****Game Over*****\n\nYou win!";
                } else {
                    isThereWinner = true;
                    return "*****Game Over*****\n\nA tie!";
                }
            }

        }
        return "No winner";//will never return
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

    public Card getStartCard() {
        return startCard;
    }

    public void setStartCard(Card startCard) {
        this.startCard = startCard;
    }
}
