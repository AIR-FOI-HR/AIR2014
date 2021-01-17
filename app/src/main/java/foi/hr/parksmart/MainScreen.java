package foi.hr.parksmart;

import androidx.annotation.NonNull;
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
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.core.IotSensor;
import com.example.irsensor.IrSensor;
import com.example.ultrasoundsensor.UltraSoundSensor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    public static final int SOUND_1 = 1;
    public static final int SOUND_2 = 2;
    public static final int SOUND_3 = 3;

    SoundPool mSoundPool;
    HashMap<Integer, Integer> mSoundMap;

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
        if(savedInstanceState == null){
            if(bleDevice.getName().contains(usSensorPrefix))
                distanceSensor = new UltraSoundSensor();
            else if (bleDevice.getName().contains(irSensorPrefix))
                distanceSensor = new IrSensor();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.fragment_container_view, (Fragment) distanceSensor).commit();
        }
        else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            distanceSensor = (IotSensor) fragmentManager.getFragment(savedInstanceState, "fragmentDistance");
        }

        //sound setup
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
        mSoundMap = new HashMap<Integer, Integer>();

        if(mSoundPool != null){
            mSoundMap.put(SOUND_1, mSoundPool.load(this, R.raw.short_sound, 1));
            mSoundMap.put(SOUND_2, mSoundPool.load(this, R.raw.medium_sound, 1));
            mSoundMap.put(SOUND_3, mSoundPool.load(this, R.raw.long_sound, 1));
        }

        //pokretanje ble komunikacije
        bleConnectionHandler = new BleHandler(this);
        bleConnectionHandler.EstablishConnection(bleDevice, this);
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
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        startActivity(intentSettings);
    }

    @Override
    public void loadData(String sensorData)
    {
        //Log.i("SensorData", sensorData);
        distanceSensor.showGraphDistance(sensorData.split(","));
        playAudio(sensorData.split(","));
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "fragmentDistance", (Fragment) distanceSensor);
    }


    public void playSound(int sound) {
        AudioManager mgr = (AudioManager)this.getSystemService(this.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if(mSoundPool != null){
            mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, 0, 1.0f);
        }
    }

    public void stopSound(int sound) {
        AudioManager mgr = (AudioManager)this.getSystemService(this.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if(mSoundPool != null){
            mSoundPool.stop(sound);
            mSoundPool.release();
        }
    }


    private void playAudio(String[] data){
        float senzor1 = Float.parseFloat(data[0]);
        float senzor2 = Float.parseFloat(data[1]);
        float senzor3 = Float.parseFloat(data[2]);
        float senzor4 = Float.parseFloat(data[3]);

        List<Float> senzori = new ArrayList<Float>();
        senzori.add(senzor1);
        senzori.add(senzor2);
        senzori.add(senzor3);
        senzori.add(senzor4);
        Collections.sort(senzori);

        float min = senzori.get(0);

        FloatingActionButton audioButton = findViewById(R.id.btnZvuk);
        if(audioButton.getVisibility()==View.VISIBLE) {
            //senzor 1
            if(min == senzor1) {
                if(senzor1 > 2)
                    stopSound(SOUND_1);
                if (senzor1 > 1 && senzor1 <= 2) {
                    playSound(SOUND_1);
                }
                if (senzor1 <= 1 && senzor1 >= 0.5) {
                    playSound(SOUND_2);
                }
                if (senzor1 < 0.5 ) {
                    playSound(SOUND_3);
                }
            }
            //senzor 2
            else if(min == senzor2) {
                if(senzor2 > 2)
                    stopSound(SOUND_1);
                if (senzor2 > 1 && senzor1 <= 2) {
                    playSound(SOUND_1);
                }
                if (senzor2 <= 1 && senzor2 >= 0.5) {
                    playSound(SOUND_2);
                }
                if (senzor2 < 0.5 ) {
                    playSound(SOUND_3);
                }
            }
            //senzor 3
            else if(min == senzor3) {
                if(senzor3 > 2)
                    stopSound(SOUND_1);
                if (senzor3 > 1 && senzor1 <= 2) {
                    playSound(SOUND_1);
                }
                if (senzor3 <= 1 && senzor3 >= 0.5) {
                    playSound(SOUND_2);
                }
                if (senzor3 < 0.5) {
                    playSound(SOUND_3);
                }
            }
            //senzor 4
            else if(min == senzor4) {
                if(senzor4 > 2)
                    stopSound(SOUND_1);
                if (senzor4 > 1 && senzor1 <= 2) {
                    playSound(SOUND_1);
                }
                if (senzor4 <= 1 && senzor4 >= 0.5) {
                    playSound(SOUND_2);
                }
                if (senzor4 < 0.5) {
                    playSound(SOUND_3);
                }
            }
        }
    }
}