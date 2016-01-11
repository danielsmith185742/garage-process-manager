package com.garage_deep_learning.net_modules_core;

import java.util.Collection;
import java.util.Vector;

public class DataBufferOutput {
	
	private static final String className = "DataBufferOutput";
	private String _buffer;
	private Vector<String> _outputList;
	private int _counter;
	private boolean _lockWrite;

	
	public DataBufferOutput()
	{
		 _lockWrite = false;
		 _outputList = new Vector<String>();
	}
	
	public boolean writeReady()
	{
		if (_outputList.size()==0)
		{
			return true;
		}
		
		return !_lockWrite;
	}
	
	public synchronized boolean readReady(String cOutputName)
	{
		if (_outputList.contains(cOutputName))
		{
			return true;
		}
		
		return false;
	}
	
	public synchronized String read(String cOutputName)
	{

	//	Log.info(className, cOutputName + " read data.");
		_outputList.remove(cOutputName);
		
		if (_outputList.size()==0)
		{
			_lockWrite=false;
		}

		
		return _buffer;
		
	}
	
	
	public synchronized void write(String cBuffer, Collection<String> outputList)
	{
		
		//Should only be called from a single thread
		_outputList = new Vector<String>(outputList);
		
		_buffer=cBuffer;
		_counter = 0;
		
		_lockWrite = true;
		
	}

}
