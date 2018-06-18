package me.vincent.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.vincent.registry.IRegistry;
import me.vincent.transport.NioTcpService;
import me.vincent.transport.TCPService;
import me.vincent.utils.ThreadUtil;

public class RpcService {
	
	public static Map<String, Object> serviceMapper = new HashMap<String, Object>(8);
	
	private IRegistry registry;
	
	private String serviceAddress;
	
	public RpcService(IRegistry r, String s){
		registry = r;
		serviceAddress = s;
		ThreadUtil.run(new NioTcpService(s));
	}
	
	public void publish(Object service, Class clazz){
		
//		if(null == services || services.length == 0){
//			System.out.println("No Services to publish");
//			return;
//		}
		
//		Arrays.stream(services).forEach(service -> {
//			serviceMapper.put(service.getClass().getName(), service);
//			registry.register(service.getClass().getName(), serviceAddress);
//		});
		serviceMapper.put(clazz.getName(), service);
		registry.register(clazz.getName(), serviceAddress);
	}

}
