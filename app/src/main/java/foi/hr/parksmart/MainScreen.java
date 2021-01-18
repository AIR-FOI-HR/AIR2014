package foi.hr.parksmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.core.IotSensor;
import com.example.irsensor.IrSensor;
import com.example.ultrasoundsensor.UltraSoundSensor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import foi.hr.parksmart.BluetoothLowEnergy.BleDataListener;
import foi.hr.parksmart.BluetoothLowEnergy.BleHandler;

public class MainScreen extends AppCompatActivity implements BleDataListener {

    private static final int REQUEST_PHONE_CALL = 1;
    private static final String usSensorPrefix = "SmartPark_Centar_Unit_US";
    private static final String irSensorPrefix = "SmartPark_Centar_Unit_IR";

    private static String sosNumber;
    private BluetoothDevice bleDevice;
    private BleHandler bleConnectionHandler;
    private IotSensor distanceSensor;

    boolean playSound;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
        sosNumber=sharedPreferences.getString("keySosNumbera","112");
        FloatingActionButton sosGumb = findViewById(R.id.btnSos);

        //popup za call dozvolu
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

        //instanciranje modula i prikaz njegovog viewa unutar fragment containera
        if(savedInstanceState == null) {

            if(bleDevice.getName().contains(usSensorPrefix))
                distanceSensor = new UltraSoundSensor();
            else if (bleDevice.getName().contains(irSensorPrefix))
                distanceSensor = new IrSensor();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.fragment_container_view, (Fragment) distanceSensor).commit();
        }
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            distanceSensor = (IotSensor) fragmentManager.getFragment(savedInstanceState, "fragmentDistance");
        }

        //pokretanje ble komunikacije
        bleConnectionHandler = new BleHandler(this);
        bleConnectionHandler.EstablishConnection(bleDevice, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        playSound = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainScreen.this);
        FloatingActionButton sosButton=findViewById(R.id.btnSos);
        Boolean toShowOrNotToShow=sharedPreferences.getBoolean("keySosOnOff",true);
        if(toShowOrNotToShow) sosButton.setVisibility(View.VISIBLE);
        else sosButton.setVisibility(View.GONE);
        sosNumber=sharedPreferences.getString("keySosNumbera","112");

        FloatingActionButton btnZvuk = findViewById(R.id.btnZvuk);
        if(btnZvuk.getVisibility() == View.VISIBLE)
        {
            playSound = true;
        }
    }

    public void hideButton(View view)
    {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvuk.setVisibility(View.GONE);
        BtnZvukOff.setVisibility(View.VISIBLE);

        playSound = false;
    }

    public void showButton(View view)
    {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvukOff.setVisibility(View.GONE);
        BtnZvuk.setVisibility(View.VISIBLE);

        playSound = true;
    }

    private void callSos(View view)
    {
        //if(sosNumber=="")sosNumber="385112";
        String sosPhoneNumber = "tel:" + sosNumber;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(sosPhoneNumber));
        startActivity(callIntent);
    }

    private void OpenSettingsActivity()
    {
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        startActivity(intentSettings);
    }

    @Override
    public void loadData(String sensorData)
    {
        Log.i("SensorData", sensorData);
        String[] sensorDataArray = sensorData.split(",");

        distanceSensor.showGraphDistance(sensorDataArray);
        if(playSound)
        {
            distanceSensor.playAudio(sensorDataArray);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragmentDistance", (Fragment) distanceSensor);
    }
}