package foi.hr.parksmart.BluetoothLowEnergy;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class BleScanner {

    private static BluetoothLeScanner bleScanner;
    private static BleScannerListener bleScannerListener;

    public static void startBleScan(BleScannerListener bleScanListener, BluetoothAdapter mBluetoothAdapter)
    {
        bleScannerListener = bleScanListener;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && !bleScannerListener.isLocationPermissionGranted()){
            bleScannerListener.requestLocationPermission();
        }
        else {
            bleScanner = mBluetoothAdapter.getBluetoothLeScanner();

            List<ScanFilter> scanFilters = new ArrayList<>();
            ScanFilter scanFilter = new ScanFilter.Builder()
                    .setDeviceName("SmartPark_Centar_Unit")
                    .build();
            scanFilters.add(scanFilter);

            ScanSettings scanSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();

            if (bleScanner != null) {
                bleScanner.startScan(null, scanSettings, scanCallback);
                //Log.d("ScanInfo", "scan started");
            }  else {
                //Log.e("ScanInfo", "could not get scanner object");
            }
        }
    }

    public static void stopBleScan()
    {
        if(bleScanner != null){
            bleScanner.stopScan(scanCallback);
        }
    }

    private static final ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            if(result.getDevice().getName().contains("SmartPark_Centar_Unit")){
                //Log.i("ScanCallback", result.getDevice().getName());
                bleScannerListener.receiveScannedDevice(result.getDevice());
            }
        }
    };
}
