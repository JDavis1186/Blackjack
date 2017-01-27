import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Dealer class for {@link Blackjack}
 * 
 * @author Josh Davis
 * @version %I% %G%
 */
public class Dealer extends CardStack{
	private int xOrigin=600;
	private int yOrigin=30;
	protected int xFan = 30;
	private Card hole;
	private int handValue=0;
	private int aceCount=0;
	private boolean BJFlag=false;
	private JPanel infoPanel;
	private JLabel valueLabel=new JLabel();
	private static ImageIcon BJIcon;
	private static ImageIcon bustedIcon;
	private JLabel BJLabel=new JLabel();;

	/**
	 * Constructor, sets up the info Panel and images.
	 */
	public Dealer(){
		infoPanel=new JPanel();
		infoPanel.setBounds(xOrigin-100, yOrigin+230, 200, 40);
		infoPanel.setBackground(new Color(238,238,238));
		valueLabel.setFont(new Font("Verdana", Font.BOLD, 15));
		valueLabel.setForeground(Color.BLACK);
		infoPanel.setBorder(new LineBorder(Color.BLACK, 3));
		infoPanel.add(valueLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(infoPanel.getWidth(), 5)));
		infoPanel.setVisible(false);
		Blackjack.addThis(infoPanel, 2);
		if(BJIcon==null)
			BJIcon = new ImageIcon(getClass().getClassLoader().getResource("blackjack.png"));
		if(bustedIcon==null)
			bustedIcon = new ImageIcon(getClass().getClassLoader().getResource("busted.png"));
	}
	
	/**
	 * Hits, adding a card from the shoe. Flips the card upright and
	 * adds its value to the total for the hand.
	 */
	public void hit(){
		push(Blackjack.shoe.draw());
		peek().flip();
		peek().setLocation(xOrigin + (xFan*cardCount), yOrigin);
		Blackjack.moveCardToFront(peek());
		cardCount++;
		if(peek().whatvalue()==1){
			handValue=handValue+11;
			aceCount++;
		} else if(peek().whatvalue()>10){
			handValue=handValue+10;
		} else {
			handValue=handValue+peek().whatvalue();
		}
		while((aceCount>0)&&(handValue>21)){
			aceCount--;
			handValue=handValue-10;
		}
		if(handValue>21){
			BJLabel.setIcon(bustedIcon);
			BJLabel.setBounds(xOrigin-120, yOrigin+60, bustedIcon.getIconWidth(), bustedIcon.getIconHeight());
			Blackjack.addThis(BJLabel, 2);
		}
		valueLabel.setText("Hand Value: " + handValue);
	}
	
	/**
	 * Draws the hole card.
	 */
	private void drawHole(){
		hole = Blackjack.shoe.draw();
		hole.setLocation(xOrigin-200, yOrigin);
		cardCount++;
	}
	
	/**
	 * Flips the hole card over. Adds the value to the hand's total.
	 */
	private void flipHole(){
		hole.flip();
		if(hole.whatvalue()==1){
			handValue=handValue+11;
			aceCount++;
		} else if(hole.whatvalue()>10){
			handValue=handValue+10;
		} else {
			handValue=handValue+hole.whatvalue();
		}
		while((aceCount>0)&&(handValue>21)){
			aceCount--;
			handValue=handValue-10;
		}
		if(handValue>21){
			BJLabel.setIcon(bustedIcon);
			BJLabel.setBounds(xOrigin-120, yOrigin+60, bustedIcon.getIconWidth(), bustedIcon.getIconHeight());
		}
		if(handValue==21){
			BJLabel.setIcon(BJIcon);
			BJLabel.setBounds(xOrigin-120, yOrigin+60, BJIcon.getIconWidth(), BJIcon.getIconHeight());
			Blackjack.addThis(BJLabel, 2);
			BJFlag=true;
		}
		valueLabel.setText("Hand Value: " + handValue);
	}
	
	/**
	 * Deals the dealer's hand.
	 */
	public void deal(){
		drawHole();
		hit();
		if(handValue==11){
			Blackjack.showInsurance();
		} else {
			Blackjack.playerDeal();
		}
		valueLabel.setText("Known Hand Value: " + handValue);
		infoPanel.setVisible(true);
	}
	
	/**
	 * Hits until the hand is at least 17, then tells the game to check for player victory.
	 */
	public void playerDone(){
		flipHole();
		while((handValue<17)||((handValue==17)&&(aceCount>0))){
			hit();
		}
		Blackjack.you.winCheck(handValue, BJFlag);
	}
	
	/**
	 * Checks for blackjack in the case of a surrender.
	 * 
	 * @return boolean indicating if the dealer has a blackjack
	 */
	public boolean BJcheck(){
		flipHole();
		return(BJFlag);
	}
	
	/**
	 * Resets the dealer's cards and resets all values to defaults for the next round.
	 */
	public void removeAll(){
		if(hole!=null)
			Blackjack.shoe.discard(hole);
			hole=null;
		while(!isEmpty()){
			Blackjack.shoe.discard(pop());
		}
		Blackjack.removeThis(BJLabel);
		handValue=0;
		aceCount=0;
		BJFlag=false;
		cardCount=0;
	}
}
