import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Player Hand class for {@link Blackjack}
 * 
 * @author Josh Davis
 * @version %I% %G%
 */
public class PlayerHand extends CardStack{
	protected int handValue=0;
	protected int aceCount=0;
	private int posIndex;
	private int[] xPosition = new int[] {50, 300, 550, 800}; 
	protected int yOrigin=500;
	protected int xFan = 30;
	protected int yFan = -40;
	protected boolean BJFlag = false;
	private boolean noHit = false;
	private int firstValue;
	private int wager=0;
	private JPanel infoPanel;
	private JLabel wagerLabel=new JLabel();
	private JLabel valueLabel=new JLabel();
	private static ImageIcon BJIcon;
	private static ImageIcon bustedIcon;
	private static ImageIcon winIcon;
	private static ImageIcon loseIcon;
	private static ImageIcon pushIcon;
	private JLabel BJLabel = new JLabel();
	private JLabel winLabel = new JLabel();
	private static ImageIcon cursorIcon;
	private JLabel cursor = new JLabel();
	private static boolean split;
	
	/**
	 * Constructs the PlayerHand, setting up its info panel and various images
	 * 
	 * @param position indicates the position of the PlayerHand on the board
	 */
	public PlayerHand(int position){
		posIndex=position;
		infoPanel=new JPanel();
		infoPanel.setBounds(xPosition[posIndex]+5, yOrigin+200, 200, 75);
		infoPanel.setBackground(new Color(238,238,238));
		valueLabel.setFont(new Font("Verdana", Font.BOLD, 15));
		valueLabel.setForeground(Color.BLACK);
		wagerLabel.setFont(new Font("Verdana", Font.BOLD, 15));
		wagerLabel.setForeground(Color.BLACK);
		infoPanel.setBorder(new LineBorder(Color.BLACK, 3));
		infoPanel.add(valueLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(infoPanel.getWidth(), 5)));
		infoPanel.add(wagerLabel);
		Blackjack.addThis(infoPanel, 2);
		if(BJIcon==null)
			BJIcon = new ImageIcon(getClass().getClassLoader().getResource("blackjack.png"));
		if(bustedIcon==null)
			bustedIcon = new ImageIcon(getClass().getClassLoader().getResource("busted.png"));
		if(winIcon==null)
			winIcon = new ImageIcon(getClass().getClassLoader().getResource("win.png"));
		if(loseIcon==null)
			loseIcon = new ImageIcon(getClass().getClassLoader().getResource("lose.png"));
		if(pushIcon==null)
			pushIcon = new ImageIcon(getClass().getClassLoader().getResource("push.png"));
		cursorIcon = new ImageIcon(getClass().getClassLoader().getResource("cursor.png"));
		cursor.setIcon(cursorIcon);
		cursor.setVisible(false);
		cursor.setBounds(xPosition[posIndex]-45, yOrigin+200, cursorIcon.getIconWidth(), cursorIcon.getIconHeight());
		
		Blackjack.addThis(cursor, 3);
	}
	
	/**
	 * Activates any legal action buttons, calls the info panel updater,
	 * and makes the cursor icon visible if more than one hand.
	 * Also checks if the player busted (went over 21).
	 */
	public void focus(){
		updatePanel();
		if(split){
			cursor.setVisible(true);
		}
		if(handValue>21){
			BJLabel.setIcon(bustedIcon);
			BJLabel.setBounds(xPosition[posIndex]-20, yOrigin+60, bustedIcon.getIconWidth(), bustedIcon.getIconHeight());
			Blackjack.addThis(BJLabel, 2);
			Blackjack.you.stand();
		} else {
			if(!noHit)
				Blackjack.enableHitButton();
			Blackjack.enableStandButton();
			if(cardCount==2)
				splitCheck();
			if((cardCount==2)&&(!noHit)&&(Blackjack.you.walletAmount()>wager))
				Blackjack.enableDoubleButton();
		}
	}
	
	/**
	 * Updates the info in the hand's info panel.
	 */
	private void updatePanel(){
		valueLabel.setText("Hand Value: " + handValue);
		wagerLabel.setText("Wager: $" + wager);
	}
	
	/** 
	 * Adds a card to the player's hand from the shoe.
	 * Adds its value to the hand's total.
	 */
	public void hit(){
		push(Blackjack.shoe.draw());
		peek().flip();
		peek().setLocation(xPosition[posIndex] + (xFan*cardCount), yOrigin + (yFan*cardCount));
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
		focus();
	}
	
	/**
	 * Handles the initial dealing of the PlayerHand.
	 * Checks for Blackjack, and (if none) gives the player the option to surrender.
	 */
	public void deal(){
		hit();
		firstValue = peek().whatvalue();
		hit();
		if(handValue==21){
			BJLabel.setIcon(BJIcon);
			BJLabel.setBounds(xPosition[posIndex]-20, yOrigin+30, BJIcon.getIconWidth(), BJIcon.getIconHeight());
			Blackjack.addThis(BJLabel, 2);
			BJFlag=true;
			Blackjack.you.stand();
		} else {
			Blackjack.enableSurrenderButton();
		}
	}
	
	/**
	 * Sets up the hand if it's being created as the result of a split.
	 * Adds the card sent from the other hand to this hand's value,
	 * then hits for the second card.
	 * @param firstCard Card sent from the other PlayerHand
	 */
	public void startFromSplit(Card firstCard){
		split=true;
		push(firstCard);
		if(peek().whatvalue()==1){
			handValue=11;
			aceCount++;
			noHit = true;
		} else if(peek().whatvalue()>10){
			handValue=10;
		} else {
			handValue=peek().whatvalue();
		}
		firstValue = peek().whatvalue();
		peek().setLocation(xPosition[posIndex], yOrigin);
		Blackjack.moveCardToFront(peek());
		cardCount++;
		hit();
	}
	
	/**
	 * Moves this PlayerHand and all of its visual elements to a new position.
	 * Called as the result of a split.
	 * @param position The position the hand is moving to.
	 */
	public void move(int position){
		posIndex = position;
		infoPanel.setLocation(xPosition[posIndex]+5, yOrigin+200);
		CardStack buffer = new CardStack();
		while(!isEmpty()){
			buffer.push(pop());
		}
		int tempCount=0;
		while(!buffer.isEmpty()){
			push(buffer.pop());
			peek().setLocation(xPosition[posIndex] + (xFan*tempCount), yOrigin + (yFan*tempCount));
			Blackjack.moveCardToFront(peek());
			tempCount++;
		}
		cursor.setLocation(xPosition[posIndex]-45, yOrigin+200);
	}
	
	/**
	 * Splits this PlayerHand into a new hand.
	 * Removes the top card (subtracting its value) and sends it
	 * to the new PlayerHand to be its first card. Then draws a new top card.
	 * Does not call hit for this second card because that would call focus,
	 * and this PlayerHand shouldn't be focused until the new PlayerHand is no
	 * longer being played.
	 * @param newHand PlayerHand that this PlayerHand is being split into
	 */
	public void splitInto(PlayerHand newHand){
		cursor.setVisible(false);
		int topValue;
		if(peek().whatvalue()>10){
			topValue=10;
		} else {
			topValue=peek().whatvalue();
		}
		newHand.startFromSplit(pop());
		handValue=handValue-topValue;
		cardCount--;
		if(aceCount>0){
			aceCount--;
			noHit = true;
		}
		push(Blackjack.shoe.draw());
		peek().flip();
		peek().setLocation(xPosition[posIndex] + (xFan*cardCount), yOrigin + (yFan*cardCount));
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
		updatePanel();
	}
	
	/**
	 * Handles the player doubling down. Sets a flag that keeps the player
	 * from hitting on this PlayerHand again, hits the final time,
	 * and rotates the new top card 90 degrees.
	 */
	public void doubleDown(){
		noHit = true;
		hit();
		peek().rotate();
	}
	
	/**
	 * Checks if a Split should be a valid option and enables the button if so.
	 */
	private void splitCheck(){
		if(Blackjack.you.walletAmount()>wager){
			if((firstValue==peek().whatvalue())||
					((firstValue>9)&&(peek().whatvalue()>9))){
				if(!Blackjack.you.fourHands()){
					Blackjack.enableSplitButton();
				}
			}
		}
	}
	
	/**
	 * Checks if this hand is a winner and takes the appropriate actions.
	 * Displays the Win/Lose/Push image and sends the Player any winnings.
	 * @param dealerHand value of the dealer's hand
	 * @param dealerBJ boolean indicating if the dealer got a blackjack
	 */
	public void winCheck(int dealerHand, boolean dealerBJ){
		if(BJFlag){
			if(dealerBJ){
				winLabel.setIcon(pushIcon);
				winLabel.setBounds(xPosition[posIndex]+15, yOrigin-80, pushIcon.getIconWidth(), pushIcon.getIconHeight());
				Blackjack.addThis(winLabel, 3);
				infoPanel.setBackground(new Color(238,238,238));
				Blackjack.you.winAmount(wager);
			} else {	
				winLabel.setIcon(winIcon);
				winLabel.setBounds(xPosition[posIndex]+15, yOrigin-60, winIcon.getIconWidth(), winIcon.getIconHeight());
				Blackjack.addThis(winLabel, 3);
				infoPanel.setBackground(new Color(107,255,141));
				Blackjack.you.winAmount((float) ((float) wager*2.5));
			}
		} else if(dealerBJ) {
			winLabel.setIcon(loseIcon);
			winLabel.setBounds(xPosition[posIndex]+15, yOrigin-60, loseIcon.getIconWidth(), loseIcon.getIconHeight());
			Blackjack.addThis(winLabel, 3);
			infoPanel.setBackground(new Color(255,76,91));
		}else if(handValue>21){
			winLabel.setIcon(loseIcon);
			winLabel.setBounds(xPosition[posIndex]+15, yOrigin-60, loseIcon.getIconWidth(), loseIcon.getIconHeight());
			Blackjack.addThis(winLabel, 3);
			infoPanel.setBackground(new Color(255,76,91));
		} else if(dealerHand>21){
			winLabel.setIcon(winIcon);
			winLabel.setBounds(xPosition[posIndex]+15, yOrigin-60, winIcon.getIconWidth(), winIcon.getIconHeight());
			Blackjack.addThis(winLabel, 3);
			infoPanel.setBackground(new Color(107,255,141));
			Blackjack.you.winAmount(wager*2);
		} else if(dealerHand < handValue){
			winLabel.setIcon(winIcon);
			winLabel.setBounds(xPosition[posIndex]+15, yOrigin-60, winIcon.getIconWidth(), winIcon.getIconHeight());
			Blackjack.addThis(winLabel, 3);
			infoPanel.setBackground(new Color(107,255,141));
			Blackjack.you.winAmount(wager*2);
		} else if(dealerHand > handValue){
			winLabel.setIcon(loseIcon);
			winLabel.setBounds(xPosition[posIndex]+15, yOrigin-60, loseIcon.getIconWidth(), loseIcon.getIconHeight());
			Blackjack.addThis(winLabel, 3);
			infoPanel.setBackground(new Color(255,76,91));
		} else {
			winLabel.setIcon(pushIcon);
			winLabel.setBounds(xPosition[posIndex]+15, yOrigin-80, pushIcon.getIconWidth(), pushIcon.getIconHeight());
			Blackjack.addThis(winLabel, 3);
			infoPanel.setBackground(new Color(238,238,238));
			Blackjack.you.winAmount(wager);
		}
	}
	
	/**
	 * Increases the wager by the specified amount
	 * @param amount amount to increase the wager by
	 */
	public void bet(int amount){
		wager=wager+amount;
	}
	
	/**
	 * Hides the cursor from view
	 */
	public void cursorOff(){
		cursor.setVisible(false);
	}
	
	/**
	 * Removes all cards from this hand, removes visual elements, and resets flags.
	 */
	public void removeAll(){
		while(!isEmpty()){
			Blackjack.shoe.discard(pop());
		}
		Blackjack.removeThis(infoPanel);
		Blackjack.removeThis(BJLabel);
		Blackjack.removeThis(winLabel);
		Blackjack.removeThis(cursor);
		split=false;
	}
	
}
