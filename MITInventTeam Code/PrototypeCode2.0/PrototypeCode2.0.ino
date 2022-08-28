


/*  Getting_BPM_to_Monitor prints the BPM to the Serial Monitor, using the least lines of code and PulseSensor Library.
 *  Tutorial Webpage: https://pulsesensor.com/pages/getting-advanced
 *
--------Use This Sketch To------------------------------------------
1) Displays user's live and changing BPM, Beats Per Minute, in Arduino's native Serial Monitor.
2) Print: "♥  A HeartBeat Happened !" when a beat is detected, live.
2) Learn about using a PulseSensor Library "Object".
4) Blinks LED on PIN 13 with user's Heartbeat.
--------------------------------------------------------------------*/

#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math.
#include <PulseSensorPlayground.h>     // Includes the PulseSensorPlayground Library.   

// Arduino pin numbers
int VRx = A2;
int VRy = A3;
int SW = 2;
const int FLEX_PIN = A4; // Pin connected to voltage divider output


int xPosition = 0;
int yPosition = 0;
int SW_state = 0;
int mapX = 0;
int mapY = 0;

//  Variables
const int PulseWire = 0;       // PulseSensor PURPLE WIRE connected to ANALOG PIN 0
//const int LED13 = 13;          // The on-board Arduino LED, close to PIN 13.
int Threshold = 900;           // Determine which Signal to "count as a beat" and which to ignore.
                               // Use the "Gettting Started Project" to fine-tune Threshold Value beyond default setting.
                               // Otherwise leave the default "550" value. 
 int sensorPin = A1;  
 int BTPin = 13;
double count =0;
int lastFlexADC =0;
double StartTime;
double BreathPerMinute = 0;
String message = "";

//int beatPerMin[50];
//int skinTemp[50];
//int breathPerMin[50];
//int index = 0;
int counter =0;
int bpm =0;
int skinTemp =0;
int breathPM =0;

                               
PulseSensorPlayground pulseSensor;  // Creates an instance of the PulseSensorPlayground object called "pulseSensor"

int findBeatsPerMin(){
   int myBPM = pulseSensor.getBeatsPerMinute();  // Calls function on our pulseSensor object that returns BPM as an "int".
                                               // "myBPM" hold this BPM value now. 

if (pulseSensor.sawStartOfBeat()) {            // Constantly test to see if "a beat happened". 
 //Serial.println("♥  A HeartBeat Happened ! "); // If test is "true", print a message "a heartbeat happened".
// Serial.print("BPM: ");                        // Print phrase "BPM: " 
// Serial.println(myBPM);                        // Print the value inside of myBPM. 
 return myBPM;
}else{
  return 0;
}
}
int findSkinTemp(){
  // Variable to store raw temperature
  long rawTemp;

  // Variable to store voltage calculation
  float voltage;

  // Variable to store Fahrenheit value
  float fahrenheit;

  // Variable to store Celsius value
  float celsius;

  // Read the raw 0-1023 value of temperature into a variable.
  rawTemp = analogRead(sensorPin);
  // Calculate the voltage, based on that value.
  // Multiply by maximum voltage (3.3V) and divide by maximum ADC value (1023).
  // If you plan on using this with a LilyPad Simple Arduino on USB power, change to 4.2
  voltage = rawTemp * (5.0 / 1023.0);
  //Serial.print("Voltage: "); // Print voltage reading to serial monitor
  //Serial.println(voltage);

  // Calculate the celsius temperature, based on that voltage..
  celsius = (voltage - 0.5) * 100;
  //Serial.print("Celsius: "); // Print celcius temp to serial monitor
  //Serial.println(celsius);
    // Use a common equation to convert celsius to Fahrenheit. F = C*9/5 + 32.
  fahrenheit = (celsius * 9.0 / 5.0) + 32.0;
  //Serial.print("Fahrenheit: "); // Print Fahrenheit temp to serial monitor
  //Serial.println(fahrenheit); 
  return fahrenheit;
}

int flexSensor(){
  // Read the ADC, and calculate voltage and resistance from it
  int flexADC = analogRead(FLEX_PIN);
  //Serial.println(flexADC);
  //long StartTime = 0;

  if((flexADC != 0) && (lastFlexADC !=0)){
     if((flexADC <= (lastFlexADC-2))|| (flexADC >= (lastFlexADC+2))){
     //Serial.print("last:");
    //Serial.println(lastFlexADC);
    count = count + 0.5;
    //Serial.print("count:");
    // Serial.println(count);
     int temp = (int) count;
    // Serial.println(temp/count);
     if((temp/count) == 1){
        double CurrentTime = millis();
        double ElapsedTime = CurrentTime - StartTime;
        StartTime = CurrentTime;
       // Serial.print("Elapsed Time:");
        ElapsedTime = ElapsedTime*0.001;
       // Serial.println(ElapsedTime);
       // Serial.print("Breath Per Minute");
        BreathPerMinute = 60/ElapsedTime;
        //Serial.println(BreathPerMinute);
  }
     }
     
  }
 
  lastFlexADC = flexADC;
  return BreathPerMinute;
}
void setup() {   

  Serial.begin(9600);
  StartTime = millis();
  pinMode(BTPin, OUTPUT);
  pinMode(VRx, INPUT);
  pinMode(VRy, INPUT);
  pinMode(SW, INPUT_PULLUP);
  // Configure the PulseSensor object, by assigning our variables to it. 
  pulseSensor.analogInput(PulseWire);   
  //pulseSensor.blinkOnPulse(LED13);       //auto-magically blink Arduino's LED with heartbeat.
  pulseSensor.setThreshold(Threshold);   

  // Double-check the "pulseSensor" object was created and "began" seeing a signal. 
   if (pulseSensor.begin()) {
    Serial.println("We created a pulseSensor Object !");  //This prints one time at Arduino power-up,  or on Arduino reset.  
  }
    pinMode(sensorPin, INPUT);
    pinMode(FLEX_PIN, INPUT);
    digitalWrite(BTPin, HIGH);



}



void loop() {
 //digitalWrite(BTPin, LOW);
 if(counter == 0){
     bpm = findBeatsPerMin();
     skinTemp = findSkinTemp();
  //Serial.print("BPM: ");
//Serial.println(bpm);
 //Serial.print("Skin Temperature: ");
 //Serial.println(skinTemp);
// Serial.print("breathPM: ");
// Serial.println(breathPM);
//  delay(2000);
 
 //Serial.println(bpm);
 //Serial.print("Skin Temperature: ");
 //Serial.println(skinTemp);
 //Serial.print("breathPM: ");
 //Serial.println(breathPM);
 //delay(500);
 //digitalWrite(BTPin, LOW);
 }

 breathPM = flexSensor();

 String enter = "  ";
 //String beatsPerMin = bpm;
 message = "Pulse:  " + String(bpm) + enter + "Skin Temperature:  " + skinTemp + enter + "Breath Per Minute:  " + breathPM;
 Serial.println(message);


counter = counter +1;
 if(counter == 22){
  counter = 0;
  //Serial.print("breathPM: ");
 }
  //Serial.print("counter:");
  //Serial.println(counter);


 delay(500);

}

  
