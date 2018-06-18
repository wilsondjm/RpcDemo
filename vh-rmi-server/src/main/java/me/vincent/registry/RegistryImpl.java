package me.vincent.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class RegistryImpl implements IRegistry {

	private CuratorFramework cf;

	private final static String ZK_REGISTRY_PATH = "/vhFrame/Registries";

	private static String connStr = "10.0.0.18:2181,10.0.0.18:3181,10.0.0.18:4181";

	public RegistryImpl(String connectStr) {
		connStr = connectStr;
		init();
	}

	private void init() {
		cf = CuratorFrameworkFactory.builder().connectString(connStr).sessionTimeoutMs(5000).connectionTimeoutMs(5000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		cf.start();
	}

	@Override
	public void register(String serviceName, String serviceAddress) {
		String servicePath = ZK_REGISTRY_PATH + "/" + serviceName;

		try {
			if (cf.checkExists().forPath(servicePath) == null) {
				cf.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
			}

			String addressPath = servicePath + "/" + serviceAddress;
			String serviceNode = cf.create().withMode(CreateMode.EPHEMERAL).forPath(addressPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(String.format("Published %s at %s", serviceName, serviceAddress));
	}

}
