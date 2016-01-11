package com.garage_deep_learning.net_modules_core;

import java.util.HashMap;
import java.util.Vector;

public class NetModuleExtendedService extends NetModuleService {

	private static final String className = "NetModuleExtendedService";
	private static HashMap<String, String> _argList = new HashMap<String, String>();
	private HashMap<String,DataBufferInput> _inputChannels = new HashMap<String,DataBufferInput>();
	
	public NetModuleExtendedService(String cRegServer, int cRegPort, String cConfigPath, String cModuleType)
	{
		super(cRegServer, cRegPort, cConfigPath, cModuleType);
		addCommand("[BIND]", new BindCommand(_inputChannels, _argList));
		addCommand("[ARGUMENTS]", new ShowArgumentsCommand(_argList));
	} 
	
	public static void setArg(String cArgName, String cDescription)
	{
		_argList.put(cArgName, cDescription);
	}
	
	public String getString(String var)
	{
		if(_inputChannels.containsKey(var))
		{
			DataBufferInput buffer = _inputChannels.get(var);
			String val = buffer.read();
			return val;
		}
		else throw new IllegalArgumentException(var + " has no associated input channel");
	}
	
	public long getLong(String var) 
	{
		try
		{
			long val;
			String tmp = getString(var);
			if (tmp!=null) val = Long.valueOf(tmp);
			else val = 0;
			return val;
		}
		catch (Exception e)
		{
			Log.error(className, e.getMessage());
			return 0;
		}
	
	}
	
	
}
