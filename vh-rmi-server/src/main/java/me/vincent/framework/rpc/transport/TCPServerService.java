package me.vincent.framework.rpc.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import me.vincent.framework.rpc.service.RequestHandleService;
import me.vincent.utils.ThreadUtil;

public class TCPServerService implements Runnable{
	
	private int port = 6636;
	
	private ServerSocket serverSocket;
	
	public TCPServerService(String serviceAddress){
		String[] args = serviceAddress.split(":");
		port = Integer.valueOf(args[1]);
	}
	
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			
			while(true){
				System.out.println(Thread.currentThread().getName() + "线程等待新的连接");
				Socket socket = serverSocket.accept();
				System.out.println("收到一个新的链接 - " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
				ThreadUtil.execute(new RequestHandleService(socket));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
