#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLE2902.h>

BLECharacteristic *pCharacteristic;
bool deviceConnected = false;
char buff[35];

#define SERVICE_UUID           "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID_TX "beb5483e-36e1-4688-b7f5-ea07361b26a8"
#define irSensor1Pin 8          // the number of the IR sensor 1, GPIO 16
#define irSensor2Pin 9          // the number of the IR sensor 2, GPIO 17
#define irSensor3Pin 11         // the number of the IR sensor 3, GPIO 18
#define irSensor4Pin 12         // the number of the IR sensor 4, GPIO 19

/*
 * Nasljeđivanje klase BLEServerCallbacks kako bi se proširile
 * metode onConnect() i onDisconect() pomoću kojih se u varijablu
 * deviceConnected zapisuje odgovarajuća bool vrijednost ovisno o tome
 * je li klijentski uređaj (smartphone) spojen na ESP32 ili nije.
 */
class MyServerCallbacks: public BLEServerCallbacks {
  void onConnect(BLEServer *pServer){
    deviceConnected = true;
  }

  void onDisconnect(BLEServer *pServer){
    deviceConnected = false;
  }
};

void setup() {
  Serial.begin(115200);

  //Kreiranje BLE Devicea unoson imena BT uređaja
  BLEDevice::init("SmartPark_Centar_Unit");

  //Postavljanje veličine MTU-a
  
  //Kreiranje BLE Server objekta
  BLEServer *pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());

  //Kreiranje BLE Service objekta
  BLEService *pService = pServer->createService(SERVICE_UUID);

  //Kreiranje BLE Caracteristica
  pCharacteristic = pService->createCharacteristic(
                                CHARACTERISTIC_UUID_TX,
                                BLECharacteristic::PROPERTY_NOTIFY
                              );

 //BLE2902 za notify
 pCharacteristic->addDescriptor(new BLE2902());

 //Pokreni service 
 pService->start();

 //Pokreni advertising
 pServer->getAdvertising()->start();
 
 pinMode(irSensor1Pin, INPUT);
 pinMode(irSensor2Pin, INPUT);
 pinMode(irSensor3Pin, INPUT);
 pinMode(irSensor4Pin, INPUT);
 
 Serial.println("Cekam na vezu s klijentom kako bi zapocelo slanje obavijest...");
}

void loop() {
  if(deviceConnected){
    
    //Konverzija txtValue
    char txtStringA[8];
    char txtStringB[8];
    char txtStringC[8];
    char txtStringD[8];
    
    dtostrf(digitalRead(irSensor1Pin), 1, 2, txtStringA);
    dtostrf(digitalRead(irSensor2Pin), 1, 2, txtStringB);
    dtostrf(digitalRead(irSensor3Pin), 1, 2, txtStringC);
    dtostrf(digitalRead(irSensor4Pin), 1, 2, txtStringD);
    
    strcpy(buff, txtStringA);
    strcat(buff, ",");
    strcat(buff, txtStringB);
    strcat(buff, ",");
    strcat(buff, txtStringC);
    strcat(buff, ",");
    strcat(buff, txtStringD);

    //Prenosenje txtString vrijednosti u characteristic
    pCharacteristic->setValue(buff);
    
    //Slanje notifikacije klijentu
    pCharacteristic->notify();
    Serial.println("Trenutna vrijednost: " + String(buff));

    delay(500);
  }
}
