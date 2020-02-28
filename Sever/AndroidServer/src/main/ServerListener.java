package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread{
	
	@Override
	public void run() {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(1996);
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("hey!有老铁链接到了本机的端口");
				MainWindow.getMainWindow().setShowMsg("有客户端连接到本机端口了");
				ChatSocket cs = new ChatSocket(socket);
				cs.start();
				ChatManager.getChatManager().add(cs);
			}
		} catch (IOException e) {
			System.out.println("出毛病啦！");
		}	
	}
}