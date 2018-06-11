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
		
		String zk_addr = "10.0.0.18:2181";
		
		IRegistry registry = new RegistryImpl(zk_addr);
		RpcService rpcService = new RpcService(registry,"10.0.0.2" + ":" + port);
		rpcService.publish(new PingServiceImpl(), IPingService.class.getName());
		System.out.println(Thread.currentThread().getName() + " 主线程结束");
	}

}
