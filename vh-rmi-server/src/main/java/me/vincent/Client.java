package me.vincent;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.vincent.api.IHelloService;
import me.vincent.api.IPingService;
import me.vincent.framework.rpc.proxy.RpcProxyService;
import me.vincent.framework.rpc.registry.IRegistry;
import me.vincent.framework.rpc.registry.RegistryImpl;
import me.vincent.framework.rpc.registry.loadbalance.ILoadBalancer;
import me.vincent.framework.rpc.registry.loadbalance.RandomLoadBalancer;

/**
 * Hello world!
 *
 */
public class Client {
	public static void main(String[] args) throws InterruptedException {

		ILoadBalancer localLB = new RandomLoadBalancer();
		IRegistry registry = new RegistryImpl("10.0.0.18:2181", localLB);

		RpcProxyService prcPrx = new RpcProxyService(registry);

		IPingService is = prcPrx.clientProxy(IPingService.class);
		IHelloService ih = prcPrx.clientProxy(IHelloService.class);

		CountDownLatch cdt = new CountDownLatch(1);

		ExecutorService es = Executors.newCachedThreadPool();
		for (int num = 0; num < 10; num++) {
			es.submit(() -> {
				try {
					cdt.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < 2; i++) {
//					IPingService ps = prcPrx.clientProxy(IPingService.class);
					System.out.println(Thread.currentThread().getName() + is.ping(" - " + i));
				}
			});
		}
		
		es.submit(()->{
			try {
				cdt.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0; i < 10; i++){
				Map<String, String> helloMessages = ih.hello("Bob", "Lisa", "John");
				for(String key : helloMessages.keySet()){
					System.out.println(Thread.currentThread().getName() + " - " + helloMessages.get(key));
				}
			}
		});
		
		Thread.sleep(1000);
		cdt.countDown();
	}
}
