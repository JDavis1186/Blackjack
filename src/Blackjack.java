import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Blackjack (21) card game by Josh Davis.
 * 
 * @author Josh Davis
 * @version %I% %G%
 */
public class Blackjack extends JPanel implements ActionListener{
	private static JLayeredPane BJWindow;
	private final static Dimension gamesize = new Dimension(1100,900);
	private static int deckCount=8;
	public static Shoe shoe;
	public static Player you;
	public static Dealer dealer;
	protected static JButton dealButton;
	protected static JButton hitButton;
	protected static JButton standButton;
	protected static JButton splitButton;
	protected static JButton surrenderButton;
	protected static JButton doubleButton;
	
	protected static JButton insuranceYes;
	protected static JButton insuranceNo;
	private static JPanel insurancePane;

	protected static JButton oneDollar;
	protected static JButton fiveDollars;
	protected static JButton tenDollars;
	protected static JButton twentyFiveDollars;
	protected static JButton fiftyDollars;
	protected static JButton hundredDollars;
	private final ImageIcon oneIcon = new ImageIcon(getClass().getClassLoader().getResource("chip1.png"));
	private final ImageIcon fiveIcon = new ImageIcon(getClass().getClassLoader().getResource("chip5.png"));
	private final ImageIcon tenIcon = new ImageIcon(getClass().getClassLoader().getResource("chip10.png"));
	private final ImageIcon twentyFiveIcon = new ImageIcon(getClass().getClassLoader().getResource("chip25.png"));
	private final ImageIcon fiftyIcon = new ImageIcon(getClass().getClassLoader().getResource("chip50.png"));
	private final ImageIcon hundredIcon = new ImageIcon(getClass().getClassLoader().getResource("chip100.png"));
	
	private static JPanel chipPane;
	private static JLabel betLabel;
	private static int wager;
	
	private static JButton rulesCard;
	private final ImageIcon rulesCardIcon = new ImageIcon(getClass().getClassLoader().getResource("rulesCard.png"));
	private static JButton rules;
	private final ImageIcon rulesIcon = new ImageIcon(getClass().getClassLoader().getResource("rules.png"));
	private boolean rulesToggle;
	private static JButton betAgain;
	
	/**
	 * Constructs the Blackjack game and sets up many of its elements.
	 * Defines the details of the window.
	 * Creates the game area.
	 * Creates the various buttons used to control the game.
	 * Sets up the Shoe, the Dealer, and the Player
	 */
	public Blackjack(){
		//set the minimum size of the window
		setMinimumSize(gamesize);
		setMaximumSize(gamesize);
		//set the background to an appropriate shade of green
		setBackground(new Color(0,100,0));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setFocusable(true);
		
		//Make the game area
		BJWindow = new JLayeredPane();
		BJWindow.setMinimumSize(gamesize);
		BJWindow.setMaximumSize(new Dimension(gamesize.width+300,Card.cardheight*5));
		BJWindow.setBackground(new Color(0,100,0));
		add(BJWindow, new Integer(1));
		
		//Make the rules card
		rulesCard = new JButton(rulesCardIcon);
		rulesCard.setBorder(null);
		rulesCard.setOpaque(false);
		rulesCard.setContentAreaFilled(false);
		rulesCard.setActionCommand("rules");
		rulesCard.addActionListener(this);
		rulesCard.setBounds(20, 20, rulesCardIcon.getIconWidth(), rulesCardIcon.getIconHeight());
		BJWindow.add(rulesCard, new Integer(1));
		rules = new JButton(rulesIcon);
		rules.setBorder(null);
		rules.setOpaque(false);
		rules.setContentAreaFilled(false);
		rules.setActionCommand("rules");
		rules.addActionListener(this);
		rules.setBounds(80, 200, rulesIcon.getIconWidth(), rulesIcon.getIconHeight());
		rules.setVisible(false);
		BJWindow.add(rules, new Integer(5));
		
		//Make the button that deals
		dealButton = new JButton("Deal");
		dealButton.setVerticalTextPosition(AbstractButton.CENTER);
		dealButton.setHorizontalTextPosition(AbstractButton.CENTER);
		dealButton.setMnemonic(KeyEvent.VK_D);
		dealButton.setActionCommand("deal");
		dealButton.setEnabled(false);
		dealButton.addActionListener(this);
		
		//Make the button that lets you hit
		hitButton = new JButton("Hit");
		hitButton.setVerticalTextPosition(AbstractButton.CENTER);
		hitButton.setHorizontalTextPosition(AbstractButton.CENTER);
		hitButton.setMnemonic(KeyEvent.VK_H);
		hitButton.setActionCommand("hit");
		hitButton.setEnabled(false);
		hitButton.addActionListener(this);
		
		//Make the button that lets you stand
		standButton = new JButton("Stand");
		standButton.setVerticalTextPosition(AbstractButton.CENTER);
		standButton.setHorizontalTextPosition(AbstractButton.CENTER);
		standButton.setMnemonic(KeyEvent.VK_S);
		standButton.setActionCommand("stand");
		standButton.setEnabled(false);
		standButton.addActionListener(this);
		
		//Make the button for a split
		splitButton = new JButton("Split");
		splitButton.setVerticalTextPosition(AbstractButton.CENTER);
		splitButton.setHorizontalTextPosition(AbstractButton.CENTER);
		splitButton.setMnemonic(KeyEvent.VK_P);
		splitButton.setActionCommand("split");
		splitButton.setEnabled(false);
		splitButton.addActionListener(this);
		
		//Make the button for a surrender
		surrenderButton = new JButton("Surrender");
		surrenderButton.setVerticalTextPosition(AbstractButton.CENTER);
		surrenderButton.setHorizontalTextPosition(AbstractButton.CENTER);
		surrenderButton.setMnemonic(KeyEvent.VK_U);
		surrenderButton.setActionCommand("surrender");
		surrenderButton.setEnabled(false);
		surrenderButton.addActionListener(this);
		
		//Make the button for a double down
		doubleButton = new JButton("Double Down");
		doubleButton.setVerticalTextPosition(AbstractButton.CENTER);
		doubleButton.setHorizontalTextPosition(AbstractButton.CENTER);
		doubleButton.setMnemonic(KeyEvent.VK_O);
		doubleButton.setActionCommand("double");
		doubleButton.setEnabled(false);
		doubleButton.addActionListener(this);
		
		
		//Create an area to hold the command buttons
		JPanel buttonPane = new JPanel();
		add(buttonPane);
		buttonPane.setBackground(new Color(0,100,0));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(dealButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(hitButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(standButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(splitButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(surrenderButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(doubleButton);
		buttonPane.add(Box.createHorizontalGlue());
		
		//Make the buttons for insurance
		insuranceYes = new JButton("Yes");
		insuranceYes.setVerticalTextPosition(AbstractButton.CENTER);
		insuranceYes.setHorizontalTextPosition(AbstractButton.CENTER);
		insuranceYes.setMnemonic(KeyEvent.VK_Y);
		insuranceYes.setActionCommand("insuranceYes");
		insuranceYes.setEnabled(true);
		insuranceYes.addActionListener(this);
		
		insuranceNo = new JButton("No");
		insuranceNo.setVerticalTextPosition(AbstractButton.CENTER);
		insuranceNo.setHorizontalTextPosition(AbstractButton.CENTER);
		insuranceNo.setMnemonic(KeyEvent.VK_N);
		insuranceNo.setActionCommand("insuranceNo");
		insuranceNo.setEnabled(true);
		insuranceNo.addActionListener(this);
		
		//Create the insurance window
		insurancePane = new JPanel();
		BJWindow.add(insurancePane, new Integer(2));
		insurancePane.setBounds(200, 320, 700, 200);
		insurancePane.setBorder(new LineBorder(Color.BLACK, 3));
		JLabel insuranceLabel = new JLabel("<html><body><center>Dealer has an Ace.<br>"
				+ "Would you like to wager insurance<br>"
				+ "against them getting Blackjack?</center></body</html>");
		insuranceLabel.setFont(new Font("Verdana", Font.BOLD, 20));
		insurancePane.setBackground(new Color(238,238,238));
		insurancePane.add(Box.createRigidArea(new Dimension(insurancePane.getWidth(), 30)));
		insurancePane.add(insuranceLabel);
		insurancePane.add(Box.createRigidArea(new Dimension(insurancePane.getWidth(), 30)));
		insurancePane.add(Box.createHorizontalGlue());
		insurancePane.add(insuranceYes);
		insurancePane.add(Box.createRigidArea(new Dimension(10, 0)));
		insurancePane.add(insuranceNo);
		insurancePane.add(Box.createHorizontalGlue());
		insurancePane.setVisible(false);
		
		oneDollar = new JButton(oneIcon);
		oneDollar.setBorder(null);
		oneDollar.setOpaque(false);
		oneDollar.setContentAreaFilled(false);
		oneDollar.setActionCommand("one");
		oneDollar.addActionListener(this);
		
		fiveDollars = new JButton(fiveIcon);
		fiveDollars.setBorder(null);
		fiveDollars.setOpaque(false);
		fiveDollars.setContentAreaFilled(false);
		fiveDollars.setActionCommand("five");
		fiveDollars.addActionListener(this);
		
		tenDollars = new JButton(tenIcon);
		tenDollars.setBorder(null);
		tenDollars.setOpaque(false);
		tenDollars.setContentAreaFilled(false);
		tenDollars.setActionCommand("ten");
		tenDollars.addActionListener(this);
		
		twentyFiveDollars = new JButton(twentyFiveIcon);
		twentyFiveDollars.setBorder(null);
		twentyFiveDollars.setOpaque(false);
		twentyFiveDollars.setContentAreaFilled(false);
		twentyFiveDollars.setActionCommand("twentyFive");
		twentyFiveDollars.addActionListener(this);
		
		fiftyDollars = new JButton(fiftyIcon);
		fiftyDollars.setBorder(null);
		fiftyDollars.setOpaque(false);
		fiftyDollars.setContentAreaFilled(false);
		fiftyDollars.setActionCommand("fifty");
		fiftyDollars.addActionListener(this);
		
		hundredDollars = new JButton(hundredIcon);
		hundredDollars.setBorder(null);
		hundredDollars.setOpaque(false);
		hundredDollars.setContentAreaFilled(false);
		hundredDollars.setActionCommand("hundred");
		hundredDollars.addActionListener(this);
		
		//Create an area to hold the chips
		chipPane = new JPanel();
		BJWindow.add(chipPane, new Integer(3));
		chipPane.setBounds(gamesize.width-150, 0, 100, 785);
		chipPane.setVisible(true);
		chipPane.setBackground(new Color(0,80,0));
		JLabel chipLabel = new JLabel("Place your bet!");
		chipLabel.setForeground(new Color(212, 175, 55));
		chipPane.add(chipLabel);
		chipPane.add(oneDollar);
		chipPane.add(fiveDollars);
		chipPane.add(tenDollars);
		chipPane.add(twentyFiveDollars);
		chipPane.add(fiftyDollars);
		chipPane.add(hundredDollars);
		betLabel = new JLabel();
		betLabel.setForeground(new Color(212, 175, 55));
		betLabel.setText("<html><center><font size=4>Current bet:<br></font><font size=15>$" + wager + "</font></center></html>");
		chipPane.add(betLabel);
		
		betAgain = new JButton("Bet again?");
		betAgain.setBorder(null);
		betAgain.setBackground(new Color(0,80,0));
		betAgain.setForeground(new Color(212, 175, 55));
		betAgain.setBounds(gamesize.width-150, 750, 100, 30);
		betAgain.setVerticalTextPosition(AbstractButton.CENTER);
		betAgain.setHorizontalTextPosition(AbstractButton.CENTER);
		betAgain.setActionCommand("betAgain");
		betAgain.setEnabled(true);
		betAgain.addActionListener(this);
		betAgain.setFocusable(false);
		betAgain.setVisible(false);
		BJWindow.add(betAgain, new Integer(2));
		
		shoe = new Shoe(deckCount);
		dealer = new Dealer();
		you=new Player();
	}
	
	/**
	 * Takes an appropriate action based on the button pressed.
	 */
	public void actionPerformed(ActionEvent e) {
		if ("deal".equals(e.getActionCommand())) {
			disableAll();
			disableChips();
			dealButton.setEnabled(false);
			dealer.removeAll();
			you.removeAll();
			BJWindow.repaint();
			if(shoe.cardCount<(deckCount*13)){
				shoe.reset();
			}
			dealer.deal();
		}
		if ("hit".equals(e.getActionCommand())) {
			disableAll();
			you.hit();
	    }
		if ("stand".equals(e.getActionCommand())){
			disableAll();
			you.stand();
		}
		if ("split".equals(e.getActionCommand())){
			disableAll();
			you.split();
		}
		if ("surrender".equals(e.getActionCommand())){
			disableAll();
			if(!dealer.BJcheck())
				you.winAmount(((float) you.wager)/2);
			betAgain.setVisible(true);
		}
		if ("double".equals(e.getActionCommand())){
			disableAll();
			you.doubleDown();
		}
		 
		if ("insuranceYes".equals(e.getActionCommand())){
			insurancePane.setVisible(false);
			playerDeal();
			you.insurance(true);			 
		}
		if ("insuranceNo".equals(e.getActionCommand())){
			insurancePane.setVisible(false);
			playerDeal();
			you.insurance(false);
		}
		if("betAgain".equals(e.getActionCommand())){
			enableChips();
		}
		if ("one".equals(e.getActionCommand())) {
			wager++;
			if (wager>=5){
				enableDealButton();
			}
		}
		if ("five".equals(e.getActionCommand())) {
			wager=wager+5;
			enableDealButton();
		}
		if ("ten".equals(e.getActionCommand())) {
			wager=wager+10;
			enableDealButton();
		}
		if ("twentyFive".equals(e.getActionCommand())) {
			wager=wager+25;
			enableDealButton();
		}
		if ("fifty".equals(e.getActionCommand())) {
			wager=wager+50;
			enableDealButton();
		}
		if ("hundred".equals(e.getActionCommand())) {
			wager=wager+100;
			enableDealButton();
		}
		if (((you.walletAmount()-wager)<100)||(wager>900)){
			hundredDollars.setEnabled(false);
		}
		if (((you.walletAmount()-wager)<50)||(wager>950)){
			fiftyDollars.setEnabled(false);
		}
		if (((you.walletAmount()-wager)<25)||(wager>975)){
			twentyFiveDollars.setEnabled(false);
		}
		if (((you.walletAmount()-wager)<10)||(wager>990)){
			tenDollars.setEnabled(false);
		}
		if (((you.walletAmount()-wager)<5)||(wager>995)){
			fiveDollars.setEnabled(false);
		}
		if (((you.walletAmount()-wager)<1)||(wager>999)){
			oneDollar.setEnabled(false);
		}
		if ("rules".equals(e.getActionCommand())) {
			rulesToggle=!rulesToggle;
			rules.setVisible(rulesToggle);
			rulesCard.setVisible(!rulesToggle);
		}
		betLabel.setText("<html><center><font size=4>Current bet:<br></font><font size=15>$" + wager + "</font></center></html>");
	}
	
	/**
	 * Tells the player to deal for the current wager amount,
	 * then resets the wager for the next round.
	 */
	public static void playerDeal(){
		you.deal(wager);
		wager=0;
	}
	 
	/**
	 * Disables all the main game buttons.
	 */
	public static void disableAll(){
		dealButton.setEnabled(false);
		hitButton.setEnabled(false);
		standButton.setEnabled(false);
		splitButton.setEnabled(false);
		surrenderButton.setEnabled(false);
		doubleButton.setEnabled(false);
	}
	 
	/**
	 * Enables the Deal button.
	 */
	public static void enableDealButton(){
		dealButton.setEnabled(true);
	}
	
	/**
	 * Enables the Hit button.
	 */
	public static void enableHitButton(){
		hitButton.setEnabled(true);
	}
	
	/**
	 * Enables the Stand button.
	 */
	public static void enableStandButton(){
		standButton.setEnabled(true);
	}
	
	/**
	 * Enables the Split button.
	 */
	public static void enableSplitButton(){
		splitButton.setEnabled(true);
	}
	
	/**
	 * Enables the Surrender button.
	 */
	public static void enableSurrenderButton(){
		surrenderButton.setEnabled(true);
	}
	
	/**
	 * Enables the Double Down button.
	 */
	public static void enableDoubleButton(){
		doubleButton.setEnabled(true);
	}
	
	/**
	 * Enables the button that lets the player bet
	 * again for the next round.
	 */
	public static void enableBet(){
		betAgain.setVisible(true);
	}
	
	/**
	 * Enables the betting chips, but only the ones the Player can afford.
	 */
	private void enableChips(){
		betAgain.setVisible(false);
		if(you.walletAmount()>=1){
			oneDollar.setEnabled(true);
			if(you.walletAmount()>=5){
				fiveDollars.setEnabled(true);
				if(you.walletAmount()>=10){
					tenDollars.setEnabled(true);
					if(you.walletAmount()>=25){
						twentyFiveDollars.setEnabled(true);
						if(you.walletAmount()>=50){
							fiftyDollars.setEnabled(true);
							if(you.walletAmount()>=100){
								hundredDollars.setEnabled(true);
							}
						}
					}
				}
			}
		}
		chipPane.setVisible(true);
	}
	
	/**
	 * Shows the window asking the player if they want to bet insurance if they have enough money for it.
	 */
	public static void showInsurance(){
		disableAll();
		if(you.walletAmount()>(wager*2)){
			insurancePane.setVisible(true);
		} else {
			playerDeal();
		}
	}
	
	/**
	 * Disables the chips and hides them.
	 */
	private static void disableChips(){
		chipPane.setVisible(false);
		oneDollar.setEnabled(false);
		fiveDollars.setEnabled(false);
		tenDollars.setEnabled(false);
		twentyFiveDollars.setEnabled(false);
		fiftyDollars.setEnabled(false);
		hundredDollars.setEnabled(false);
	}
	
	/**
	 * Moves the specified card to the front of its layer of the game window.
	 * @param card The card to move to the front
	 */
	public static void moveCardToFront(Card card){
		BJWindow.moveToFront(card);
	}
	
	/**
	 * Adds the specified component to the specified layer of the game window.
	 * @param thing component to be added
	 * @param layer layer number to add it to
	 */
	public static void addThis(Component thing, int layer){
		BJWindow.add(thing, new Integer(layer));
	}
	
	/**
	 * Removes the specified component from the game window.
	 * @param thing component to be removed
	 */
	public static void removeThis(Component thing){
		BJWindow.remove(thing);
	}

	/**
	 * Creates the GUI and makes it visible, etc.
	 */
	private static void createAndShowGUI(){
		JFrame frame = new JFrame("JDavis presents: Blackjack!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(gamesize);
		JComponent newContentPane = new Blackjack();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				createAndShowGUI();
			}
		});
	}
}
