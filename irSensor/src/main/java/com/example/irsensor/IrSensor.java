package com.example.irsensor;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.core.IotSensor;
import com.example.core.SensorDataListener;

public class IrSensor implements IotSensor {

    public SensorDataListener sensorDataListener;

    @Override
    public void SendData(String sensorData) {
        sensorDataListener.GetSensorData(sensorData);
    }

}
