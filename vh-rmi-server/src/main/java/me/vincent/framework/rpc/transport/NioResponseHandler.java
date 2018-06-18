package me.vincent.framework.rpc.transport;

import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import me.vincent.framework.rpc.core.RPCFuture;
import me.vincent.framework.rpc.core.RPCResponse;


public class NioResponseHandler extends SimpleChannelHandler{
	
	private Map<String, RPCFuture> rfMap;
	
	public NioResponseHandler(Map<String, RPCFuture> rfMap){
		this.rfMap = rfMap;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
//		System.out.println(Thread.currentThread().getName() + " - 收到服务端回执");
		try {
			RPCResponse response = (RPCResponse)e.getMessage();
			String id = response.getAckID();
			RPCFuture rpcFuture = rfMap.get(id);
//			System.out.println(Thread.currentThread().getName() + " - 处理回执 - " + id);
			if(response.getStatus() == RPCResponse.status_enum.success) {
				Object result = response.getData();
				rpcFuture.markResultReady(result);
//				System.out.println(Thread.currentThread().getName() + " Request handler : " + result);
			}else {
				throw new RuntimeException(Thread.currentThread().getName() + " Failed to ");
			}
		}finally {
//			e.getChannel().close();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e){
		e.getCause().printStackTrace();
		
		Channel channel = e.getChannel();
		channel.close();
	}
}
