package com.garage_deep_learning.net_modules_core;
import java.util.Date;

public class Log {
	
	private static Log _log=null;
	private static boolean _verbose=true;
	
	
	private static Log logfile()
	{
		if(_log==null)
		{
			_log = new Log();
		}
		return _log;
	}
	
	private Log()
	{
		
	}
	
	public static void off()
	{
		_verbose=false;
	}
	
	public static void error(String cls, String msg)
	{
		System.out.println("<ERROR> " + logfile().timeStamp() + ": " + cls + ": " + msg + " </ERROR>");
	}
	
	public static void info(String cls, String msg)
	{
		if (_verbose)
		{
			System.out.println("<INFO> " + logfile().timeStamp() + ": " + cls + ": " + msg + " </INFO>");
		}
	}

	private String timeStamp()
	{
		 Date dt = new Date();
		 return dt.toString();
	}
	
	
}
