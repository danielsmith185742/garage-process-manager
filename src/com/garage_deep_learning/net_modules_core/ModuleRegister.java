package com.garage_deep_learning.net_modules_core;

import java.util.HashMap;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ModuleRegister {

	private static final String className = "ModuleRegister";
	private HashMap<String, ModuleRegisterItem> _registry;
	private String _backup_file;
	
	public ModuleRegister(String cBackupFile, boolean cReload)
	{
		_registry = new HashMap();
		_backup_file = cBackupFile;
	}
	
	public void add(ModuleRegisterItem item){
		
		String registerKey;
		
		Log.info(className, "Registering " + item.serviceName);
		
		registerKey = item.server + ":" + item.port;
		
		if(_registry.containsKey(registerKey))
		{
			_registry.remove(registerKey);
		}
		
		_registry.put(registerKey, item);
		
	}
	
	public Set<String> keySet()
	{
		return _registry.keySet(); 
	}
	
	public ModuleRegisterItem item(String key)
	{
		return _registry.get(key);
	}
	
	public void remove(NetModule module){
		;
	}
	
	public void write(BufferedWriter cOut) throws IOException
	{
		JSONArray jsonArray = new JSONArray();

        for(ModuleRegisterItem item: _registry.values()){

                JSONObject jsonObj = item.json();
                jsonArray.add(jsonObj);
        }

		NetModuleUtil.write(cOut, jsonArray.toJSONString());
	}
		
	public void print()
	{		
        for(String key: _registry.keySet())
        {
        	ModuleRegisterItem item = _registry.get(key);
        	Log.info(className, item.serviceName + "," + item.moduleName + "," + item.description + "," + item.server + "," + item.port + "," + item.logPath);
       }
		
	}
}
