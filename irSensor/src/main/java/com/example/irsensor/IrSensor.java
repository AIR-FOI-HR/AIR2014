package com.example.irsensor;

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

import com.example.core.IotSensor;

public class IrSensor extends Fragment implements IotSensor {

    private ImageView senzor1lvl3, senzor2lvl3, senzor3lvl3, senzor4lvl3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View sensorDataView = inflater.inflate(R.layout.irview,container,false);

        senzor1lvl3 =  sensorDataView.findViewById(R.id.idSenzor1Lvl3);
        senzor2lvl3 =  sensorDataView.findViewById(R.id.idSenzor2Lvl3);
        senzor3lvl3 =  sensorDataView.findViewById(R.id.idSenzor3Lvl3);
        senzor4lvl3 =  sensorDataView.findViewById(R.id.idSenzor4Lvl3);

        return sensorDataView;
    }

    @Override
    public void showGraphDistance(String[] data){

        float senzor1 = Float.parseFloat(data[0]);
        float senzor2 = Float.parseFloat(data[1]);
        float senzor3 = Float.parseFloat(data[2]);
        float senzor4 = Float.parseFloat(data[3]);

        int red = Color.parseColor("#f70000");
        int transparent= Color.parseColor("#00000000");

        if(senzor1 == 0){
            senzor1lvl3.setColorFilter(red);
        }
        else{
            senzor1lvl3.setColorFilter(transparent);
        }

        //senzor2
        if(senzor2 == 0 ){
            senzor2lvl3.setColorFilter(red);
        }
        else{

            senzor2lvl3.setColorFilter(transparent);
        }

        //senzor3
        if(senzor3 == 0){
            senzor3lvl3.setColorFilter(red);
        }
        else{
            senzor3lvl3.setColorFilter(transparent);
        }

        //senzor4
        if(senzor4 == 0){
            senzor4lvl3.setColorFilter(red);
        }
        else{
            senzor4lvl3.setColorFilter(transparent);
        }
    }
}
