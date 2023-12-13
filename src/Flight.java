import java.util.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;

/**
 * Flight
 * Flight class contains all the specific flight info 
 * 
 * @author bains
 */
public class Flight {
	private int flightID; 
	private String origin;
	private String destination;
	private Date date;
	private Time time; 
	
	/**
	 * Constructor for Flight object 
	 * @param f: flightID, int
	 * @param o: origin, String
	 * @param d: destination, String
	 * @param date: Date
	 * @param t: Time
	 */
	Flight(int f, String o, String d, Date date, Time t){
		flightID = f;
		origin = o;
		destination = d;
		this.date = date;
		time = t; 
	}
	
	/*
	 * 	---------- GETTERS -----------
	 */
	public String getOrigin() {
		return this.origin;
	}
	
	public String getDestination() {
		return this.destination; 
	}
	
	/**
	 * getDateTime() puts the date and time objects into a String format and combines them
	 * @return date and time, String
	 */
	public String getDateTime() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");		
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = getTime().toLocalTime();
        
		String formattedTime = localTime.format(timeFormat);
        String dateString = dateFormat.format(this.getDate());
		
		return dateString + " " + formattedTime; 
	}
	
	public Date getDate() {
		return this.date; 
	}
	
	public Time getTime() {
		return this.time; 
	}
	
	public int getFlightID() {
		return this.flightID; 
	}
	
	/**
	 * toString() prints to terminal the flight information conveniently 
	 */
	public String toString() {
		System.out.println("Flight: " + flightID 
				+ " Origin: " + this.origin 
				+ " Destination: " + this.destination
				+ " Date: " + this.date
				+ " Time: " + this.time); 
		return null; 
	}
	
}
