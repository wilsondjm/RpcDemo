package me.vincent;

import java.io.IOException;
import java.net.InetAddress;

import me.vincent.api.IPingService;
import me.vincent.registry.IRegistry;
import me.vincent.registry.RegistryImpl;
import me.vincent.service.PingServiceImpl;
import me.vincent.service.RpcService;

public class Server {

	public static void main(String[] args) throws IOException {
		
		InetAddress inetAddress = InetAddress.getLocalHost();
		String port = "6366";
		
		String zk_addr = "168.61.11.221:2181";
		
		IRegistry registry = new RegistryImpl(zk_addr);
		RpcService rpcService = new RpcService(registry,"127.0.0.1" + ":" + port);
		rpcService.publish(new PingServiceImpl(), IPingService.class.getName());
		System.out.println(Thread.currentThread().getName() + " 主线程结束");
	}

}
