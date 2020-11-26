#include <Arduino.h>

#define sensorSignal 4//6
int minSignal = 100, maxSignal = 0;
byte signal[5];
String senzorA, senzorB, senzorC, senzorD;
void GetSignalDigital()
{
  
  long int time = micros();
  while (digitalRead(sensorSignal) == HIGH)
  {
  }
  //Serial.print("High: ");
  Serial.print(micros() - time);
  Serial.print(" ");

  //Identify the length of the LOW signal---------------LOW
  time = micros();
  while (digitalRead(sensorSignal) == LOW )
  {
  }
  //Serial.print("Low: ");
  Serial.println(micros() - time);
  //rawSignal[i + 1] =micros()-time;



  //Serial.println(minSignal);
  //Serial.println(maxSignal);
}
void GetSignalAnalog()
{
  long int time = micros();
  while (analogRead(sensorSignal) >900)
  {
  }
  //Serial.print("High: ");
  Serial.print(micros() - time);
  Serial.print(" ");

  //Identify the length of the LOW signal---------------LOW
  time = micros();
  while (analogRead(sensorSignal) <900)
  {
  }
  //Serial.print("Low: ");
  Serial.println(micros() - time);
}




void setup()
{
  Serial.begin(230400);
  pinMode(sensorSignal, INPUT);

}

void loop()
{
  senzorA=" ";senzorB=" ";senzorC=" ";senzorD=" ";
  if(pulseIn(sensorSignal,HIGH)>1000)
  {
    //delay(1);
    for(int i=0;i<9;i++)if(pulseIn(sensorSignal,HIGH)>150) senzorA+="1";else senzorA+=0;//signal[i]=pulseIn(sensorSignal,HIGH);
    for(int i=0;i<8;i++)if(pulseIn(sensorSignal,HIGH)>150) senzorB+="1";else senzorB+=0;//signal[i]=pulseIn(sensorSignal,HIGH);
    for(int i=0;i<8;i++)if(pulseIn(sensorSignal,HIGH)>150) senzorC+="1";else senzorC+=0;//signal[i]=pulseIn(sensorSignal,HIGH);
    for(int i=0;i<8;i++)if(pulseIn(sensorSignal,HIGH)>150) senzorD+="1";else senzorD+=0;//signal[i]=pulseIn(sensorSignal,HIGH);
  }
  //for(int i=0;i<5;i++)Serial.println(signal[i]);
  Serial.println(senzorA);
  Serial.println(senzorB);
  Serial.println(senzorC);
  Serial.println(senzorD);
  Serial.println("--------------------------------------------");
  char *charKey = new char[senzorA.length()];
  senzorA.toCharArray(charKey, senzorA.length()+1);
  long decKey = strtol(charKey, NULL, 2);
  
 
  

  //GetSignalDigital();
  //GetSignalAnalog();
  //Serial.println(analogRead(sensorSignal));
  //Serial.println(pulseInLong(sensorSignal,HIGH));
  //Serial.print(" ");
  //Serial.println(pulseInLong(sensorSignal,LOW));

}
