package me.vincent.core;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelFuture;

public class RPCFuture {
	
	private ChannelFuture channelFuture;
	
	private volatile Object result = null;
	
	private static long defaultTimeout = 3000;
	
	private boolean isAsync = false;
	
	private volatile Object syncer;
	
	public RPCFuture(){
		this(false);
	}
	
	public RPCFuture(boolean isAsync){
		this(isAsync, defaultTimeout);
	}
	
	public RPCFuture(boolean isAsync, long timeout){
		this.isAsync = isAsync;
		this.defaultTimeout = timeout;
		
		this.isAsync = isAsync;
		if(!isAsync){
			syncer = new Object();
		}
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}
	
	private boolean await() throws InterruptedException{
		if(isAsync){
			return false;
		}
		
		synchronized(syncer){
			syncer.wait(defaultTimeout);
		}
		
		
		return true;
	}
	
	public void markResultReady(Object result){
		setResult(result);
		System.out.println(Thread.currentThread().getName() + " - start releasing");
		synchronized(syncer){
			syncer.notify();
		}
		System.out.println(Thread.currentThread().getName() + " - end releasing");
	}
	
	public Object getResult() throws InterruptedException{
		if((!isAsync) && await()){
			return result;
		}
		return this;
	}
	
	public void setResult(Object result){
		this.result = result;
	}
	
	public boolean isAsnyc(){
		return isAsync;
	}

}
