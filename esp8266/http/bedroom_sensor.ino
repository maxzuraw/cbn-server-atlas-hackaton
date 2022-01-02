// Including the ESP8266 WiFi library
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <OneWire.h>
#include <DallasTemperature.h>

const char *host = "192.168.1.10:8080";
const char *uri = "/api/temperature";

// Replace with your network details
String ssid = "<YOUR_SSID>";
String password = "<YOUR_WIFI_PASSWORD>";
unsigned long timerDelay = 5000;
unsigned long lastTime = 0;

// GPIO 5
#define ONE_WIRE_BUS 5

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature.
DallasTemperature DS18B20(&oneWire);
char temperatureCString[7];

void getTemperature() {
  float tempC;
  DS18B20.requestTemperatures();
  tempC = DS18B20.getTempCByIndex(0);
  // Details: https://www.microchip.com/webdoc/AVRLibcReferenceManual/group__avr__stdlib_1ga060c998e77fb5fc0d3168b3ce8771d42.html
  dtostrf(tempC, 2, 2, temperatureCString);
  delay(100);
}

// only runs once on boot
void setup() {
  // Initializing serial port for debugging purposes
  Serial.begin(115200);
  delay(30);

  DS18B20.begin();

  // Connecting to WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  // put your main code here, to run repeatedly:
}


void loop() {

    Serial.print("connecting to ");
    Serial.println(host);

    HTTPClient client;

    client.setTimeout(15000); // 15 Seconds
    delay(1000);

    Serial.print("HTTPS Connecting");
    int r=0; //retry counter
    while((!client.connect(host)) && (r < 30)){
        delay(100);
        Serial.print(".");
        r++;
    }
    if(r==30) {
      Serial.println("Connection failed");
    }
    else {
      Serial.println("Connected to web");
    }

    Serial.println("Getting temperature");
    getTemperature();

    String body = String("{\"sensor\":\"bedroom\", \"value\": ") + String(temperatureCString) + String(" }");
    const char *bodyChar = body.c_str();
    Serial.println(strlen(bodyChar));

    char postStr[60];
    sprintf(postStr, "POST %s HTTP/1.1", uri);
    client.println(postStr);
    client.print("Host: ");
    client.println(host);
    client.println("Content-Type: application/json");
    client.println("Authorization: Basic c2Vuc29yOnNlbnNvcg=="); // sensor:sensor (TODO: adjust if settings changed)
    client.print("Content-Length: ");
    client.println(strlen(bodyChar));
    client.println();    // extra `\r\n` to separate the http header and http body
    client.println(bodyChar);


    while (client.connected()) {
      String line = client.readStringUntil('\n');
      if (line == "\r") {
        Serial.println("headers received");
        break;
      }
    }

    Serial.println("reply was:");
    Serial.println("==========");
    String line;
    while(client.available()){
        line = client.readStringUntil('\n');  //Read Line by Line
        Serial.println(line); //Print response
    }
    Serial.println("==========");
    Serial.println("closing connection");

    delay(300000);

}