package me.vincent.transport;

import java.lang.reflect.InvocationTargetException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import me.vincent.api.RPCRequest;
import me.vincent.api.RPCResponse;
import me.vincent.service.RpcService;

public class NioRequestHandler extends SimpleChannelHandler{

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
		System.out.println("收到客户端请求");
		try{
			RPCRequest rpcRequest = (RPCRequest)e.getMessage();
			String requestService = rpcRequest.getClassName();
			Object service = RpcService.serviceMapper.get(requestService);
			if(null == service){
				System.out.println("Unable to locate the service + " + requestService);
				return ;
			}
			
			Object[] parameters = rpcRequest.getArgs();
			String methodName = rpcRequest.getMethodName();
			Class[] types = new Class[parameters.length];
			for(int i=0; i < parameters.length; i++){
				types[i] = parameters[i].getClass();
			}
			Object result = service.getClass().getMethod(methodName, types).invoke(service, parameters);
			RPCResponse rs = new RPCResponse();
			rs.setStatus(RPCResponse.status_enum.success);
			rs.setData(result);
			ChannelFuture f = e.getChannel().write(rs);
			f.addListener(ChannelFutureListener.CLOSE);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e){
		e.getCause().printStackTrace();
		
		Channel channel = e.getChannel();
		channel.close();
	}
}
