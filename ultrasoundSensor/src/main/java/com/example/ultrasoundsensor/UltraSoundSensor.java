package com.example.ultrasoundsensor;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.core.FragmentListener;
import com.example.core.IotSensor;
import com.example.core.SensorDataListener;

public class UltraSoundSensor extends Fragment implements IotSensor {

    public SensorDataListener sensorDataListener;
    private ImageView senzor1lvl1,senzor1lvl2, senzor1lvl3, senzor2lvl1, senzor2lvl2, senzor2lvl3, senzor3lvl1, senzor3lvl2
            ,senzor3lvl3, senzor4lvl1, senzor4lvl2, senzor4lvl3,level1,level2;
    TextView firstDistance , secondDistance, thirdDistance, txtSenzor1,txtSenzor2,txtSenzor3,txtSenzor4;
    volatile String[] arrayOfDataFromMcu={"1.00","1.00","1.00","1.00"};
    //View usView;
    FragmentListener fragmentListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graphview,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //usView = view;

        fragmentListener.getFragmentView(view);

        //int red = Color.parseColor("#f70000");
        //int yellow = Color.parseColor("#f5f242");

        //Log.i("onViewCreated", "Ovdje sam");
        //senzor1lvl1 = usView.findViewById(R.id.idSenzor1Lvl1);
        //senzor1lvl1.setColorFilter(red);


        //DrawableCompat.setTint(senzor1lvl1.getDrawable(), ContextCompat.getColor(, R.color.black));

        //firstDistance = view.findViewById(R.id.textView2);
        //firstDistance.setText(arrayOfDataFromMcu[0]);

        /*
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(1000);
                        Log.i("DretvaPodaci", arrayOfDataFromMcu[0]);
                        showGraphDistance(arrayOfDataFromMcu);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
         */
    }

    public UltraSoundSensor(FragmentListener fragListener){
        this.fragmentListener = fragListener;
    }

    public UltraSoundSensor(){

    }

    @Override
    public void SendData(String sensorData) {
        sensorDataListener.GetSensorData(sensorData);
    }

    /*
    @Override
    public void InitBleConnection(BluetoothDevice bleDevice, Context msContext) {

        bleConnector = new BleHandler();

        bleConnector.bleDataListener = new BleDataListener() {
            @Override
            public void loadData(String sensorData) {
                SendData(sensorData);
                //arrayOfDataFromMcu = sensorData.split(",");
                //showGraphDistance(arrayOfDataFromMcu);
            }
        };

        bleConnector.EstablishConnection(bleDevice, msContext);
    }

     */

    @Override
    public void onStart() {
        super.onStart();
        //showGraphDistance(arrayOfDataFromMcu);
    }

    public void showGraphDistance(String[] data, View usView){

        float senzor1 = Float.parseFloat(data[0]);
        float senzor2 = Float.parseFloat(data[1]);
        float senzor3 = Float.parseFloat(data[2]);
        float senzor4 = Float.parseFloat(data[3]);

        int orange = Color.parseColor("#ff6600");
        int yellow = Color.parseColor("#f5f242");
        int red = Color.parseColor("#f70000");
        int transparent= Color.parseColor("#00000000");

        senzor1lvl1 =  usView.findViewById(R.id.idSenzor1Lvl1);
        senzor1lvl2 =  usView.findViewById(R.id.idSenzor1Lvl2);
        senzor1lvl3 =  usView.findViewById(R.id.idSenzor1Lvl3);
        senzor2lvl1 =  usView.findViewById(R.id.idSenzor2Lvl1);
        senzor2lvl2 =  usView.findViewById(R.id.idSenzor2Lvl2);
        senzor2lvl3 =  usView.findViewById(R.id.idSenzor2Lvl3);
        senzor3lvl1 =  usView.findViewById(R.id.idSenzor3Lvl1);
        senzor3lvl2 =  usView.findViewById(R.id.idSenzor3Lvl2);
        senzor3lvl3 =  usView.findViewById(R.id.idSenzor3Lvl3);
        senzor4lvl1 =  usView.findViewById(R.id.idSenzor4Lvl1);
        senzor4lvl2 =  usView.findViewById(R.id.idSenzor4Lvl2);
        senzor4lvl3 =  usView.findViewById(R.id.idSenzor4Lvl3);
        level1 =  usView.findViewById(R.id.idLevel);
        level2 = usView.findViewById(R.id.idLevel2);
        firstDistance =  usView.findViewById(R.id.textView2);
        secondDistance = usView.findViewById(R.id.textView3);
        thirdDistance = usView.findViewById(R.id.textView4);
        txtSenzor1 =  usView.findViewById(R.id.txtSenzor1);
        txtSenzor2 = usView.findViewById(R.id.txtSenzor2);
        txtSenzor3 = usView.findViewById(R.id.txtSenzor3);
        txtSenzor4 = usView.findViewById(R.id.txtSenzor4);

        if(senzor1 > 1  && senzor1 < 2){
            senzor1lvl1.setColorFilter(yellow);
        }
        else if (senzor1 <=1 && senzor1>=0.5){
            senzor1lvl1.setColorFilter(yellow); //yellow
            senzor1lvl2.setColorFilter(orange); //orange
        }
        else if(senzor1 < 0.5 && senzor1 > 0.1 ){
            senzor1lvl1.setColorFilter(yellow);
            senzor1lvl2.setColorFilter(orange);
            senzor1lvl3.setColorFilter(red);
        }
        else{
            senzor1lvl1.setColorFilter(transparent);
            senzor1lvl2.setColorFilter(transparent);
            senzor1lvl3.setColorFilter(transparent);
        }

        //senzor2
        if(senzor2 > 1  && senzor2 < 2){
            senzor2lvl1.setColorFilter(yellow);
        }
        else if(senzor2 <=1 && senzor2>=0.5){
            senzor2lvl1.setColorFilter(yellow);
            senzor2lvl2.setColorFilter(orange);
        }
        else if(senzor2 < 0.5 && senzor2 >0.1){
            senzor2lvl1.setColorFilter(yellow);
            senzor2lvl2.setColorFilter(orange);
            senzor2lvl3.setColorFilter(red);
        }
        else{
            senzor2lvl1.setColorFilter(transparent);
            senzor2lvl2.setColorFilter(transparent);
            senzor2lvl3.setColorFilter(transparent);
        }

        //senzor3
        if(senzor3 > 1  && senzor3 < 2){
            senzor3lvl1.setColorFilter(yellow);
        }
        else if(senzor3 <=1 && senzor3>=0.5){
            senzor3lvl1.setColorFilter(yellow);
            senzor3lvl2.setColorFilter(orange);
        }
        else if(senzor3 < 0.5 && senzor3 >0.1){
            senzor3lvl1.setColorFilter(yellow);
            senzor3lvl2.setColorFilter(orange);
            senzor3lvl3.setColorFilter(red);
        }
        else{
            senzor3lvl1.setColorFilter(transparent);
            senzor3lvl2.setColorFilter(transparent);
            senzor3lvl3.setColorFilter(transparent);
        }

        //senzor4
        if(senzor4 > 1 && senzor4 < 2){
            senzor4lvl1.setColorFilter(yellow);
        }
        else if(senzor4 <=1 && senzor4>=0.5){
            senzor4lvl1.setColorFilter(yellow);
            senzor4lvl2.setColorFilter(orange);
        }
        else if(senzor4 < 0.5 && senzor4 >0.1){
            senzor4lvl1.setColorFilter(yellow);
            senzor4lvl2.setColorFilter(orange);
            senzor4lvl3.setColorFilter(red);
        }
        else{
            senzor4lvl1.setColorFilter(transparent);
            senzor4lvl2.setColorFilter(transparent);
            senzor4lvl3.setColorFilter(transparent);
        }
    }
}
