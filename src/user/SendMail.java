package user;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public class SendMail {

	public SendMail() {
		super();
	}

	public static String resetPasswordLink(String account) throws Exception {
		MakeToken token = new MakeToken();
		return "http://localhost:8080/SearcHair/api-user-password-reset?token=" + token.encrypt(account);

	}

	public static void sendMail(String sendTo) throws Exception {
		String host = "smtp.gmail.com";
		String port = "587";
		String username = "searchair109@gmail.com";
		String password = "searcHair1091";
		String from = "searchair109@gmail";
		String subject = "SearcHair 帳號重設密碼通知";

		String content = "此為系統發出的通知，請勿直接回覆，感謝配合！\n" + "我們收到您發出重設密碼的通知，若沒有，請無視這封信。\n" + "若需要重設密碼，請點以下連結：\n"
				+ resetPasswordLink(sendTo);

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
}
