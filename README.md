# NaijaWhotGameProject
Modelling the Naija Whot game via CLI
# NaijaWhotGameProject
Modelling the Naija Whot game
              Modelling The NaijaWhot Game via CLI



Commercial games based on the Eights family, in which players try to be the first to get rid of their cards by following number(face) or shape (suit). Whot! was invented (but, apparently, not patented) and trademarked by William Henry Storey, of Southend-on-Sea, England in 1935. It was first marketed by the company Storey & Co of Croydon, which he set up in 1930, but was soon acquired by Waddingtons of Leeds, and was popular in Britain in the 1950's and 1960's. Waddingtons continued to sell it until the 1990's; nowadays it is distributed by Winning Moves UK in their Top Cards series.
Whot! has become the national card game of Nigeria.


Players and Cards

There can be two or more players. A special pack is used, consisting of five unequal suits identified by symbols (circle, triangle, cross, square, star) or in some Nigerian terminology are identified by  (ball, angle, carpet, star), and some wild "Whot" cards. The composition of the suits is as follows.

Circles: 1 2 3 4 5    7 8    10 11 12 13 14

Triangles: 1 2 3 4 5    7 8    10 11 12 13 14

Crosses: 1 2 3 5    7   10 11 13 14

Squares: 1 2 3  5   7   10 11 13 14

Stars: 1 2 3 4 5   7 8

Whot! Four or five cards, numbered "20" in some packs.

Note: With Five Whot! There are 54 cards in the pack(deck) of the NaijaWhot.
Star number(face) is doubled when scoring by counting faces. The rules of the game vary from place to place in Nigeria.

Deal

Deal and play are clockwise and the turn to deal passes to the left after each hand. To choose the first dealer each player cuts the shuffled pack. The dealer deals 6 cards to each player, one at a time, and puts the rest of the pack face down to form a draw pile.

Play

The player to dealer's left begins by playing any card from their hand face up to start a play pile.

Subsequent players at their turn may play one card face up on top of the play pile, if they have a card that fits. When the previous play was a card of one of the five suits, the possible plays are:

(1)  any card with the same symbol (suit) as the previous play;

(2) any card with the same number as the previous play;

(3) any "Whot" card.

If you cannot or do not wish to play a card at your turn, you draw one card from the draw pile instead; having drawn a card, you are not allowed to play a card on this turn.

If the draw pile runs out, all except the top card of the play pile are shuffled to form a new draw pile, and play continues. To avoid prolonging the game play, if no player finishes his cards before others and the draw pile runs out, the number in each players' card is counted, any players that has lower count wins the game, note star card's number is counted twice. So star 4 will have a count of 8.



Card                                           Explanation

1          HOLD ON: The same player plays again.

2          PICK TWO: The next player must either play another two, or must draw two cards from the stock instead.
If two or more consecutive players play twos, the first player who does not play a two must draw two cards for each consecutive two that was played (four cards for two twos, six cards for three twos, etc.).
After cards have been drawn for the two(s), the next player can play any legal card.

5          PICK THREE: The next player must either play another five or must draw three cards from the stock instead.
If two or more consecutive players play fives, the first player who does not play a five must draw three cards for each consecutive five that was played (six cards for two fives, nine cards for three fives, etc.).
After cards have been drawn for the five(s), the next player can play any legal card.

8         SUSPENSION: The next player misses a turn.
If the STAR 8 is played, the next two players must each miss a turn.

14       GENERAL MARKET: All players except the person who played the 14 must immediately draw one card from the stock.

Note that in a two-player game, there is no difference between "HOLD ON" and "SUSPENSION" since there is only one opponent to skip. In the multi-player game, the use of the 1 and 8 varies. Some play that "HOLD ON" means that just the next player misses a turn, while "SUSPENSION" causes all the other players to wait while the player of the 8 plays another card. 

The next card played after a "HOLD ON" or "SUSPENSION" has to follow the normal rules - it must have the same symbol as the 1 or 8, or the same number (causing another hold on or suspension) or a wild 20 (whot) card accompanied by a call for the shape that should be played next.

A player whose hand is reduced to two cards must warn the other players by saying "semi last card". A player with just one card must warn the others by saying "last card". A player who omits these warnings is penalised by drawing two cards from the stock.

Some of the rules may not be applied but the logic of the game is the same. As the rules differ from place to place, you can model the again according to your locality.


Hint
Only two players are allowed in our game: computer and human.
You may choose any paradigm but I think OOP will be better. Using the object oriented programming will enable you to bundle the shape(suit) and the number(face) as a single object. This is necessary so that when you get the card object you can get its corresponding suit and face by using the dot operator like --- card.suit, card.face. If you are using OOP,  you will have to create at least five classes each doing its own work. The first class called Card, should bundle the suit and its number as an object.
And also has a method that returns the card object as string for identification, for example in this format:
*"3 of Circle", "7 of Star", "20 of Whot", "1 of Triangle" etc
Create another class called NaijaWhots that has a method called initialise that instantiates 54 card objects into a list or array. The class must also have a method called shuffle that completely rearranges all the cards in the list in a random order. The class must also have method that returns the shuffled card, for use in game play.
Create another class called NaijaWhotGame.
This class has many methods and contains the main logic of the game:

deal: a method that deals a specific number of cards to each player.

rule: a method that checks for available rule on any card played, if there is a rule, it will issue a command to the affected player.

draw: this method will enable the player to draw a card from the draw pile if he has no suitable card to play or was instructed to do so according to the rule.

checkWinner: this method check whether the card in draw  pile or cards in each player's draw pile has finished, and declare the winner accordingly, the method must be called each time a player plays a card.

play: this method will display the card which a player plays and automatically remove the card from the player's card pile so that the player's card pile is always reduce when he plays a card. etc

Create a class called NaijaWhotGamePlay, this class contains the main method for playing the card, it will use the already created classes to play the game.

This description may not be complete, as you model the game, you may see needs for more methods and data
More details about the Whot can be extracted from www.pagat.com/com/whot.html
