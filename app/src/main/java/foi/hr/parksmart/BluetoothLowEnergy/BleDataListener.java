package foi.hr.parksmart.BluetoothLowEnergy;

import android.bluetooth.BluetoothGatt;

public interface BleDataListener {
        void loadData(String sensorData);
        void startBleScanActivity();
}
