package com.garage_deep_learning.net_modules;

import java.io.IOException;
import java.net.Socket;

import com.garage_deep_learning.net_modules_core.*;


final public class XStream extends NetModuleService implements NetModuleCommand{

	private static final String className = "XStream";
	
	private XStream(String cRegServer, int cRegPort, String cConfigPath)
	{
		super(cRegServer, cRegPort, cConfigPath, className);
		addCommand("[START]", this);
	} 
	
	public String helpDescription(){return "generates variable x for testing";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		//Does nothing 
	}

	public boolean execute() throws IOException
	{

			NetModuleUtil.start();		

			Log.info(className, "Sending stream data");
			
			int x=0;
			
			while(NetModuleUtil.executing())
			{

				publishData(Integer.toString(x++));		
			
			}
		
		return true;
	}
	
	
	public static void main(String [] cArgs)
	{
		try
		{
			NetModuleArgs args = NetModuleUtil.parseArgs(cArgs, className);
			XStream _xStream = new XStream(args.server, args.port, args.configFile);
			NetModuleUtil.runModule(_xStream, className);
		}
		catch (Exception e)
		{
			Log.info(className, e.getMessage());
		}
	}
	

	
}
