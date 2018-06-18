package me.vincent.api.impl;

import java.util.HashMap;
import java.util.Map;

import me.vincent.api.IHelloService;

public class HelloServiceImpl implements IHelloService {

	@Override
	public Map<String, String> hello(String... names) {
		Map<String, String> helloMessages = new HashMap<>(names.length);
		for(String name : names){
			helloMessages.put(name, "Hello from server for " + name);
		}
		return helloMessages;
	}

}
