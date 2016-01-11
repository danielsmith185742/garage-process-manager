package com.garage_deep_learning.net_modules_core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DisconnectCommand implements NetModuleCommand{

	private static final String className = "DisconnectCommand";
	Socket _socket;
	NetModule _netModule;
	
	public String helpDescription(){ return "Disonnect output data stream to requesting module";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
		_netModule = cNet;
	}
	
	public boolean execute()
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			
			String jsonStr = in.readLine();
			
			JSONParser parser = new JSONParser();
			JSONObject serviceDetails = (JSONObject) parser.parse(jsonStr);
			
			String outputModuleName = (String) serviceDetails.get("service_name");
		
			// First unregister connection in the connection manager
			NetModuleConnectionManager.unregister(serviceDetails);
			
			Log.info(className, " disconnected output channel to " + outputModuleName);
			
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
