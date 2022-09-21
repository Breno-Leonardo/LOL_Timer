package com.onerb.timerlol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onerb.timerlol.api.MatchApiUtil;
import com.onerb.timerlol.ui.main.MainViewModel;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final int RECORD_AUDIO_REQUEST_CODE = 1;
    private long timeIcon, timeIcon2, timeImage, timeImage2;
    private CountDownTimer timerIcon, timerImages;
    private ImageView thumb;
    private int rotation, currentImage;
    private float scaleImage;
    private float percentulTime, percentualScale;
    private MainViewModel viewModel;
    private ImageView icon;
    private String[] images;
    private AssetManager am;
    private Spinner dropdown;
    private int dropdownPosition = -1;
    private EditText etSummonerName;
    private SharedPreferences sharedPref;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sharedPref = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPref.edit();
        SettingsActivity.setLocale(this,sharedPref.getString("languageApp","" + Locale.getDefault().toString().charAt(0) + Locale.getDefault().toString().charAt(1)));
        editor.putString("languageApp", "" + Locale.getDefault().toString().charAt(0) + Locale.getDefault().toString().charAt(1));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewMainActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        viewModel = getViewModel();
        viewModel.showImages.setValue(true);
        Button btnStartOff= findViewById(R.id.btnStartInCardOffline);
        btnStartOff.setText(getString(R.string.start)+" Offline");

        verifySummonerAndRegion();


                am = getApplicationContext().getAssets();
        images = null;
        try {
            images = am.list("thumbs");
        } catch (IOException e) {
            e.printStackTrace();
        }

        thumb = findViewById(R.id.imageThumb);
        rotateIcon();
        scaleImages();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        findViewById(R.id.btnStart).setOnClickListener(view -> {
            if (!sharedPref.getBoolean("offline",true) ) {
                MatchApiUtil matchApiUtil = new MatchApiUtil(getViewModel(),sharedPref.getString("name","Not contain this name &*&*(¨*("), sharedPref.getString("route","Not contain this name &*&*(¨*("));
                System.out.println("MainActivity.onCreate Nome: "+sharedPref.getString("name","Not contain this name &*&*(¨*(")+ " Route:"+sharedPref.getString("route","Not contain this name &*&*(¨*("));
                try {
                    matchApiUtil.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (matchApiUtil.getRespCodeSumonnerId() == 404) {
                    showCardIdAndRegion();
                    findViewById(R.id.textErrorInCard).setVisibility(View.VISIBLE);
                    TextView txt=findViewById(R.id.textErrorInCard);
                    txt.setText(R.string.error);
                    btnStartOff.setVisibility(View.VISIBLE);
                }
                viewModel.showImages.setValue(false);
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
            }
            else if(sharedPref.getBoolean("offline",false)){
                viewModel.showImages.setValue(false);
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
            }
            else {
                showCardIdAndRegion();
            }
        });
        findViewById(R.id.btnStartInCardOffline).setOnClickListener(view -> {
            editor.putBoolean("offline", true);
            editor.commit();
            viewModel.showImages.setValue(false);
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            startActivity(intent);

        });

        findViewById(R.id.btnStartInCard).setOnClickListener(view -> {
            System.out.println("MainActivity.onCreate route: " + MatchApiUtil.REGIONS_ROUTES[dropdownPosition]);
            MatchApiUtil matchApiUtil = new MatchApiUtil(getViewModel(), etSummonerName.getText().toString(), MatchApiUtil.REGIONS_ROUTES[dropdownPosition]);

            try {
                matchApiUtil.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (matchApiUtil.getRespCodeSumonnerId() == 200) {//sucesss
                editor.putBoolean("offline",false );
                String name= String.valueOf(etSummonerName.getText());
                name.replace(" ","");
                System.out.println("MainActivity.onCreate o nome é "+name);
                editor.putString("name",name );
                editor.putString("route",MatchApiUtil.REGIONS_ROUTES[dropdownPosition] );
                editor.commit();
                viewModel.showImages.setValue(false);
                findViewById(R.id.textErrorInCard).setVisibility(View.GONE);

                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
            } else if (matchApiUtil.getRespCodeSumonnerId() == 404) {
                findViewById(R.id.textErrorInCard).setVisibility(View.VISIBLE);
                TextView txt=findViewById(R.id.textErrorInCard);
                txt.setText(R.string.error);
                btnStartOff.setVisibility(View.VISIBLE);
            }
             else {
                btnStartOff.setVisibility(View.VISIBLE);
                TextView txt=findViewById(R.id.textErrorInCard);
                txt.setText(R.string.error_conection);
                 findViewById(R.id.textErrorInCard).setVisibility(View.VISIBLE);
             }

        });


        findViewById(R.id.btnCommands).setOnClickListener(view -> {
            viewModel.showImages.setValue(false);
            Intent intent = new Intent(MainActivity.this, CommandsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnSettings).setOnClickListener(view -> {
            viewModel.showImages.setValue(false);
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });



    }
    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }


    }
    public MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
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

    public void showCardIdAndRegion() {
        findViewById(R.id.btnStart).setEnabled(false);
        findViewById(R.id.btnCommands).setEnabled(false);
        findViewById(R.id.btnSettings).setEnabled(false);
        findViewById(R.id.shadowLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.cardIdAndRegion).setVisibility(View.VISIBLE);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
//        AudioManager audioManager =
//                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_MUTE,
//                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        AudioManager audioManager =
//                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE,
//                AudioManager.FLAG_VIBRATE);
//    }
    public void rotateIcon() {
        icon = findViewById(R.id.imageTime);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeIcon = 6500;
                timeIcon2 = timeIcon / 25;
                rotation = 360;
                percentulTime = 0;
                icon.animate().rotation(rotation).setDuration(timeIcon);
                timerIcon = new CountDownTimer(timeIcon, timeIcon2) {
                    @Override
                    public void onTick(long l) {
                        percentulTime += timeIcon2;
                        float p = percentulTime / timeIcon;
                        if (p >= 0.375f && p < 0.5f)
                            icon.setImageDrawable(getDrawable(R.drawable.timer_sand_paused));
                        else if (p >= 0.5f && p < 0.84f)
                            icon.setImageDrawable(getDrawable(R.drawable.timer_sand));
                        else if (p >= 0.8f && p < 1f)
                            icon.setImageDrawable(getDrawable(R.drawable.timer_sand_paused));


                    }

                    @Override
                    public void onFinish() {
                        icon.setImageDrawable(getDrawable(R.drawable.timer_sand_complete));
                        icon.setRotation(0);
                        rotateIcon();
                    }
                }.start();


            }
        });
    }

    public void scaleImages() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeImage2 = 4000;// tempo entre cada imagem
                timeImage = timeImage2 * images.length;
                scaleImage = 0.10f;
                currentImage = 0;
                percentulTime = 0;

                timerImages = new CountDownTimer(timeImage, timeImage2) {
                    @Override
                    public void onTick(long l) {
                        thumb.setScaleX(1);
                        thumb.setScaleX(1);
                        thumb.animate().scaleX(1 + scaleImage).setDuration(timeImage2);
                        thumb.animate().scaleY(1 + scaleImage).setDuration(timeImage2);

                        currentImage++;
                        if (currentImage == images.length)
                            currentImage = 0;
                        try {

                            thumb.setBackground(Drawable.createFromStream(am.open("thumbs/" + images[currentImage]), null));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFinish() {

                        scaleImages();
                    }
                }.start();


            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}