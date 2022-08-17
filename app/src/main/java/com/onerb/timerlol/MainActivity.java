package com.onerb.timerlol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.onerb.timerlol.api.InfosGameApiUtil;
import com.onerb.timerlol.api.MatchApiUtil;
import com.onerb.timerlol.ui.main.MainViewModel;

import java.io.IOException;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        viewModel = getViewModel();
        viewModel.showImages.setValue(true);


//        verifySummonerAndRegion();


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

        findViewById(R.id.btnStart).setOnTouchListener((view, motionEvent) -> {
//            if ((viewModel.summonerName.getValue() != null) && (viewModel.region.getValue() != null)) {
                viewModel.showImages.setValue(false);
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
//            } else {
//                showCardIdAndRegion();
//            }
            return false;
        });

        findViewById(R.id.btnStartInCard).setOnTouchListener((view, motionEvent) -> {
            System.out.println("MainActivity.onCreate route: " + InfosGameApiUtil.REGIONS_ROUTES[dropdownPosition]);
            MatchApiUtil matchApiUtil = new MatchApiUtil(getViewModel(), etSummonerName.getText().toString(), InfosGameApiUtil.REGIONS_ROUTES[dropdownPosition]);

            try {
                matchApiUtil.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (matchApiUtil.getRespCode() == 200) {
                viewModel.showImages.setValue(false);
                Intent intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
            } else if (matchApiUtil.getRespCode() == 404) {
                findViewById(R.id.textErrorInCard).setVisibility(View.VISIBLE);
            }
//             else if (matchApiUtil.getRespCode() == 403) {
//                 findViewById(R.id.textErrorInCard).setVisibility(View.VISIBLE);
//             }
            System.out.println("MainActivity.onCreate resposta: " + matchApiUtil.getRespCode());
            if ((viewModel.summonerName.getValue() != null) && (viewModel.region.getValue() != null)) {

            }

            return false;
        });


        findViewById(R.id.btnCommands).setOnTouchListener((view, motionEvent) -> {
            viewModel.showImages.setValue(false);
            Intent intent = new Intent(MainActivity.this, CreateCustomCommands.class);
            startActivity(intent);
            return false;
        });

        findViewById(R.id.btnSettings).setOnTouchListener((view, motionEvent) -> {
            viewModel.showImages.setValue(false);
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return false;
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
        String[] items = new String[]{InfosGameApiUtil.BRAZIL, InfosGameApiUtil.EUNE, InfosGameApiUtil.EUW1, InfosGameApiUtil.JP1, InfosGameApiUtil.KR, InfosGameApiUtil.LA1, InfosGameApiUtil.LA2, InfosGameApiUtil.NA1, InfosGameApiUtil.OC1, InfosGameApiUtil.TR1, InfosGameApiUtil.RU};
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