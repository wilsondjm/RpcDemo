package me.vincent.registry.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer extends AbstractLoadBalancer{

	@Override
	public String doLoadBalance(List<String> nodes) {
		// TODO Auto-generated method stub
		Random random = new Random(System.currentTimeMillis());
		int index = random.nextInt(nodes.size());
		
		return nodes.get(index);
	}

}
