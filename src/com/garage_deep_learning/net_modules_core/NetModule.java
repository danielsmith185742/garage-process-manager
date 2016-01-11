package com.garage_deep_learning.net_modules_core;

import java.util.HashMap;

import org.json.simple.JSONObject;

public interface NetModule {

	public String moduleName();
	public String serviceName();
	public String logPath();
	public String server();
	public int port();
	public String description();
	public long upTime();
	public HashMap<String, NetModuleCommand> commandList();
	public JSONObject info();
	
	public void pause();
	
	// json info();
	// bool pulse ();
	// list connections(); services connected
	// log(); send log details
	// 
	
}
