import java.util.*;

/**
 * Shoe class for {@link Blackjack}
 * 
 * @author Josh Davis
 * @version %I% %G%
 */
public class Shoe extends CardStack {
	private CardStack discardPile = new CardStack();
	
	/**
	 * Constructs the shoe and fills it with
	 * the specified number of card decks, then shuffles.
	 * @param deckCount How many card decks should be in the shoe
	 */
	public Shoe(int deckCount){
		for(int i=0;i<deckCount;i++){
			Deck tempDeck = new Deck();
			while(!tempDeck.isEmpty()){
				push(tempDeck.pop());
				peek().setVisible(false);
				cardCount++;
			}
		}
		Collections.shuffle(this);
	}
	
	/**
	 * Draws a card from the shoe.
	 * Makes it visible and adjusts the shoe's count.
	 * @return the Card drawn
	 */
	public Card draw(){
		cardCount--;
		peek().setVisible(true);
		return pop();
	}
	
	/**
	 * Resets the shoe, moving all cards from the discard
	 * pile into it and then shuffling all the cards.
	 */
	public void reset(){
		while(!discardPile.isEmpty()){
			discardPile.peek().flip();
			cardCount++;
			push(discardPile.pop());
		}
		Collections.shuffle(this);
	}
	
	/**
	 * Puts the specified card into the discard pile,
	 * hiding it from view.
	 * @param card The Card to discard
	 */
	public void discard(Card card){
		card.setVisible(false);
		discardPile.push(card);
	}
}
