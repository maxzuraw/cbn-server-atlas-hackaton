#include <OneWire.h>
#include <DallasTemperature.h>
#include <ESP8266HTTPClient.h>
#include <ESP8266WiFi.h>

const char *host = "<YOUR_HOST_DOMAIN>";
const char *uri = "/api/temperature";

String ssid = "<YOUR_SSID>";
String password = "<YOUR_WIFI_PASSWORD>";
unsigned long timerDelay = 5000;
unsigned long lastTime = 0;
const char fingerprint[] PROGMEM = "<YOUR_FINGERPRINT>";

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

void setup() {
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

}

void loop() {
  WiFiClientSecure httpsClient;

   Serial.printf("Using fingerprint '%s'\n", fingerprint);
   httpsClient.setFingerprint(fingerprint);

  httpsClient.setTimeout(15000); // 15 Seconds
  delay(1000);

  Serial.print("HTTPS Connecting");
  int r=0; //retry counter
  while((!httpsClient.connect(host, 443)) && (r < 30)){
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

  // reading temperature
  getTemperature();
  Serial.println(String(temperatureCString));

  String body = String("{\"sensor\":\"living-room\", \"value\": ") + String(temperatureCString) + String(" }");
  const char *bodyChar = body.c_str();
  Serial.println(strlen(bodyChar));

  char postStr[60];
  sprintf(postStr, "POST %s HTTP/1.1", uri);
  httpsClient.println(postStr);
  httpsClient.print("Host: "); httpsClient.println(host);
  httpsClient.println("Authorization: <PLACE_YOUR_BASIC_AUTH_ENCODED>");
  httpsClient.println("Content-Type: application/json");
  httpsClient.print("Content-Length: "); httpsClient.println(strlen(bodyChar));
  httpsClient.println();    // extra `\r\n` to separate the http header and http body
  httpsClient.println(bodyChar);

  while (httpsClient.connected()) {
      String line = httpsClient.readStringUntil('\n');
      if (line == "\r") {
        Serial.println("headers received");
        break;
      }
    }

  Serial.println("reply was:");
  Serial.println("==========");
  String line;
  while(httpsClient.available()){
    line = httpsClient.readStringUntil('\n');  //Read Line by Line
    Serial.println(line); //Print response
  }
  Serial.println("==========");
  Serial.println("closing connection");

  delay(300000);
}
