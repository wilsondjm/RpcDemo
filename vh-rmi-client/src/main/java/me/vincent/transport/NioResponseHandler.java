package me.vincent.transport;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import me.vincent.api.RPCResponse;

public class NioResponseHandler extends SimpleChannelHandler{
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
		System.out.println("收到服务端回执");
		try {
			RPCResponse response = (RPCResponse)e.getMessage();
			if(response.getStatus() == RPCResponse.status_enum.success) {
				System.out.println(response.getData());
			}else {
				throw new RuntimeException("Failed to ");
			}
		}finally {
			e.getChannel().close();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e){
		e.getCause().printStackTrace();
		
		Channel channel = e.getChannel();
		channel.close();
	}
}
