package com.garage_deep_learning.net_modules_core;
import java.io.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ModuleRegisterItem {
	
	public String moduleName;
	public String serviceName;
	public String logPath;
	public String server;
	public int port;
	public String description;
	
	
	public ModuleRegisterItem(){;}
	
	public ModuleRegisterItem(ModuleRegisterItem itm)
	{
		moduleName = itm.moduleName;
		serviceName = itm.serviceName;
		logPath = itm.logPath;
		server = itm.server;
		port = itm.port;
		description = itm.description;
	}
		
	public void read(BufferedReader cIn) throws IOException
	{
		JSONObject json;
		String jsonString = cIn.readLine();
		try {
			json = (JSONObject) new JSONParser().parse(jsonString);
			
			Long tmp = (long) json.get("port");
			moduleName = (String) json.get("module_name");
			serviceName  = (String) json.get("service_name");
			description = (String) json.get("description");
			server = (String) json.get("server");
			port = tmp.intValue();
			logPath = (String) json.get("log_path");
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void write(BufferedWriter cOut) throws IOException
	{
		NetModuleUtil.write(cOut, json().toJSONString());
	}
	
	public JSONObject json()
	{
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("port", port);
		jsonObj.put("module_name", moduleName);
		jsonObj.put("service_name", serviceName);
		jsonObj.put("description", description);
		jsonObj.put("server", server);
		jsonObj.put("log_path", logPath);
		
		return jsonObj;
		
	}
	
}
