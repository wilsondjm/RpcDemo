package me.vincent;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.vincent.api.IHelloService;
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
public class App {
	public static void main(String[] args) throws InterruptedException {

		ILoadBalancer localLB = new RandomLoadBalancer();
		IRegistry registry = new RegistryImpl("10.0.0.18:2181", localLB);

		RpcProxyService prcPrx = new RpcProxyService(registry);

		IPingService is = prcPrx.clientProxy(IPingService.class);
//		System.out.println(is.ping("ping"));
		IHelloService ih = prcPrx.clientProxy(IHelloService.class);

		final Object syncer = new Object();
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
