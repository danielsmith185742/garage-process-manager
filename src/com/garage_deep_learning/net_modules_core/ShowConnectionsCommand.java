package com.garage_deep_learning.net_modules_core;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;


public class ShowConnectionsCommand implements NetModuleCommand{

	private static final String className = "ShowConnectsCommand";
	Socket _socket;
	NetModule _netModule;
	
	
	public String helpDescription(){ return "Show active module connections";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
		_netModule = cNet;
	}
	
	public boolean execute()
	{
		try
		{
			
			Log.info(className, "Connection data requested ");
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
			
			String connectionInfo = NetModuleConnectionManager.dump();
			
			NetModuleUtil.write(out, "<DATA>");
			NetModuleUtil.write(out, connectionInfo);
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
