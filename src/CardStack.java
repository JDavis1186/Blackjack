import java.util.*;

/**
 * Generic {@link Card} stack class, to be used as the basis for any stack of cards.
 * 
 * @author Josh Davis
 * @version %I% %G%
 */
public class CardStack extends Stack<Card>{
	protected int cardwidth = Card.cardwidth;
	protected int cardheight = Card.cardheight;
	protected int xOrigin;
	protected int yOrigin;
	protected int id;
	protected int cardCount=0;
	protected int xFan;
	protected int yFan;
}
