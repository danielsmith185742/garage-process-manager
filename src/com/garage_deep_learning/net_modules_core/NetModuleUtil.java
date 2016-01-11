package com.garage_deep_learning.net_modules_core;

import java.io.BufferedWriter;
import java.io.IOException;

public class NetModuleUtil {

	private static boolean _isAlive = true;
	private static boolean _isExecuting = false;
		
	public static void write(BufferedWriter br, String msg) throws IOException
	{
	
	   br.write(msg);
	   br.newLine();
	   br.flush(); 
	
	}
		
	  
	public static synchronized boolean isAlive()
	{
		return _isAlive;
	}
	
	public static synchronized void kill()
	{
		_isAlive = false;
	}
	
	public static synchronized boolean executing()
	{
		return _isExecuting;
	}
	
	public static synchronized void start()
	{
		_isExecuting=true;
	}
	
	public static synchronized void stop()
	{
		_isExecuting=false;
	}
	
	public static NetModuleArgs parseArgs(String[] cArgs, String className) throws Exception
	{
		NetModuleArgs args = new NetModuleArgs();
		
		Log.info(className, "Started.");
		
		if (cArgs.length < 2 || cArgs.length > 3)
		{
			Log.info(className, "Requires <registration_server> <registration_port> [config_path]");
			throw new IllegalArgumentException("Incorrect Arguments");
		}
		else 
		{
			args.server = cArgs[0];
			args.port = Integer.parseInt(cArgs[1]);
			
			if (cArgs.length == 2)
			{
				args.configFile = "./" + className + ".cfg"; 
	    	  
			}
			else
			{
				args.configFile = cArgs[2];
			}
		}
		
		return args;
	}
	
	public static void runModule(NetModulesCommon cNet, String className)
	{
		if(cNet.register())
		{
			
			cNet.startConnectionHandler();
		
			Log.info(className, "Running.");
				while (NetModuleUtil.isAlive()){;}
				
				cNet.terminateAllThreads();
				Log.info(className, "Completed.");
				
			}
			else
			{				
				Log.error(className, "Failed to register, exiting ");
			}
		}
	}

