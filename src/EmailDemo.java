
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailDemo {
	public static void main(String[] args) throws SQLException, AddressException {

		int count_pending = StageDatabase.getCount('N', 650);
		int count_processed = StageDatabase.getCount('Y', 650);
		int count_error = StageDatabase.getCount('X', 650);
		
		System.out.println("count_pending:"+count_pending);
		System.out.println("count_processed:"+count_processed);
		InternetAddress[] to = InternetAddress
				.parse("gayathri.guttikonda@accenture.com,mihir.wachasundar@accenture.com");
		InternetAddress[] bcc = InternetAddress.parse("bajjuri.vyshnavi@accenture.com");
		String from = "no-reply@fairpoint.com";
		String host = "172.24.37.230";// or IP address
		String timeStamp = new SimpleDateFormat("MMM dd, hh:mm aaa").format(new Date());
		System.out.println("timestamp:" + timeStamp);
		final String username = "";
		final String password = "";

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", false);
		properties.put("mail.smtp.starttls.enable", true);
		properties.setProperty("mail.smtp.host", host);
		properties.put("mail.smtp.port", "25");

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, to);
			message.setRecipients(Message.RecipientType.BCC, bcc);
			message.setSubject("Migration Records status - " + timeStamp);

			MimeMultipart multipart = new MimeMultipart("related");

			StringBuilder body = new StringBuilder();
			body.append("<div style=\"padding:20px\">")
					.append("<table border=\"1\" style=\"border-collapse:collapse;text-align:center; font-family: Calibri; font-size:20px\">")
					.append("  <tr>")
					// .append(" <td ><img
					// src=\"C:/Users/gayathri.guttikonda/workspace_fairpoint/EmailFunctionality/src/heading_1.jpg\"></td>")
					.append("	<td><img src=\"cid:image1\"></td>").append("  </tr>").append("   <tr>")
					.append("    <td bgcolor = \"#FFA500\" height=\"50\" style=\"color:#FFFFFF;font-size:22px\"><b>Migration Records Status</b></td>")
					.append("  </tr>").append("   <tr>")
					.append("    <td align = \"left\">The event will begin in 24 hours<br>Planned Start: <br>Planned End:</td>")
					.append("  </tr>").append("  <tr align=\"left\">")
					.append("    <table colspan=\"2\" style=\"border-collapse:collapse;text-align:left;font-family: Calibri; font-size:20px;width:850px\">")
					.append("		<tr><td style=\"color:#4863A0;border-bottom: 1pt solid black\"><b>Event Type</b></td><td style=\"border-bottom: 1pt solid black\"></td></tr>")
					.append("		<tr><td style=\"color:#4863A0;border-bottom: 1pt solid black\"><b>Event Title</b></td><td style=\"border-bottom: 1pt solid black\"></td></tr>")
					.append("		<tr><td style=\"color:#4863A0;border-bottom: 1pt solid black\"><b>Event Description</b></td><td style=\"border-bottom: 1pt solid black\"> Number of records Pending&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:"
							+ count_pending + "<br>Number of records processed&nbsp;&nbsp;:" + count_processed
							+ "<br>Number of records errored&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:" + count_error + "</td></tr>")
					.append("		<tr><td style=\"color:#4863A0;border-bottom: 1pt solid black\"><b>Systems</b></td><td style=\"border-bottom: 1pt solid black\"></td></tr>")
					.append("		<tr><td style=\"color:#4863A0;border-bottom: 1pt solid black\"><b>Environments</b></td><td style=\"border-bottom: 1pt solid black\"></td></tr>")
					.append("		<tr><td style=\"color:#4863A0;border-bottom: 1pt solid black\"><b>Locations</b></td><td style=\"border-bottom: 1pt solid black\"></td></tr>")
					.append("		<tr><td style=\"color:#4863A0\"><b>Repeating Event?</b></td><td></td></tr>")
					.append("	  </table>").append("  </tr> ").append("   <tr>")
					.append("    <td height=\"50\" align=\"left\">Please do not reply; this mailbox is not monitored.</td>")
					.append("  </tr>").append("  <tr>")
					.append("    <td ><img src=\"cid:image2\"></td>")
					.append("  </tr>").append("</table>").append("</div>");

			String s = body.toString();
			System.out.println("body:" + body);

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(s, "text/html");
			multipart.addBodyPart(messageBodyPart);

			messageBodyPart = new MimeBodyPart();
			DataSource fds1 = new FileDataSource(
					"C:/Users/gayathri.guttikonda/workspace_fairpoint/EmailFunctionality/src/heading_1.jpg");
			messageBodyPart.setDataHandler(new DataHandler(fds1));
			messageBodyPart.setHeader("Content-ID", "<image1>");
			
			multipart.addBodyPart(messageBodyPart);
			
			messageBodyPart = new MimeBodyPart();
			DataSource fds2 = new FileDataSource(
					"C:/Users/gayathri.guttikonda/workspace_fairpoint/EmailFunctionality/src/footer.jpg");
			messageBodyPart.setDataHandler(new DataHandler(fds2));
			messageBodyPart.setHeader("Content-ID", "<image2>");
			
			multipart.addBodyPart(messageBodyPart);
			
			message.setContent(multipart);
			message.saveChanges();

			System.out.println("\nSending Message.....");
			Transport transport = session.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			// Transport.send(message);
			System.out.println("message sent successfully....");

		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}