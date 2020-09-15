#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;
String ulaz = ""; 

void setup() {
  pinMode(27, OUTPUT);
  Serial.begin(115200);
  SerialBT.begin("Smart Control");
}

void loop() {
   while(SerialBT.available()){
    ulaz = SerialBT.readString();  
    Serial.println(ulaz);          
    Serial.print("\n");   
    if(ulaz == "upali")           
      digitalWrite(27, HIGH);  
    else if(ulaz == "ugasi")       
      digitalWrite(27, LOW);   
  } 
}
