package com.garage_deep_learning.net_modules;


import java.io.BufferedWriter;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.net.Socket;


import com.garage_deep_learning.net_modules_core.*;

final public class LinearFit extends NetModulesCommon implements NetModuleCommand{

	private static final String className = "LinearFit";
	private Socket _socket;
	private static LinearFit _linearFit = null;
	
	private LinearFit(String cRegServer, int cRegPort, String cConfigPath)
	{
		super(cRegServer, cRegPort, cConfigPath, className);
		addCommand("[FIT]", this);
	} 
	
	public String helpDescription(){return "Expects datastream of single valued function, and computes running linear fit";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
	}

	public boolean execute() throws IOException
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));

			Log.info(className, "Sending FIT data");
			
			for (int x=0; x<100; x++)
			{
			
				NetModuleUtil.write(out, Integer.toString(x));
			
			}
				
			return false;
		}
		catch(IOException e)
        {
			Log.error(className, e.getMessage());
			throw new IOException("Could not write data to client");
        }
	}
	
	
	public static void main(String [] cArgs)
	{
		try
		{
			NetModuleArgs args = NetModuleUtil.parseArgs(cArgs, className);
			_linearFit = new LinearFit(args.server, args.port, args.configFile);
			NetModuleUtil.runModule(_linearFit, className);
		}
		catch (Exception e)
		{
			Log.info(className, e.getMessage());
		}
	}
	

	
}
