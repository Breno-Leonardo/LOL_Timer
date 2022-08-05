package com.example.timerlol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.timerlol.ui.main.MainViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
        ImageView icon = findViewById(R.id.imageTime);
        ArrayList<Drawable> drawables = new ArrayList<>();
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
        ImageView thumb = findViewById(R.id.imageThumb);
        int tam = inputStreams.size();
        viewModel.countImage.observe(this, countImage -> {// observe for change image
            if (getViewModel().countImage.getValue() == tam) {
                getViewModel().countImage.setValue(0);
            }


            drawables.add(Drawable.createFromStream(inputStreams.get(getViewModel().countImage.getValue()), null));
            thumb.setScaleX(1);
            thumb.setScaleY(1);
            if(getViewModel().countImage.getValue()<drawables.size())
            thumb.setBackground(drawables.get(getViewModel().countImage.getValue()));

        });


        if (inputStreams.size() > 0) {//thread thumbs and icon
            Thread thread = new Thread() {
                public void run() {
                    boolean sand = false;
                    while (getViewModel().showImages.getValue()) {
                        getViewModel().countImage.postValue(getViewModel().countImage.getValue() + 1);
                        try {
                            int t = 1000;
                            int millis = 4000;
                            for (int i = 0; i < t; i++) {
                                icon.setRotation(icon.getRotation() + 0.27f);

                                if (!sand && icon.getRotation() > 135 && icon.getRotation() < 180) {
                                    icon.setImageDrawable(getDrawable(R.drawable.timer_sand_paused));
                                    sand = true;
                                } else if (sand && icon.getRotation() > 180 && icon.getRotation() < 305) {
                                    icon.setImageDrawable(getDrawable(R.drawable.timer_sand));
                                    sand = false;
                                } else if (!sand && icon.getRotation() > 305 && icon.getRotation() < 360) {
                                    icon.setImageDrawable(getDrawable(R.drawable.timer_sand_paused));
                                    sand = true;
                                } else if (sand && icon.getRotation() > 360) {
                                    icon.setRotation(0);
                                    icon.setImageDrawable(getDrawable(R.drawable.timer_sand_complete));
                                    sand = false;
                                }
                                thumb.setScaleX(thumb.getScaleX() + 0.00005f);
                                thumb.setScaleY(thumb.getScaleY() + 0.00005f);
                                Thread.currentThread().sleep(4000 / t);
                            }


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            thread.start();
        }
//        else
//            v.setBackgroundColor(getResources().getColor(R.color.white));

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
    public void rotateIcon(ImageView icon) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                icon.animate().rotation(90).setDuration(1000);
                icon.animate().rotation(180).setDuration(1000);
                icon.animate().rotation(270).setDuration(1000);
                icon.animate().rotation(360).setDuration(1000);

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