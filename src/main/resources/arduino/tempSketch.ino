int systemLedPin = 2;
int fanLedPin = 8;
int fanRelayPin = 9;
int fanPWMPin = 10;

int inputValue = 0;
int tempReading;
int incomingByte = 0;
int probe1 = 1; // "1"
int probe2 = 2; // "2"
int probe3 = 3; // "3"
int probe4 = 4; // "4"
int fan = 5; // "f" or 70 = "F"
int systemLed = 6;

int debug = 0;
int debugdelay = 0;

#define TEMP_READ_DELAY 10000 // 10 seconds between temp reading
#define BAUD_RATE 9600 // Baudrate for serial monitor and ESP

// Temperature Probes 
#define TEMPERATURENOMINAL 25
#define NUMSAMPLES 10 // Number of samples to smooth input

#define PROBE_1_PIN 0
#define PROBE_2_PIN 1
#define PROBE_3_PIN 2
#define PROBE_4_PIN 3
#define FOOD_THERMISTORNOMINAL 1000000 // resistance at 25 degrees C 
#define FOOD_BCOEFFICIENT 4600 // The beta coefficient of the thermistor
#define FOOD_SERIESRESISTOR 1000000 // the value of the 'other' resistor
 
#define GRILL_THERMISTORNOMINAL 1000000 // resistance at 25 degrees C 
#define GRILL_BCOEFFICIENT 4800 // The beta coefficient of the thermistor
#define GRILL_SERIESRESISTOR 1000000 // the value of the 'other' resistor
float samples[NUMSAMPLES];

float foodAverage;
float foodResistance;
float foodDegC;
float foodDegF;

float grillAverage;
float grillResistance;
float grillDegC;
float grillDegF;

void setup(void) {
  // put your setup code here, to run once:
  Serial.begin(BAUD_RATE);
  
  pinMode(fanLedPin, OUTPUT);
  pinMode(systemLedPin, OUTPUT);
  pinMode(fanPWMPin, OUTPUT);
  pinMode(fanRelayPin, OUTPUT);
  
  analogReference(EXTERNAL);
  
  foodAverage = sampleTempData(PROBE_2_PIN, NUMSAMPLES);
  foodResistance = convertAnalogToResistance(foodAverage, FOOD_SERIESRESISTOR);
  foodDegC = calculateCFromResistance(foodResistance, FOOD_THERMISTORNOMINAL, FOOD_BCOEFFICIENT, TEMPERATURENOMINAL);
  foodDegF = convertCtoF(foodDegC);

  grillAverage = sampleTempData(PROBE_1_PIN, NUMSAMPLES);
  grillResistance = convertAnalogToResistance(grillAverage, GRILL_SERIESRESISTOR);
  grillDegC = calculateCFromResistance(grillResistance, GRILL_THERMISTORNOMINAL, GRILL_BCOEFFICIENT, TEMPERATURENOMINAL);
  grillDegF = convertCtoF(grillDegC);

}

void loop(void) {

    readSerialPort();

    if(incomingByte == probe1) { // Read and return probe #1
      foodAverage = sampleTempData(PROBE_1_PIN, NUMSAMPLES);
      Serial.print("1=");
      Serial.println(foodAverage);
      if(debug == 1) {
        foodResistance = convertAnalogToResistance(foodAverage, FOOD_SERIESRESISTOR);
        foodDegC = calculateCFromResistance(foodResistance, FOOD_THERMISTORNOMINAL, FOOD_BCOEFFICIENT, TEMPERATURENOMINAL);
        foodDegF = convertCtoF(foodDegC);
        Serial.print("Probe 1: ");
        Serial.print("You manually read the probe!");
        Serial.println(foodDegF);
      }
      
    } else if(incomingByte == probe2) { // Read and return probe #2
      grillAverage = sampleTempData(PROBE_2_PIN, NUMSAMPLES);
      Serial.print("2=");
      Serial.println(grillAverage);
      if(debug == 1) {
        grillResistance = convertAnalogToResistance(grillAverage, GRILL_SERIESRESISTOR);
        grillDegC = calculateCFromResistance(grillResistance, GRILL_THERMISTORNOMINAL, GRILL_BCOEFFICIENT, TEMPERATURENOMINAL);
        grillDegF = convertCtoF(grillDegC);
        Serial.println("Probe 2: ");
        Serial.print("You manually read the probe!");
        Serial.println(grillDegF);
      }

    } else if(incomingByte == probe3) { // Read and return probe #3
     grillAverage = sampleTempData(PROBE_3_PIN, NUMSAMPLES);
      Serial.print("3=");
      Serial.println(grillAverage);
      if(debug == 1) {
        grillResistance = convertAnalogToResistance(grillAverage, GRILL_SERIESRESISTOR);
        grillDegC = calculateCFromResistance(grillResistance, GRILL_THERMISTORNOMINAL, GRILL_BCOEFFICIENT, TEMPERATURENOMINAL);
        grillDegF = convertCtoF(grillDegC);
        Serial.println("Probe 3: ");
        Serial.print("You manually read the probe!");
        Serial.println(grillDegF);
      }
    } else if(incomingByte == probe4) { // Read and return probe #4
     grillAverage = sampleTempData(PROBE_4_PIN, NUMSAMPLES);
      Serial.print("4=");
      Serial.println(grillAverage);
      if(debug == 1) {
        grillResistance = convertAnalogToResistance(grillAverage, GRILL_SERIESRESISTOR);
        grillDegC = calculateCFromResistance(grillResistance, GRILL_THERMISTORNOMINAL, GRILL_BCOEFFICIENT, TEMPERATURENOMINAL);
        grillDegF = convertCtoF(grillDegC);
        Serial.println("Probe 4: ");
        Serial.print("You manually read the probe!");
        Serial.println(grillDegF);
      }
    } else if(incomingByte == fan) { // Fan speed
      if(debug == 1) {
        Serial.print("Changing the fan value");
      }
//      readSerialPort();
      if(inputValue > 0) {
        digitalWrite(fanLedPin, HIGH);
        digitalWrite(fanRelayPin, HIGH);
        analogWrite(fanPWMPin, inputValue);
        if(debug == 1) {
          Serial.println(" fan ON ");
        }
      } else {
        digitalWrite(fanLedPin, LOW);
        digitalWrite(fanRelayPin, LOW);
        analogWrite(fanPWMPin, 0);
        if(debug == 1) {
          Serial.println(" fan OFF ");
        }
      }
      if(debug == 1) {
        Serial.write("inputValue: ");
      }
      Serial.print("5=");
      Serial.println(inputValue);

    } else if (incomingByte == systemLed) {
      if(debug == 1) {
        Serial.print("Changing the system LED value");
      }
      if (inputValue > 0) {
        digitalWrite(systemLedPin, HIGH);
        if(debug == 1) {
          Serial.println(" system LED ON ");
        }
      } else {
        digitalWrite(systemLedPin, LOW);
        if(debug == 1) {
          Serial.println(" system LED OFF ");
        }
      }      
      Serial.print("6=");
      Serial.println(inputValue);
    }

  Serial.flush();
}

byte readSerialPort() {
  int peekValue = Serial.peek();
  if(debug == 1) {
    if(peekValue != -1) {
      Serial.print("Serial.peek() = ");
      Serial.println(peekValue);
    }
  }
  if(peekValue == -1) {
    incomingByte = 0;
  }

  if (Serial.available() > 0) { // == 1
    if(peekValue == 100) {
      if(debug == 0) {
        Serial.println("Debug is ON");
        debug = 1;
      } else {
        Serial.println("Debug is OFF");
        debug = 0;
      }
    }
    if(peekValue == 97) {
      if(debugdelay == 0) {
        debugdelay = 1;
      } else {
        debugdelay = 0;
      }
    }
    
    // read the next incoming byte:
    incomingByte = Serial.parseInt();
    if(incomingByte == 5 || incomingByte == 6) { // "5"
      if(Serial.peek() == 44) {
        inputValue = Serial.parseInt();
        if(debug == 1) {
          Serial.print(" inputValue: ");
          Serial.println(inputValue);
        }
      }
    }

    if(debug == 1) {
      Serial.print(" incoming int: ");
      Serial.println(incomingByte);
    }
  }
  return incomingByte;
}

/**
 * Samples the analog input numSamples number of times to smooth out the final value
 */
float sampleTempData(int pin, int numSamples){
  uint8_t i;
  float average;
 
  average = 0;
  // take N samples in a row, with a slight delay
  for (i=0; i< numSamples; i++) {
   samples[i] = analogRead(pin);
   average += samples[i];
   if(debug == 1) {
     Serial.print("analog Read ");
     Serial.print(i);
     Serial.print(": ");
     Serial.println(samples[i]);
   }
//   delay(100);
  }
 
  // average all the samples out
  average /= numSamples;

  if(debug == 1) {
    Serial.print("Average analog reading pin ");
    Serial.print(pin);
    Serial.print(" ");
    Serial.println(average);
  }

  return average;
}

/**
 * Helper method to convert analog data to resistance. Taken from https://learn.adafruit.com/thermistor/using-a-thermistor
 */
float convertAnalogToResistance(float avg, long resistor){
  avg = 1023 / avg - 1;
  avg = resistor / avg;
  if(debug == 1) {
    Serial.print("Thermistor resistance "); 
    Serial.println(avg);
  }
  return avg;
}

/**
 * Helper method to calculate C from a resistance. This taken from https://learn.adafruit.com/thermistor/using-a-thermistor
 */
float calculateCFromResistance(float resistance, long thermNominal, int coefficient, int tempNominal){
  float steinhart;
  steinhart = resistance / thermNominal;     // (R/Ro)
  steinhart = log(steinhart);                  // ln(R/Ro)
  steinhart /= coefficient;                   // 1/B * ln(R/Ro)
  steinhart += 1.0 / (tempNominal + 273.15); // + (1/To)
  steinhart = 1.0 / steinhart;                 // Invert
  steinhart -= 273.15;                         // convert to C
  if(debug == 1) {
    Serial.print("C from Resistance ");
    Serial.println(steinhart);
  }
  return steinhart;
}

/**
 * Converts C to F
 */
float convertCtoF(float degC){
  if(debug == 1) {
    Serial.print("C to F ");
    Serial.println(degC * (9.0/5.0) + 32.0);
  }
  return degC * (9.0/5.0) + 32.0;
}

/**
 * Log the temps to the serial monitor
 */
void writeTempDatatoSerial(float temp1, float temp2){
  if(debug == 1) {
    Serial.print("Grill: "); 
    Serial.println(temp1, 2);
    Serial.print("Food: ");
    Serial.println(temp2, 2);
  }
}

