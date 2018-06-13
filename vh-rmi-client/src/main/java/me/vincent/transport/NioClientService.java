package me.vincent.transport;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import me.vincent.api.RPCRequest;

public class NioClientService {

	private String serviceHost;
	private int port;
	
	public NioClientService(String serviceAddress)
	{
		String[] args = serviceAddress.split(":");
		serviceHost = args[0];
		port = Integer.valueOf(args[1]);
	}
	
	public Object request(RPCRequest rpcRequest) throws InterruptedException {
		ChannelFactory factory = new NioClientSocketChannelFactory();
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(()->{
			return Channels.pipeline(new ObjectDecoder(1024 * 1024, ClassResolvers  
                    .weakCachingConcurrentResolver(this.getClass().getClassLoader())), 
					new ObjectEncoder(),
					new NioResponseHandler());
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		ChannelFuture cf = bootstrap.connect(new InetSocketAddress(serviceHost, port)).sync();
		ChannelFuture newcf = cf.getChannel().write(rpcRequest);
		
		return null;
	}
}
