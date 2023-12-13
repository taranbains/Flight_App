import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;


/**
 * RegisteredLoginView
 * Class creates a login GUI 
 * 
 * @author bains
 */
public class RegisteredLoginView {
	DatabaseController db; 												// Database connection 

	JFrame frame = new JFrame(); 								
	JPanel panel = new JPanel(); 
	JTextField usernameField = new JTextField(15); 
	JPasswordField passwordField = new JPasswordField(15); 
	JButton loginButton = new JButton("Login"); 
	
	LoginButton loginButtonListener = new LoginButton();				// ActionListener for the login button 
	
	/**
	 * Constructor sets the GUI up with labels and text fields 
	 * @throws Exception
	 */
	RegisteredLoginView() throws Exception{
		db = DatabaseController.createInstance(); 
		frame.setTitle("Login View");
		
		frame.setSize(400,250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		frame.add(panel);
		panel.setLayout(null);
		
		JLabel username = new JLabel("Username/Email: "); 
		username.setBounds(40,20,120,25);
		JLabel password = new JLabel("Password: ");
		password.setBounds(40,60,100,25);
		
		usernameField.setBounds(200,20,140,25);
		passwordField.setBounds(200,60,140,25); 
		
		panel.add(username);
		panel.add(usernameField);
		panel.add(password);
		panel.add(passwordField);
		
		loginButton.setBounds(225, 100, 80, 25); 
		loginButton.setEnabled(true); 
		panel.add(loginButton);
		loginButton.addActionListener(loginButtonListener);
		 
	}
	
	/*
	 * ACTION LISTENER FOR LOGIN BUTTON 
	 */
	class LoginButton implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String username = usernameField.getText(); 					// Get username from user input 
			@SuppressWarnings("deprecation")
			String password = passwordField.getText(); 					// Get password from user input
			System.out.println("Confirming Login."); 	
			
			// Tries to find registered user in the database 
			RegisteredUser registeredUser = db.confirmRegisteredUser(username, password);
			
			if(registeredUser != null) {
				System.out.println("Welcome back " + registeredUser.getFName() + " " + registeredUser.getLName() + "."); 
			}
			else {
				System.out.println("I am sorry, we could not find you in the system."); 
			}
			
		}
		
	}
	 
}
