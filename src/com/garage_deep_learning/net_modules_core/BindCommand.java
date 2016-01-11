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


public class BindCommand implements NetModuleCommand{

	private static final String className = "BindCommand";
	Socket _socket;
	NetModule _netModule;
	HashMap<String,DataBufferInput> _inputChannels;
	HashMap<String, String> _argList;
	
	public BindCommand(HashMap<String,DataBufferInput> cInputChannels,  HashMap<String, String> cArgList)
	{
		_inputChannels = cInputChannels;
		_argList = cArgList;		
	}
	
	public String helpDescription(){ return "Command to instruct Module to send a Bind request";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
		_netModule = cNet;
	}
	
	public boolean execute() throws IOException
	{
	
		//Need to attempt to bind to another service
		
		//First need to receive the commands from one socket
		//then listen to another socket as determined by the commands received
		
		//need to allocate a data buffer for this input channel bound to the correct variable name
		
		//input channels are read asynchronously
		try
		{
			
			BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			
			String jsonStr = in.readLine();
			
			Log.info(className, jsonStr);
			
			JSONParser parser = new JSONParser();
			JSONObject serviceDetails = (JSONObject) parser.parse(jsonStr);
			
			String hostname = (String) serviceDetails.get("hostname");
			
			String tmp = (String) serviceDetails.get("port");
			
			int port = Integer.valueOf(tmp);
			
			String var = (String) serviceDetails.get("var");
			
			_socket.close();
			
			Log.info(className, "received parameters, " + hostname + " " + Integer.toString(port) + " " + var);
			
			if (!_argList.containsKey(var))
			{
				Log.info(className, "Binding variable, " + var + " is not valid");
			}
			else
			{

				 DataBufferInput buffer = new DataBufferInput();
		
				 Socket client = new Socket(hostname, port);
		         Log.info(className, "Just connected to " + client.getRemoteSocketAddress());
		         
		         String uniqueChannelName = NetModuleConnectionManager.registerInputChannel(serviceDetails);
		         _inputChannels.put(var, buffer);
		         
		        
			    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			    NetModuleUtil.write(out, "[OUTPUT_START]");
		        
			    String serviceInfo="{ \"hostname\" : \"" + _netModule.server() + "\", \"port\" : \"" + Integer.toString(_netModule.port()) + 
			    	"\", \"var\" : \"" + var + "\", \"service_name\" : \"" + _netModule.serviceName() + "\" }"; 
			    
			    
			    _netModule.pause(); 
			    NetModuleUtil.write(out, serviceInfo);
			    
			    while(!NetModuleUtil.executing()){;}
			    

		        	 
			    BufferedReader dataIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
		         
		        while(NetModuleUtil.executing())
			    {	 
		        	 
		        	 String data = dataIn.readLine();
		        //	 Log.info(className, data);
		        	 if (data!=null) buffer.write(data);
		        	 
		         }
		        
				 Socket client2 = new Socket(hostname, port);
		         Log.info(className, "Just connected to " + client2.getRemoteSocketAddress());
		        
		        
		        out = new BufferedWriter(new OutputStreamWriter(client2.getOutputStream()));
		        NetModuleUtil.write(out, "[OUTPUT_STOP]");
		        _netModule.pause();
			    Log.info(className, "Output stop command sent");
		        NetModuleUtil.write(out, serviceInfo);
		        _netModule.pause();
		        client2.close();
		        NetModuleConnectionManager.unregisterInputChannel(uniqueChannelName);
		        client.close();
		        
		        
		        return true;
			
			}
		}
		catch(IOException e)
        {
			Log.error(className, e.getMessage());
        }
		catch(Exception e)
        {
			Log.error(className, e.getMessage());
        }
		
		return false;
	}
	
}
