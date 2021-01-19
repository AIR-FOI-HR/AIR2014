package com.example.ultrasoundsensor;


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
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UltraSoundSensor extends Fragment implements IotSensor {

    private ImageView senzor1lvl1,senzor1lvl2, senzor1lvl3, senzor2lvl1, senzor2lvl2, senzor2lvl3, senzor3lvl1, senzor3lvl2
            ,senzor3lvl3, senzor4lvl1, senzor4lvl2, senzor4lvl3,level1,level2;
    TextView firstDistance , secondDistance, thirdDistance;
    Slider distanceSlider;

    public static final int SOUND_1 = 1;
    public static final int SOUND_2 = 2;
    public static final int SOUND_3 = 3;

    SoundPool mSoundPool;
    HashMap<Integer, Integer> mSoundMap;
    AudioManager mgr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
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
        distanceSlider = view.findViewById(R.id.slider);

        return view;
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
            mSoundMap.put(SOUND_1, mSoundPool.load(this.getContext(), R.raw.short_sound, 1));
            mSoundMap.put(SOUND_2, mSoundPool.load(this.getContext(), R.raw.medium_sound, 1));
            mSoundMap.put(SOUND_3, mSoundPool.load(this.getContext(), R.raw.long_sound, 1));
        }
    }

    @Override
    public void showGraphDistance(String[] data)
    {
        float senzor1 = Float.parseFloat(data[0]);
        float senzor2 = Float.parseFloat(data[1]);
        float senzor3 = Float.parseFloat(data[2]);
        float senzor4 = Float.parseFloat(data[3]);

        int orange = Color.parseColor("#ff6600");
        int yellow = Color.parseColor("#f5f242");
        int red = Color.parseColor("#f70000");
        int transparent= Color.parseColor("#00000000");

        float sensitivity = distanceSlider.getValue();

        if(senzor1 < sensitivity)
            senzor1 = 0;
        if(senzor2 < sensitivity)
            senzor2 = 0;
        if(senzor3 < sensitivity)
            senzor3 = 0;
        if(senzor4 < sensitivity)
            senzor4 = 0;

        if(senzor1 > 1  && senzor1 < 2){
            senzor1lvl1.setColorFilter(yellow);
            senzor1lvl2.setColorFilter(transparent);
            senzor1lvl3.setColorFilter(transparent);
        }
        else if (senzor1 <= 1 && senzor1 > 0.5){
            senzor1lvl1.setColorFilter(yellow); //yellow
            senzor1lvl2.setColorFilter(orange); //orange
            senzor1lvl3.setColorFilter(transparent);
        }
        else if(senzor1 <= 0.5 && senzor1 >= 0.0 ){
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
            senzor2lvl2.setColorFilter(transparent);
            senzor2lvl3.setColorFilter(transparent);
        }
        else if(senzor2 <= 1 && senzor2 > 0.5){
            senzor2lvl1.setColorFilter(yellow);
            senzor2lvl2.setColorFilter(orange);
            senzor2lvl3.setColorFilter(transparent);
        }
        else if(senzor2 <= 0.5 && senzor2 >= 0.0){
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
            senzor3lvl2.setColorFilter(transparent);
            senzor3lvl3.setColorFilter(transparent);
        }
        else if(senzor3 <= 1 && senzor3 > 0.5){
            senzor3lvl1.setColorFilter(yellow);
            senzor3lvl2.setColorFilter(orange);
            senzor3lvl3.setColorFilter(transparent);
        }
        else if(senzor3 <= 0.5 && senzor3 >= 0.0){
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
            senzor4lvl2.setColorFilter(transparent);
            senzor4lvl3.setColorFilter(transparent);
        }
        else if(senzor4 <= 1 && senzor4 > 0.5){
            senzor4lvl1.setColorFilter(yellow);
            senzor4lvl2.setColorFilter(orange);
            senzor4lvl3.setColorFilter(transparent);
        }
        else if(senzor4 <= 0.5 && senzor4 >= 0.0){
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
    public void playAudio(String[] data){
        float senzor1 = Float.parseFloat(data[0]);
        float senzor2 = Float.parseFloat(data[1]);
        float senzor3 = Float.parseFloat(data[2]);
        float senzor4 = Float.parseFloat(data[3]);

        float sensitivity = distanceSlider.getValue();

        if(senzor1 < sensitivity)
            senzor1 = 0;
        if(senzor2 < sensitivity)
            senzor2 = 0;
        if(senzor3 < sensitivity)
            senzor3 = 0;
        if(senzor4 < sensitivity)
            senzor4 = 0;

        List<Float> senzori = new ArrayList<Float>();
        senzori.add(senzor1);
        senzori.add(senzor2);
        senzori.add(senzor3);
        senzori.add(senzor4);
        Collections.sort(senzori);

        float min = senzori.get(0);

        //senzor 1
        if(min == senzor1) {
            if(senzor1 > 2) {
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                stopSound(SOUND_3);
            }
            else if (senzor1 > 1 && senzor1 <= 2) {
                stopSound(SOUND_2);
                stopSound(SOUND_3);
                playSound(SOUND_1);
            }
            else if (senzor1 <= 1 && senzor1 > 0.5) {
                stopSound(SOUND_1);
                stopSound(SOUND_3);
                playSound(SOUND_2);
            }
            else {
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                playSound(SOUND_3);
            }
        }
        //senzor 2
        else if(min == senzor2) {
            if(senzor2 > 2){
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                stopSound(SOUND_3);
            }
            else if (senzor2 > 1 && senzor1 <= 2) {
                stopSound(SOUND_2);
                stopSound(SOUND_3);
                playSound(SOUND_1);
            }
            else if (senzor2 <= 1 && senzor2 > 0.5) {
                stopSound(SOUND_1);
                stopSound(SOUND_3);
                playSound(SOUND_2);
            }
            else {
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                playSound(SOUND_3);
            }
        }
        //senzor 3
        else if(min == senzor3) {
            if(senzor3 > 2){
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                stopSound(SOUND_3);
            }
            else if (senzor3 > 1 && senzor1 <= 2) {
                stopSound(SOUND_2);
                stopSound(SOUND_3);
                playSound(SOUND_1);
            }
            else if (senzor3 <= 1 && senzor3 > 0.5) {
                stopSound(SOUND_1);
                stopSound(SOUND_3);
                playSound(SOUND_2);
            }
            else {
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                playSound(SOUND_3);
            }
        }
        //senzor 4
        else if(min == senzor4) {
            if (senzor4 > 2){
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                stopSound(SOUND_3);
            }
            else if (senzor4 > 1 && senzor1 <= 2) {
                stopSound(SOUND_2);
                stopSound(SOUND_3);
                playSound(SOUND_1);
            }
            else if (senzor4 <= 1 && senzor4 > 0.5) {
                stopSound(SOUND_1);
                stopSound(SOUND_3);
                playSound(SOUND_2);
            }
            else {
                stopSound(SOUND_1);
                stopSound(SOUND_2);
                playSound(SOUND_3);
            }
        }
    }
}
