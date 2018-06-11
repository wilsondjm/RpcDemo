package me.vincent.service;

import me.vincent.api.IPingService;

public class PingServiceImpl implements IPingService {

	public String ping(String ping) {
		
		return "Pong ! -> " + ping;
	}

}
