package com.onerb.timerlol;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
         int[][] states = new int[][] {
                new int[] {android.R.attr.state_checked}, // checked
                new int[] {-android.R.attr.state_checked}, // unchecked
        };

         int[] colors = new int[] {
                getColor(R.color.greenTime),
                Color.BLACK,

        };
        ColorStateList listColors = new ColorStateList(states, colors);

        Switch[] switches={findViewById(R.id.switchVibrateOnCreate),findViewById(R.id.switchVibrateOnFinish)};
        for (Switch s:
             switches) {
            s.setThumbTintList(listColors);
            s.setTrackTintList(listColors);
        }
        findViewById(R.id.btnCreateCommandInSettings).setOnTouchListener((view, motionEvent) -> {
            Intent intent = new Intent(SettingsActivity.this, CreateCustomCommands.class);
            startActivity(intent);
            return false;
        });
        findViewById(R.id.switchVibrateOnCreate).setOnTouchListener((view, motionEvent) -> {
            Switch sw=  findViewById(R.id.switchVibrateOnCreate);

            return false;
        });

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