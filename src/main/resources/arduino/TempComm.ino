// debug mode should only be used with the Arduino Serial Monitor.  It will cause problems with the java program
int debug = 0;

#define BAUD_RATE 9600 // Baudrate for serial monitor and ESP

// Temperature Probes 
#define TEMPERATURENOMINAL 25
#define NUMSAMPLES 10 // Number of samples to smooth input

#define PROBE_1_CMD '1'
#define PROBE_2_CMD '2'
#define PROBE_3_CMD '3'
#define PROBE_4_CMD '4'

#define FAN_CMD '5'
#define SYSTEM_LED_CMD '6'

#define PROBE_1_PIN 0
#define PROBE_2_PIN 1
#define PROBE_3_PIN 2
#define PROBE_4_PIN 3

#define SYSTEM_LED_PIN 2
#define FAN_LED_PIN 8
#define FAN_RELAY_PIN 9
#define FAN_PWM_PIN 10

void setup(void) {
	// put your setup code here, to run once:
	Serial.begin(BAUD_RATE);

	pinMode(FAN_LED_PIN, OUTPUT);
	pinMode(SYSTEM_LED_PIN, OUTPUT);
	pinMode(FAN_PWM_PIN, OUTPUT);
	pinMode(FAN_RELAY_PIN, OUTPUT);

	analogReference (EXTERNAL);
}

void loop(void) {
	if (Serial.available() > 0) {
		String cmds = Serial.readStringUntil(';');
		String response = processCommands(cmds);
		Serial.println(response);
		Serial.flush();
	}
}

String processCommands(String cmds) {
	String response = "";
	String cmdResponse;
	int lastIndex = 0;
	String cmd;

	while (lastIndex >= 0) {
		lastIndex = cmds.indexOf(',');
		if (lastIndex == -1) {
			cmd = cmds;
		} else {
			cmd = cmds.substring(0, lastIndex);
			cmds = cmds.substring(lastIndex + 1);
		}
		cmdResponse = runCommand(cmd);
		if (cmdResponse.length() > 0) {
			response += cmdResponse;
			if (lastIndex != -1) {
				response += ",";
			}
		} else if (lastIndex == -1) {
			// last command failed.  need to remove the trailing ","
			response = response.substring(0, response.length() - 1);
		}
	}
	response += ";";
	return response;
}

String runCommand(String cmd) {
	String response = "";
	if (debug == 1) {
		Serial.println("Processing Command: " + cmd);
	}
	switch (cmd.charAt(0)) {
	case PROBE_1_CMD:
		response += "1=";
		response += sampleTempData(PROBE_1_PIN, NUMSAMPLES);
		break;
	case PROBE_2_CMD:
		response += "2=";
		response += sampleTempData(PROBE_2_PIN, NUMSAMPLES);
		break;
	case PROBE_3_CMD:
		response += "3=";
		response += sampleTempData(PROBE_3_PIN, NUMSAMPLES);
		break;
	case PROBE_4_CMD:
		response += "4=";
		response += sampleTempData(PROBE_4_PIN, NUMSAMPLES);
		break;
	case FAN_CMD:
		response += fanManager(cmd);
		break;
	case SYSTEM_LED_CMD:
		response += lightManager(cmd);
		break;
	default:
		if (debug == 1) {
			Serial.println("Unknown Command: " + cmd);
		}
		break;
	}
	return response;
}

String fanManager(String cmd) {
	String response = "";
	if (cmd.length() > 2) { // should be 5=num
		int inputValue = cmd.substring(2).toInt();
		if (inputValue > 0) {
			if (inputValue > 255) {
				inputValue = 255;
			}
			digitalWrite(FAN_LED_PIN, HIGH);
			digitalWrite(FAN_RELAY_PIN, HIGH);
			analogWrite(FAN_PWM_PIN, inputValue);
		} else {
			if (inputValue < 0) {
				inputValue = 0;
			}
			digitalWrite(FAN_LED_PIN, LOW);
			digitalWrite(FAN_RELAY_PIN, LOW);
			analogWrite(FAN_PWM_PIN, 0);
		}
		response += "5=";
		response += inputValue;
	}
	return response;
}

String lightManager(String cmd) {
	String response = "";
	if (cmd.length() > 2) { // should be 5=num
		int inputValue = cmd.substring(2).toInt();
		if (inputValue > 0) {
			digitalWrite(SYSTEM_LED_PIN, HIGH);
		} else {
			digitalWrite(SYSTEM_LED_PIN, LOW);
		}
		response += "6=";
		response += inputValue;
	}
	return response;
}

/**
 * Samples the analog input numSamples number of times to smooth out the final value
 */
int sampleTempData(int pin, int numSamples) {
	uint8_t i;
	int sum = 0;
	int average = 0;
	int samples[numSamples];

	// take N samples in a row, with a slight delay
	for (i = 0; i < numSamples; i++) {
		samples[i] = analogRead(pin);
		sum += samples[i];
		if (debug == 1) {
			Serial.print("analog Read ");
			Serial.print(i);
			Serial.print(": ");
			Serial.println(samples[i]);
		}
		delay(10);
	}

	// average all the samples out
	average = sum/numSamples;
	
	// joys of integer math.  If our remainder is more than half the average, add one (poor mans rounding)
	if ( (sum % numSamples) >= numSamples / 2 ) {
		average =+ 1;
	}

	if (debug == 1) {
		Serial.print("Average analog reading pin ");
		Serial.print(pin);
		Serial.print(" ");
		Serial.println(average);
	}

	return average;
}

