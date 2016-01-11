package com.garage_deep_learning.net_modules_core;

public class DataBufferInput {
	
	private static final String className = "DataBufferInput";
	private String _buffer;
	
	public synchronized void write(String cData)
	{	
		_buffer=cData;
	//	Log.info(className, _buffer);
	}
	public synchronized String read()
	{
	//	Log.info(className, _buffer);
		return _buffer;
	}

}
