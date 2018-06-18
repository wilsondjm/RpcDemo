package me.vincent;

import java.io.IOException;
import java.net.InetAddress;

import me.vincent.api.IHelloService;
import me.vincent.api.IPingService;
import me.vincent.api.impl.HelloServiceImpl;
import me.vincent.api.impl.PingServiceImpl;
import me.vincent.framework.rpc.registry.IRegistry;
import me.vincent.framework.rpc.registry.RegistryImpl;
import me.vincent.framework.rpc.service.RpcService;

public class Server {

	public static void main(String[] args) throws IOException {
		
		InetAddress inetAddress = InetAddress.getLocalHost();
		String port = "6366";
		
		String zk_addr = "10.0.0.18:2181";
		
		IRegistry registry = new RegistryImpl(zk_addr);
		RpcService rpcService = new RpcService(registry,"127.0.0.1" + ":" + port);
		IPingService ip = new PingServiceImpl();
		IHelloService ih = new HelloServiceImpl();
		rpcService.publish(ip, IPingService.class);
		rpcService.publish(ih, IHelloService.class);
		
		System.out.println(Thread.currentThread().getName() + " 主线程结束");
	}

}
