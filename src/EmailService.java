
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.process.common.bean.CalendarHistoryDataBean;
import com.process.common.bean.EmailCSVAttachmentDataBean;
import com.process.common.bean.NotificationDataBean;
import com.process.common.dao.DatabaseDao;
import com.process.utilities.Constants;

@Service
public class EmailService {
String CRLF = Constants.CRLF;
	
	@Autowired
	DatabaseDao databaseDao;
	
	public EmailService() {}
	
	public void sendNewInvite(NotificationDataBean notificationObj, CalendarHistoryDataBean calendarHistoryData) {
		String to = "";
		  
		  for(String receipient : notificationObj.getReceiver()){
			  if(!to.equals("")){
				  to = to + ", " + receipient; 
			  }else{
				  to = receipient;
			  }
		  }
		  System.out.println("to: " + "");

	      String from = Constants.FROM;
	      
	      final String username = "";
	      final String password = "";

	      String host = Constants.HOST;

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", Constants.FALSE);
	      props.put("mail.smtp.starttls.enable", Constants.TRUE);
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", Constants.PORT);

	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
		   }
	         });

	      try {

		   Message message = new MimeMessage(session);
		
		   message.setFrom(new InternetAddress(from));
		
		   message.setRecipients(Message.RecipientType.TO,
	               InternetAddress.parse(to));
		   
		   setMultipartContent(message, notificationObj, calendarHistoryData);
		   
		   Transport.send(message);

		   System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      }
	}
	
	public void sendCancelInvite(CalendarHistoryDataBean calendarHistoryData) {
		String to = "";
		  
		  for(String receipient : calendarHistoryData.getRecipient()){
			  if(!to.equals("")){
				  to = to + ", " + receipient; 
			  }else{
				  to = receipient;
			  }
		  }
		  System.out.println("to: " + "");

	      String from = Constants.FROM;
	      
	      final String username = "";
	      final String password = "";

	      String host = Constants.HOST;

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", Constants.FALSE);
	      props.put("mail.smtp.starttls.enable", Constants.TRUE);
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", Constants.PORT);

	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
		   }
	         });

	      try {

		   Message message = new MimeMessage(session);
		
		   message.setFrom(new InternetAddress(from));
		
		   message.setRecipients(Message.RecipientType.TO,
	               InternetAddress.parse(to));
		   
		   setMultipartContent(message, calendarHistoryData);
		   
		   Transport.send(message);

		   System.out.println("Sent message successfully....");

	      } catch (MessagingException e) {
	         throw new RuntimeException(e);
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      }
	}
   
   private String buildVcalendar(NotificationDataBean notificationObj, CalendarHistoryDataBean calendarHistoryData) {
	   System.out.println("Start Date: " + notificationObj.getStartDate());
	   System.out.println("Start Time: " + notificationObj.getStartMeetingTime());
	   System.out.println("End Time: " + notificationObj.getEndMeetingTime());
	   
	   int seqNo = 0;
	   
	    Calendar calendar = Calendar.getInstance();
	    //Date
	    String startDate = notificationObj.getStartDate();
	    String year = startDate.substring(0,4);
	    calendar.set(Calendar.YEAR, Integer.parseInt(year));
	    
	    String month = startDate.substring(5,7);
	    calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
	    
	    String day = startDate.substring(8,10);
	    calendar.set(Calendar.DATE, Integer.parseInt(day));
	    
	    
	    System.out.println("YEAR:"+year);
	    System.out.println("MONTH:"+month);
	    System.out.println("DAY:"+day);
	    
	    //Start Time
	    String startTime = notificationObj.getStartMeetingTime();
		
	    
	    String strStartTime = startTime.toString();
	    
	    Integer startHour = Integer.parseInt(strStartTime.substring(0,2));
	    System.out.println("startHour: " + startHour);
	    calendar.set(Calendar.HOUR_OF_DAY, startHour);
	    Integer startMin = Integer.parseInt(strStartTime.substring(3,5));
	    calendar.set(Calendar.MINUTE, startMin);
	    
	    Date start = calendar.getTime();
	   
	    //End Time
	    String endDate = notificationObj.getEndDate();
	    year = endDate.substring(0,4);
	    calendar.set(Calendar.YEAR, Integer.parseInt(year));
	    
	    month = endDate.substring(5,7);
	    calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
	    
	    day = endDate.substring(8,10);
	    calendar.set(Calendar.DATE, Integer.parseInt(day));
	    String endTime = notificationObj.getEndMeetingTime();
		
	    String strEndTime = endTime.toString();
	    
	    Integer endHour = Integer.parseInt(strEndTime.substring(0,2));
	    calendar.set(Calendar.HOUR_OF_DAY, endHour);
	    Integer endMin = Integer.parseInt(strEndTime.substring(3,5));
	    calendar.set(Calendar.MINUTE, endMin);
	    
	    Date end = calendar.getTime();
	    
	    System.out.println("start: " + start);
	    System.out.println("end: " + end);
	    
	    SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
	    iCalendarDateFormat.setTimeZone(TimeZone.getTimeZone("est"));
	    System.out.println(iCalendarDateFormat.format(start));
	    System.out.println(iCalendarDateFormat.format(end));
	    String currentDate = (new SimpleDateFormat("yyyyMMdd'T'HHmm'00Z'")).format(new Date());
	    String uid = currentDate + "-" + UUID.randomUUID().toString() + "@fairpoint.com";
	    System.out.println("UID : " + uid);
	    
	    
	    String calendarContent =
	    		"BEGIN:VCALENDAR\n" +
		        "METHOD:REQUEST\n" +
		        "X-MS-OLK-FORCEINSPECTOROPEN:TRUE\n" +
		        "PRODID: //Microsoft Corporation//Outlook 14.0 MIMEDIR//EN\n" +
		        "VERSION:2.0\n" +
		        "BEGIN:VEVENT\n" + 
		        "ORGANIZER;CN=Fairpoint Notification:MAILTO:" + notificationObj.getOrganizer() + "\n" +
		        "DTSTAMP:" + currentDate + "\n" +
		        "DTSTART:" + iCalendarDateFormat.format(start)+ "\n" +
		        "DTEND:"  + iCalendarDateFormat.format(end)+ "\n" +
		        "TRANSP:TRANSPARENT" +"\n" +
		        "LOCATION:" + notificationObj.getLocation() + "\n" +
		        "SUMMARY:" + notificationObj.getSubject() + "\n" +
		        "UID:" + uid + "\n" +
		        notificationObj.getAtendee() +
		        "CATEGORIES:Fairpoint Advisory\n" +
		        "SEQUENCE:"+ String.valueOf(seqNo) +"\n" +
		        "PRIORITY:0\n" +
		        "CLASS:PUBLIC\n" +
		        "STATUS:CONFIRMED\n" +
		        "BEGIN:VALARM\n" +
		        "ACTION:DISPLAY\n" +
		        "DESCRIPTION:callback action\n" +
		        "TRIGGER;RELATED=START:-PT00H15M00S\n" +
		        "END:VALARM\n" +
		        "END:VEVENT\n" +
		        "BEGIN:VTIMEZONE\n" +
		        "TZID:US-Eastern\n" +
		        "LAST-MODIFIED:19870101T000000Z\n" +
		        "BEGIN:STANDARD\n" +
		        "DTSTART:" + iCalendarDateFormat.format(start)+ "\n" +
		        "DTEND:"  + iCalendarDateFormat.format(end)+ "\n" +
		        "TZOFFSETFROM:-0000\n" +
		        "TZOFFSETTO:-0000\n" +
		        "TZNAME:EST\n" +
		        "END:STANDARD\n" +
		        "BEGIN:DAYLIGHT\n" +
		        "DTSTART:" + iCalendarDateFormat.format(start)+ "\n" +
		        "DTEND:"  + iCalendarDateFormat.format(end)+ "\n" +
		        "TZOFFSETFROM:-0000\n" +
		        "TZOFFSETTO:-0000\n" +
		        "TZNAME:EDT\n" +
		        "END:DAYLIGHT\n" +
		        "END:VTIMEZONE\n" +
		        "END:VCALENDAR";

	    System.out.println("after setting calendar");
	    
	    SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
	    iCalendarDateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
	    
	    calendarHistoryData.setuId(uid);
	    calendarHistoryData.setDtStamp(currentDate);
	    calendarHistoryData.setDtStart(dbDateFormat.format(start));
	    calendarHistoryData.setDtEnd(dbDateFormat.format(end));
	    calendarHistoryData.setSeqNo(String.valueOf(seqNo));
	    calendarHistoryData.setSubject(notificationObj.getSubject());
	    calendarHistoryData.setLocation(notificationObj.getLocation());
	     return calendarContent;
	}
   
   private String buildVcalendar(CalendarHistoryDataBean calendarHistoryData) {
	    
	    String currentDate = (new SimpleDateFormat("yyyyMMdd'T'HHmm'00Z'")).format(new Date());	    
	    
	    String calendarContent =
	    		"BEGIN:VCALENDAR\n" +
		        "METHOD:CANCEL\n" +
		        "X-MS-OLK-FORCEINSPECTOROPEN:TRUE\n" +
		        "PRODID: //Microsoft Corporation//Outlook 14.0 MIMEDIR//EN\n" +
		        "VERSION:2.0\n" +
		        "BEGIN:VEVENT\n" + 
		        "ORGANIZER;CN=Fairpoint Notification:MAILTO:no-reply@fairpoint.com\n" +
		        "DTSTAMP:" + currentDate + "\n" +
		        "DTSTART:" + calendarHistoryData.getDtStart()+ "\n" +
		        "DTEND:"  + calendarHistoryData.getDtEnd()+ "\n" +
		        "TRANSP:TRANSPARENT" +"\n" +
		        "LOCATION:" + calendarHistoryData.getLocation() + "\n" +
		        "SUMMARY:" + calendarHistoryData.getSubject() + "\n" +
		        "UID:" + calendarHistoryData.getuId() + "\n" +
		        calendarHistoryData.getAttendee() +
		        "CATEGORIES:Fairpoint Advisory\n" +
		        "SEQUENCE:"+ String.valueOf(Integer.parseInt(calendarHistoryData.getSeqNo()) + 1)+"\n" +
		        "PRIORITY:0\n" +
		        "CLASS:PUBLIC\n" +
		        "STATUS:CANCELLED\n" +
		        "BEGIN:VALARM\n" +
		        "ACTION:DISPLAY\n" +
		        "DESCRIPTION:callback action\n" +
		        "TRIGGER;RELATED=START:-PT00H15M00S\n" +
		        "END:VALARM\n" +
		        "END:VEVENT\n" +
		        "BEGIN:VTIMEZONE\n" +
		        "TZID:US-Eastern\n" +
		        "LAST-MODIFIED:19870101T000000Z\n" +
		        "BEGIN:STANDARD\n" +
		        "DTSTART:" + calendarHistoryData.getDtStart()+ "\n" +
		        "DTEND:"  + calendarHistoryData.getDtEnd()+ "\n" +
		        "TZOFFSETFROM:-0000\n" +
		        "TZOFFSETTO:-0000\n" +
		        "TZNAME:EST\n" +
		        "END:STANDARD\n" +
		        "BEGIN:DAYLIGHT\n" +
		        "DTSTART:" + calendarHistoryData.getDtStart()+ "\n" +
		        "DTEND:"  + calendarHistoryData.getDtEnd()+ "\n" +
		        "TZOFFSETFROM:-0000\n" +
		        "TZOFFSETTO:-0000\n" +
		        "TZNAME:EDT\n" +
		        "END:DAYLIGHT\n" +
		        "END:VTIMEZONE\n" +
		        "END:VCALENDAR";

	    calendarHistoryData.setDtStamp(currentDate);
	    
	     return calendarContent;
	}
   
   private String buildHtmlTextPart(NotificationDataBean notificationObj, CalendarHistoryDataBean calendarHistoryData) throws MessagingException {
	     
       StringBuilder cbContent = new StringBuilder();
       
       cbContent.append("")
       .append("<html><head><style>body{font-family:arial;font-size:10pt}h4{font-size:10pt;}</style></head><body>")
       .append("<center><h4>DESCRIPTION: Maintenance Summary:</h4> " + "<p>" + notificationObj.getMaintenanceSummary() + "</center>")
       .append("<p><b>Maintenance Type:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getMaintenanceType())
       .append("<p><b>Maintenance Detail(s):</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getMaintenanceDetails().getHeader());
       
       cbContent.append(notificationObj.getMaintenanceDetails().getCircuitList());
       
       cbContent.append("<p><p><b>Technology:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getTechnology())
       .append("<p><b>Date:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getStartDate())
       .append("<p><b>Start time:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getStartTime())
       .append("<p><b>Technician on-site:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getTechnicianOnsite())
       .append("<p><b>Service affecting:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getServiceAffecting())
       .append("<p><b>Outage length:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getOtgLength())
       .append("<p><b>Service impacted:</b>&nbsp;&nbsp;&nbsp;" + Constants.replaceNullWithEmptyString(notificationObj.getServicesImpacted()))
       .append("<p><b>Regions impacted:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getRegionsImpacted())
       .append("<p><b>Ticket number:</b>&nbsp;&nbsp;&nbsp;" + Constants.replaceNullWithEmptyString(notificationObj.getTicketNumber()))
       .append("<p><b>Device name(s):</b>&nbsp;&nbsp;&nbsp;" + Constants.replaceNullWithEmptyString(notificationObj.getDeviceNames()))
       .append("<p><b>Slot/port #:</b>&nbsp;&nbsp;&nbsp;" + Constants.replaceNullWithEmptyString(notificationObj.getPort()))
       .append("<p><b>Primary contact NOC:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getPrimaryContact())
       .append("<p><b>Bridge needed:</b>&nbsp;&nbsp;&nbsp;" + notificationObj.getBridgeNeeded())
       .append("<p><b>Bridge number:</b>&nbsp;&nbsp;&nbsp;" + Constants.replaceNullWithEmptyString(notificationObj.getBridgeNumbers()))
       .append("<p><b>Notes:</b>&nbsp;&nbsp;&nbsp;" + Constants.replaceNullWithEmptyString(notificationObj.getNotes()))
       .append("</body></html>");
 
       calendarHistoryData.setEmailBody(cbContent.toString());

       return cbContent.toString();
   }
   
   public String buildAttachment(NotificationDataBean notificationObj){
	   
	   StringBuilder sbAttachment = new StringBuilder();
	   
	   String attachmentHeader = Constants.HEADER_CKT_ID + Constants.SEPARATOR + Constants.HEADER_IC_CUST + Constants.SEPARATOR
			   					+ Constants.HEADER_CCNA + Constants.SEPARATOR + Constants.HEADER_CKR + Constants.SEPARATOR
			   					+ Constants.HEADER_PON + Constants.SEPARATOR + Constants.HEADER_PRIMARY_LOCATION + Constants.SEPARATOR
			   					+ Constants.HEADER_STREET + Constants.SEPARATOR + Constants.HEADER_CITY + Constants.SEPARATOR
			   					+ Constants.HEADER_STATE + Constants.SEPARATOR + Constants.HEADER_SECONDARY_LOCATION + Constants.SEPARATOR
			   					+ Constants.HEADER_STREET + Constants.SEPARATOR + Constants.HEADER_CITY + Constants.SEPARATOR
			   					+ Constants.HEADER_STATE + "\n";
	   
	   sbAttachment.append(attachmentHeader);
	   
	   for(EmailCSVAttachmentDataBean csvTemp : notificationObj.getCsvAttachmentsList()){
		   sbAttachment.append(Constants.replaceNullWithEmptyString(csvTemp.getCktID())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getIcCust())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getCcna())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getCkr())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getPon())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getPrimaryLocation())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getStreetPrimary())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getCityPrimary())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getStatePrimary())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getSecondaryLocation())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getStreetSecondary())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getCitySecondary())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getStateSecondary())).append(Constants.SEPARATOR)
		   				.append(Constants.replaceNullWithEmptyString(csvTemp.getServiceManager())).append("\n");
	   }
	   
	   return sbAttachment.toString();
   }
   
   public void setMultipartContent(Message message, NotificationDataBean notificationObj, CalendarHistoryDataBean calendarHistoryData) throws MessagingException, IOException {
	   Multipart multipart = new MimeMultipart("mixed");
	   
	   BodyPart messageBodyPart = new MimeBodyPart(); 
	   messageBodyPart.setContent(buildHtmlTextPart(notificationObj, calendarHistoryData), "text/html");
	   multipart.addBodyPart(messageBodyPart);	   
	   
	   messageBodyPart = new MimeBodyPart();
	   String vCal = buildVcalendar(notificationObj, calendarHistoryData);
	   byte[] invite = vCal.getBytes();
	   messageBodyPart.addHeader("Content-Class", "url:content-classes:calendarmessage");
	   messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(new ByteArrayInputStream(invite), "text/calendar;method=REQUEST;charset=\"UTF-8\"")));
	   multipart.addBodyPart(messageBodyPart);
	   
	   
	   String attachment =  buildAttachment(notificationObj);
	   DataSource ds = new ByteArrayDataSource(attachment.getBytes("UTF-8"), "application/octet-stream");
	   messageBodyPart = new MimeBodyPart();
	   messageBodyPart.setDataHandler(new DataHandler(ds));
	   messageBodyPart.setFileName("Circuits Customer Information.csv");
	   messageBodyPart.setDisposition(MimeBodyPart.ATTACHMENT);
	   multipart.addBodyPart(messageBodyPart);
	   
	   message.setContent(multipart);
   }
   
   private void setMultipartContent(Message message, CalendarHistoryDataBean calendarHistoryData) throws MessagingException, IOException {
	   Multipart multipart = new MimeMultipart("mixed");
	   
	   BodyPart messageBodyPart = new MimeBodyPart();   
	   
	   messageBodyPart = new MimeBodyPart();
	   String vCal = buildVcalendar(calendarHistoryData);
	   byte[] invite = vCal.getBytes();
	   messageBodyPart.addHeader("Content-Class", "url:content-classes:calendarmessage");
	   messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(new ByteArrayInputStream(invite), "text/calendar;method=REQUEST;charset=\"UTF-8\"")));
	   multipart.addBodyPart(messageBodyPart);
	   
	   message.setContent(multipart);
   }
}
