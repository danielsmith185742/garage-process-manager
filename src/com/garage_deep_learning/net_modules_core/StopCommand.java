package com.garage_deep_learning.net_modules_core;

import java.net.Socket;

public class StopCommand implements NetModuleCommand{

	private static final String className = "StopCommand";

	public String helpDescription(){ return "Stops the process";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		//Does nothing
	}
	
	public boolean execute()
	{
		try
		{
		
			Log.info(className, "Stopping Process");
			NetModuleUtil.stop();
		
		}
		catch(Exception e)
        {
			Log.error(className, e.getMessage());
        }
		
		return false;
		
	}
	
}
