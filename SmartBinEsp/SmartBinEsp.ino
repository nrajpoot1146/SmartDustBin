/*
    This sketch sends data via HTTP GET requests to data.sparkfun.com service.

    You need to get streamId and privateKey at data.sparkfun.com and paste them
    below. Or just customize this script to talk to other HTTP servers.

*/

#include <ESP8266WiFi.h>

const char* ssid     = "smartbin";
const char* password = "king_chandel";

const char* host = "192.168.43.97";


void setup() {
  Serial.begin(9600);
  delay(10);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

int value = 0;

void loop() {
  if(Serial.available()){
    String li = Serial.readStringUntil('#');
    Serial.print("connecting to ");
    Serial.println(host);

  WiFiClient client;
  const int httpPort = 8070;
  if (!client.connect(host, httpPort)) {
    Serial.println("connection failed");
    return;
  }

  client.println(li);
  unsigned long timeout = millis();
  while (client.available() == 0) {
    if (millis() - timeout > 2000) {
      Serial.println("no responce.");
      client.stop();
      return;
    }
  }
  if (client.available()) {
    String line = client.readStringUntil('#');
    Serial.println(line);
  }
  }

}
