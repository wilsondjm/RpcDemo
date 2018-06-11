package me.vincent.registry.loadbalance;

import java.util.List;

public interface ILoadBalancer {
	
	String loadBalance(List<String> nodes);

}
