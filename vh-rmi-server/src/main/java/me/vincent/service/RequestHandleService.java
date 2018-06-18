package me.vincent.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.Callable;

import me.vincent.core.RPCRequest;

public class RequestHandleService implements Callable<Object>{
	
	private Map<String, Object> serviceMapper;
	private Socket socket;
	
	public RequestHandleService(Socket socket){
		serviceMapper = RpcService.serviceMapper;
		this.socket  = socket;
	}

	public Object call() throws Exception {
		
		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		Object requestObject = objectInputStream.readObject();
		if (requestObject instanceof RPCRequest){
			try{
				RPCRequest rpcRequest = (RPCRequest) requestObject;
				String requestService = rpcRequest.getClassName();
				Object service = serviceMapper.get(requestService);
				if(null == service){
					System.out.println("Unable to locate the service + " + requestService);
					return null;
				}
				
				Object[] parameters = rpcRequest.getArgs();
				String methodName = rpcRequest.getMethodName();
				Class[] types = new Class[parameters.length];
				for(int i=0; i < parameters.length; i++){
					types[i] = parameters[i].getClass();
				}
				Object result = service.getClass().getMethod(methodName, types).invoke(service, parameters);
	
				objectOutputStream.writeObject(result);
				objectOutputStream.flush();
			}finally{
				if(null != objectInputStream){
					objectInputStream.close();
				}
				if(null != objectOutputStream){
					objectOutputStream.close();
				}
				System.out.println("数据处理完毕");
			}
		}
		
		return null;
	}

}
