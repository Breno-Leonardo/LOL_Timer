package com.onerb.timerlol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class CommandsActivity extends AppCompatActivity {
private Spinner dropdownLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_commands);

        findViewById(R.id.btnCreateCommandInCommands).setOnTouchListener((view, motionEvent) -> {
            Intent intent = new Intent(CommandsActivity.this, CreateCustomCommands.class);
            startActivity(intent);
            return false;
        });
        TextView[] txts= {findViewById(R.id.txtTop),findViewById(R.id.txtJungle),findViewById(R.id.txtMid),findViewById(R.id.txtBot),findViewById(R.id.txtSuport),
                findViewById(R.id.txtFlash),findViewById(R.id.txtIgnite),findViewById(R.id.txtTeleport),findViewById(R.id.txtHeal),findViewById(R.id.txtExhaust),
                findViewById(R.id.txtBarrier),findViewById(R.id.txtGhost),findViewById(R.id.txtCleanse),findViewById(R.id.txtBoots)};
        dropdownLanguage = findViewById(R.id.spinnerLanguageInCommands);
        dropdownLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parentView.getChildAt(0)).setTextSize(20);
                    ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                }
                txts[0].setText(TimerActivity.laneTopExtra.split(" ")[position].toUpperCase());
                txts[1].setText(TimerActivity.laneJungleExtra.split(" ")[position].toUpperCase());
                txts[2].setText(TimerActivity.laneMidExtra.split(" ")[position].toUpperCase());
                txts[3].setText(TimerActivity.laneAdcExtra.split(" ")[position].toUpperCase());
                txts[4].setText(TimerActivity.laneSupportExtra.split(" ")[position].toUpperCase());


                txts[5].setText(TimerActivity.flashExtra.split(" ")[position].toUpperCase());
                txts[6].setText(TimerActivity.igniteExtra.split(" ")[position].toUpperCase());
                txts[7].setText(TimerActivity.teleportExtra.split(" ")[position].toUpperCase());
                txts[8].setText(TimerActivity.healExtra.split(" ")[position].toUpperCase());
                txts[9].setText(TimerActivity.exhaustExtra.split(" ")[position].toUpperCase());
                txts[10].setText(TimerActivity.barrierExtra.split(" ")[position].toUpperCase());
                txts[11].setText(TimerActivity.ghostExtra.split(" ")[position].toUpperCase());
                txts[12].setText(TimerActivity.cleanseExtra.split(" ")[position].toUpperCase());
                txts[13].setText(TimerActivity.bootExtra.split(" ")[position].toUpperCase());
                TextView example= findViewById(R.id.activateExample);
                example.setText("\""+txts[2].getText()+" "+txts[5].getText()+"\" "+getString(R.string.or)+" \"" +txts[5].getText()+" "+txts[2].getText()+"\" ");

                TextView exampleBoots= findViewById(R.id.activateExampleBoots);
                exampleBoots.setText("\""+txts[2].getText()+" "+txts[13].getText()+"\" "+getString(R.string.or)+" \"" +txts[13].getText()+" "+txts[2].getText()+"\" "
                        +getString(R.string.or)+" \""+txts[2].getText()+" "+TimerActivity.bootsExtra.split(" ")[position]+"\" "+getString(R.string.or)+" \""
                        +TimerActivity.bootsExtra.split(" ")[position]+" "+txts[2].getText()+"\" ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] languages={"EN","PT","DE","ES","FR","IT","PL","EL","RO","HU","CS","JA","RU","TR","KO","ZH"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        dropdownLanguage.setAdapter(adapter);
        dropdownLanguage.setSelection(0);


    }



    @Override
    protected void onStop() {
        super.onStop();
        try {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE,
                    AudioManager.FLAG_VIBRATE);
        }catch (IllegalArgumentException e) {

        }

    }

}