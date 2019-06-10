package smartdust;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaEmail {

	private Properties emailProperties;
	private Session mailSession;
	private MimeMessage emailMessage;

	public JavaEmail() throws AddressException,
			MessagingException {

		this.setMailServerProperties();
	}

	public void setMailServerProperties() {

		String emailPort = "587";//gmail's smtp port

		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", emailPort);
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
		
		mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);

	}
	

	public void sendMail(String msg,String binId) throws AddressException,
			MessagingException {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String[] toEmails = { "pranjaljnp1789@gmail.com" };
		String emailSubject = "smart dustbin Id : "+ binId;
		String emailBody = "<html><body><table>";
		emailBody += "<tr><td>Bin id</td><td>:</td><td>"+ binId +"</td></tr>";
		emailBody += "<tr><td>Bin Status</td><td>:</td><td>"+ msg +"</td></tr>";
		emailBody += "<tr><td>Date and Time</td><td>:</td><td>"+ dateFormat.format(date) +"</td></tr>";
		emailBody += "</table></body></html>";

		

		for (int i = 0; i < toEmails.length; i++) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		}

		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailBody, "text/html");//for a html email
		//emailMessage.setText(emailBody);// for a text email
		
		new sendEmail().start();
	}
	
	class sendEmail extends Thread{
		private String emailHost;
		private String fromUser ;//just the id alone without @gmail.com
		private String fromUserEmailPassword;
		public sendEmail() {
			emailHost = "smtp.gmail.com";
			fromUser = "pranjaljnp@gmail.com";
			fromUserEmailPassword = "@Chandel123";
		}
		
		@Override
		public void run() {
			Transport transport;
			try {
				transport = mailSession.getTransport("smtp");
				transport.connect(emailHost, fromUser, fromUserEmailPassword);
				transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
				transport.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Email sent successfully.");
			smartbin.appui.append("Email sent successfully.");
		}
	}

}