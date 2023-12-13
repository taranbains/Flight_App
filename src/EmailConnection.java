import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * EmailConnection()
 * Class to send an email with one static method that sets up the email and sends from account using java mail API
 * 
 * @author bains
 */
public class EmailConnection {
	
    /**
     * emailSetup()
     * 
     * 
     * @param ticket: Ticket
     * @param seat: Seat
     * @param flight: Flight
     * @param user: RegularUser
     */
    public static void emailSetup(Ticket ticket, Seat seat, Flight flight, RegularUser user) {
        final String username = "bains.meng@gmail.com";
        final String appPassword = "dgcm ckoy kflw msgh";  									// Use App Password here
        
        Properties props = new Properties();												// items for the connection 
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
        		new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, appPassword);
                    }
                });    
  
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("bains.meng@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail())); 
            message.setSubject("Flight Purchase Confirmation");

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();										// Add text body 
            textPart.setText("To " + user.getFName() + " " + user.getLName() + ", \n\n"
            		+ "Below are the details of your flight. \n"
            		+ "Ticket ID: " + Integer.toString(ticket.getTicketID()) + "\n"
            		+ "Flight Origin: " + flight.getOrigin() + "\n"
            		+ "Flight Destination: " + flight.getDestination() + "\n"
            		+ "Flight Time: " + flight.getDateTime() + "\n"
            		+ "Seat Number: " + seat.getRow() + Integer.toString(seat.getCol()) + "\n"
            		+ "Cost: $" + Integer.toString(seat.getPrice()) + ".00 \n\n"
            		+ "Thank you for your purchase and enjoy your flight!");
            
            multipart.addBodyPart(textPart);
            message.setContent(multipart); 													// Set the content of the message
            Transport.send(message);														// send actual email 

            System.out.println("Email ticket confirmation sent successfull!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
