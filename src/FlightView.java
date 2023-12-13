import java.awt.*;
import java .awt. event .*;
import java.util.ArrayList;
import javax.swing.*; 

/**
 * FlightView
 * Class that contains all the objects that are displayed onto the GUI for the user 
 * This class is very messy because of the difficulty of setting a nice layout for the main GUI 
 * 
 * @author bains
 */
public class FlightView extends JFrame{
	// ArrayList's for combo box and other things being displayed on the GUI
    ArrayList<String> oFlightOptions = new ArrayList<String>();	
    ArrayList<String> destinationOptions = new ArrayList<String>();
    ArrayList<String> dateTimeOptions = new ArrayList<String>();
    ArrayList<Seat> seatOptions = new ArrayList<>(); 
    
    // Some other labels and combo boxes on the screen, mainly for the flight info 
	private JLabel generalFlightLabel = new JLabel("Flight Information: ");
	private JLabel originLabel = new JLabel("Origin");
	private JComboBox<String> flightSelectorComboBox = new JComboBox<>(); 
	private JLabel destLabel = new JLabel("Destination"); 
	private JComboBox<String> destinationSelectionComboBox = new JComboBox<>(); 
	private JLabel dateTimeLabel = new JLabel("Date & Time"); 
	private JComboBox<String> dateTimeSelectionComboBox = new JComboBox<>(); 	
	
	// Buttons and boxes related to the Purchasing Info and Text Fields
	private JButton purchaseButton = new JButton("Purchase Ticket"); 	
	private JButton confirmationButton = new JButton("Confirm Purchase"); 
	private JComboBox<String> expiryMonth = new JComboBox<>();
	private JComboBox<String> expiryYear = new JComboBox<>();
	private JLabel ticketAmount; 
	ArrayList<JTextField> paymentInfoArray = new ArrayList<>(); 	// order = fname, lname, email, credit number, ccv

	// Buttons and Boxes for Cancelling a Ticket 
	private JButton cancelButton = new JButton("Cancel Ticket");
	JTextField cancelTicketIDField = new JTextField(15);
	private JButton confirmCancelButton = new JButton("Confirm Cancellation");
	private JButton registeredUserLoginButton = new JButton("Registered User Login");
	
	JButton[][] seats;
	JPanel seatPanel = new JPanel();
	JPanel purchaseInfoPanel = new JPanel(); 
    
	
	/**
	 * FlightView()
	 * Constructor makes the basic frame 
	 */
	FlightView(){ 
		this.setTitle("Flight Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(850, 500);
        this.setLayout(new FlowLayout());
        
        seats = new JButton[8][4];

		this.setTitle("Flight Application");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(850,800);
		this.setLayout(new FlowLayout());
		
        dropdownMenuSetup();
	    createSeats();
	    addPurchasingInformation(); 
	    
        setVisible(true);
	}
	
	/*
	 * 		------ SET UP THINGS -------
	 */
	
	/**
	 * dropdownMenuSetup()
	 * Add the basic boxes to the main screen, mainly the flight things 
	 */
	private void dropdownMenuSetup() {        
		flightSelectorComboBox.setPreferredSize(new Dimension(150, 30));
		destinationSelectionComboBox.setPreferredSize(new Dimension(150, 30));
		dateTimeSelectionComboBox.setPreferredSize(new Dimension(150, 30));
		
	    destinationSelectionComboBox.setEnabled(false);
	    dateTimeSelectionComboBox.setEnabled(false);
	    
		add(generalFlightLabel); 
		add(originLabel); 
		add(flightSelectorComboBox);
		add(destLabel);
		add(destinationSelectionComboBox);
		add(dateTimeLabel);
		add(dateTimeSelectionComboBox);
        
		flightSelectorComboBox.setModel(new DefaultComboBoxModel(oFlightOptions.toArray()));
        flightSelectorComboBox.setFocusable(false);
        flightSelectorComboBox.setSelectedIndex(-1);
        flightSelectorComboBox.setEnabled(false);
	}
	
    /**
     * createSeats() 
     * create array of JButtons which will represent the seats on our plane
     */
	public void createSeats() {
	    int width = 50;
	    int height = 30;
	    
	    // General labelling for the seat related items 
	    add(Box.createRigidArea(new Dimension(850, height))); 			
	    JLabel seatTierLabel = new JLabel("\nRow A: Business Class  |  Row B: Comfort  |  Row C & D = Economy");    
	    add(seatTierLabel);   
	   
	    JLabel leftLabel = new JLabel("Left Wing", SwingConstants.LEFT);
	    JLabel rightLabel = new JLabel("Right Wing", SwingConstants.RIGHT);
	    
	    add(Box.createRigidArea(new Dimension(850, height))); 
	    add(leftLabel);
	    add(Box.createRigidArea(new Dimension(85, height))); 
        
	    // Actual seat panel 
	    seatPanel.setLayout(new GridLayout(4, 2)); 								// Set GridLayout for 4 rows and 2 column	   	    	    
        add(seatPanel); 

	    add(Box.createRigidArea(new Dimension(85, height))); 
	    add(rightLabel);
	    
        for (int i = 0; i < 4; i++) { 											// Iterate over 4 rows
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));

            for (int j = 0; j < 2; j++) { 										// For the two columns in each row
                JButton btn = new JButton("" + (char) (i + 65) + (j + 1)); 		// Adjust seat label                
                btn.setEnabled(false);
                btn.setMinimumSize(new Dimension(width, height));
                btn.setMaximumSize(new Dimension(width, height));

                btn.addActionListener(new seatButtonColorChangeListener());

                rowPanel.add(btn);

                if (j < 1) {
                    // Add a gap between the seats
                    rowPanel.add(Box.createRigidArea(new Dimension(width, height)));
                }
                seats[i][j] = btn;
            }
            seatPanel.add(rowPanel);
        }
        // Adding more elements to the seat panel and general panel and disabling/enabling buttons 
        add(Box.createRigidArea(new Dimension(850, height))); 
        seatPanel.setVisible(true);
        add(this.purchaseButton); 
        cancelButton.setEnabled(true); 
        add(this.cancelButton);
        this.setPurchaseButton();  
        registeredUserLoginButton.setEnabled(true);
        add(this.registeredUserLoginButton);
        
        // ADDING CANCELLATION STUFF TO THE MAIN GUI -- Adding it here for the layout 
        add(Box.createRigidArea(new Dimension(850, 20))); 
        add(new JLabel("Ticket ID To Cancel:"));
        this.cancelTicketIDField.setEditable(false); 
        add(this.cancelTicketIDField); 
        this.confirmCancelButton.setEnabled(false);
        add(this.confirmCancelButton);

    }

    /**
     * addPurchasingInformation()
     * Adds purchasing labels and text boxes to a purchasing information panel, none of them are enabled 
     */
    public void addPurchasingInformation() {
        add(Box.createRigidArea(new Dimension(850, 60))); 
    	add(new JLabel("Please Enter Purchasing Information"));
        add(Box.createRigidArea(new Dimension(850, 5))); 

    	JLabel fname = new JLabel("First Name: "); 
    	JLabel lname = new JLabel("Last Name: "); 
    	JLabel email = new JLabel("Email: ");
    	JLabel creditNum = new JLabel("Credit Card Number: ");
    	JLabel creditCCV = new JLabel("CCV: ");

    	JLabel expiryMonthLabel = new JLabel("Expiry Month: ");
    	JLabel expiryYearLabel = new JLabel("Expiry Year: "); 
    	
    	JTextField fnameField = new JTextField(20);
    	JTextField lnameField = new JTextField(20);
    	JTextField emailField = new JTextField(20);
    	JTextField creditNumField = new JTextField(20);
    	JTextField creditCCVField = new JTextField(3);
    	
    	// Add all the text boxes to an array of text boxes so it will be easier to read them 
    	// Order: first name, last name, email, credit card number, credit card ccv -- Order they displayed on the GUI
    	paymentInfoArray.add(fnameField);
    	paymentInfoArray.add(lnameField); 
    	paymentInfoArray.add(emailField);
    	paymentInfoArray.add(creditNumField);
    	paymentInfoArray.add(creditCCVField);    	
    	
    	purchaseInfoPanel.setLayout(new GridLayout(13, 8));
    	add(purchaseInfoPanel); 

    	// Add all the labels and text fields 
    	purchaseInfoPanel.add(fname); 
    	purchaseInfoPanel.add(fnameField); 
    	purchaseInfoPanel.add(lname); 
    	purchaseInfoPanel.add(lnameField); 
    	purchaseInfoPanel.add(email);     
    	purchaseInfoPanel.add(emailField); 
    	purchaseInfoPanel.add(creditNum);
    	purchaseInfoPanel.add(creditNumField); 
    	purchaseInfoPanel.add(creditCCV);
    	purchaseInfoPanel.add(creditCCVField);
    	
    	for(int i = 0; i < paymentInfoArray.size(); i++) {						// Cannot edit text fields until we press the "Purchase" Button
    		paymentInfoArray.get(i).setEditable(false);

    	}
    	
    	// Values for the combo boxes for credit card information, month and year
    	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
    			"October", "November", "December"};
    	String[] years = {"2024", "2025", "2026"}; 
    	
    	expiryMonth.setModel(new DefaultComboBoxModel<String>(months)); 		// add & disable all the boxes
    	expiryMonth.setSelectedIndex(-1);
    	expiryMonth.setEnabled(false);
    	expiryYear.setModel(new DefaultComboBoxModel<String>(years));
    	expiryYear.setSelectedIndex(-1);
    	expiryYear.setEnabled(false);
    	
    	purchaseInfoPanel.add(expiryMonthLabel);								// Add combo boxes and labels to the GUI
    	purchaseInfoPanel.add(expiryMonth); 
    	purchaseInfoPanel.add(expiryYearLabel);
    	purchaseInfoPanel.add(expiryYear);
    	
    	purchaseInfoPanel.setVisible(true);
    	
        add(Box.createRigidArea(new Dimension(60, 10))); 
    	add(this.confirmationButton); 
        this.setConfirmAllButton();
    }
    
    
    /**
     * setPurchasingInformation()
     * Actually enables all the purchasing information for the user and displays the price at the bottom somewhere 
     * Note to self: Error Prone Method
     */
    public void setPurchasingInformation() {
    	for(int i = 0; i < paymentInfoArray.size(); i++) {
    		paymentInfoArray.get(i).setEditable(true);
    	}
    	expiryYear.setEnabled(true);
    	expiryMonth.setEnabled(true);   	
    	confirmationButton.setEnabled(true);
		int price = 0; 

		for(int i = 0; i < seatOptions.size(); i++) {
			Seat tempSeat = seatOptions.get(i);
			String chosenSeat = getChosenSeats().replaceAll("\\s", ""); 
			String temp = seatOptions.get(i).getRow() + Integer.toString(seatOptions.get(i).getCol()) ; 			
			temp.replaceAll("\\s", "");
			
			if(chosenSeat.equals(temp)) {
				price = tempSeat.getPrice(); 
			}
		}
		
		JLabel seatCost = new JLabel("Cost of Seat: $" + price + ".00"); 
    	add(seatCost); 
    }
    
    /*
     * 		----- SEAT RELATED NONSENSE ------
     */
    
    /**
     * disableAllSeats()
     * set all the seats to disabled 
     */
    public void disableAllSeats() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                seats[i][j].setEnabled(false);
                seats[i][j].setBackground(null);
            }
        }
    }
    
    /**
     * seatSeats() 
     * Set seats based on an input grid that shows if each seat is taken or not.
     * @param flighttimeSeats: int[][]
     */
    public void setSeats(int[][] flighttimeSeats) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                this.seats[i][j].setEnabled(flighttimeSeats[i][j] == 1);
            }
        }
        this.setVisible(true);
    }
    
    /**
     * getChosenSeats()
     * Returns the row and column of the chosen seat 
     * @return chosenSeat: String
     */
    public String getChosenSeats() {
        String chosenSeat = ""; 
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
            	if(seats[i][j] == null) {
            		continue; 
            	}
            	else if(this.seats[i][j].getBackground() == Color.green) {
            		chosenSeat = this.seats[i][j].getText(); 
            	}
            }
        }		
    	return chosenSeat;
    }
    
    /*
     *  ------ CONFIRMATION (ALL) BUTTON THINGS ------
     */
 	/**
 	 * setConfirmAllButton()
 	 * Enable confirm all button 
 	 */
 	public void setConfirmAllButton() {
 		confirmationButton.setEnabled(false);
     }
 	
     /**
      * removeConfirmAllButton()
      * Remove action listener from confirm all button 
     * @param a: ActionListener
     */
    public void removeConfirmAllButton(ActionListener a) {
    	 confirmationButton.removeActionListener(a);
     }
     
 	/**
 	 * addConfirmAllButtonListener()
 	 * Adds action listener to the confirm all button
 	 * @param a: Action Listener 
 	 */
 	public void addConfirmAllButtonListener(ActionListener a) {
 		confirmationButton.addActionListener(a);
 	}
 	
    /*
     *  --------- PURCHASE BUTTON ----------
     */
 	/**
 	 * setPurchaseButton()
 	 * disables the purchase button 
 	 */
 	public void setPurchaseButton() {
 		purchaseButton.setEnabled(false);
     }
 	
     /**
      * removePurchaseButton()
      * Removes the action listener from the button 
     * @param a: ActionListener
     */
    public void removePurchaseButton(ActionListener a) {
    	 purchaseButton.removeActionListener(a);
     }
     
 	/**
 	 * addPurchaseButtonActionListener()
 	 * Add action listener to the purchase button 
 	 * @param a: ActionListener
 	 */
 	public void addPurchaseButtonActionListener(ActionListener a) {
 		purchaseButton.addActionListener(a);
 	}
    
 	/*
 	 *  --------- CANCEL TICKET BUTTON TO SHOW FIELDS ----------
 	 */
 	/**
 	 * addCancelTicketButtonActionListener()
 	 * Adds an action listener to the cancel button 
 	 * @param a: ActionListener
 	 */
 	public void addCancelTicketButtonActionListener(ActionListener a) {
 		cancelButton.addActionListener(a);
 	}
 	
 	/**
 	 * setCancelTicketInfo()
 	 * Enables the cancel ticket ID field and the confirming cancel button 
 	 */
 	public void setCancelTicketInfo() {
 		this.cancelTicketIDField.setEditable(true);
 		this.confirmCancelButton.setEnabled(true);
 		
 	}
 	
 	public void addConfirmCancelButtonActionListener(ActionListener a) {
 		confirmCancelButton.addActionListener(a); 
 	}
 	
 	/*
 	 *  ----------- LOGIN AS REGISTERED USER -----------
 	 */
 	/**
 	 * addregisteredUserLoginButtonActionListener()
 	 * Adds action listener to the login as a registered user button on the main panel 
 	 * @param a: Action Listener
 	 */
 	public void addregisteredUserLoginButtonActionListener(ActionListener a) {
 		registeredUserLoginButton.addActionListener(a);
 	}
 	
 	/*
 	 *  ---------- ORIGIN FLIGHTS THINGS -----------
 	 */
	/**
	 * setFlightOptions
	 * Sets the combo box with the origin flights 
	 * @param origin: ArrayList<String>
	 */
	public void setFlightOptions(ArrayList<String> origin) {
		this.oFlightOptions = origin; 
		flightSelectorComboBox.setModel(new DefaultComboBoxModel(oFlightOptions.toArray()));
		flightSelectorComboBox.setSelectedIndex(-1);
		flightSelectorComboBox.setEnabled(true);
    }
	
    /**
     * removeFlightComboBoxActionListener()
     * @param a: ActionListener
     */
    public void removeFlightComboBoxActionListener(ActionListener a) {
        flightSelectorComboBox.removeActionListener(a);
    }
    
	/**
	 * addFlightBoxComboBoxActionListener()
	 * Adds an action listener to the combo box for origin flights 
	 * @param a: ActionListener
	 */
	public void addFlightComboBoxActionListener(ActionListener a) {
        flightSelectorComboBox.addActionListener(a);
	}
	
    /**
     * getFlightInput()
     * Gets the input origin flight 
     * @return Flight: String
     */
    public String getFlightInput() {
        return (flightSelectorComboBox.getSelectedItem() == null) ? "null" : flightSelectorComboBox.getSelectedItem().toString();
    }
    
    /*
     *  -------- DESTINATION FLIGHT THINGS ----------
     */
    /**
     * setDestinationOptions()
     * sets the destination flight options for the combo box 
     * @param dests: ArrayList
     */
    public void setDestinationOptions(ArrayList<String> dests) {
		this.destinationOptions = dests; 
	   	destinationSelectionComboBox.setModel(new DefaultComboBoxModel(destinationOptions.toArray()));
    	destinationSelectionComboBox.setSelectedIndex(-1);
    	destinationSelectionComboBox.setEnabled(true);
    }
    
    /**
     * removeDestinationComboBoxActionListener()
     * @param a: ActionListener
     */
    public void removeDestinationComboBoxActionListener(ActionListener a) {
		destinationSelectionComboBox.removeActionListener(a);
    }
    
	/**
	 * addDestinationComboBoxActionListener() 
	 * @param a: ActionListener
	 */
	public void addDestinationComboBoxActionListener(ActionListener a) {
		destinationSelectionComboBox.addActionListener(a);
    }
	
    /**
     * getDestinationInput()
     * @return flight destination: String
     */
    public String getDestinationInput() {
        return (destinationSelectionComboBox.getSelectedItem() == null) ? "null" : destinationSelectionComboBox.getSelectedItem().toString();
    }
    
    /**
     * clearDestinationOptions()
     * Clear the destination options if the origin flights change 
     */
    public void clearDestinationOptions() {
    	this.destinationOptions = new ArrayList<String>();
    	destinationSelectionComboBox.setModel(new DefaultComboBoxModel(destinationOptions.toArray()));
    	destinationSelectionComboBox.setSelectedIndex(-1);
    	destinationSelectionComboBox.setEnabled(false);
    }
    
    /*
     *  --------- DATE AND TIME THINGS ----------
     */
    /**
     * setDateTimeOptions()
     * Given the destination and origin flights, set the date and times combo box 
     * @param dateTimes: ArrayList<String>
     */
    public void setDateTimeOptions(ArrayList<String> dateTimes) {
		this.dateTimeOptions = dateTimes; 
		dateTimeSelectionComboBox.setModel(new DefaultComboBoxModel(dateTimeOptions.toArray()));
		dateTimeSelectionComboBox.setSelectedIndex(-1);
		dateTimeSelectionComboBox.setEnabled(true);
    }
    
    /**
     * removeDateTimeComboBoxActionListener()
     * @param a: ActionListener
     */
    public void removeDateTimeComboBoxActionListener(ActionListener a) {
    	dateTimeSelectionComboBox.removeActionListener(a);
    }
    
	/**
	 * addDateTimeComboBoxActionListener
	 * @param a: ActionListener
	 */
	public void addDateTimeComboBoxActionListener(ActionListener a) {
		dateTimeSelectionComboBox.addActionListener(a);
    }
	
    /**
     * getDateTimeInput()
     * @return date + time: String
     */
    public String getDateTimeInput() {
        return (dateTimeSelectionComboBox.getSelectedItem() == null) ? "null" : dateTimeSelectionComboBox.getSelectedItem().toString();
    }
    
    /**
     * clearDateTimeOptions()
     * clear the options if the user picks different flights  
     */
    public void clearDateTimeOptions() {
    	this.dateTimeOptions = new ArrayList<String>();
    	dateTimeSelectionComboBox.setModel(new DefaultComboBoxModel(dateTimeOptions.toArray()));
    	dateTimeSelectionComboBox.setSelectedIndex(-1);
    	dateTimeSelectionComboBox.setEnabled(false);
    }
    
    /*
     *  ------------- SEAT THINGS -------------	
     */
    /**
     * setSeatArrayList() 
     * @param seatlist: ArrayList 
     */
    public void setSeatArrayList(ArrayList<Seat> seatlist) {
    	this.seatOptions = seatlist; 
    }
    
    
    /**
     * seatButtonColorChangeListener
     * Action listener class for the seat buttons
     * Includes logic to make sure only one seat can be selected 
     * 
     * @author bains
     */
    class seatButtonColorChangeListener implements ActionListener {
                
        public void actionPerformed(ActionEvent e) {
        	JButton x = (JButton) e.getSource();
        	
        	String prevButtonName = getChosenSeats(); 											// finds the previous button 
        	
        	// logic to turn the previous button off so we can only select one ticket 
        	if(prevButtonName != x.getText() && !prevButtonName.equals("")) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 2; j++) {
                    	if(prevButtonName.equals(seats[i][j].getText())) {
                    		seats[i][j].setBackground(null);
                    	}
                    }
                }
        	}
        	
        	if (x.getBackground() == Color.green) {
                x.setBackground(null);
            } else {
                x.setBackground(Color.green);
            }
        	
        	System.out.println("Seat Selected: " + x.getText());  
            purchaseButton.setEnabled(x.getBackground() == Color.green);						// enable the purchase button after we have selected a seat 
            }
    }


}
