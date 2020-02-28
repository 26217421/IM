package main;

import java.util.HashMap;
import java.util.Vector;

public class ChatManager {
	private Vector<ChatSocket> vector = new Vector<ChatSocket>();
	public HashMap<String, ChatSocket> hashMap = new HashMap<String, ChatSocket>();
	
	private ChatManager () { }
	private static final ChatManager cm = new ChatManager();
	public static ChatManager getChatManager() {
		return cm;
	}
	
	public void add(ChatSocket chatSocket) {
		vector.add(chatSocket);
	}
	
	public void publish(ChatSocket chatSocket,String s) {
		for(int i = 0;i < vector.size();i++) {
			ChatSocket cs = vector.get(i);
			if(!cs.equals(chatSocket)) {
				cs.out(s);
			}
		}
	}
}
