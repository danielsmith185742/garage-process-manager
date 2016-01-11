package com.garage_deep_learning.net_modules_core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class NetModuleConnection implements Runnable{

	private static final String className = "NetModuleConnection";
	private Socket _server;
	private NetModule _netModule;
	private ServerSocket _serverSocket;
	
	NetModuleConnection(NetModule cNetModule, ServerSocket cServerSocket)
	{
		 _netModule = cNetModule;
		 _serverSocket = cServerSocket;
	}
	
	public boolean listenForConnection()
	{
		
		try
		{
			//ServerSocket serverSocket = new ServerSocket(_netModule.port());
			Log.info(className, "Waiting for client on port " + _serverSocket.getLocalPort());
	        _server = _serverSocket.accept();	            
	        Log.info(className, "Just connected to " + _server.getRemoteSocketAddress());
	        return true;
		}
		catch(IOException e)
        {
			if (NetModuleUtil.isAlive()) Log.error(className, e.getMessage());
			return false;
        }
		

		
	}
	
	public void run()
	{
		String command;
		
		try
		{
			while(true)
			{
				 BufferedReader in
		         = new BufferedReader(new InputStreamReader(_server.getInputStream()));

				 command = in.readLine();
	         
				 Log.info(className, "Received command, " + command);
	
				 HashMap<String, NetModuleCommand> cmdList = _netModule.commandList();
				 
				 if(cmdList.containsKey(command))
				 {
					 Log.info(className, command + " is a valid command");
					 NetModuleCommand cmd= cmdList.get(command);
					 cmd.set(_server, _netModule);
					 if(cmd.execute())
					 {
						 break;
					 }
				 }
				 else if(command.equals("[EXIT]"))
				 {
					 Log.info(className, "Exiting...");
					 
					 _server.close();
					 NetModuleUtil.kill();
					 break;
				 }
				 else if(command.equals("[DISCONNECT]"))
				 { 
					 _server.close();
					 break;
				 }
				 else
				 {
					 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_server.getOutputStream()));
					 
					 NetModuleUtil.write(out, "<ERROR> " + command + " not recognized by " + _netModule.serviceName() + "</ERROR>");
					 Log.error(className, "Received command, " + command + " not recognized." );
					 _server.close();
					 break;
				 }
				 
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
				
		NetModuleThreadManager.remove(Thread.currentThread().getName());
		
	}
	
	
}
