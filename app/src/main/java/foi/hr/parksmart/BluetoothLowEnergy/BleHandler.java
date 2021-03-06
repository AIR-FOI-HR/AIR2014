package foi.hr.parksmart.BluetoothLowEnergy;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.UUID;

import foi.hr.parksmart.MainScreen;
import foi.hr.parksmart.R;

public class BleHandler {

    //BLE MTU size
    //payload size = MTU - 3
    private static final int GATT_MAX_MTU_SIZE = 46;

    // UUIDs for the Distance service
    private static final UUID ESP32_SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    private static final UUID ESP32_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");
    private UUID ESP32_CHAR_DESRIPTOR_UUID = convertFromInteger(0x2902);

    public BleDataListener bleDataListener;
    public BluetoothGatt gatt;

    //Bluetooth connection
    public void EstablishConnection(BluetoothDevice bleDevice, Context msContext, BleDataListener bleDataListener)
    {
        this.bleDataListener = bleDataListener;

        if(bleDevice != null && gatt == null){
            BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            //Log.w("BluetoothGattCallback", "Successfully connected to $deviceAddress");
                            Boolean stat = gatt.requestMtu(GATT_MAX_MTU_SIZE);
                            //Log.i("MTUstat", stat.toString());
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            //Log.w("BluetoothGattCallback", "Successfully disconnected from $deviceAddress");
                            gatt.close();
                            bleDataListener.startBleScanActivity();
                        }
                    } else {
                        //Log.w("BluetoothGattCallback", "Error $status encountered for $deviceAddress! Disconnecting...");
                        gatt.close();
                        bleDataListener.startBleScanActivity();
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    super.onServicesDiscovered(gatt, status);
                    if(gatt != null){
                        //Log.w("BluetoothGattCallback", "Discovered ${services.size} services for ${device.address}");
                        setNotifications(gatt);
                    }
                }
                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                    bleDataListener.loadData(hexToString(byteArrayToString(characteristic.getValue())));
                }
                @Override
                public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                    super.onMtuChanged(gatt, mtu, status);
                    String logStatus = mtu + " status = ";
                    if(status == BluetoothGatt.GATT_SUCCESS){
                        //Log.i("MTU changed: ", logStatus+"uspjesno");
                        gatt.discoverServices();
                    }
                    else{
                        //Log.i("MTU changed: ", logStatus+"neuspjeno");
                    }
                }
            };
            gatt = bleDevice.connectGatt(msContext, false, gattCallback, BluetoothDevice.TRANSPORT_LE);
        }
    }

    public void Disconnect(){
        if(gatt != null)
            gatt.disconnect();
    }

    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

    String byteArrayToString(byte[] in) {
        char out[] = new char[in.length * 2];
        for (int i = 0; i < in.length; i++) {
            out[i * 2] = "0123456789ABCDEF".charAt((in[i] >> 4) & 15);
            out[i * 2 + 1] = "0123456789ABCDEF".charAt(in[i] & 15);
        }
        return new String(out);
    }

    String hexToString(String hex){
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return new String(output);
    }

    private void setNotifications(BluetoothGatt gatt) {
        BluetoothGattService distanceService = gatt.getService(ESP32_SERVICE_UUID);
        if (distanceService != null) {
            BluetoothGattCharacteristic distanceCharacteristic = distanceService.getCharacteristic(ESP32_CHARACTERISTIC_UUID);
            if (distanceCharacteristic != null & isNotifiable(distanceCharacteristic)) {
                gatt.setCharacteristicNotification(distanceCharacteristic, true);

                BluetoothGattDescriptor descriptor = distanceCharacteristic.getDescriptor(ESP32_CHAR_DESRIPTOR_UUID);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                gatt.writeDescriptor(descriptor);
            }
        }
    }

    private boolean isNotifiable(BluetoothGattCharacteristic characteristic) {
        return ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0);
    }
}
