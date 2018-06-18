package me.vincent.api;

import java.util.Map;

public interface IHelloService {
	
	Map<String, String> hello(String... names);

}
