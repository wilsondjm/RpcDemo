package me.vincent.service;

import java.lang.reflect.Proxy;

import me.vincent.registry.IRegistry;

public class RpcProxyService {
	
	private IRegistry registry;
	
	public RpcProxyService(IRegistry r){
		registry = r;
	}

	public <T> T clientProxy(final Class<T> interfaceCls) {
		
		String serviceAddress = registry.findService(interfaceCls.getName());
		
		if (null == serviceAddress || "".equalsIgnoreCase(serviceAddress)){
			throw new RuntimeException("Fuck the service, not found in registry");
		}
		
		System.out.println("Found the target service : " + serviceAddress);
		// 使用到了动态代理。
		return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[] { interfaceCls },
				new RemoteInvocationHandler(serviceAddress));
	}

}
