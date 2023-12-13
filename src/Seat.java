// INSERT INTO  SEAT (SeatID, RowNum, Number, Tier, AircraftID) VALUES
import java.util.Random;


/**
 * Seat
 * Seat object for the flight 
 * 
 * @author bains
 */
public class Seat {
	private int col; 
	private char row;
	private String tier;
	private int aircraftID; 
	private double price=0; 
	private int seatID; 
	
	/**
	 * Constructor to make object 
	 * @param r: row
	 * @param c: column
	 * @param t: tier
	 * @param a: aircraftID
	 * @param sid: seatID
	 */
	public Seat(char r, int c, String t, int a, int sid) {
		row = r;
		col = c;
		tier = t;
		aircraftID = a;
		this.seatID = sid; 
	}

	
	/**
	 * getPrice() returns a ceiling rounded price of the seat 
	 * @return price: int
	 */
	public int getPrice() {
		if(this.price == 0) {
			price = calculatePrice(); 
		}
		return (int) Math.ceil(price); 
	}   
    
	/**
	 * calculatePrice() 
	 * Uses random to calculate cost of a seat depending on tier 
	 * @return base cost: double 
	 */
	private double calculatePrice() {
		Random random = new Random(); 								// generate random number for the cost of the seats 
		int min = 130;
        int max = 160;
        double base = random.nextDouble(max - min + 1) + min; 		// random number between 120-150 for economy 
        		
		if(tier.equals("Business")) {								// Business Class Cost
			return base * 2.2;
		}
		
		else if(tier.equals("Comfort")) {							// Comfort Class Cost 
			return base * 1.4; 
		}
		
		return base; 
		
	}
	
	/*
	 * ---------- BASIC GETTERS -----------
	 */
	public int getSeatID() {
		return this.seatID; 
	}
	
	public char getRow() {
		return this.row; 
	}
	
	public int getCol() {
		return this.col; 
	}
	
	/**
	 * convertString()
	 * Displays seat information to the terminal in a nice format 
	 */
	public void convertString() {
		System.out.println("Seat Info: " + this.seatID + " " + this.row + this.col); 
	}
}
