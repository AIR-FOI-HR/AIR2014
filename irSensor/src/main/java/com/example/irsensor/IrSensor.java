package com.example.irsensor;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.core.BluetoothLE.BleDataListener;
import com.example.core.BluetoothLE.BleHandler;
import com.example.core.IotSensor;
import com.example.core.SensorDataListener;

public class IrSensor implements IotSensor {

    BleHandler bleConnector;
    public SensorDataListener sensorDataListener;

    @Override
    public void SendData(String sensorData) {
        sensorDataListener.GetSensorData(sensorData);
    }

    @Override
    public void InitBleConnection(BluetoothDevice bleDevice, Context msContext) {

        bleConnector = new BleHandler();

        bleConnector.bleDataListener = new BleDataListener() {
            @Override
            public void loadData(String sensorData) {
                SendData(sensorData);
            }
        };

        bleConnector.EstablishConnection(bleDevice, msContext);
    }
}
