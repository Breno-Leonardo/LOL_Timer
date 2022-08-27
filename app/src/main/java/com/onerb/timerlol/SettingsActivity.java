package com.onerb.timerlol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private Spinner dropdownLanguage;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        sharedPref = this.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
         int[][] states = new int[][] {
                new int[] {android.R.attr.state_checked}, // checked
                new int[] {-android.R.attr.state_checked}, // unchecked
        };

         int[] colors = new int[] {
                getColor(R.color.greenTime),
                Color.BLACK,

        };
        ColorStateList listColors = new ColorStateList(states, colors);

        Switch[] switches={findViewById(R.id.switchVibrateOnCreate),findViewById(R.id.switchVibrateOnFinish),findViewById(R.id.switchSpeakOnEnd)};
        for (Switch s:
             switches) {
            s.setThumbTintList(listColors);
            s.setTrackTintList(listColors);
        }
        System.out.println("SettingsActivity.onCreate all configs "+sharedPref.getAll());
        switches[0].setChecked(sharedPref.getBoolean("vibrateCreate",true));
        switches[1].setChecked(sharedPref.getBoolean("vibrateFinish",true));
        switches[2].setChecked(sharedPref.getBoolean("speakFinish",true));


        switches[0].setOnCheckedChangeListener((compoundButton, b) -> {//create
            editor.putBoolean("vibrateCreate", b );
            editor.commit();
        });
        switches[1].setOnCheckedChangeListener((compoundButton, b) -> {//finish
            editor.putBoolean("vibrateFinish", b );
            editor.commit();
        });
        switches[2].setOnCheckedChangeListener((compoundButton, b) -> {//speak
            editor.putBoolean("speakFinish", b );
            editor.commit();
        });

        findViewById(R.id.btnCreateCommandInSettings).setOnTouchListener((view, motionEvent) -> {
            Intent intent = new Intent(SettingsActivity.this, CreateCustomCommands.class);
            startActivity(intent);
            return false;
        });
        findViewById(R.id.switchVibrateOnCreate).setOnTouchListener((view, motionEvent) -> {
            Switch sw=  findViewById(R.id.switchVibrateOnCreate);

            return false;
        });


        dropdownLanguage = findViewById(R.id.spinnerLanguage);
        dropdownLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parentView.getChildAt(0)).setTextSize(20);
                    ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                }
                if(position==0){
                    editor.putString("language", Locale.getDefault().toString());
                    editor.putInt("languagePosition", position);
                    System.out.println("SettingsActivity.onItemSelected  configs coloquei a padrao");
                }
                else if(position>0){
                    editor.putString("language", Locale.getAvailableLocales()[position-1].toString() );
                    editor.putInt("languagePosition", position);
                    System.out.println("SettingsActivity.onItemSelected configs coloquei a nao padrao");
                    System.out.println("SettingsActivity.onItemSelected "+Locale.getAvailableLocales()[position-1].toString() );
                }
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayList<String> languages= new ArrayList<>();
        languages.add(Locale.getDefault().toString());
        for (Locale s:
                Locale.getAvailableLocales()) {
            languages.add(s.toString());
        }




        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        dropdownLanguage.setAdapter(adapter);
        dropdownLanguage.setSelection(sharedPref.getInt("languagePosition",0));

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