package user;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public class SendMail {
	
	public SendMail() {
		super();
	}

	public static String resetPwdLink(String account) throws Exception {
		makeToken token = new makeToken();
		return "http://localhost:8080/SearcHair/api/user/password/reset?token=" + token.encrypt(account);
		// return "http://localhost:8080/SearcHair/ResetPwdServlet?user=" + account + "&token=" + makeToken.convertMD5(account);

	}

	public static void sendMail(String sendTo) throws Exception {
		String host = "smtp.gmail.com";
		String port = "587";
		String username = "searchair109@gmail.com";
		String password = "searcHair1091";
		String from = "searchair109@gmail";
		String subject = "java mail test";
		
		
		String content = "�����t�εo�X���q���A�ФŪ����^�СA�P�°t�X�I\n"
				+ "�ڭ̦���z�o�X���]�K�X���q���A�Y�S���A�еL���o�ʫH�C\n"
				+ "�Y�ݭn���]�K�X�A���I�H�U�s���G\n"
				+ resetPwdLink(sendTo);

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
		message.setSubject(subject);
		message.setText(content);
		System.out.println(content);

		Transport.send(message);
	}

	
	public static void main(String[] args) throws Exception {
		SendMail.sendMail("suara1201fxt@gmail.com");
		
	}
}
