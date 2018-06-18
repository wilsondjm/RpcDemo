package me.vincent.framework.rpc.registry.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalancer implements ILoadBalancer {

	@Override
	public String loadBalance(List<String> nodes) {
		if(nodes.isEmpty()){
			return "";
		}else if (nodes.size() == 1){
			return nodes.get(0);
		}else{
			return doLoadBalance(nodes);
		}
	}
	
	public abstract String doLoadBalance(List<String> nodes);

}
