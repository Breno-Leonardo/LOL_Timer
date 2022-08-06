package com.onerb.timerlol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.onerb.timerlol.ui.main.MainViewModel;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private long timeIcon, timeIcon2,timeImage, timeImage2;
    private CountDownTimer timerIcon, timerImages;
    private ImageView thumb;
    private int rotation,currentImage;
    private float scaleImage;
    private float percentulTime, percentualScale;
    private  MainViewModel viewModel ;
    private ImageView icon ;
    private String[] images ;
    private AssetManager am;
    private Drawable d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

         viewModel = getViewModel();
        viewModel.showImages.setValue(true);
         icon = findViewById(R.id.imageTime);

         am = getApplicationContext().getAssets();
         images = null;
        try {
            images = am.list("thumbs");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MainActivity.run tamanho antes" + images.length);

         thumb = findViewById(R.id.imageThumb);
        rotateIcon();
        scaleImages();


        findViewById(R.id.btnStart).setOnTouchListener((view, motionEvent) -> {
            viewModel.showImages.setValue(false);
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            startActivity(intent);
            return false;
        });

        findViewById(R.id.btnCommands).setOnTouchListener((view, motionEvent) -> {
            viewModel.showImages.setValue(false);
            Intent intent = new Intent(MainActivity.this, CommandsActivity.class);
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

    public MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timeIcon = 6500;
                timeIcon2 = timeIcon /25;
                rotation = 360;
                percentulTime = 0;
                icon.animate().rotation(rotation).setDuration(timeIcon);
                timerIcon = new CountDownTimer(timeIcon, timeIcon2) {
                    @Override
                    public void onTick(long l) {
                        percentulTime += timeIcon2;
                        float p= percentulTime / timeIcon;
                        if (p>=0.375f && p<0.5f)
                            icon.setImageDrawable(getDrawable(R.drawable.timer_sand_paused));
                        else if (p>=0.5f && p<0.84f)
                            icon.setImageDrawable(getDrawable(R.drawable.timer_sand));
                        else if (p>=0.84f && p<1f)
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
                timeImage = timeImage2*images.length;
                scaleImage=0.10f;
                 currentImage=0;
                percentulTime = 0;

                timerImages = new CountDownTimer(timeImage, timeImage2) {
                    @Override
                    public void onTick(long l) {
                        thumb.setScaleX(1);
                        thumb.setScaleX(1);
                        thumb.animate().scaleX(1+scaleImage).setDuration(timeImage2);
                        thumb.animate().scaleY(1+scaleImage/2).setDuration(timeImage2);

                        currentImage++;
                        if(currentImage==images.length)
                            currentImage=0;
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