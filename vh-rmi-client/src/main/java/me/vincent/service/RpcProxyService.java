package me.vincent.service;

import java.lang.reflect.Proxy;

import me.vincent.registry.IRegistry;
import me.vincent.transport.NioClientService;

public class RpcProxyService {
	
	private IRegistry registry;
	
	private NioClientService ncs;
	
	public RpcProxyService(IRegistry r){
		registry = r;
		ncs = new NioClientService();
	}

	public <T> T clientProxy(final Class<T> interfaceCls) {
		
		String serviceAddress = registry.findService(interfaceCls.getName());
		
		if (null == serviceAddress || "".equalsIgnoreCase(serviceAddress)){
			throw new RuntimeException("Fuck the service, not found in registry");
		}
		
		System.out.println("Found the target service : " + serviceAddress);
		
		// 使用到了动态代理。
		return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[] { interfaceCls },
				new RemoteInvocationHandler(ncs, serviceAddress));
	}

}
