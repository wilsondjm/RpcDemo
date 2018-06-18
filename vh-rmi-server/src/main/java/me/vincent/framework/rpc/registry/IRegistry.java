package me.vincent.framework.rpc.registry;

public interface IRegistry {
	
	void register(String serviceName, String serviceAddress);
	
	String findService(String serviceName);
}
