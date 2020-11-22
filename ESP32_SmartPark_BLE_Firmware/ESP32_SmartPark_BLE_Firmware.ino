#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLE2902.h>

BLECharacteristic *pCharacteristic;
bool deviceConnected = false;
int txtValue = 0;

#define SERVICE_UUID           "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID_TX "beb5483e-36e1-4688-b7f5-ea07361b26a8"

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
 Serial.println("Cekam na vezu s klijentom kako bi zapocelo slanje obavijest...");
}

void loop() {
  if(deviceConnected){
    txtValue = random(-15,40);

    //Konverzija txtValue
    char txtString[8];
    dtostrf(txtValue, 1, 2, txtString);

    //Prenosenje txtString vrijednosti u characteristic
    pCharacteristic->setValue(txtString);

    //Slanje notifikacije klijentu
    pCharacteristic->notify(); 
    Serial.println("Trenutna vrijednost: " + String(txtString));
    delay(500);
  }
}
