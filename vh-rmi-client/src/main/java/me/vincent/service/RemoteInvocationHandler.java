package me.vincent.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import me.vincent.core.RPCRequest;
import me.vincent.transport.NioClientService;

public class RemoteInvocationHandler implements InvocationHandler {
	
	private String serviceAddress;
	private NioClientService ncs;
	
	public RemoteInvocationHandler(NioClientService ncs, String serviceAddress){
		this.serviceAddress = serviceAddress;
		this.ncs = ncs;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RPCRequest rpcRequest = new RPCRequest();
		rpcRequest.setArgs(args);
		rpcRequest.setClassName(method.getDeclaringClass().getName());
		rpcRequest.setMethodName(method.getName());
		rpcRequest.setSyncID(UUID.randomUUID().toString());
		
		return ncs.requestSync(serviceAddress, rpcRequest);
	}

}
