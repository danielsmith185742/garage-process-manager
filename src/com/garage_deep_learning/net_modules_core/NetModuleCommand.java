package com.garage_deep_learning.net_modules_core;

import java.io.IOException;
import java.net.Socket;

public interface NetModuleCommand {
	
	public String helpDescription();
	public void set(Socket cSocket, NetModule cNet );
	public boolean execute() throws IOException;

}
