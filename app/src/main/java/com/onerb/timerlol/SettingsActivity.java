package com.onerb.timerlol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onerb.timerlol.api.MatchApiUtil;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class SettingsActivity extends AppCompatActivity {
    private Spinner dropdownLanguage, dropdownLanguageApp;
    private SharedPreferences sharedPref;
    private Activity activity;
    private AdView mAdView;
    private Spinner dropdown;
    private EditText etSummonerName;
    private int dropdownPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewSettingsActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPref = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked}, // checked
                new int[]{-android.R.attr.state_checked}, // unchecked
        };

        int[] colors = new int[]{
                getColor(R.color.greenTime),
                Color.BLACK,

        };
        ColorStateList listColors = new ColorStateList(states, colors);

        Switch[] switches = {findViewById(R.id.switchVibrateOnCreate), findViewById(R.id.switchVibrateOnFinish), findViewById(R.id.switchSpeakOnEnd)};
        for (Switch s :
                switches) {
            s.setThumbTintList(listColors);
            s.setTrackTintList(listColors);
        }
        System.out.println("SettingsActivity.onCreate all configs " + sharedPref.getAll());
        switches[0].setChecked(sharedPref.getBoolean("vibrateCreate", true));
        switches[1].setChecked(sharedPref.getBoolean("vibrateFinish", true));
        switches[2].setChecked(sharedPref.getBoolean("speakFinish", true));


        switches[0].setOnCheckedChangeListener((compoundButton, b) -> {//create
            editor.putBoolean("vibrateCreate", b);
            editor.commit();
        });
        switches[1].setOnCheckedChangeListener((compoundButton, b) -> {//finish
            editor.putBoolean("vibrateFinish", b);
            editor.commit();
        });
        switches[2].setOnCheckedChangeListener((compoundButton, b) -> {//speak
            editor.putBoolean("speakFinish", b);
            editor.commit();
        });

        findViewById(R.id.btnCreateCommandInSettings).setOnTouchListener((view, motionEvent) -> {
            Intent intent = new Intent(SettingsActivity.this, CreateCustomCommands.class);
            startActivity(intent);
            return false;
        });
        findViewById(R.id.switchVibrateOnCreate).setOnTouchListener((view, motionEvent) -> {
            Switch sw = findViewById(R.id.switchVibrateOnCreate);

            return false;
        });

        verifySummonerAndRegion();
        dropdownLanguage = findViewById(R.id.spinnerLanguage);
        dropdownLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parentView.getChildAt(0)).setTextSize(20);
                    ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                }
                if (position == 0) {
                    editor.putString("language", Locale.getDefault().toString());
                    editor.putInt("languagePosition", position);
                    System.out.println("SettingsActivity.onItemSelected  configs coloquei a padrao");
                } else if (position > 0) {
                    editor.putString("language", Locale.getAvailableLocales()[position - 1].toString());
                    editor.putInt("languagePosition", position);
                    System.out.println("SettingsActivity.onItemSelected configs coloquei a nao padrao");
                    System.out.println("SettingsActivity.onItemSelected " + Locale.getAvailableLocales()[position - 1].toString());
                }
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayList<String> languages = new ArrayList<>();
        languages.add(Locale.getDefault().toString());
        for (Locale s :
                Locale.getAvailableLocales()) {
            languages.add(s.toString());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        dropdownLanguage.setAdapter(adapter);
        dropdownLanguage.setSelection(sharedPref.getInt("languagePosition", 0));
        final boolean[] first = {true};

        String[] languagesApp = {"en", "pt","zh","de","es","el","ru","ja","ro","tr","ko","cs","it","hu","pl"};
        activity = this;
        dropdownLanguageApp = findViewById(R.id.spinnerLanguageApp);
        dropdownLanguageApp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parentView.getChildAt(0)).setTextSize(20);
                    ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                }


                if (!first[0]) {
                    editor.putString("languageApp", languagesApp[position]);
                    editor.putInt("languageAppPosition", position);
                    editor.commit();
                        setLocale(activity, languagesApp[position]);

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);

                }
                first[0] = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languagesApp);
        dropdownLanguageApp.setAdapter(adapter2);
        int pos=0;
        for (int i = 0; i < languagesApp.length; i++) {
            if(languagesApp[i].equalsIgnoreCase(sharedPref.getString("languageApp","" + Locale.getDefault().toString().charAt(0) + Locale.getDefault().toString().charAt(1))))
                pos=i;
        }
        dropdownLanguageApp.setSelection(sharedPref.getInt("languageAppPosition",pos));

        TextView txtLanguageApp= findViewById(R.id.languageAppTxt);
        txtLanguageApp.setText(txtLanguageApp.getText()+" App");

        findViewById(R.id.btnCancel).setOnClickListener(view -> {
            findViewById(R.id.cardIdAndRegionSettings).setVisibility(View.GONE);

        });
        findViewById(R.id.btnChangeSummoner).setOnClickListener(view -> {
            findViewById(R.id.cardIdAndRegionSettings).setVisibility(View.VISIBLE);

        });
        Button btnCancel= findViewById(R.id.btnCancel);
        btnCancel.setText(getString(android.R.string.cancel));
        findViewById(R.id.btnChange).setOnClickListener(view -> {
            System.out.println("MainActivity.onCreate route: " + MatchApiUtil.REGIONS_ROUTES[dropdownPosition]);
            MatchApiUtil matchApiUtil = new MatchApiUtil( etSummonerName.getText().toString(), MatchApiUtil.REGIONS_ROUTES[dropdownPosition]);

            try {
                matchApiUtil.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (matchApiUtil.getRespCode() == 200) {//sucesss
                editor.putBoolean("offline",false );
                String name= String.valueOf(etSummonerName.getText());
                name.replace(" ","");
                System.out.println("MainActivity.onCreate o nome é "+name);
                editor.putString("name",name );
                editor.putString("route",MatchApiUtil.REGIONS_ROUTES[dropdownPosition] );
                editor.commit();
                findViewById(R.id.cardIdAndRegionSettings).setVisibility(View.GONE);
            } else if (matchApiUtil.getRespCode() == 404) {
                findViewById(R.id.textErrorInCard).setVisibility(View.VISIBLE);
                TextView txt=findViewById(R.id.textErrorInCard);
                txt.setText(R.string.error);
            }
            else {
                TextView txt=findViewById(R.id.textErrorInCard);
                txt.setText(R.string.error_conection);
                findViewById(R.id.textErrorInCard).setVisibility(View.VISIBLE);
            }
            System.out.println("MainActivity.onCreate resposta: " + matchApiUtil.getRespCode());

        });

    }
    public void verifySummonerAndRegion() {
        etSummonerName = findViewById(R.id.editTextSummonerName);
        etSummonerName.setOnTouchListener((view, motionEvent) -> {
            if (etSummonerName.getText().toString().equals(getResources().getString(R.string.putSummonerName)))
                etSummonerName.setText("");
            return false;
        });
        dropdown = findViewById(R.id.spinnerRegion);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null)
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                dropdownPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String[] items = new String[]{MatchApiUtil.BRAZIL, MatchApiUtil.EUNE, MatchApiUtil.EUW1, MatchApiUtil.JP1, MatchApiUtil.KR, MatchApiUtil.LA1, MatchApiUtil.LA2, MatchApiUtil.NA1, MatchApiUtil.OC1, MatchApiUtil.TR1, MatchApiUtil.RU};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void showCardIdAndRegion(boolean bool) {
        if(bool) {
            findViewById(R.id.btnStart).setEnabled(false);
            findViewById(R.id.btnCommands).setEnabled(false);
            findViewById(R.id.btnSettings).setEnabled(false);
            findViewById(R.id.shadowLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.cardIdAndRegion).setVisibility(View.VISIBLE);
        }
        else{

        }
    }
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        } catch (IllegalArgumentException e) {

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE,
                    AudioManager.FLAG_VIBRATE);
        } catch (IllegalArgumentException e) {

        }
    }

}