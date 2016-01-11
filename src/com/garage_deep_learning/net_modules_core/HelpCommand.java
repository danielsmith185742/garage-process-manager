package com.garage_deep_learning.net_modules_core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

public class HelpCommand implements NetModuleCommand{

	private static final String className = "HelpCommand";
	Socket _socket;
	NetModule _netModule;
	
	public String helpDescription(){ return "Displays help message for each command function";}
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
		_netModule = cNet;
	}
	
	public boolean execute()
	{
		HashMap<String, NetModuleCommand> cmdList = _netModule.commandList();
		
		try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
			
			NetModuleUtil.write(out, "<DATA>");
			for (String key: cmdList.keySet())
			{
				NetModuleCommand cmd = cmdList.get(key);
				NetModuleUtil.write(out, key + " - " + cmd.helpDescription());
			}
			
			NetModuleUtil.write(out, "[EXIT] - Terminates the process");
			NetModuleUtil.write(out, "</DATA>");
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
