
/**
 * RegisteredUser() 
 * Extends same function as RegularUser plus some 
 * 
 * @author bains
 */
public class RegisteredUser extends RegularUser{
	// card number also exists in the database but whatever 
	private static int membershipIDCounter = 1; 
	private String password;
	private int membershipID; 
	
	/**
	 * Basic Constructor is used to make a new RegisteredUser, that needs a membership ID
	 * @param fname: String
	 * @param lname: String
	 * @param email: String
	 */
	public RegisteredUser(String fname, String lname, String email) {
		super(fname, lname, email);
		membershipID = membershipIDCounter; 
		membershipIDCounter++;
	}
	
	/**
	 * Constructor that makes a RegisteredUser object from information in the database, 
	 * ie. they already have a membershipID in the database 
	 * @param fname: String
	 * @param lname: String
	 * @param email: String
	 * @param ID: int
	 */
	public RegisteredUser(String fname, String lname, String email, int ID) {
		super(fname, lname, email);
		membershipID = ID; 
	}
	
	/**
	 * setMembershipCounter() 
	 * @param maxMembershipID: int
	 */
	static public void setMembershipCounter(int maxMembershipID) {
		membershipIDCounter = ++maxMembershipID; 
	}

}
