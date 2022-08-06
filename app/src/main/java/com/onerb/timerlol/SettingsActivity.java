package com.onerb.timerlol;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);


    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        try{
        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_MUTE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);}catch (IllegalArgumentException e) {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE,
                AudioManager.FLAG_VIBRATE);}
        catch (IllegalArgumentException e) {

        }
    }

}