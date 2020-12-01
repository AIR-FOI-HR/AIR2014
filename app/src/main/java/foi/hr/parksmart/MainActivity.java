package foi.hr.parksmart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements Adapter.OnBluetoothDeviceListener {

    private static final int ENABLE_BLUETOOTH_REQUEST_CODE = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;


    public Dialog dialog;
    TextView printWarning;
    private BluetoothAdapter mBluetoothAdapter;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<String> deviceName = new ArrayList<>();;
    ArrayList<String> deviceAddress = new ArrayList<>();;
    List<String> filterName;
    List<String> filterAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceName.add("Nešto nešto");
        deviceName.add(" nešto");
        deviceName.add("Neštoešto");
        deviceAddress.add("1232142412412");
        deviceAddress.add("32141244214");
        deviceAddress.add("512141241241");





        TextView greetings = (TextView) findViewById(R.id.textView);
        greetings.setText("Dobro došli!");
        ImageView myImageView = (ImageView) findViewById(R.id.imageView);
        myImageView.setImageResource(R.drawable.logo);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.bluetooth_message);


        final BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = btManager.getAdapter();

        /*
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    printWarning = (TextView) dialog.findViewById(R.id.txtWarning);
                    printWarning.setText("Bluetooth Vam nije uključen, kako bi nastavili dalje koristiti aplikaciju uključite bluetooth pritiskom na tipku 'UKLJUČI'");
                    dialog.show();
                }
                else{
                    //GoToMainScreen();
                }
            }
        }, 1500);

        Button btnOn = (Button) dialog.findViewById(R.id.btnTurnOn);
        btnOn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final int REQUEST_ENABLE_BT = 1;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            }
        });
        */
    }


    private void showRecyclerView() {
        filterName = deviceName.stream().distinct().collect(Collectors.toList());
        // treba pogledati vraća li dobre MAC adrese od pojedinog uređaja ili treba napraviti metodu
        filterAddress = deviceAddress.stream().distinct().collect(Collectors.toList());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,deviceName,deviceAddress,this);
        recyclerView.setAdapter(adapter);
    }

    //sve iznad je staro

    @Override
    protected void onResume() {
        super.onResume();
        if(!mBluetoothAdapter.isEnabled()){
            promptEnableBluetooth();
        }

        startBleScan();
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


    private boolean isLocationPermissionGranted()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            return  false;
        }
    }

    private void startBleScan()
    {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && !isLocationPermissionGranted()){
            requestLocationPermission();
        }
        else {
            BluetoothLeScanner bleScanner = mBluetoothAdapter.getBluetoothLeScanner();

            List<ScanFilter> scanFilters = new ArrayList<>();
            ScanFilter scanFilter = new ScanFilter.Builder()
                    .setDeviceName("Makeblock_LE")
                    .build();
            scanFilters.add(scanFilter);

            ScanSettings scanSettings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();

            if (bleScanner != null) {
                bleScanner.startScan(null, scanSettings, scanCallback);
                Log.d("ScanInfo", "scan started");
            }  else {
                Log.e("ScanInfo", "could not get scanner object");
            }
        }
    }

    private final ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            Log.i("ScanCallback", result.getDevice().getName());
            deviceName.add(result.getDevice().getName());
            deviceAddress.add(result.getDevice().getAddress());



        }
    };

    private void requestLocationPermission(){
        if(isLocationPermissionGranted()){
            return;
        }
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Lokacija je obavezna za BLE skeniranje.")
                .setMessage("Molimo dozvolite pristup lokaciji.")
                .setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                })
                .create()
                .show();
    }

    //sluzi kao looper za konstantni prikaz popupa za ukljucivanje lokacije,
    //sve dok ju korisnik ne ukljuci
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if (hasAllPermissionsGranted(grantResults)) {
                startBleScan();
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
        Log.i("BluetoothDeviceClick", filterName.get(position));
        // stavi povezivanje s uređajem

    }


    //sve ispod je starom

    /*
        Metoda onActivityResult(int requestCode, int resultCode, Intent data) poziva se nakon završavanja aktivonsti zahtjeva
        requestCode - označava stanje početnog zahtjeva
        resultCode - što vraća zahtjev
        data - dodatana podatak ukoliko postoji
        provjerava vraćeni zahtjev i izvršava jedan od IF uvjeta

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            //GoToMainScreen();
        }else if(resultCode==RESULT_CANCELED){
            printWarning = (TextView) dialog.findViewById(R.id.txtWarning);
            printWarning.setText("Ukoliko ne dopustite uključivanje ili sami ne uključite Bluetooth, aplikacija ne može nastaviti s daljnjim radom.");
            dialog.show();
        }
    }

    /*
    GoToMainScreen() poziva klasu Intent te se objekt salje u startActivity(intent) te omogućuje otvaranja novog zaslona
     *//*
    protected void GoToMainScreen(){
        Intent intent = new Intent(MainActivity.this, MainScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }*/
}