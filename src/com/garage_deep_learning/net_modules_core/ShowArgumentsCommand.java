package com.garage_deep_learning.net_modules_core;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;


public class ShowArgumentsCommand implements NetModuleCommand{

	private static final String className = "ShowArgumentsCommand";
	Socket _socket;
	HashMap<String, String> _args;
	
	public ShowArgumentsCommand(HashMap<String, String> cArgs)
	{
		_args=cArgs;
	}
	
	public String helpDescription(){ return "Shows service function arguments";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
	}
	
	public boolean execute()
	{
		try
		{
			String jsonOutput;
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
			
			NetModuleUtil.write(out, "<DATA>");
			jsonOutput = "{ \n";
			for(String arg: _args.keySet())
			{
				String description = _args.get(arg);
				jsonOutput = jsonOutput + "\"" + arg + "\" : \"" + description + "\"\n";
			}
			
			jsonOutput = jsonOutput + " }";
			
			NetModuleUtil.write(out, jsonOutput);
			NetModuleUtil.write(out, "</DATA>");
			//_socket.close();
		
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
