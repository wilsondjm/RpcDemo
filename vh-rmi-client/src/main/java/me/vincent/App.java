package me.vincent;

import me.vincent.api.IPingService;
import me.vincent.registry.IRegistry;
import me.vincent.registry.RegistryImpl;
import me.vincent.registry.loadbalance.ILoadBalancer;
import me.vincent.registry.loadbalance.RandomLoadBalancer;
import me.vincent.service.RpcProxyService;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
    	
    	ILoadBalancer localLB = new RandomLoadBalancer();
    	IRegistry registry  = new RegistryImpl("10.0.0.18:2181",localLB);
    	
    	RpcProxyService prcPrx = new RpcProxyService(registry);
    	
    	for(int i = 0; i< 20; i++){
	        IPingService is = prcPrx.clientProxy(IPingService.class);
	        System.out.println(is.ping("ping"));
	        Thread.sleep(100);
    	}
    }
}
