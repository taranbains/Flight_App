import java.util.ArrayList;

/**
 * Ticket()
 * Ticket object for the flight 
 * 
 * @author bains
 */
public class Ticket {
	private RegularUser user;
	private Seat seat;
	private Flight flight;
	private int ticketID; 	
	private static int ticketCounter = 1;								// static ticket counter so we don't 
	private ArrayList<Integer> ticketInfo = new ArrayList<>();			// stores the ticket info in a convenient ArrayList to put into database
	
	/**
	 * Constructor to 
	 * @param user: User
	 * @param seat: Seat
	 * @param flight: Flight
	 */
	public Ticket(RegularUser user, Seat seat, Flight flight) {
		this.user = user;
		this.seat = seat;
		this.flight = flight;
		this.ticketID = ticketCounter; 
		
		// save ticket info as the same order that is in the database 
		// order stored in database: ticketID, seatID, flightID, membershipID, price 	
		ticketInfo.add(ticketID); 
		ticketInfo.add(seat.getSeatID()); 
		ticketInfo.add(flight.getFlightID());
		ticketInfo.add(null); 											// membership number is currently empty 
		ticketInfo.add(seat.getPrice()); 
		
		ticketCounter++; 
	}
	
	/**
	 * setTicketCounter() based on the current max ticket in database 
	 * @param maxTicketID: int
	 */
	static public void setTicketCounter(int maxTicketID) {
		ticketCounter = ++maxTicketID; 
	}
	
	/**
	 * getTicketID() 
	 * @return ticketID: int
	 */
	public int getTicketID() {
		return this.ticketID; 
	}
	
	/**
	 * getTicketInfo() 
	 * @return ticketInfo: ArrayList
	 */
	public ArrayList<Integer> getTicketInfo(){
		return this.ticketInfo; 
	}	
}
