package com.garage_deep_learning.net_modules_core;

import java.util.Collection;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class NetModuleConnectionManager 
{
	private static HashMap<String, JSONObject> _inputs = new HashMap<String, JSONObject>();
	private static HashMap<String, JSONObject> _outputs = new HashMap<String, JSONObject>();
	
//	private NetModuleConnectionManager()
//	{
//		_inputs = new HashMap<String, JSONObject>();
//		_outputs = new HashMap<String, JSONObject>();
//	}
	
	public static String register(JSONObject json)
	{
		String key = makeUniqueId(json);
		_outputs.put(key, json);
		
		return key;
		
	}
	
	public static String registerInputChannel(JSONObject json)
	{
		String key = makeUniqueId(json);
		_inputs.put(key, json);
		return key;
	}
	
	
	public static void unregister(JSONObject json)
	{
		String key = makeUniqueId(json);
		_outputs.remove(key);
	}
	
	public static void unregisterInputChannel(String cChannelName)
	{
		_inputs.remove(cChannelName);
	}
	
	
	public static boolean isOutputDisconnected(String cChannelName)
	{
		
		if(_outputs.containsKey(cChannelName))
		{
			return false;
		}
		
		return true;
		
	}
	
	
	public static Collection<String> getOutputChannels()
	{
		return _outputs.keySet();
	}
	
	
	public static String dump()
	{
		boolean first=true;
		String jsonString="{";
		
		if (_inputs.size()>0)
		{
			jsonString = "{ Input : [ ";
			for (JSONObject obj: _inputs.values())
			{
				if (!first) jsonString = jsonString + ","; first=false;
				jsonString = jsonString + obj.toJSONString();
			}
			jsonString = jsonString + "] ";
		}
		
		first=true;
		
		if (_outputs.size()>0)
		{
			jsonString = " Output : [ ";
			for (JSONObject obj: _outputs.values())
			{
				if (!first) jsonString = jsonString + ","; first=false;
				jsonString = jsonString + obj.toJSONString();
			}
			jsonString = jsonString + "] ";
		}
		
		jsonString = jsonString + " }";
		
		return jsonString;
		
	}
	
	private static String makeUniqueId(JSONObject json)
	{
		String key = ((String)json.get("hostname")) + ":" + ((String)json.get("port"));
		return key;
	}
}
