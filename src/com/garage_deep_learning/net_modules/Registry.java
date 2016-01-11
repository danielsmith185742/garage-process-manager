package com.garage_deep_learning.net_modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


import com.garage_deep_learning.net_modules_core.*;


final public class Registry extends NetModulesCommon implements NetModuleCommand{

	private static final String className = "Registry";
	private Socket _socket;
	private ModuleRegister _register;
	private static Registry _registry = null;
	
	private Registry(String cConfigPath)
	{
		super("Not used", 0, cConfigPath, className);
		addCommand("[REGISTER]", this);
		
		String backup_file = getConfigFileParam("backup_register_file");
		_register = new ModuleRegister(backup_file, false);
		
		class RegistryCommand implements NetModuleCommand{
			
			private ModuleRegister _register;
			
			RegistryCommand(ModuleRegister cRegister){_register=cRegister;}
			
			public String helpDescription(){return "Returns details from central registry";}
		
			public void set(Socket cSocket, NetModule cNet ) {_socket = cSocket;}
			
			public boolean execute() throws IOException
			{
				try
				{
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
			
					NetModuleUtil.write(out,"<DATA>");
					_register.write(out);
					NetModuleUtil.write(out,"</DATA>");
					
					return false;
					
				}
				catch(IOException e)
		        {
					Log.error(className, e.getMessage());
					throw new IOException("Could not communicate with client");
		        }
			}
		}
		
		addCommand("[REGISTRY]", new RegistryCommand(_register));
		
	} 
	
	public String helpDescription(){return "Registers netmodule in central Registry";}
	
	public void set(Socket cSocket, NetModule cNet )
	{
		_socket = cSocket;
	}

	public boolean execute() throws IOException
	{
		try
		{
			//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(_socket.getOutputStream()));
			
			//NetModuleUtil.write(out,"[ALLOW]");
         	
			BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			
            ModuleRegisterItem item = new ModuleRegisterItem();
            item.read(in);
            
            _register.add(item);
            _register.print();
			
            return true;
            
		}
		catch(IOException e)
        {
			Log.error(className, e.getMessage());
			throw new IOException("Could not communicate with client");
        }
	}
	
	
	public static void main(String [] cArgs)
	{
		try
		{

			String configFile;
			Log.info(className, "Started.");
			
			if (cArgs.length > 1)
			{
				Log.info(className, "Requires optional [config_path]");
				throw new IllegalArgumentException("Incorrect Arguments");
			}
			else 
			{
			
				if (cArgs.length == 0)
				{
					configFile = "./" + className + ".cfg"; 
		    	  
				}
				else
				{
					configFile = cArgs[1];
				}
			}
			
			_registry = new Registry(configFile);
			
			_registry.startConnectionHandler();
			
			Log.info(className, "Running.");
			
			while (NetModuleUtil.isAlive()){;}
				
			_registry.terminateAllThreads();
			Log.info(className, "Completed.");
			
		}
		catch (Exception e)
		{
			Log.info(className, e.getMessage());
		}
	}
	

	
}
