package foi.hr.parksmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.core.IotSensor;
import com.example.ultrasoundsensor.UltraSoundSensor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import foi.hr.parksmart.BluetoothLowEnergy.BleDataListener;
import foi.hr.parksmart.BluetoothLowEnergy.BleHandler;

public class MainScreen extends AppCompatActivity implements BleDataListener {

    private static final int REQUEST_PHONE_CALL = 1;

    private static String sosNumber;
    private BluetoothDevice bleDevice;
    private BleHandler bleConnectionHandler;
    private Dialog dialogTurnedOFF, missingDevice;
    private IotSensor distanceSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        /*
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).
                    add(R.id.fragment_container_view, UltraSoundSensor.class, null).commit();
        }*/

        /*
        //Stapicev nacin
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.replace(R.id.fragment_container_view, new UltraSoundSensor());
        fts.commit();*/

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

        distanceSensor = new UltraSoundSensor();
        //Fragment frag = new UltraSoundSensor(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.fragment_container_view, frag).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container_view, (Fragment) distanceSensor).commit();
        //modulFragmentView = frag.getView();

        bleConnectionHandler = new BleHandler(this);
        bleConnectionHandler.EstablishConnection(bleDevice, this);
    }
    //Preference.OnPreferenceChangeListener()

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
    }

    public void hideButton(View view)
    {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvuk.setVisibility(View.GONE);
        BtnZvukOff.setVisibility(View.VISIBLE);
    }

    public void showButton(View view)
    {
        FloatingActionButton BtnZvuk = findViewById(R.id.btnZvuk);
        FloatingActionButton BtnZvukOff = findViewById(R.id.btnZvukOff);
        BtnZvukOff.setVisibility(View.GONE);
        BtnZvuk.setVisibility(View.VISIBLE);
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
        Intent intentSettings=new Intent(this, SettingsActivity.class);
        startActivity(intentSettings);
    }

    @Override
    public void loadData(String sensorData)
    {
        String[] arrayOfDataFromMcu = sensorData.split(",");
        distanceSensor.showGraphDistance(arrayOfDataFromMcu);
    }

    /*
    @Override
    public void showBluetoothConnectionButton()
    {
        Log.i("ShowBluetoothConnection","Radi" );

        dialogTurnedOFF = new Dialog(this);
        dialogTurnedOFF.setContentView(R.layout.bluetooth_message_turned_off);
        dialogTurnedOFF.setCanceledOnTouchOutside(false);

        Button btnOn = (Button) dialogTurnedOFF.findViewById(R.id.btnTurnOn);
        Context context = this.getApplicationContext();

        btnOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final int REQUEST_ENABLE_BT = 1;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                bleConnectionHandler.EstablishConnection(bleDevice, context);
            }
        });
    }*/
}