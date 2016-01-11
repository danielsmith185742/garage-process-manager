package com.garage_deep_learning.net_modules;

import java.net.*;

import com.garage_deep_learning.net_modules_core.*;
	
import java.io.*;

public class TestClient extends NetModulesCommon
{
	private static final String className = "TestClient";
	
	private TestClient(String cConfigPath)
	{
		super("dummy", 0, cConfigPath, className);
	} 
	
	public void doCommand(String command, BufferedWriter out, BufferedReader in) throws IOException
	{
		try
		{
		
			if (command.equals("[HELP]") || command.equals("[REGISTRY]") 
					|| command.equals("[ARGUMENTS]") || command.equals("[SHOW_CONNECTIONS]")){
		         NetModuleUtil.write(out,command);
	
		         String msg=""; 
		         while(!msg.equals("</DATA>"))
		         {
		        	 msg = in.readLine();
		        	 if(!msg.equals("<DATA>") && !msg.equals("</DATA>"))
		        		 System.out.println(msg);
		         }
		         NetModuleUtil.write(out,"[DISCONNECT]");
		         
	        }

	        else if (command.equals("[FIT]")){
	       	 
		         NetModuleUtil.write(out,command);
	
		         String msg=""; 
		         while(!msg.equals("[COMPLETED]"))
		         {
		        	 msg = in.readLine();
		        	 System.out.println(msg);
		         }
	        
	        }
	        else if (command.equals("[START]")){
	       	 
		         NetModuleUtil.write(out,command);
		         pause();
		         NetModuleUtil.write(out,"[DISCONNECT]");
	        }
	        else if (command.equals("[STOP]")){
		       	 
		         NetModuleUtil.write(out,command);
		         pause();
		         NetModuleUtil.write(out,"[DISCONNECT]");
	        }
	        else if (command.equals("[PULSE]")){
	       	 
	        	Log.info(className, "Sending command " + command);
	        	NetModuleUtil.write(out,command);
	
			    String msg=""; 
		
		       	msg = in.readLine();
		       	System.out.println(msg);
		       	NetModuleUtil.write(out,"[DISCONNECT]");
	        
	        }
	        else if (command.equals("[BIND]")){
	       	 
	        	Log.info(className, "Sending command " + command);
	       	 	NetModuleUtil.write(out,command);
	       	 	
	       	 	pause();
		         
		        String msg="{ \"hostname\" : \"localhost\" \"var\" : \"x\" \"port\" : \"1339\" }";  
		        Log.info(className, msg);
		        NetModuleUtil.write(out,msg);
		         
	        
	        }
	        else if (command.equals("[BINDY]")){
		       	 
	        	Log.info(className, "Sending command [BIND]");
	       	 	NetModuleUtil.write(out,"[BIND]");
	       	 	
	       	 	pause();
		         
		        String msg="{ \"hostname\" : \"localhost\" \"var\" : \"y\" \"port\" : \"1340\" }";  
		        Log.info(className, msg);
		        NetModuleUtil.write(out,msg);
		         
	        
	        }
	        else if (command.equals("[BINDZ]")){
		       	 
	        	Log.info(className, "Sending command [BIND]");
	       	 	NetModuleUtil.write(out,"[BIND]");
	       	 	
	       	 	pause();
		         
		        String msg="{ \"hostname\" : \"localhost\" \"var\" : \"x\" \"port\" : \"1342\" }";  
		        Log.info(className, msg);
		        NetModuleUtil.write(out,msg);
		         
	        
	        }
	        else if (command.equals("[EXIT]")){
	       	 
	        	Log.info(className, "Sending command " + command);
	       	 	NetModuleUtil.write(out,command);

	        }
	        else
	        {
	       	 NetModuleUtil.write(out,command);
	       	 NetModuleUtil.write(out,"[DISCONNECT]");
	        }	
			
		}
      catch(IOException e)
      {
         throw e;
      }
		
	}
	
	
   
	public static void main(String [] cArgs)
	{

	  Log.off();
	  try
	  {
		  
		  if (cArgs.length !=3)
		  {
			  Log.error(className, "Requires <server> <port> <command>");
		  }
		  else
		  {
	    	  NetModuleArgs args = NetModuleUtil.parseArgs(cArgs, className);
	    	  TestClient t = new TestClient("./" + className + ".cfg");
	    	  
	    	  String hostName = args.server;
	    	  int port = args.port;
	    	  String command = args.configFile; //reuse this parameter to send command argument
	    	  
	    	  Log.info(className, "Connecting to " + hostName + " on port " + port);
	         
	    	  Socket client = new Socket(hostName, port);
	         
	    	  Log.info(className, "Just connected to " + client.getRemoteSocketAddress());
	       
	    	  OutputStream outToServer = client.getOutputStream();
	    	  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outToServer));
	    	  
	    	  BufferedReader in
	            = new BufferedReader(new InputStreamReader(client.getInputStream()));
	    	  
	    	  t.doCommand(command, out, in);
	    	  
	    	  client.close();
	    	  Log.info(className, "Completed");
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
      
   }

	   
}
