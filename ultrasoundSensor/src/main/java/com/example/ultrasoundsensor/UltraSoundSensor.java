package com.example.ultrasoundsensor;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.core.IotSensor;
import com.google.android.material.slider.Slider;

public class UltraSoundSensor extends Fragment implements IotSensor {

    private ImageView senzor1lvl1,senzor1lvl2, senzor1lvl3, senzor2lvl1, senzor2lvl2, senzor2lvl3, senzor3lvl1, senzor3lvl2
            ,senzor3lvl3, senzor4lvl1, senzor4lvl2, senzor4lvl3,level1,level2;
    TextView firstDistance , secondDistance, thirdDistance, txtSenzor1,txtSenzor2,txtSenzor3,txtSenzor4;
    Slider distanceSlider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.graphview, container,false);

        senzor1lvl1 =  view.findViewById(R.id.idSenzor1Lvl1);
        senzor1lvl2 =  view.findViewById(R.id.idSenzor1Lvl2);
        senzor1lvl3 =  view.findViewById(R.id.idSenzor1Lvl3);
        senzor2lvl1 =  view.findViewById(R.id.idSenzor2Lvl1);
        senzor2lvl2 =  view.findViewById(R.id.idSenzor2Lvl2);
        senzor2lvl3 =  view.findViewById(R.id.idSenzor2Lvl3);
        senzor3lvl1 =  view.findViewById(R.id.idSenzor3Lvl1);
        senzor3lvl2 =  view.findViewById(R.id.idSenzor3Lvl2);
        senzor3lvl3 =  view.findViewById(R.id.idSenzor3Lvl3);
        senzor4lvl1 =  view.findViewById(R.id.idSenzor4Lvl1);
        senzor4lvl2 =  view.findViewById(R.id.idSenzor4Lvl2);
        senzor4lvl3 =  view.findViewById(R.id.idSenzor4Lvl3);
        level1 =  view.findViewById(R.id.idLevel);
        level2 = view.findViewById(R.id.idLevel2);
        firstDistance =  view.findViewById(R.id.textView2);
        secondDistance = view.findViewById(R.id.textView3);
        thirdDistance = view.findViewById(R.id.textView4);
        txtSenzor1 =  view.findViewById(R.id.txtSenzor1);
        txtSenzor2 = view.findViewById(R.id.txtSenzor2);
        txtSenzor3 = view.findViewById(R.id.txtSenzor3);
        txtSenzor4 = view.findViewById(R.id.txtSenzor4);
        distanceSlider = view.findViewById(R.id.slider);

        Log.i("Fragment: ", "inicijaliziran");

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Fragment", "Destroied");
    }

    @Override
    public void showGraphDistance(String[] data){

        Log.i("showGraphDistance:", "Ulazim");

        float senzor1 = Float.parseFloat(data[0]);
        float senzor2 = Float.parseFloat(data[1]);
        float senzor3 = Float.parseFloat(data[2]);
        float senzor4 = Float.parseFloat(data[3]);

        int orange = Color.parseColor("#ff6600");
        int yellow = Color.parseColor("#f5f242");
        int red = Color.parseColor("#f70000");
        int transparent= Color.parseColor("#00000000");

        Log.i("Slider", String.valueOf(distanceSlider.getValue()));

        /*
        float minSensDistance = distanceSlider.getValue();
        if(senzor1 < minSensDistance)
            senzor1 = (float) 0.11;
        if(senzor2 < minSensDistance)
            senzor2 = (float) 0.11;
        if(senzor3 < minSensDistance)
            senzor3 = (float) 0.11;
        if(senzor4 < minSensDistance)
            senzor4 = (float) 0.11;

         */

        if(senzor1 > 1  && senzor1 < 2){
            senzor1lvl1.setColorFilter(yellow);
        }
        else if (senzor1 <= 1 && senzor1 >= 0.5){
            senzor1lvl1.setColorFilter(yellow); //yellow
            senzor1lvl2.setColorFilter(orange); //orange
        }
        else if(senzor1 < 0.5 && senzor1 >= 0.0 ){
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
        else if(senzor2 <= 1 && senzor2 >= 0.5){
            senzor2lvl1.setColorFilter(yellow);
            senzor2lvl2.setColorFilter(orange);
        }
        else if(senzor2 < 0.5 && senzor2 > 0.0){
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
        else if(senzor3 <= 1 && senzor3 >= 0.5){
            senzor3lvl1.setColorFilter(yellow);
            senzor3lvl2.setColorFilter(orange);
        }
        else if(senzor3 < 0.5 && senzor3 > 0.0){
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
        else if(senzor4 <= 1 && senzor4 >= 0.5){
            senzor4lvl1.setColorFilter(yellow);
            senzor4lvl2.setColorFilter(orange);
        }
        else if(senzor4 < 0.5 && senzor4 > 0.0){
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
