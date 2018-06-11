package me.vincent.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import me.vincent.api.RPCRequest;

public class TCPTransportService {

    private String host = "127.0.0.1";

    private int port = 6636;

    public TCPTransportService(String address) {
    	String[] args = address.split(":");
    	host = args[0];
    	port = Integer.valueOf(args[1]);
    }

    //创建一个socket连接
    private Socket newSocket(){
        System.out.println(String.format("创建一个新的连接 %s:%d", host, port));
        Socket socket;
        try{
            socket=new Socket(host,port);
            System.out.println("链接创建完毕");
            return socket;
        }catch (Exception e){
            throw new RuntimeException("连接建立失败");
        }
    }

    public Object send(RPCRequest request){
        Socket socket=null;
        try {
            socket = newSocket();
            System.out.println("开始发送数据");
            //获取输出流，将客户端需要调用的远程方法参数request发送给
            ObjectOutputStream outputStream=new ObjectOutputStream
                    (socket.getOutputStream());
            outputStream.writeObject(request);
            outputStream.flush();
            //获取输入流，得到服务端的返回结果
            System.out.println("数据发送完毕，开始接收数据");
            ObjectInputStream inputStream=new ObjectInputStream
                    (socket.getInputStream());
            Object result=inputStream.readObject();
            System.out.println("数据接收完毕");
            inputStream.close();
            outputStream.close();
            return result;

        }catch (Exception e ){
            throw new RuntimeException("发起远程调用异常:",e);
        }finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
