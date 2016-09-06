package com.stg.io;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;

@Component
class HardwareCommunicatorImpl implements HardwareCommunicator {

	Log logger = LogFactory.getLog(getClass());

	@Value("${serial.port.path}")
	private String serialPortPath;

	@Value("${serial.port.baud.rate}")
	private int baud;

	@Value("${arduino.read.sleep.time}")
	private int sleep;

	@Value("${arduino.read.sleep.count}")
	private int timesToRetry;

	private Serial serialPort;
	private BufferedReader responseReader;

	private volatile boolean initialized = false;
	
	/* (non-Javadoc)
	 * @see com.stg.io.HardwareIOInterface#init()
	 */
	@Override
	public synchronized void init() throws IOException {
		if (initialized) {
			return;
		}
		serialPort = SerialFactory.createInstance();
		logger.info("connecting to port at: " + serialPortPath + " at " + baud + " baud.");
		if (!serialPort.isOpen()) {
			serialPort.open(serialPortPath, baud);
		}
		responseReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));

		initialized = true;
	}
	
	/**
	 * Send a message to the Arduino over the provided serial port. Waits for a
	 * response. return an empty map if no response is received in time.
	 * 
	 * @param send
	 * @return response from the Arduino
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public synchronized Map<String, String> sendReceive(Map<String, String> send)
			throws IllegalStateException, IOException, InterruptedException {
		Map<String, String> results = new HashMap<>();
		StringBuilder cmdString = new StringBuilder();

		if (!initialized) {
			this.init();
		}

		// build up the command string from the map.
		for (Entry<String, String> cmd : send.entrySet()) {
			cmdString.append(cmd.getKey());
			if (!StringUtils.isEmpty(cmd.getValue())) {
				cmdString.append("=");
				cmdString.append(cmd.getValue());
			}
			cmdString.append(",");
		}
		cmdString.setCharAt(cmdString.length() - 1, ';');
		
		serialPort.write(cmdString);
		serialPort.flush();
		
		results = getResponse(send);
		if (!validateResponse(send, results)) {
			// communication got out of sync.  Lets give it another read try.
			logger.warn("Invalid response: " + send + " : " + results);
			results = getResponse(send);
		}
		return results;
	}

	@Override
	public boolean isAvailable() {
		if (serialPort != null) {
			return serialPort.isOpen();
		}
		return false;
	}

	@Override
	public void shutdown() {
		if (serialPort != null && serialPort.isOpen()) {
			try {
				serialPort.close();
			} catch (IllegalStateException | IOException e) {
				logger.error("Exception closing the serial port", e);
			}
		}
		initialized = false;
	}
	
	Map<String, String> parseResponse( String response) {
		Map<String, String> results = new HashMap<>();
		
		if (response.charAt(response.length() - 1) != ';') {
			logger.error("Invalid response from hardware, missing ; : " + response);
			return new HashMap<>();
		}
		// get rid of the trailing ";"
		response = response.substring(0, response.length() - 1);
		String[] cmdResults = response.split(",");
		for (String cmdResult : cmdResults) {
			String[] cmdParts = cmdResult.split("=");
			if (cmdParts.length == 2) {
				results.put(cmdParts[0], cmdParts[1]);
			} else {
				logger.error("Invalid command response from hardware: " + cmdResult);
			}
		}
		return results;
	}

	private boolean validateResponse(Map<String, String> send, Map<String, String> results) {
		// validate that what we asked for from the arduino also came back.
		for (Entry<String, String> sentCmd : send.entrySet()) {
			if ( !results.containsKey(sentCmd.getKey()) ) {
				return false;
			} else if ( results.get(sentCmd.getKey()) == null || StringUtils.isEmpty(results.get(sentCmd.getKey()) )) {
				return false;
			}
		}
		return true;
	}

	private Map<String, String> getResponse(Map<String, String> send)
			throws IOException, InterruptedException {
		Map<String, String> results = new HashMap<>();
		// wait for the response
		for (int loop = 0; loop < timesToRetry && !responseReader.ready(); loop++) {
			Thread.sleep(sleep);
			if (logger.isTraceEnabled()) {
				logger.trace("No response yet from Arduino.  Sleeping for the " + (loop + 1) + " time");
			}
		}
		
		if (responseReader.ready()) {
			String response = responseReader.readLine();
			if (logger.isInfoEnabled()) {
				logger.info("Communication with Arduino: " + send + " : " + response);
			}
			if (StringUtils.hasText(response)) {
				results = parseResponse(response);
			}
		} else {
			logger.warn("Failed to return message from Arduino: " + send);
		}
		return results;
	}
}
