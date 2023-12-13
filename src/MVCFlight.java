
/**
 * Main function that calls the Model View Controller design pattern, or at least my attempt of it 
 * View: the Gui 
 * Controller: Controller for the gui actions and a controller for the Model
 * Model: all the domain objects and database connection/controller  
 *  
 * @author bains
 *
 */
public class MVCFlight {

	public static void main(String[] args) throws Exception {
		FlightView view = new FlightView(); 											// this is the gui view 
		FlightController flightModelControl = new FlightController(); 					// this is the model 		
		ViewController viewControl = new ViewController(view, flightModelControl); 		// main controller 
	}

}
