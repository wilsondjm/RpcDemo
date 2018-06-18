package me.vincent.framework.rpc.transport;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import me.vincent.framework.rpc.core.RPCFuture;
import me.vincent.framework.rpc.core.RPCRequest;


public class NioClientService {

	private Map<String, Channel> cfMap = new ConcurrentHashMap<String, Channel>();
	private Map<String, RPCFuture> rfMap = new ConcurrentHashMap<String, RPCFuture>();

	public NioClientService connect(String serviceAddress) throws InterruptedException {
		if (cfMap.containsKey(serviceAddress)) {
			return this;
		}
		String[] args = serviceAddress.split(":");
		String serviceHost = args[0];
		int port = Integer.valueOf(args[1]);
		ChannelFactory factory = new NioClientSocketChannelFactory();
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(() -> {
			return Channels.pipeline(
					new ObjectDecoder(1024 * 1024,
							ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())),
					new ObjectEncoder(), new NioResponseHandler(rfMap));
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		ChannelFuture cf = bootstrap.connect(new InetSocketAddress(serviceHost, port)).sync();
		cfMap.put(serviceAddress, cf.getChannel());
		return this;
	}

	private Object request(boolean isAsync, String serviceAddress, RPCRequest rpcRequest) throws InterruptedException {
		RPCFuture rpcFuture = new RPCFuture(isAsync);

		if (!cfMap.containsKey(serviceAddress)) {
			connect(serviceAddress);
		}

		Channel ch = cfMap.get(serviceAddress);
		if (ch.isWritable()) {
			rfMap.put(rpcRequest.getSyncID(), rpcFuture);
			ChannelFuture cf = ch.write(rpcRequest);
			
			rpcFuture.setChannelFuture(cf);
		}

		if (!rpcFuture.isAsnyc()) {
			return rpcFuture.getResult();
		}

		return rpcFuture;
	}

	public Object requestSync(String serviceAddress, RPCRequest rpcRequest) throws InterruptedException{
		return request(false, serviceAddress, rpcRequest);
	}

	public RPCFuture requestAsync(String serviceAddress, RPCRequest rpcRequest) throws InterruptedException{
		return (RPCFuture) request(true, serviceAddress, rpcRequest);
	}
}
