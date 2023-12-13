import java.io.*;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;


/**
 * Class DatabaseController
 * Connects to MySQL database and implements the Singleton Design Pattern to maintain one single 
 * connection to the database only and performs necessary queries to the database. 
 * 
 * @author bains
 */
public class DatabaseController {
    private static DatabaseController instance;									// the one database instance 
	private Connection con;							
	
	/**
	 * DatabaseController() constructor is private. Makes the one connection to the database 
	 */
	private DatabaseController(){				
		try {
			String url = "jdbc:mysql://127.0.0.1:3306/Flight_Database"; 		// table details
	        String username = "ensf607"; 										// MySQL credentials that have privileges to access database  
	        String password = "student";
	        
	        Class.forName("com.mysql.cj.jdbc.Driver"); 								
	        
	        con = DriverManager.getConnection(url, username, password);			// connection object to mqsyl database
	        
	        System.out.println("Connection to Database Successfully.\n");
		}
		catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Create Single instance of the object, Singleton design pattern with only one connection to the database
	 * @return instance: DatabaseController
	 * @throws Exception
	 */
	public static DatabaseController createInstance() throws Exception {
        if(instance == null) {
        	instance = new DatabaseController(); 
        	return instance;
        }
        else return instance; 
    }  
	
	/**
	 * selectAllFlights() 
	 * Retrieves all the flights from the database, makes them into objects and adds them to a list
	 * @return flights: ArrayList<Flights>
	 * @throws SQLException
	 */
	public ArrayList<Flight> selectAllFlights() throws SQLException{
        Statement st = con.createStatement();								// statement object for the current database connection
        String query = "SELECT * FROM Flight"; 								// select all from flight 						
        ResultSet rs = st.executeQuery(query); 								       
        
		ArrayList<Flight> FlightList = new ArrayList<>();  					// ArrayList of Flight objects 

        while (rs.next()) {
            // retrieve the data 
        	int flightID = rs.getInt("FlightID"); 
        	String origin = rs.getString("Origin");
        	String destination = rs.getString("Destination");
        	Date date = rs.getDate("Date");
        	Time time = rs.getTime("Time"); 
        	
        	// create a flight object 
        	Flight flight = new Flight(flightID, origin, destination, date, time);         	
        	
        	// add object to array list 
            FlightList.add(flight); 
        }
        
        st.close(); 
		return FlightList;
		
	}

	 /**
	 * seatsOnFlight()
	 * Generate a list of all the seats on a specific flight
	 * From the flightID we find the aircraftID and from the aircraft, we can grab all the seats 
	 * @param flightID: int
	 * @return seats: ArrayList<seat>
	 * @throws SQLException
	 */
	public ArrayList<Seat> seatsOnFlight(int flightID) throws SQLException{
		Statement st = con.createStatement();									// statement object for the current database connection		
		String query = "SELECT * FROM Flight Where FlightID = " + flightID; 	// find flight 						
        ResultSet rs = st.executeQuery(query); 								       
        int aircraftID = 0;
        
        if(rs.next()) {
        	aircraftID = rs.getInt("AircraftID");								// find aircraftID for the flight
        }
        
        query = "SELECT * FROM Seat WHERE AircraftID = " + aircraftID;			// find the seats on the aircrafts 
        rs = st.executeQuery(query);
        
		ArrayList<Seat> SeatList = new ArrayList<>();  							// ArrayList of seats

        while (rs.next()) {														// retrieve data 
        	int seatID = rs.getInt("SeatID"); 
        	int column = rs.getInt("Col"); 
        	char row = rs.getString("RowLetter").charAt(0);
        	String tier = rs.getString("Tier"); 
        	
        	Seat seat = new Seat(row, column, tier, aircraftID, seatID);  		// create seat object 
            SeatList.add(seat); 
        }
        
        st.close(); 
		return SeatList;
	}
	
	
    /**
     * seatGrid()
     * Find the tickets associated to a particular flight, and check if the seats on the flight are taken or not 
     * @param flightID: int
     * @return seatGrid: int[][]
     */
    public int[][] seatGrid(int flightID) {
        ArrayList<Integer> tickets = ticketsAtFlighttime(flightID);
        ArrayList<Integer> row = new ArrayList<Integer>();
        ArrayList<Integer> col = new ArrayList<Integer>();
        int[][] seatGrid = new int[4][2];														// aircrafts are 4 rows by 2 columns 
        		
        try {
        	for (int i = 0; i < tickets.size(); i++) {

                String query = "SELECT Seat.Col, Seat.RowLetter FROM Ticket, Seat "
                		+ "WHERE  Ticket.SeatID = Seat.SeatID AND Ticket.FlightID = ?"; 		// query to find the tickets on a flight
                PreparedStatement myStmt = con.prepareStatement(query);							
                myStmt.setInt(1, tickets.get(i));
                                
                ResultSet results = myStmt.executeQuery();
                while (results.next()) {
                    row.add((int)results.getString("RowLetter").charAt(0) - 65); 				// adding row and column numbers to the ArrayList
                    col.add(results.getInt("Col") - 1);                   
                }
            }

            for (int i = 0; i < 4; i++) {														// default set the seatgrid to 2
                for (int j = 0; j < 2; j++) {						
                    seatGrid[i][j] = 2;
                }
            }
            
            for (int i = 0; i < 4; i++) {														// if the row+column seat has been taken, set to 0
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < row.size(); k++) {                       
                        if (i == row.get(k) && j == col.get(k)) {
                            seatGrid[i][j] = 0;
                        }
                        
                        if (seatGrid[i][j] != 0) {
                            seatGrid[i][j] = 1;
                        }
                    }
                }
            }
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        if (tickets.size() == 0) {																// default loop if we have no tickets, all the seat are free 
            for (int i = 0; i < 4; i++) {	
                for (int j = 0; j < 2; j++) {
                    seatGrid[i][j] = 1;
                }
            }
        }

        return seatGrid;
    }
	
    /**
     * ticketsAtFlighttime() 
     * Return the an ArrayList of the ticketID's already booked for a specific flight 
     * @param flightID: int
     * @return ticketID's: ArrayList<Integer>
     */
    public ArrayList<Integer> ticketsAtFlighttime(int flightID) {
        ArrayList<Integer> tickets = new ArrayList<Integer>();

        try {
            String query = "SELECT * FROM TICKET Where FlightID = ?";
            PreparedStatement myStmt = con.prepareStatement(query);

            myStmt.setInt(1, flightID);
            ResultSet results = myStmt.executeQuery();

            while (results.next()) {
                tickets.add(results.getInt("flightID"));						// gets the flight ID numbers 
            }
            myStmt.close();
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * getMaxTicketID()
     * Finds the maximum ticket ID that is in the database so that we can set the ticket counter in Java 
     * to create more tickets with a unique ticketID number. 
     * @return ticketID: int
     */
    public int getMaxTicketID() {
    	int ticketID = 1;
    	
    	try {
            String query = "SELECT MAX(TicketID) AS MaxTicketID From TICKET";
            PreparedStatement myStmt = con.prepareStatement(query);

            ResultSet results = myStmt.executeQuery();

            while (results.next()) {
                ticketID = results.getInt("MaxTicketID");
            }
            myStmt.close();
        }

        catch (SQLException ex) {
            ex.printStackTrace();
        }
    	return ticketID;
    }
    
    /**
     * saveTicket()
     * Takes a ticket object, seat object, and flight object and saves all the necessary info into the database ticker area.
     * @param ticket: Ticket
     * @param seat: Seat
     * @param flight: Flight
     * @throws SQLException
     */
    public void saveTicket(Ticket ticket, Seat seat, Flight flight) throws SQLException {
    	Statement st;
    	st = con.createStatement();
    	
    	// save necessary details into the ticket table 
    	String query = "INSERT INTO TICKET (TicketID, SeatID, FlightID, MembershipID, Price) VALUES " 		
    			+ "(" + ticket.getTicketID()
    			+ ", " + seat.getSeatID()
    			+ ", " + flight.getFlightID()
    			+ ", " + null
    			+ ", " + seat.getPrice() + ");";
    	System.out.println("Ticket Entered into Database."); 
    	System.out.println("Ticket ID: " + ticket.getTicketID() + " Origin: " + flight.getOrigin() + " Destination: " + flight.getDestination()); 
    	st.executeUpdate(query);  	
        st.close(); 

    }
    
    /**
     * deleteTicket()
     * Given the ticketID, deletes the ticket from the database if found 
     * @param ticketID: int
     */
    public void deleteTicket(int ticketID) {
    	Statement st;
    	
    	try {
			st= con.createStatement();
			String query = "DELETE FROM ticket WHERE ticketID = " + ticketID; 
			int rowsAffected = st.executeUpdate(query);
			
			if(rowsAffected != 0) {
				System.out.println("Confirm Ticket ID " + ticketID + " Cancelled.");
			}
			
			else {
				System.out.println("Could not find TicketID in database."); 
			}
			
			st.close();
        
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * RegisteredUser() 
     * Given a username and password, finds the associated registered user, if found, creates a RegisteredUser object 
     * and returns the user
     * @param username: String
     * @param password: String
     * @return RegisteredUser
     */
    public RegisteredUser confirmRegisteredUser(String username, String password) {
    	RegisteredUser regUser = null; // (String fname, String lname, String email, int ID)
    	
    	Statement st;
    	
    	String fname = null;
    	String lname = null;
    	int memberID = 0; 
    	
    	try {
        	st = con.createStatement();
        	    	
        	String query = "SELECT * FROM REGISTERED_USER WHERE Email = '" + username + "' AND Password = '" + password + "'";
        	PreparedStatement myStmt = con.prepareStatement(query);

            ResultSet results = myStmt.executeQuery();

            while (results.next()) {
                memberID = results.getInt("MembershipID");
                fname = results.getString("FName"); 
                lname = results.getString("LName"); 
            }
            
            regUser = new RegisteredUser(fname, lname, username, memberID); 		// make a registered user out of info from database
        
    	} catch (SQLException e) {
			System.out.println("Help me."); 
    		e.printStackTrace();
		}
    	return regUser; 
    }
    
	/**
	 * closeConnection()
	 * Closes the connection to the database
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		con.close();
		System.out.println("\nConnection Closed."); 
	}
	
}
