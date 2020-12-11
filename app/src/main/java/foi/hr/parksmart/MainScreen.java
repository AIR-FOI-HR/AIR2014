package foi.hr.parksmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.UUID;

public class MainScreen extends AppCompatActivity  {

    private static final int REQUEST_PHONE_CALL = 1;

    // UUIDs for the Distance service (BLP)
    private static final UUID ESP32_SERVICE_UUID = UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b");
    private static final UUID ESP32_CHARACTERISTIC_UUID = UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8");
    private UUID ESP32_CHAR_DESRIPTOR_UUID = convertFromInteger(0x2902);

    private static String sosNumber;
    private BluetoothDevice bleDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
       // if(sosNumber=="")sosNumber="+385112";
        sosNumber=sharedPreferences.getString("keySosNumbera","112");
        FloatingActionButton sosGumb = findViewById(R.id.btnSos);
        sosGumb.setOnClickListener((View v) -> {
            if (ContextCompat.checkSelfPermission(MainScreen.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainScreen.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else {
                callSos(v);
            }
        });
        //Idi na postavke gumb
        FloatingActionButton btnSettings=findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener((View view)->{
            OpenSettingsActivity();
        });

        bleDevice = getIntent().getParcelableExtra("BLE_DEVICE");
        EstablishConnection();
    }
    //Preference.OnPreferenceChangeListener()

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
        FloatingActionButton sosButton=findViewById(R.id.btnSos);
        Boolean toShowOrNotToShow=sharedPreferences.getBoolean("keySosOnOff",true);
        if(toShowOrNotToShow) sosButton.setVisibility(View.VISIBLE);
        else sosButton.setVisibility(View.GONE);
        sosNumber=sharedPreferences.getString("keySosNumbera","112");
    }


    public void hideButton(View view) {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvuk.setVisibility(View.GONE);
        BtnZvukOff.setVisibility(View.VISIBLE);
    }

    public void showButton(View view) {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvukOff.setVisibility(View.GONE);
        BtnZvuk.setVisibility(View.VISIBLE);
    }

    private void callSos(View view) {
        //if(sosNumber=="")sosNumber="385112";
        String sosPhoneNumber = "tel:" + sosNumber;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(sosPhoneNumber));
        startActivity(callIntent);
    }
    private void OpenSettingsActivity()
    {
        Intent intentSettings=new Intent(this, SettingsActivity.class);
        startActivity(intentSettings);
    }

    public UUID convertFromInteger(int i) {
        final long MSB = 0x0000000000001000L;
        final long LSB = 0x800000805f9b34fbL;
        long value = i & 0xFFFFFFFF;
        return new UUID(MSB | (value << 32), LSB);
    }

    //Bluetooth connection
    private void EstablishConnection() {
        if(bleDevice != null){
            BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            Log.w("BluetoothGattCallback", "Successfully connected to $deviceAddress");
                            gatt.discoverServices();
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            Log.w("BluetoothGattCallback", "Successfully disconnected from $deviceAddress");
                            gatt.close();
                        }
                    } else {
                        Log.w("BluetoothGattCallback", "Error $status encountered for $deviceAddress! Disconnecting...");
                        gatt.close();
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    super.onServicesDiscovered(gatt, status);
                    if(gatt != null){
                        Log.w("BluetoothGattCallback", "Discovered ${services.size} services for ${device.address}");
                        //printGattTable() // See implementation just above this section
                        setNotifications(gatt);
                    }
                }
            };
            BluetoothGatt gatt = bleDevice.connectGatt(this, false, gattCallback, BluetoothDevice.TRANSPORT_LE);
        }
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


