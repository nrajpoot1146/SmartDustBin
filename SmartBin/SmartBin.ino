//#include "Arduino.h"
//The setup function is called once at startup of the sketch
#include"inc/resource.h"

#include <SPI.h>
#include <MFRC522.h>

#define SS_PIN 10
#define RST_PIN 9

MFRC522 rfid(SS_PIN, RST_PIN); // Instance of the class

MFRC522::MIFARE_Key key;

// Init array that will store new NUID
byte nuidPICC[4];

IRSensor obj(A0);
//Smotor cap(9,100,0);
Cap *c;

void setup() {
  c = new Cap(4,5,false);
  Serial.begin(9600);
  ep.begin();
  //cap.write(0);*/

   SPI.begin(); // Init SPI bus
  rfid.PCD_Init(); // Init MFRC522

  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }

  Serial.println(F("This code scan the MIFARE Classsic NUID."));
  Serial.print(F("Using the following key:"));
  printHex(key.keyByte, MFRC522::MF_KEY_SIZE);
}

void loop() {
  if(Serial.available()){
    String str = Serial.readStringUntil('\n');
    if(str.length()>0)
      ep.write(str);
  }

  if(obj.isObject()&&obj.oflag){
     c->open();
     obj.oflag=false;
     obj.cflag=true;
     if(capClose.isStart()){
      capClose.stopCount();
     }
  }
  else if(!obj.isObject() && obj.cflag){
    obj.oflag = true;
    obj.cflag = false;
    if(!capClose.isStart()){
      capClose.startCount();
    }
  }

  /*if(capClose.isStart()){
    Serial.println("cap close counting..");
    if(capClose.isDelayComplete()){
      cap.copen(false);
      capClose.stopCount();
    }
  }
  if(capDetach.isStart()){
    Serial.println("detach counting..");
    if(capDetach.isDelayComplete()){
      cap.detach();
      capDetach.stopCount();
    }
  }*/
  if(capDuration.isStart()){
    Serial.println("cap open/close...");
    if(capDuration.isDelayComplete()){
      c->stop();
      buz.off();
      capDuration.stopCount();
      if(capDuration.getType()==1){
        bin.updateStatus();
        delay(100);
        String str = "{\"binId\":\""+ String(bin.binId) +"\",\"binStatus\":\""+ String(bin.getbins()) +"\"}";
        ep.write(str);
      }
    }
  }
  if(capCloseDuration.isStart()){
      //Serial.println("cap open/close...");
      if(capCloseDuration.isDelayComplete()){
        c->stop();
        buz.off();
        capCloseDuration.stopCount();
        if(capCloseDuration.getType()==1){
          delay(500);
          bin.updateStatus();
          delay(200);
          String str = "{\"binId\":\""+ String(bin.binId) +"\",\"binStatus\":\""+ String(bin.getbins()) +"\"}";
          ep.write(str);
        }
      }
    }

  if(capClose.isStart()){
   // Serial.println("cap close counting..");
    if(capClose.isDelayComplete()){
      c->close();
      capClose.stopCount();
    }
  }

  //Serial.println(bin.getbins());
  String r = ep.read();
  if(r!=null){
    Serial.println(r);
  }
  //Serial.println(bin.getBinStatus());

  if ( ! rfid.PICC_IsNewCardPresent())
    return;

  // Verify if the NUID has been readed
  if ( ! rfid.PICC_ReadCardSerial())
    return;

  Serial.print(F("PICC type: "));
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  Serial.println(rfid.PICC_GetTypeName(piccType));

  // Check is the PICC of Classic MIFARE type
  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI &&
    piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
    piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println(F("Your tag is not of type MIFARE Classic."));
    return;
  }

  if (rfid.uid.uidByte[0] != nuidPICC[0] ||
    rfid.uid.uidByte[1] != nuidPICC[1] ||
    rfid.uid.uidByte[2] != nuidPICC[2] ||
    rfid.uid.uidByte[3] != nuidPICC[3] ) {
    Serial.println(F("A new card has been detected."));

    // Store NUID into nuidPICC array
    for (byte i = 0; i < 4; i++) {
      nuidPICC[i] = rfid.uid.uidByte[i];
    }

    Serial.println(F("The NUID tag is:"));
    Serial.print(F("In hex: "));
    printHex(rfid.uid.uidByte, rfid.uid.size);
    Serial.println();
    Serial.print(F("In dec: "));
    printDec(rfid.uid.uidByte, rfid.uid.size);
    Serial.println();
  }
  else Serial.println(F("Card read previously."));

  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();
}

void printHex(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
}

/**
 * Helper routine to dump a byte array as dec values to Serial.
 */
void printDec(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], DEC);
  }
}
