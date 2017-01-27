import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Player class for {@link Blackjack}
 * 
 * @author Josh Davis
 * @version %I% %G%
 */
public class Player {
	PlayerHand[] hands = new PlayerHand[4];
	private int currentHand=0;
	private int handCount=0;
	private float wallet=500;
	int wager=0;
	boolean insuranceFlag;
	private JPanel insuranceBet;
	private JLabel insuranceLabel;
	private JPanel walletPanel;
	private JLabel walletLabel;
	
	/**
	 * Constructs this Player, setting up the windows for
	 * the insurance bet and wallet amounts.
	 */
	public Player(){
		insuranceBet=new JPanel();
		insuranceBet.setBounds(195, 200, 200, 80);
		insuranceLabel = new JLabel();
		insuranceLabel.setFont(new Font("Verdana", Font.BOLD, 15));
		insuranceLabel.setForeground(Color.BLACK);
		insuranceBet.setBorder(new LineBorder(Color.BLACK, 3));
		insuranceBet.setBackground(new Color(238,238,238));
		insuranceBet.add(Box.createRigidArea(new Dimension(insuranceBet.getWidth(), 10)));
		insuranceBet.add(insuranceLabel);
		insuranceBet.setVisible(false);
		Blackjack.addThis(insuranceBet, 2);
		
		walletPanel=new JPanel();
		walletPanel.setBounds(920, 780, 160, 40);
		walletLabel=new JLabel("Wallet: $" + String.format("%.2f", wallet));
		walletLabel.setFont(new Font("Verdana", Font.BOLD, 15));
		walletLabel.setForeground(Color.BLACK);
		walletPanel.setBorder(new LineBorder(Color.BLACK, 3));
		walletPanel.setBackground(new Color(238,238,238));
		walletPanel.add(walletLabel);
		Blackjack.addThis(walletPanel, 4);
	}
	
	/**
	 * Updates the wallet window with the current amount.
	 */
	public void walletUpdate(){
		walletLabel.setText("Wallet: $" + String.format("%.2f", wallet));
	}
	
	/**
	 * Deals an initial PlayerHand with the specified wager amount.
	 * @param amount The amount being wagered
	 */
	public void deal(int amount){
		hands[0] = new PlayerHand(2);
		handCount=1;
		wager=amount;
		bet(wager);
		hands[0].deal();
	}
	
	/**
	 * Tells the current hand to hit.
	 */
	public void hit(){
		hands[currentHand].hit();
	}
	
	/**
	 * Handles the stand command from the user, turning off the current hand's
	 * cursor, then moving focus to the next hand.
	 */
	public void stand(){
		Blackjack.disableAll();
		hands[currentHand].cursorOff();
		if(currentHand==0){
			Blackjack.dealer.playerDone();
		} else {
			currentHand--;
			hands[currentHand].focus();
		}
	}
	
	/**
	 * Called when the user wants to split his hand.
	 * Moves the hands as needed and splits the current hand into a new one.
	 */
	public void split(){
		switch(handCount){
			case 1:	hands[0].move(1);
					hands[1]=new PlayerHand(3);
					hands[0].splitInto(hands[1]);
					break;
			case 2: if(currentHand==1){
						hands[1].move(2);
						hands[2]=new PlayerHand(3);
						hands[1].splitInto(hands[2]);
					} else {
						hands[2]=hands[1];
						hands[1]=new PlayerHand(2);
						hands[0].splitInto(hands[1]);
					}
					break;
			case 3: for(int i=currentHand; i>=0; i--){
						hands[i].move(i);
					}
					for(int i=2; i>currentHand; i--){
						hands[i+1]=hands[i];
					}
					hands[currentHand+1]=new PlayerHand(currentHand+1);
					hands[currentHand].splitInto(hands[currentHand+1]);
					break;
			default: System.err.println("Error: Trying to split with " + handCount + " hands!");
		}
		if(handCount<3){
			handCount++;
			currentHand++;
			bet(wager);
		}
		hands[currentHand].focus();
	}
	
	/**
	 * Tells the current hand to double down and increases its wager accordingly.
	 */
	public void doubleDown(){
		bet(wager);
		hands[currentHand].doubleDown();
	}
	
	/**
	 * Sets whether insurance was taken and handles the insurance bet.
	 * @param flag boolean indicating if the user chose insurance.
	 */
	public void insurance(boolean flag){
		insuranceFlag=flag;
		if(insuranceFlag){
			insuranceLabel.setText("Insurance Bet: $" + wager);
			wallet=wallet-wager;
			walletUpdate();
		}
		insuranceBet.setVisible(insuranceFlag);
		hands[0].focus();
	}
	
	/**
	 * Returns if the user already has four hands in play.
	 * Used to prevent additional splitting.
	 * @return boolean indicating if four hands in play
	 */
	public boolean fourHands(){
		return(handCount>3);
	}
	
	/**
	 * Calls the win check for each hand, checks if insurance was won,
	 * and gives the user the option to bet on a new round.
	 * @param dealerHand Value of the dealer's hand
	 * @param dealerBJ boolean if the dealer got a blackjack
	 */
	public void winCheck(int dealerHand, boolean dealerBJ){
		if(insuranceFlag){
			if(dealerBJ){
				insuranceBet.setBackground(new Color(107,255,141));
				wallet=wallet+(wager*2);
			} else {
				insuranceBet.setBackground(new Color(255,76,91));
			}
		}
		for(int i=handCount; i>0; i--){
			hands[i-1].winCheck(dealerHand, dealerBJ);
		}
		walletUpdate();
		Blackjack.enableBet();
	}
	
	/**
	 * Removes the PlayerHands, resets visual elements and variables.
	 */
	public void removeAll(){
		wager=0;
		insuranceBet.setVisible(false);
		insuranceBet.setBackground(new Color(238,238,238));
		for(int i=0; handCount>i; handCount--){
			hands[handCount-1].removeAll();
			hands[handCount-1]=null;
		}
	}
	
	/**
	 * Increases the current hand's wager by the specified amount.
	 * @param amount Amount to increase the wager by.
	 */
	public void bet(int amount){
		if(wallet>=amount){
			wallet=wallet-amount;
			hands[currentHand].bet(amount);
			walletUpdate();
		}
	}
	
	/**
	 * Returns the amount in the player's wallet.
	 * @return amount in the player's wallet
	 */
	public float walletAmount(){
		return wallet;
	}
	
	/**
	 * Adds the specified amount to the wallet.
	 * @param amount the amount to increase the wallet by
	 */
	public void winAmount(float amount){
		wallet=wallet+amount;
	}
}
