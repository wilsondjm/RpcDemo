package me.vincent.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import me.vincent.api.RPCRequest;

public class RemoteInvocationHandler implements InvocationHandler {
	
	private String serviceAddress;
	
	public RemoteInvocationHandler(String serviceAddress){
		this.serviceAddress = serviceAddress;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RPCRequest rpcRequest = new RPCRequest();
		rpcRequest.setArgs(args);
		rpcRequest.setClassName(method.getDeclaringClass().getName());
		rpcRequest.setMethodName(method.getName());
		
		TCPTransportService s = new TCPTransportService(serviceAddress);
		Object result = s.send(rpcRequest);
		
		return result;
	}

}
