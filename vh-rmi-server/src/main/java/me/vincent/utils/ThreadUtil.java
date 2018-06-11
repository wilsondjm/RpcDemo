package me.vincent.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
	
	public static ExecutorService service = Executors.newCachedThreadPool();
	
	public static Object execute(Callable task){
		return service.submit(task);
	}
	
	public static void run(Runnable task){
		service.execute(task);
	}

}
