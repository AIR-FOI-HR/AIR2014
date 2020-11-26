#include <Arduino.h>

#define sensorSignal 4//6
int minSignal = 100, maxSignal = 0;

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
  //  if (digitalRead(sensorSignal) == HIGH)Serial.print("1");
  //  else {
  //    Serial.print(" ");
  //    Serial.println("0");
  // }
  //GetSignalDigital();
  //GetSignalAnalog();
  //Serial.println(analogRead(sensorSignal));
  Serial.println(pulseInLong(sensorSignal,HIGH));
  //Serial.print(" ");
  //Serial.println(pulseInLong(sensorSignal,LOW));

}
