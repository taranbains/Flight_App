
/**
 * RegularUser 
 * User object for any guest booking a flight
 * @author bains
 *
 */
public class RegularUser {
	private String fname; 
	private String lname; 
	private String email; 
	
	/**
	 * @param fname: String
	 * @param lname: String
	 * @param email: String
	 */
	public RegularUser(String fname, String lname, String email) {
		this.fname = fname;
		this.lname = lname;
		this.email = email; 
	}
	
	/*
	 * ---- ASSOCIATED GETTERS ----
	 */
	public String getEmail() {
		return this.email;
	}
	
	public String getFName() {
		return this.fname;
	}
	
	public String getLName() {
		return this.lname; 
	}
	
}
