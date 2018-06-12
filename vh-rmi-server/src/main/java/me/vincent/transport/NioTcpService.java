package me.vincent.transport;

import java.net.InetSocketAddress;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

public class NioTcpService implements Runnable{
	
	private String serviceAddress;
	private int port;
	
	public NioTcpService(String serviceAddress){
		this.serviceAddress = serviceAddress;
		port = Integer.valueOf(serviceAddress.split(":")[1]);
	}
	
	@Override
	public void run(){
		ChannelFactory factory = new NioServerSocketChannelFactory();
		
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(()->{
			return Channels.pipeline(new ObjectDecoder(1024 * 1024, ClassResolvers  
                    .weakCachingConcurrentResolver(this.getClass().getClassLoader())), 
					new ObjectEncoder(), 
					new NioRequestHandler());
		});
		
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.bind(new InetSocketAddress(port));
	}
}
