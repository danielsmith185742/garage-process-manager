package com.garage_deep_learning.net_modules;

import java.io.IOException;
import java.net.Socket;

import com.garage_deep_learning.net_modules_core.*;


final public class StreamFeed extends NetModuleExtendedService implements NetModuleCommand{

	private static final String className = "StreamFeed";
	
	private StreamFeed(String cRegServer, int cRegPort, String cConfigPath)
	{
		super(cRegServer, cRegPort, cConfigPath, className);
		addCommand("[START]", this);
		
		//Set-up the arguments
		
		setArg("x","Long");
	
	} 
	
	public String helpDescription(){return "Adds two input streams and return the output";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		//Does nothing 
	}

	public boolean execute() throws IOException
	{

			//Main routine
			Log.info(className, "Retrieving x data");
		
			NetModuleUtil.start();		
			
			while(NetModuleUtil.executing())
			{
				
				long x=getLong("x");
		
				Log.info(className, Long.toString(x));
	
			
			}
				
			return true;
			
	}
	
	
	public static void main(String [] cArgs)
	{
		try
		{
			NetModuleArgs args = NetModuleUtil.parseArgs(cArgs, className);
			StreamFeed _xStream = new StreamFeed(args.server, args.port, args.configFile);
			NetModuleUtil.runModule(_xStream, className);
		}
		catch (Exception e)
		{
			Log.info(className, e.getMessage());
		}
	}
	

	
}
