package com.example.timerlol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;

import com.example.timerlol.ui.main.MainViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        MainViewModel viewModel = getViewModel();
        viewModel.showImages.setValue(true);


        AssetManager am = getApplicationContext().getAssets();
        String[] images = null;
        try {
            images = am.list("thumbs");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MainActivity.run tamanho antes" + images.length);

        ArrayList<InputStream> inputStreams = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            try {
                inputStreams.add(am.open("thumbs/" + images[i]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        View v = findViewById(R.id.containerMain);

        int tam = inputStreams.size();
        viewModel.countImage.observe(this, countImage -> {// observe for change image
            if (getViewModel().countImage.getValue() == tam) {
                getViewModel().countImage.setValue(0);
            }

                Drawable d = Drawable.createFromStream(inputStreams.get(getViewModel().countImage.getValue()), null);
                v.setBackground(d);

        });
        if (inputStreams.size() > 0) {
            Thread thread = new Thread() {
                public void run() {

                    while (getViewModel().showImages.getValue()) {
                        getViewModel().countImage.postValue(getViewModel().countImage.getValue() + 1);
                        try {
                            Thread.currentThread().sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            thread.start();
        }
        else
            v.setBackgroundColor(getResources().getColor(R.color.white));

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

    @Override
    public void onBackPressed() {
        finishAffinity();

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivity.onResume resume main aqui");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("MainActivity.onStop stop main aqui");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity.onDestroy aqui");
    }
}