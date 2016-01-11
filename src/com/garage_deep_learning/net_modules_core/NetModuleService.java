package com.garage_deep_learning.net_modules_core;

import java.util.Collection;
import java.util.HashMap;

public class NetModuleService extends NetModulesCommon {

	private static final String className = "NetModuleService";
	private DataBufferOutput _dataBufferOut = new DataBufferOutput(); 
	
	public NetModuleService(String cRegServer, int cRegPort, String cConfigPath, String cModuleType)
	{
		super(cRegServer, cRegPort, cConfigPath, cModuleType);

		addCommand("[STOP]", new StopCommand());
		addCommand("[OUTPUT_START]", new ConnectCommand(_dataBufferOut));
		addCommand("[OUTPUT_STOP]", new DisconnectCommand());
		addCommand("[SHOW_CONNECTIONS]", new ShowConnectionsCommand());
		
	} 
		
	public void publishData(String data)
	{

		Collection<String> outputList = NetModuleConnectionManager.getOutputChannels();		
		
		if(_dataBufferOut.writeReady())
		{
		//	Log.info(className, data);
			_dataBufferOut.write(data, outputList);
		}
		
	}
	
	
}
