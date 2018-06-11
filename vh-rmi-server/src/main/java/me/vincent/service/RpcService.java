package me.vincent.service;

import java.util.HashMap;
import java.util.Map;

import me.vincent.registry.IRegistry;
import me.vincent.utils.ThreadUtil;

public class RpcService {
	
	public static Map<String, Object> serviceMapper = new HashMap<String, Object>(8);
	
	private IRegistry registry;
	
	private String serviceAddress;
	
	public RpcService(IRegistry r, String s){
		registry = r;
		serviceAddress = s;
		ThreadUtil.run(new TCPService(s));
	}
	
	public void publish(Object service, String serviceName){
		registry.register(serviceName, serviceAddress);
		serviceMapper.put(serviceName, service);
	}

}
