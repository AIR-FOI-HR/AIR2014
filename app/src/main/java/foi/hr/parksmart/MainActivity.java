package foi.hr.parksmart;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import foi.hr.parksmart.BluetoothLowEnergy.BleScanner;
import foi.hr.parksmart.BluetoothLowEnergy.BleScannerListener;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements Adapter.OnBluetoothDeviceListener, BleScannerListener {

    private static final int ENABLE_BLUETOOTH_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    public Dialog dialog;
    private BluetoothAdapter mBluetoothAdapter;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<BluetoothDevice> bleDevices = new ArrayList<>();;
    SharedPreferences sharedPreferencesMod;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        sharedPreferencesMod = getSharedPreferences("idDarkMode",0);
        Boolean booleanMod = sharedPreferencesMod.getBoolean("keyDarkMode",false);
        if(booleanMod) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        ImageView myImageView = (ImageView) findViewById(R.id.imageView);
        myImageView.setImageResource(R.drawable.logo);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.bluetooth_message);
        dialog.setCanceledOnTouchOutside(false);

        showRecyclerView();

        final BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = btManager.getAdapter();
    }

    private void showRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, bleDevices,  this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mBluetoothAdapter.isEnabled()){
            promptEnableBluetooth();
        }

        if (bleDevices.isEmpty());{
            BleScanner.startBleScan(this, mBluetoothAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    private void promptEnableBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(mBluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE);
        }
    }

    //sluzi kao looper za konstantni prikaz popupa za ukljucivanje BT-a,
    //sve dok ga korisnik ne ukljuci
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_BLUETOOTH_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                promptEnableBluetooth();
            }
        }
    }

    @Override
    public boolean isLocationPermissionGranted()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            return  false;
        }
    }

    @Override
    public void requestLocationPermission(){
        if(isLocationPermissionGranted()){
            return;
        }
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.DialogTitle)
                .setMessage(R.string.DialogMessage)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void receiveScannedDevice(BluetoothDevice bluetoothDevice) {
        if(!bleDevices.contains(bluetoothDevice)){
            bleDevices.add(bluetoothDevice);
            adapter.notifyItemChanged(bleDevices.size() - 1);
        }
    }

    //sluzi kao looper za konstantni prikaz popupa za ukljucivanje lokacije,
    //sve dok ju korisnik ne ukljuci
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if (hasAllPermissionsGranted(grantResults)) {
                BleScanner.startBleScan(this, mBluetoothAdapter);
            } else {
                requestLocationPermission();
            }
        }
    }

    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBluetoothDeviceClick(int position) {
        //Log.i("BluetoothDeviceClick", bleDevices.get(position).getName());
        BleScanner.stopBleScan();
        GoToMainScreen(bleDevices.get(position));
    }

    // GoToMainScreen() poziva klasu Intent te se objekt salje u startActivity(intent) te omogućuje otvaranja novog zaslona
    protected void GoToMainScreen(BluetoothDevice bleDevice) {
            Intent intent = new Intent(MainActivity.this, MainScreen.class);
            intent.putExtra("BLE_DEVICE", bleDevice);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }