public class GamePlay {
    public static void main(String[] args) {
        WhotGame game = new WhotGame();
        game.deal(6);
        for (Card card : game.getComputerCardPile()) {
            System.out.println(card.toString());
        }
        System.out.println();
        for (Card card : game.getPlayerCardPile()) {
            System.out.println(card.toString());
        }
        System.out.println();
        game.play();
    }
}
