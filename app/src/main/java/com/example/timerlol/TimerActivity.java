package com.example.timerlol;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.timerlol.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private static final int RECORD_AUDIO_REQUEST_CODE = 1;
    private EditText editText;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private Intent srIntent;
    private CountDownTimer timer;
    private LinearLayout container;
    private View containerTimer;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();



//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_timer);

        container=findViewById(R.id.timersContainer);

containerTimer=  getLayoutInflater().inflate(R.layout.view_timer,container , false);
container.addView(containerTimer);
        editText = findViewById(R.id.textTimer);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);

                }
            }
        });
        time = 60000;
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                time = l;
//                updateTextTime();
            }

            @Override
            public void onFinish() {

            }
        }.start();



        StartListening();

    }
    public MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }


    private void StartListening() {
//mute();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        srIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "breno");
        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
//        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
//                System.out.println("TimerActivity.onError erro "+i);
                if(i==7|| i==8) {
                speechRecognizer.destroy();
                speechRecognizer = null;
                StartListening();
                }


            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> command = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String result = command.get(0);
                String[] commandCheck = result.split(" ");
                System.out.println("TimerActivity.onResults comand " + result);
                if (commandCheck.length >= 3) {

//                    if (command != null)
//                        editText.setText(command.get(0));


// o textToSpeak faz o delay entre comandos seguidos aumentar.
//                    textToSpeech.speak("Flash Mid Return", TextToSpeech.QUEUE_FLUSH, null, null);


                }
                if (command != null)
                        editText.setText(command.get(0));

                speechRecognizer.destroy();
                speechRecognizer = null;
                StartListening();


//                for (int i = 0; i < Locale.getAvailableLocales().length; i++) {
//                    System.out.println("TimerActivity.onResults "+Locale.getAvailableLocales()[i]);
//                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }

        });
        speechRecognizer.startListening(srIntent);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }
    }

    private void updateTextTime(TextView textView) {
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;
//        int seconds= (int) (time/1000);
        String formatedMinutes = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textView.setText(formatedMinutes);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        speechRecognizer.destroy();
        speechRecognizer = null;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
    public void mute(){
        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_MUTE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
    public void unmute(){
        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,AudioManager.ADJUST_UNMUTE,
                AudioManager.FLAG_VIBRATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mute();
StartListening();
        System.out.println("TimerActivity.onResume timer aqui");
    }




    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("TimerActivity.onStop stop timer aqui");
        if(speechRecognizer!=null)
            speechRecognizer.destroy();
            speechRecognizer = null;
        unmute();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if(speechRecognizer!=null)
        speechRecognizer.destroy();
        unmute();

    }
}