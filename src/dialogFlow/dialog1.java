package dialogFlow;

import java.util.Scanner;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class dialog1 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectionConfiguration connectionConfig = new ConnectionConfiguration("talk.google.com", 5222, "gmail.com");
		connectionConfig.setSASLAuthenticationEnabled(false); // Gtalk不支援
		XMPPConnection connection = new XMPPConnection(connectionConfig);
		try{
			connection.connect();
			connection.login("username", "password");
			ChatManager chatmanager = connection.getChatManager();
			Chat gChat = chatmanager.createChat("aliyunzixun@xxx.com", new MessageListener() {
				public void processMessage(Chat chat, Message message) {
					System.out.println("Received message:" + message.getBody());
				}
			});
			gChat.sendMessage("Hi, 我是機器人");
		}catch(XMPPException e1){
			e1.printStackTrace();
		}

		try{
			Thread.sleep(10000);// 暫停十秒接受回信。
		}catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
