package com.stg.io;

import java.io.IOException;
import java.util.Map;

interface HardwareCommunicator {

	void init() throws IOException;

	Map<String, String> sendReceive(Map<String, String> send) throws IllegalStateException, IOException, InterruptedException;
	
	boolean isAvailable();
	
	void shutdown();

}