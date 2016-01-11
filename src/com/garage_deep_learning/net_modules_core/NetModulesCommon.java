package com.garage_deep_learning.net_modules_core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NetModulesCommon implements NetModule, Runnable{

	private static final String className = "NetModulesCommon";
	private String _configPath;
	private String _moduleType;
	private NetModuleConfigParams _cfg;
	private static HashMap<String, NetModuleCommand> _cmdList  = new HashMap<String, NetModuleCommand> ();
	private ServerSocket _serverSocket;
	private String _regServer;
	private int _regPort;
	private Date _startDate;
	
	public NetModulesCommon(String cRegServer, int cRegPort, String cConfigPath, String cModuleType)
	{
		_configPath = cConfigPath;
		_moduleType = cModuleType;
		_regServer = cRegServer;
		_regPort = cRegPort;
		_startDate = new Date();
		readConfigFile(_cfg = new NetModuleConfigParams());
		addCommand("[PULSE]", new PulseCommand());
		addCommand("[HELP]", new HelpCommand());
		
	}
	
	public String moduleName(){return _moduleType;}
	public String serviceName(){return _cfg._serviceName;}
	public String logPath(){return _cfg._logPath;}
	public String server(){return _cfg._ipAddress;}
	public int port(){return _cfg._port;}
	public String description(){return _cfg._description;}
	public long upTime()
	{
		Date now = new Date();
		long seconds = (now.getTime()-_startDate.getTime())/1000;
		return seconds;
	}
	
   public JSONObject info()
   {
	   
	   JSONObject obj = new JSONObject();
	   obj.put("module_name", moduleName());
	   obj.put("service_name",serviceName());
	   obj.put("log_path",logPath());
	   obj.put("server",server());
	   obj.put("port",port());
	   obj.put("description",description());
	   obj.put("up_time",upTime());
	   return obj;
	   
   }
	
	public HashMap<String, NetModuleCommand> commandList()
	{
		return _cmdList;
	}
		
	public void run()
	{
		NetModuleConnection connectionHandler;
		
		try
		{
			_serverSocket = new ServerSocket(port());
			connectionHandler = new NetModuleConnection(this, _serverSocket);
			
			long i=0;
			
			while(NetModuleUtil.isAlive())
			{
				if (connectionHandler.listenForConnection())
				{
					Log.info(className, "Invoking Handler");
					Thread connectHandle = NetModuleThreadManager.create(connectionHandler, className + "Handler" + i++);
					connectHandle.start();
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
		NetModuleUtil.kill();
		
	}
	
	public void startConnectionHandler()
	{
		Thread m = new Thread(this, serviceName() + "_main");
		NetModuleThreadManager.put(serviceName() + "_main", m);
		m.start();
	}
		
	public void terminateAllThreads()
	{
		try
		{
			_serverSocket.close();
		}
		catch (Exception e)
		{
			
		}
		for(Thread m: NetModuleThreadManager.values())
		{
			Log.info(className, m.getName());
			m.interrupt();
			m = null;
		}
	}
	
	public boolean register()
	{
		try
		{
		   	Log.info(className,"Connecting to " + _regServer + " on port " + _regPort);
		    Socket client = new Socket(_regServer, _regPort);
		    Log.info(className, "Connected to " + client.getRemoteSocketAddress());
		     
		    OutputStream outToServer = client.getOutputStream();
		  
		    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outToServer));
				
		    NetModuleUtil.write(out,"[REGISTER]");
		//	BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		//	String msg=in.readLine();
		//	if(msg.equals("[ALLOW]"))
		//	{ 
		    pause();
			NetModuleUtil.write(out,info().toJSONString());
			pause();
			client.close();
	//		}
		}
		catch (IOException e)
		{
			Log.info(className, e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public String getConfigFileParam(String param)
	   {
	   
		   String paramValue;
		   JSONParser parser = new JSONParser();
		   
	        try {
	 
	        	Log.info(className, "Opening Config File: " + _configPath);
	        	
	        	FileReader file = new FileReader(_configPath);
	        	
	        	Log.info(className, "Reading Config File: " + _configPath);
	            Object obj = parser.parse(file);
	            
	            JSONObject jsonObject = (JSONObject) obj;

	            paramValue = (String) jsonObject.get(param);
	            
	            return paramValue;
	 
	        } catch (Exception e) {
	        	Log.error(className, "Failed to Read Config File:" + _configPath);
	        	throw new IllegalArgumentException("config file path incorrect");
	        }
		   
	
	   }
	
	public void pause()
	{
	
	  try {
		    Thread.sleep(250);                 //250 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	
	}
	
	
	protected void addCommand(String cName, NetModuleCommand cModule)
	{
		_cmdList.put(cName, cModule);
	}
	
	
	private void readConfigFile(NetModuleConfigParams p)
	   {
	   
		   Long tmp;
		   JSONParser parser = new JSONParser();
		   
	        try {
	 
	        	Log.info(className, "Opening Config File: " + _configPath);
	        	
	        	FileReader file = new FileReader(_configPath);
	        	
	        	Log.info(className, "Reading Config File: " + _configPath);
	            Object obj = parser.parse(file);
	            
	            JSONObject jsonObject = (JSONObject) obj;
	            	 
	            tmp = (long) jsonObject.get("port");
	            p.setPort(tmp.intValue());
	            p._serviceName = (String) jsonObject.get("service_name");
	            p._logPath = (String) jsonObject.get("log_path");
	            p._description = (String) jsonObject.get("description");
	        

	            setIPAddress(p);
	            
	 
	        } catch (Exception e) {
	        	Log.error(className, "Failed to Read Config File:" + _configPath);
	        	throw new IllegalArgumentException("config file path incorrect");
	        }
		   
	
	   }
	
	   private void setIPAddress(NetModuleConfigParams p)
	   {
		   try {
			 //  System.out.println("Full list of Network Interfaces:");
			   for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			     NetworkInterface intf = en.nextElement();
			//     System.out.println("    " + intf.getName() + " " + intf.getDisplayName());
			     if (intf.getName().equals("eth0")){
			    	 for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
			    		 p._ipAddress = enumIpAddr.nextElement().toString();
			    	 }
			     }
			   }
			   Log.info(className,"Found IP Address, " + p._ipAddress);
			 } catch (SocketException e) {
				 Log.error(className, "(error retrieving network interface list)");
			 }
	   }
	
	
}
