import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * ViewController()
 * Class contains all the mini action listener classes that listen to buttons on the GUI and then interact with 
 * the Model controller to follow through on tasks 
 * 
 * @author bains
 */
public class ViewController {
	private FlightView theView;
	private FlightController theModel;
	
	// ComboBox Listeners from the GUI 
    FlightComboBoxListener flightListener;
    DestinationSelectionComboBox destinationListener; 
    DateTimeSelectionComboBox dateTimeListener;
    PurchaseButton purchaseButtonListener;
    ConfirmationButton confirmationButtonListener;
    CancelTicketButton cancelButtonListener; 
    ConfirmCancelButton confirmCancelButtonListener; 
    RegisteredUserLoginButton registeredUserLoginListener; 
    
	/**
	 * Constructor receives the View and the Model to control 
	 * @param v: FlightView
	 * @param m: FlightController
	 */
	public ViewController(FlightView v, FlightController m){
		this.theView = v;
		this.theModel = m;
	     		
		// Initialize action listeners to the buttons on the GUI
        theView.addFlightComboBoxActionListener(flightListener);
        flightListener = new FlightComboBoxListener();
        destinationListener = new DestinationSelectionComboBox(); 
        dateTimeListener = new DateTimeSelectionComboBox(); 
        purchaseButtonListener = new PurchaseButton(); 
        confirmationButtonListener = new ConfirmationButton(); 
        cancelButtonListener = new CancelTicketButton(); 
        confirmCancelButtonListener = new ConfirmCancelButton(); 
        registeredUserLoginListener = new RegisteredUserLoginButton(); 
        
        theView.addCancelTicketButtonActionListener(cancelButtonListener); 
        theView.addregisteredUserLoginButtonActionListener(registeredUserLoginListener); 
        
        populateFlightComboBox();	// populate the origin flight combo box 

        theView.setVisible(true);
	}
	
    /**
     * getOFlights()
     * Get the origin flights from the Model 
     * @return origin flights: ArrayList<String>
     */
    public ArrayList<String> getOFlights() {
    	return theModel.getOFlights();
    }
    
    /**
     * populateFlightComboBox() 
     * method to populate flight selection box
     */
    private void populateFlightComboBox() {
        ArrayList<String> flightOptions = this.getOFlights();
        theView.removeFlightComboBoxActionListener(flightListener);
        theView.setFlightOptions(flightOptions);
        theView.addFlightComboBoxActionListener(flightListener);
    }    
    
    
    /**
    *
    * Flight ComboBoxListener to listen to origin flights selection 
    *
    */
   class FlightComboBoxListener implements ActionListener {

       @Override
       public void actionPerformed(ActionEvent e) {
           System.out.println("Flight Selected: " + theView.getFlightInput());
       
           ArrayList<String> destinationOptions = theModel.getDestinationFlights(theView.getFlightInput());
           
           // add destination options based on movie selected
           theView.removeDestinationComboBoxActionListener(destinationListener);
           theView.clearDestinationOptions();
           theView.setDestinationOptions(destinationOptions);
           theView.addDestinationComboBoxActionListener(destinationListener);
       }
   }
   
   /**
   *
   * DestinationComboBoxListener to listen to destination flight selection 
   *
   */
  class DestinationSelectionComboBox implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {

          String origin = theView.getFlightInput();
          String destination = theView.getDestinationInput();
          System.out.println("Destination Selected: " + destination);
          
          ArrayList<String> dateTimes = theModel.getFlightTimes(origin, destination);

          // add date and time options based on flight selected
          theView.removeDateTimeComboBoxActionListener(dateTimeListener);
          theView.clearDateTimeOptions();
          theView.setDateTimeOptions(dateTimes);
          theView.addDateTimeComboBoxActionListener(dateTimeListener);
          
      }
  }
  
  /**
  *
  * DateTimeComboBoxListener to listen to date and time combo for the flight  
  *
  */
  class DateTimeSelectionComboBox implements ActionListener {

     @Override
     public void actionPerformed(ActionEvent e) {
         String origin = theView.getFlightInput();
         String destination = theView.getDestinationInput();
         String dateTime = theView.getDateTimeInput(); 
         System.out.println("Date and Time Selected: " + dateTime);
         
         ArrayList<Flight> flightOptions = theModel.getFlightArray(); 
         Flight chosenflight = null;
         
         for(int i = 0; i < flightOptions.size(); i++) {					// find the chosen flight object 
        	 Flight temp = flightOptions.get(i);
        	 
        	 if(origin.equals(temp.getOrigin()) && destination.equals(temp.getDestination()) && dateTime.equals(temp.getDateTime())) {
        		 chosenflight = temp;
        	 }
         }
         
    	 theModel.setSeatsOnFlight(chosenflight.getFlightID());  			// set the seats on the flight given the flightIF
    	 theView.setSeatArrayList(theModel.getSeatsOnFlight());  			// set the seat array of the flights 
    	 
         int[][] seats = theModel.getSeats(origin, destination, dateTime);	// get the seats array [][]

         theView.setSeats(seats);
         theView.addPurchaseButtonActionListener(purchaseButtonListener);
     }
  }
  
  /**
  *
  * LISTENER TO PURCHASE BUTTON 
  *
  */
  class PurchaseButton implements ActionListener {

     @Override
     public void actionPerformed(ActionEvent e) {
    	 System.out.println("We ready to purchase now!");
    	 
    	 theView.setPurchasingInformation(); 								// set purchasing information in the GUI
    	 theView.addConfirmAllButtonListener(confirmationButtonListener);
     }
  }

  /**
  *
  * LISTENER TO CANCELLATION BUTTON 
  *
  */
  class CancelTicketButton implements ActionListener {

     @Override
     public void actionPerformed(ActionEvent e) {
    	 System.out.println("Proceeding to Ticket Cancellation.");
    	 theView.setCancelTicketInfo(); 									// set the cancellation information on the GUI 
    	 theView.addConfirmCancelButtonActionListener(confirmCancelButtonListener);
     }
  }
  
  /**
  *
  * LISTENER TO CONFIRM CANCELLATION BUTTON 
  *
  */
  class ConfirmCancelButton implements ActionListener {

     @Override
     public void actionPerformed(ActionEvent e) {    	 
    	 String ticketID = theView.cancelTicketIDField.getText(); 			// get cancellation ticket ID from user input 
    	 
    	 if(ticketID.equals("")) {											// check field is not empty 
    		 System.out.println("Please enter the ticket ID you want to cancel.");
    		 return; 
    	 }
    	 
    	 ticketID.replace(" ", "");    	 
    	 System.out.println("Cancelling Ticket ID: " + ticketID);
    	 theModel.deleteTicketFromDB(Integer.valueOf(ticketID)); 			// delete said ticket 
     }
  }
  /**
  *
  * LISTENER TO REGISTERED USER LOGIN BUTTON
  *
  */
  class RegisteredUserLoginButton implements ActionListener {

	     @Override
	     public void actionPerformed(ActionEvent e) {    	 
	    	  try {
				RegisteredLoginView login = new RegisteredLoginView();		// if user clicks on the Login Button, open the Login GUI 
			} catch (Exception e1) {
				e1.printStackTrace();
			}
	     }
	  }
  
  /**
  *
  * LISTENER TO CONFIRM PURCHASE ALL BUTTON 
  *
  */
  class ConfirmationButton implements ActionListener {

     @Override
     public void actionPerformed(ActionEvent e) {    	 
    	 ArrayList<JTextField> paymentFields = theView.paymentInfoArray; 
    	 
    	 for(int i = 0; i < paymentFields.size(); i++) {    		 		// get user input for payment info and ensure they are not empty 
    		 if(paymentFields.get(i).getText().equals("")) {
    			 System.out.println("Please Enter Infomration to Purchase Ticket."); 
    			 return; 
    		 }
    	 }
    	 
    	 System.out.println("Ticket Purchased! ");
    	 
    	 String origin = theView.getFlightInput();							// get user input and save 
         String destination = theView.getDestinationInput();
         String dateTime = theView.getDateTimeInput();
         String chosenSeatNum = theView.getChosenSeats(); 
         String fname = theView.paymentInfoArray.get(0).getText(); 
         String lname = theView.paymentInfoArray.get(1).getText();
         String email = theView.paymentInfoArray.get(2).getText(); 
         
         // sends all the appropriate information to the model to generate a ticket for the user 
    	 theModel.generateTicket(origin, destination, dateTime, chosenSeatNum, fname, lname, email);  	
    	 
     }
  } 
  
  
}
