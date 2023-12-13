import java.sql.SQLException;
import java.util.ArrayList;

/**
 * FlightController()
 * Essentially the model controller and controls information relating to any flight or database needs 
 * 
 * @author bains
 */
public class FlightController {
	private ArrayList<Flight> flights;
	private ArrayList<String> destinationFlights; 
	private ArrayList<String> flightTimes; 
	private ArrayList<Seat> seatsOnFlight; 
	
	DatabaseController db; 									// database connection 

	/**
	 * Default Constructor that calls our database to grab all the flights from database
	 * @throws Exception 
	 */
	public FlightController() throws Exception {
		db = DatabaseController.createInstance(); 
		
		ArrayList<Flight> flightsdb = db.selectAllFlights(); 
		this.flights = flightsdb;
		
		setMaxTicketID(); 					// set the max ticketID so that we can increase the value from there 
	}
	
	
	/**
	 * getOFlights()
	 * Creates and returns a list of the possible Origins from the Flights list 
	 * @return Origin Flights: ArrayList<String>
	 */
	public ArrayList<String> getOFlights() {
		ArrayList<String> oflights = new ArrayList<>();
		String tempFlight;
		
		for(int i = 0; i < flights.size(); i++) {
			tempFlight = flights.get(i).getOrigin(); 
			
			if(oflights.indexOf(flights.get(i)) == -1) {
				oflights.add(tempFlight); 
			}
		}
		return oflights;
	}
	
	
	/**
	 * getDestinationFlights()
	 * Given an origin flight, finds all the associated destinations and adds them to an ArrayList to return 
	 * @param originFlight: String
	 * @return DestinationFlights: ArrayList<String>
	 */
	public ArrayList<String> getDestinationFlights(String originFlight) {
		destinationFlights = new ArrayList<>();
		
		for(int i = 0; i < flights.size(); i++) {
			if(flights.get(i).getOrigin().equals(originFlight))
			destinationFlights.add(flights.get(i).getDestination()); 
		}
		return destinationFlights;
	}
	
	/**
	 * getFlightTimes()
	 * Given an origin flight and destination flight, find all the date and times and add them to a list to return 
	 * @param originFlight: String
	 * @param destinationFlight: String
	 * @return FlightTimes: ArrayList<String>
	 */
	public ArrayList<String> getFlightTimes(String originFlight, String destinationFlight) {
		flightTimes = new ArrayList<>();
		
		for(int i = 0; i < flights.size(); i++) {
			if(flights.get(i).getOrigin().equals(originFlight) && flights.get(i).getDestination().equals(destinationFlight))
				flightTimes.add(flights.get(i).getDateTime()); 
		}
		return flightTimes;
	}
	
	/**
	 * setSeatsOnFlight()
	 * Given a particular flightID, creates a list of all the seats on the plane from the database 
	 * @param flightID: int
	 */
	public void setSeatsOnFlight(int flightID) {
		try {
			this.seatsOnFlight = db.seatsOnFlight(flightID);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * generateTicket()
	 * Given all the information from the user, we find the flight object, the seat object and generate a user object 
	 * for the buyer and create a ticket from it and send that to the database to save 
	 * 
	 * Note to self: A highly error prone function. 
	 * 
	 * @param origin: String
	 * @param destination: String
	 * @param dateTime: String
	 * @param chosenSeatNum: String
	 * @param fname: String
	 * @param lname: String
	 * @param email: String
	 */
	public void generateTicket(String origin, String destination, String dateTime, String chosenSeatNum, 
			String fname, String lname, String email) {
		int flightID = 0; 	
		Flight chosenFlight = null; 													// flight object 
		Seat chosenSeat = null; 														// seat object 
		
		for(int i = 0; i < flights.size(); i++) {
			Flight tempFlight = flights.get(i); 
			if(destination.equals(tempFlight.getDestination()) && origin.equals(tempFlight.getOrigin()) 
					&& dateTime.equals(tempFlight.getDateTime())) {
				chosenFlight = tempFlight; 
				flightID = tempFlight.getFlightID(); 
			}
		}
		
		for(int i = 0; i < seatsOnFlight.size(); i++) {
			Seat tempSeat = seatsOnFlight.get(i);
			chosenSeatNum.replaceAll("\\s", ""); 										// just in case we got some weird business going on		
			String temp = seatsOnFlight.get(i).getRow() + Integer.toString(seatsOnFlight.get(i).getCol()) ; 
			temp.replaceAll("\\s", "");													// just in case of weird business #2 
						
			if(chosenSeatNum.equals(temp)) {
				chosenSeat = tempSeat; 
			}
		}
				
		RegularUser person = new RegularUser(fname, lname, email);						// person object 
		Ticket ticketPurchase = new Ticket(person, chosenSeat, chosenFlight); 			// generate ticket object from all the information we have gathered 
		
		try {
			db.saveTicket(ticketPurchase, chosenSeat, chosenFlight);					// save the ticket into database 
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		EmailConnection.emailSetup(ticketPurchase, chosenSeat, chosenFlight, person); 	// send the user an email 
		
	}
	
	/**
	 * getSeats()
	 * Gets the seat grid from the database for a given flight and returns for the View 
	 * @param origin: String
	 * @param destination: String
	 * @param dateTime: String
	 * @return seats: int[0][0]
	 */
	public int[][] getSeats(String origin, String destination, String dateTime){
		int flightID = 0;
		
		for(int i = 0; i < flights.size(); i++) {
			if(flights.get(i).getOrigin().equals(origin) && flights.get(i).getDestination().equals(destination) 
					&& flights.get(i).getDateTime().equals(dateTime))
				flightID = flights.get(i).getFlightID(); 
		}
		
		return db.seatGrid(flightID);
	}
	
	/**
	 * deleteTicket()
	 * Delete ticket from the database 
	 * @param ticketID: int
	 */
	public void deleteTicketFromDB(int ticketID) {
		db.deleteTicket(ticketID);
	}
	
	/**
	 * getFlightArray()
	 * @return flights: ArrayList<Flight>
	 */
	public ArrayList<Flight> getFlightArray(){
		return this.flights; 
	}
	
	/**
	 * setMaxTicketID()
	 * Grabs the max ticketID from the database and sets it in the Ticket object, where ticketID is a static variable 
	 */
	public void setMaxTicketID() {
		int ticketID = db.getMaxTicketID(); 
		Ticket.setTicketCounter(ticketID); 
	}
	
	/**
	 * getSeatsOnFlight()
	 * Getter for the ArrayList of seats on the a particular flight 
	 * @return Seats: ArrayList<Seat>
	 */
	public ArrayList<Seat> getSeatsOnFlight() {
		return this.seatsOnFlight; 
	}
	
	
}
