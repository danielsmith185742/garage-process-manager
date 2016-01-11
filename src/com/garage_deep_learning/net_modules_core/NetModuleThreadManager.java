package com.garage_deep_learning.net_modules_core;

import java.util.Collection;
import java.util.HashMap;

public class NetModuleThreadManager {
	
	private static HashMap<String, Thread> _threadList = new HashMap<String, Thread>();
	
	public static synchronized Thread create(NetModuleConnection cnt, String cName)
	{
		Thread thread = new Thread(cnt, cName);
		_threadList.put(cName, thread);
		return thread;
	}
	
	public static synchronized void put(String cName, Thread cThread)
	{
		_threadList.put(cName, cThread);
	}
	
	public static synchronized Thread get(String cName)
	{
		return _threadList.get(cName);
	}
	
	public static synchronized void remove(String cName)
	{
		_threadList.remove(cName);
	}
	
	public static synchronized Collection<Thread> values()
	{
		return _threadList.values();
	}

}
