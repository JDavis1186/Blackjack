import java.util.*;

/**
 * Generic Card Deck class
 * 
 * @author Josh Davis
 * @version %I% %G%
 */
public class Deck extends CardStack {
	//Defines the four card suits
	private Character[] suits = {'S', 'C', 'H', 'D'};
	
	/**
	 * Constructs the deck. Generates the 52 playing
	 * cards, placing them on the deck face-down.
	 */
	public Deck(){
		//For each card suit, create the 13 cards and push them onto the deck
		for(Character suitsymbol : suits){
			for(int i = 1; i <= 13; i++){
				push(new Card(i, suitsymbol));
			}
		}
		//Shuffle the deck
		Collections.shuffle(this);
		//Add each card to the game window, set its position where the deck is, layer it on top
		for(Card card:this){
			Blackjack.addThis(card, 1);
		}
	}
}