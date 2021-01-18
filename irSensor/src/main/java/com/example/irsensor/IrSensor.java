package com.example.irsensor;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class IrSensor extends Fragment implements IotSensor {

    private ImageView senzor1lvl3, senzor2lvl3, senzor3lvl3, senzor4lvl3;

    public static final int SOUND_1 = 1;

    SoundPool mSoundPool;
    HashMap<Integer, Integer> mSoundMap;
    AudioManager mgr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View sensorDataView = inflater.inflate(R.layout.irview,container,false);

        senzor1lvl3 =  sensorDataView.findViewById(R.id.idSenzor1Lvl3);
        senzor2lvl3 =  sensorDataView.findViewById(R.id.idSenzor2Lvl3);
        senzor3lvl3 =  sensorDataView.findViewById(R.id.idSenzor3Lvl3);
        senzor4lvl3 =  sensorDataView.findViewById(R.id.idSenzor4Lvl3);

        return sensorDataView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //sound setup
        mgr = (AudioManager) this.getContext().getSystemService(this.getContext().AUDIO_SERVICE);

        AudioAttributes audioAttrib = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttrib)
                .build();

        mSoundMap = new HashMap<Integer, Integer>();

        if(mSoundPool != null){
            mSoundMap.put(SOUND_1, mSoundPool.load(this.getContext(), R.raw.long_sound, 1));
        }
    }

    @Override
    public void showGraphDistance(String[] data)
    {
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

    public void playSound(int sound)
    {
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;

        if(mSoundPool != null){
            mSoundPool.play(mSoundMap.get(sound), volume, volume, 1, 0, 1.0f);
        }
    }

    public void stopSound(int sound)
    {
        if(mSoundPool != null){
            mSoundPool.stop(sound);
        }
    }

    @Override
    public void playAudio(String[] data) {
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

        float max = senzori.get(3);

        //senzor 1
        if(max == senzor1) {
            if(senzor1 == 1)
                stopSound(SOUND_1);
            else {
                playSound(SOUND_1);
            }
        }
        //senzor 2
        else if(max == senzor2) {
            if(senzor2 == 1)
                stopSound(SOUND_1);
            else {
                playSound(SOUND_1);
            }
        }
        //senzor 3
        else if(max == senzor3) {
            if(senzor3 == 1)
                stopSound(SOUND_1);
            else {
                playSound(SOUND_1);
            }
        }
        //senzor 4
        else if(max == senzor4) {
            if(senzor4 == 1)
                stopSound(SOUND_1);
            else {
                playSound(SOUND_1);
            }
        }
    }
}
