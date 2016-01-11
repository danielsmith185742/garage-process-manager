package com.garage_deep_learning.net_modules_core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConnectCommand implements NetModuleCommand{

	private static final String className = "ConnectCommand";
	Socket _socket;
	NetModule _netModule;
	DataBufferOutput _dataBuffer;
	
	public ConnectCommand(DataBufferOutput cData)
	{
		_dataBuffer = cData;
	}
	
	public String helpDescription(){ return "Connect output data stream to requesting module";}
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
		_netModule = cNet;
	}
	
	public boolean execute()
	{
		
		String outputModuleName;
		String uniqueChannelName;
		
		try
		{
			Log.info(className, "Entering connection command handler");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			
			String jsonStr = in.readLine();
			
			
			JSONParser parser = new JSONParser();
						
			JSONObject serviceDetails = (JSONObject) parser.parse(jsonStr);
			
			
			outputModuleName = (String) serviceDetails.get("service_name");
		
			Log.info(className, outputModuleName + " connected to output.");
			
			// First register connection in the connection manager
			uniqueChannelName = NetModuleConnectionManager.register(serviceDetails);
			
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
			
			while(!NetModuleConnectionManager.isOutputDisconnected(uniqueChannelName))
			{	
				
				if (NetModuleUtil.executing())
				{

						if(_dataBuffer.readReady(uniqueChannelName))
						{
							String localBuffer = _dataBuffer.read(uniqueChannelName);
							NetModuleUtil.write(out, localBuffer);
						}

				}
		
			}
			
			_socket.close();

        }
		catch(IOException e)
        {
			Log.error(className, e.getMessage());
        }
		catch(Exception e)
        {
			Log.error(className, e.getMessage());
        }
		
		return true;
		
	}
	
}
