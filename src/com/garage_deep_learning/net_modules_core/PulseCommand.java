package com.garage_deep_learning.net_modules_core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

public class PulseCommand implements NetModuleCommand{

	private static final String className = "PulseCommand";
	Socket _socket;
	NetModule _netModule;
	
	public String helpDescription(){ return "Returns the number of seconds since this process started.";}
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
		_netModule = cNet;
	}
	
	public boolean execute()
	{
		long seconds = _netModule.upTime();
		
		try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));			
			NetModuleUtil.write(out, "<DATA>" + Long.toString(seconds) + "</DATA>");

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
