#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>
#include <BLE2902.h>


//BLE
BLECharacteristic *pCharacteristic;
bool deviceConnected = false;
int txtValue = 0;

#define SERVICE_UUID           "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID_TX "beb5483e-36e1-4688-b7f5-ea07361b26a8"

#define sensorSignal 15
String senzorA, senzorB, senzorC, senzorD;
float senzorAdec, senzorBdec, senzorCdec, senzorDdec;
char buf[150];


class MyServerCallbacks: public BLEServerCallbacks {
    void onConnect(BLEServer *pServer) {
      deviceConnected = true;
    }

    void onDisconnect(BLEServer *pServer) {
      deviceConnected = false;
    }
};


void setup() {
  Serial.begin(230400);
  pinMode(sensorSignal, INPUT);

  //Kreiranje BLE Devicea unoson imena BT uređaja
  BLEDevice::init("SmartPark_Centar_Unit");\
  BLEDevice::setMTU(100);

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

void loop()
{
  senzorA = " "; senzorB = " "; senzorC = " "; senzorD = " ";
  if (pulseIn(sensorSignal, HIGH) > 1000)
  {
    delay(1);
      for (int i = 0; i < 8; i++)if (pulseIn(sensorSignal, HIGH) > 150) senzorA += "1"; else senzorA += 0; //signal[i]=pulseIn(sensorSignal,HIGH);
      for (int i = 0; i < 8; i++)if (pulseIn(sensorSignal, HIGH) > 150) senzorB += "1"; else senzorB += 0; //signal[i]=pulseIn(sensorSignal,HIGH);
      for (int i = 0; i < 8; i++)if (pulseIn(sensorSignal, HIGH) > 150) senzorC += "1"; else senzorC += 0; //signal[i]=pulseIn(sensorSignal,HIGH);
      for (int i = 0; i < 8; i++)if (pulseIn(sensorSignal, HIGH) > 150) senzorD += "1"; else senzorD += 0; //signal[i]=pulseIn(sensorSignal,HIGH);
    //for(int i=0;i<5;i++)Serial.println(signal[i]);
    //Serial.println(senzorA);
    //Serial.println(senzorB);
    //Serial.println(senzorC);
    //Serial.println(senzorD);

    char *charKeyA = new char[senzorA.length()];
    senzorA.toCharArray(charKeyA, senzorA.length() + 1);
    senzorAdec = strtol(charKeyA, NULL, 2);

    char *charKeyB = new char[senzorB.length()];
    senzorB.toCharArray(charKeyB, senzorB.length() + 1);
    senzorBdec = strtol(charKeyB, NULL, 2);

    char *charKeyC = new char[senzorC.length()];
    senzorC.toCharArray(charKeyC, senzorC.length() + 1);
    senzorCdec = strtol(charKeyC, NULL, 2);

    char *charKeyD = new char[senzorD.length()];
    senzorD.toCharArray(charKeyD, senzorD.length() + 1);
    senzorDdec = strtol(charKeyD, NULL, 2);



    Serial.println(senzorAdec / 10);
    Serial.println(senzorBdec / 10);
    Serial.println(senzorCdec / 10);
    Serial.println(senzorDdec / 10);
    Serial.println("--------------------------------------------");
  }

  if (deviceConnected) {
    //Konverzija txtValue
        char txtStringA[8];
        char txtStringB[8];
        char txtStringC[8];
        char txtStringD[8];
        dtostrf(senzorAdec/10, 1, 2, txtStringA);
        dtostrf(senzorBdec/10, 1, 2, txtStringB);
        dtostrf(senzorCdec/10, 1, 2, txtStringC);
        dtostrf(senzorDdec/10, 1, 2, txtStringD);
        strcpy(buf,txtStringA);
        strcat(buf,",");
        strcat(buf,txtStringB);
        strcat(buf,",");
        strcat(buf,txtStringC);
        strcat(buf,",");
        strcat(buf,txtStringD);


    
    //Prenosenje txtString vrijednosti u characteristic
//    txtValue = random(-15, 40);
//
//    //Konverzija txtValue
//    char txtString[8];
//    dtostrf(txtValue, 1, 2, txtString);
//
//    //Prenosenje txtString vrijednosti u characteristic
    pCharacteristic->setValue(buf);
//
//    //Slanje notifikacije klijentu
    pCharacteristic->notify();
   Serial.println("Trenutna vrijednost: " + String(buf));
  }
  delay(50);


}
