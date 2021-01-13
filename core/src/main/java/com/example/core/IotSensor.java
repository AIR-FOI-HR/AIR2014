package com.example.core;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public interface IotSensor {
    void SendData(String sensorData);
    void InitBleConnection(BluetoothDevice bleDevice, Context msContext);
}
