package foi.hr.parksmart.BluetoothLowEnergy;

import android.bluetooth.BluetoothDevice;

public interface BleScannerListener {
    public boolean isLocationPermissionGranted();
    public void requestLocationPermission();
    public void receiveScannedDevice(BluetoothDevice bluetoothDevice);
}
