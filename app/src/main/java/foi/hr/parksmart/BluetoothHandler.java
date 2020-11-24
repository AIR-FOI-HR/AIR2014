package foi.hr.parksmart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.welie.blessed.BluetoothBytesParser;
import com.welie.blessed.BluetoothCentral;
import com.welie.blessed.BluetoothCentralCallback;
import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.BluetoothPeripheralCallback;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static android.bluetooth.BluetoothGatt.CONNECTION_PRIORITY_BALANCED;
import static com.welie.blessed.BluetoothPeripheral.GATT_SUCCESS;

public class BluetoothHandler {
    private static final UUID ESP32_SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    private static final UUID ESP32_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");

    // Local variables
    public BluetoothCentral central;
    private static BluetoothHandler instance = null;
    private Context context;
    private Handler handler = new Handler();
    private int currentTimeCounter = 0;

    public final BluetoothPeripheralCallback peripheralCallback = new BluetoothPeripheralCallback() {
        @Override
        public void onServicesDiscovered(BluetoothPeripheral peripheral) {
            peripheral.requestMtu(185);
            peripheral.requestConnectionPriority(CONNECTION_PRIORITY_BALANCED); //postavi kasnije na CONNECTION_PRIORITY_LOW_POWER 100-125ms je period, balnced je 30-50ms

            //set notify to ESP32_CHARACTERISTIC_UUID value
            if(peripheral.getService(ESP32_SERVICE_UUID) != null) {
                BluetoothGattCharacteristic distanceCharacteristic = peripheral.getCharacteristic(ESP32_SERVICE_UUID, ESP32_CHARACTERISTIC_UUID);
                if (distanceCharacteristic != null) {
                    peripheral.setNotify(distanceCharacteristic, true);
                }
            }
        }

        @Override
        public void onNotificationStateUpdate(@NotNull BluetoothPeripheral peripheral, @NotNull BluetoothGattCharacteristic characteristic, int status) {
            if( status == GATT_SUCCESS) {
                if(peripheral.isNotifying(characteristic)) {
                    Log.i("SUCCESS: set to 'on': ", characteristic.getUuid().toString());
                } else {
                    Log.i("SUCCESS: set to 'off': ", characteristic.getUuid().toString());
                }
            } else {
                Log.e("Changing state failed: ", characteristic.getUuid().toString());
            }
        }

        @Override
        public void onCharacteristicUpdate(@NotNull BluetoothPeripheral peripheral, @NotNull byte[] value, @NotNull BluetoothGattCharacteristic characteristic, int status) {
            if (status != GATT_SUCCESS) return;
            UUID characteristicUUID = characteristic.getUuid();
            BluetoothBytesParser parser = new BluetoothBytesParser(value);

            if(characteristicUUID.equals(ESP32_CHARACTERISTIC_UUID)) {
                String distanceValue = parser.getStringValue(0);
                Log.i("Received manufacturer:", distanceValue);
            }
        }

        @Override
        public void onMtuChanged(@NotNull BluetoothPeripheral peripheral, int mtu, int status) {
            Log.i("new MTU set: ", Integer.toString(mtu));
        }
    };

    //callback for central object
    public final BluetoothCentralCallback bluetoothCentralCallback = new BluetoothCentralCallback() {
        @Override
        public void onConnectedPeripheral(BluetoothPeripheral peripheral) {
            Log.i("Nasao peripheral '%s'", peripheral.getName());
            central.stopScan();
            central.connectPeripheral(peripheral, peripheralCallback);
        }

        //ovo se preklapa s Kolarovima, tako da pazi. Posebno metoda startPairingPopupHack()
        @Override
        public void onBluetoothAdapterStateChanged(int state) {
            Log.i("bluetooth chg state: ", Integer.toString(state));
            if(state == BluetoothAdapter.STATE_ON) {
                // Bluetooth is on now, start scanning again
                // Scan for peripherals with a certain service UUIDs
                central.startPairingPopupHack();
                central.scanForPeripheralsWithServices(new UUID[]{ESP32_SERVICE_UUID});
            }
        }
    };

    public static synchronized BluetoothHandler getInstance(Context context) {
        if (instance == null) {
            instance = new BluetoothHandler(context.getApplicationContext());
        }
        return instance;
    }

    private BluetoothHandler(Context context) {
        this.context = context;

        // Create BluetoothCentral
        central = new BluetoothCentral(context, bluetoothCentralCallback, new Handler());

        // Scan for peripherals with a certain service UUIDs
        central.startPairingPopupHack();
        central.scanForPeripheralsWithServices(new UUID[]{ESP32_SERVICE_UUID});
    }
}
