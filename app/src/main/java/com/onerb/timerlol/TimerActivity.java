package com.onerb.timerlol;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.onerb.timerlol.ui.main.MainViewModel;

import java.util.ArrayList;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private static final int RECORD_AUDIO_REQUEST_CODE = 1;

    private static final int TOP = 2;
    private static final int JUNGLE = 3;
    private static final int MID = 4;
    private static final int ADC = 5;
    private static final int SUPPORT = 6;

    private static final int FLASH = 7;
    private static final int IGNITE = 8;
    private static final int HEAL = 9;
    private static final int GHOST = 10;
    private static final int BARRIER = 11;
    private static final int EXHAUST = 12;
    private static final int TELEPORT = 13;
    private static final int CLEANSE = 14;

    private static final int FLASH_TIME = 300;
    private static final int IGNITE_TIME = 180;
    private static final int HEAL_TIME = 240;
    private static final int GHOST_TIME = 210;
    private static final int BARRIER_TIME = 180;
    private static final int EXHAUST_TIME = 210;
    private static final int TELEPORT_TIME = 360;
    private static final int CLEANSE_TIME = 210;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //put the commands glued
    //FLASH
    private String commandsTopFlash = "tf topflash ";
    private String commandsJungleFlash = "jf jungleflash djangoflash ";
    private String commandsMidFlash = "mf midflash ";
    private String commandsAdcFlash = "af adcflash bf botflash";
    private String commandsSupportFlash = "sf supportflash ";

    //HEAL
    private String commandsTopHeal = "th topheal ";
    private String commandsJungleHeal = "jh jungleheal djangoheal ";
    private String commandsMidHeal = "mh midheal ";
    private String commandsAdcHeal = "ah adcheal bh botheal";
    private String commandsSupportHeal = "sh supportheal ";

    //IGNITE
    private String commandsTopIgnite = "ti topignite ";
    private String commandsJungleIgnite = "ji jungleignite djangoignite ";
    private String commandsMidIgnite = "mi midignite ";
    private String commandsAdcIgnite = "ai adcignite bf botignite";
    private String commandsSupportIgnite = "si supportignite ";

    //BARRIER
    private String commandsTopBarrier = "tb topbarrier ";
    private String commandsJungleBarrier = "jb junglebarrier djangobarrier ";
    private String commandsMidBarrier = "mb midbarrier ";
    private String commandsAdcBarrier = "ab adcbarrier bb botbarrier";
    private String commandsSupportBarrier = "sb supportbarrier ";

    //GHOST
    private String commandsTopGhost = "tg topghost ";
    private String commandsJungleGhost = "jg jungleghost djangoghost ";
    private String commandsMidGhost = "mg midghost ";
    private String commandsAdcGhost = "ag adcghost bg botghost";
    private String commandsSupportGhost = "sg supportghost ";

    //EXHAUST
    private String commandsTopExhaust = "te topexhaust ";
    private String commandsJungleExhaust = "je jungleexhaust djangoexhaust ";
    private String commandsMidExhaust = "me midexhaust ";
    private String commandsAdcExhaust = "ae adcexhaust be botexhaust";
    private String commandsSupportExhaust = "se supportexhaust ";

    //TELEPORT
    private String commandsTopTeleport = "tt topteleport ";
    private String commandsJungleTeleport = "jt jungleteleport djangoteleport ";
    private String commandsMidTeleport = "mt midteleport ";
    private String commandsAdcTeleport = "at adcteleport bt botteleport";
    private String commandsSupportTeleport = "st supportteleport ";

    //CLEANSE
    private String commandsTopCleanse = "tf topcleanse ";
    private String commandsJungleCleanse = "jf junglecleanse djangocleanse ";
    private String commandsMidCleanse = "mf midcleanse ";
    private String commandsAdcCleanse = "af adccleanse bf botcleanse";
    private String commandsSupportCleanse = "sf supportcleanse ";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean topBoots = false;
    private boolean jungleBoots = false;
    private boolean midBoots = false;
    private boolean adcBoots = false;
    private boolean supportBoots = false;

    private int topHaste = 0;
    private int jungleHaste = 0;
    private int midHaste = 0;
    private int adcHaste = 0;
    private int supportHaste = 0;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private TextView textCommand, tipText;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private Intent intent;
    private CountDownTimer timer2;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();

//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_timer);
        System.out.println("TimerActivity.onCreate teste " + ("tfg".contains("tf")));

        container = findViewById(R.id.timersContainer);


        textCommand = findViewById(R.id.textTimer);
        tipText = findViewById(R.id.tipText);

        tipText.setText(getResources().getString(R.string.tipStart) + " \"Mid Flash \". \n " + "\n" + getResources().getString(R.string.tipEnd) + "\n" + "\n" + getResources().getString(R.string.tipCustomizeCommands));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        findViewById(R.id.btnPermission).setOnTouchListener((view, motionEvent) -> {
            if (getApplicationContext() != null) {

                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                getApplicationContext().startActivity(i);
            }
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Uri uri = Uri.fromParts("package", getPackageName(), null);
//            intent.setData(uri);
//            startActivity(intent);
            return false;
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);

                }
            }
        });


        StartListening();

    }

    public MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }


    private void StartListening() {

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
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
                if (i == 7 || i == 8) {
                    if (speechRecognizer != null)
                        speechRecognizer.destroy();
                    speechRecognizer = null;
                    StartListening();
                }


            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> c = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String command = c.get(0);
                tipText.setVisibility(View.GONE);

                checkCommand(command);

                if (command != null) {// set max length text command in screen
                    String[] commandMax = command.split(" ");
                    String text = "";
                    for (int i = 0; i < 3; i++) {
                        if (i < commandMax.length)
                            text += (" " + commandMax[i]);
                    }
                    textCommand.setText("Command: " + text);
                }
                if (speechRecognizer != null)
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
        speechRecognizer.startListening(intent);
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        }


    }

    private void updateTextTime(long time, TextView textView) {
        long minutes = (time / 1000) / 60;
        long seconds = (time / 1000) % 60;
//        int seconds= (int) (time/1000);
        String formatedMinutes = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textView.setText(formatedMinutes);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (speechRecognizer != null)
            speechRecognizer.destroy();
        speechRecognizer = null;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    public void createTimer(int lane, int spell, View containerTimer, String speakLane, String speakSpell) {
        TextView textView = containerTimer.findViewById(R.id.textTimer);
        CardView btnBoots = containerTimer.findViewById(R.id.btnBoots);
        CountDownTimer timer;
        final double[] haste = {0};
        final double[] hasteBoots = {12};
        final double[] hasteRune = {0};
        final CountDownTimer[] timerSpeaker = new CountDownTimer[1];
        final long[] time = new long[1];
        final long[] originalTime = new long[1];

        if (lane == TOP) {
            haste[0] = topHaste;
        } else if (lane == JUNGLE) {
            haste[0] = jungleHaste;
        } else if (lane == MID) {
            haste[0] = midHaste;
        } else if (lane == ADC) {
            haste[0] = adcHaste;
        } else if (lane == SUPPORT) {
            haste[0] = supportHaste;
        }
        if (haste[0] > 0)
            hasteRune[0] = 18;

        if (spell == FLASH) {
            time[0] = FLASH_TIME;
        } else if (spell == HEAL) {
            time[0] = HEAL_TIME;
        } else if (spell == GHOST) {
            time[0] = GHOST_TIME;
        } else if (spell == BARRIER) {
            time[0] = BARRIER_TIME;
        } else if (spell == EXHAUST) {
            time[0] = EXHAUST_TIME;
        } else if (spell == TELEPORT) {
            time[0] = TELEPORT_TIME;
        } else if (spell == CLEANSE) {
            time[0] = CLEANSE_TIME;
        } else if (spell == IGNITE) {
            time[0] = IGNITE_TIME;
        }
        originalTime[0] = time[0] * 1000;
        time[0] = time[0] * 1000;
        time[0] -= originalTime[0] * (haste[0] / (haste[0] + 100));
        long diferencaInicial = originalTime[0] - time[0];
        long diferencaBoots = (long) (diferencaInicial+ (originalTime[0] * (hasteRune[0] / (hasteRune[0] + 100))) - originalTime[0] * ((hasteBoots[0]+hasteRune[0]) / ((hasteBoots[0]+hasteRune[0]) + 100)));
        if(diferencaBoots<0)
            diferencaBoots=-diferencaBoots;
        long finalDiferencaBoots = diferencaBoots;
        timer = new CountDownTimer(time[0], 1000) {
            boolean finishing = false;//to not have an onFinish() loop
            ImageView iconBoots = containerTimer.findViewById(R.id.iconBoots);
            boolean change = false;

            @Override
            public void onTick(long l) {
                if (lane == TOP) {
                    if (topBoots && !change) {
                        haste[0] += 12;
                        time[0] = (long) (time[0] + diferencaInicial - originalTime[0] * (haste[0] / (haste[0] + 100)));
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots));
                        change = true;

                    }
                    if (!topBoots && change) {

                        time[0] = (long) (time[0] + finalDiferencaBoots);
                        haste[0] -= 12;
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        change = false;

                    }
                } else if (lane == JUNGLE) {
                    if (jungleBoots && !change) {
                        haste[0] += 12;
                        time[0] = (long) (time[0] + diferencaInicial - originalTime[0] * (haste[0] / (haste[0] + 100)));
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots));
                        change = true;
                    }
                    if (!jungleBoots && change) {

                        time[0] = (long) (time[0] + originalTime[0] * (hasteBoots[0] / (hasteBoots[0] + 100)));
                        haste[0] -= 12;
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        change = false;

                    }
                } else if (lane == MID) {
                    if (midBoots && !change) {
                        haste[0] += 12;
                        time[0] = (long) (time[0] + diferencaInicial - originalTime[0] * (haste[0] / (haste[0] + 100)));
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots));
                        change = true;
                    }
                    if (!midBoots && change) {

                        time[0] = (long) (time[0] + finalDiferencaBoots);
                        haste[0] -= 12;
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        change = false;

                    }
                } else if (lane == ADC) {
                    if (adcBoots && !change) {
                        haste[0] += 12;
                        time[0] = (long) (time[0] + diferencaInicial - originalTime[0] * (haste[0] / (haste[0] + 100)));
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots));
                        change = true;
                    }
                    if (!adcBoots && change) {

                        time[0] = (long) (time[0] + originalTime[0] * (hasteBoots[0] / (hasteBoots[0] + 100)));
                        haste[0] -= 12;
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        change = false;

                    }
                } else if (lane == SUPPORT) {
                    if (supportBoots && !change) {
                        haste[0] += 12;
                        time[0] = (long) (time[0] + diferencaInicial - originalTime[0] * (haste[0] / (haste[0] + 100)));
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots));
                        change = true;
                    }
                    if (!supportBoots && change) {

                        time[0] = (long) (time[0] + originalTime[0] * (hasteBoots[0] / (hasteBoots[0] + 100)));
                        haste[0] -= 12;
                        iconBoots.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        change = false;

                    }
                }

                if (time[0] <= 0 && !finishing) {
                    finishing = true;
                    onFinish();
                } else if (time[0] > 0)
                    time[0] -= 1000;
                updateTextTime(time[0], textView);
            }

            @Override
            public void onFinish() {
                if (speechRecognizer != null)
                    speechRecognizer.destroy();
                speechRecognizer = null;
                String speak = speakLane + " " + speakSpell + " return. ";
                speak += speak;
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(1000);
                }
                final boolean[] secondVibrate = {true};
                textToSpeech.speak(speak, TextToSpeech.QUEUE_FLUSH, null, null);

                timerSpeaker[0] = new CountDownTimer(3000, 300) {

                    @Override
                    public void onTick(long l) {
                        if (textView.getAlpha() > 0)
                            textView.animate().alpha(0).setDuration(250);
                        else
                            textView.animate().alpha(1).setDuration(250);
                        if (l <= 1500 && secondVibrate[0]) {
                            secondVibrate[0] = false;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                v.vibrate(1000);
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        containerTimer.animate().alpha(0).setDuration(500);

                        StartListening();
                        container.removeView(containerTimer);
                    }
                }.start();

            }
        }.start();
    }

    public boolean verifyCommands(String command, String commands) {
        boolean in = false;
        String[] laneCommands = commands.split(" ");
        for (String c :
                laneCommands) {
            if (c.equals(command))
                in = true;
        }
        return in;
    }

    public void checkCommand(String command) {
        int lane = 0;
        int spell = 0;
        char inicialLetter1 = ' ', inicialLetter2 = ' ', inicialLetter3 = ' ';
        String word1 = "", word2 = "", word3 = "";
        String[] separateCommand = command.toLowerCase().split(" ");


        if (separateCommand.length >= 1) {
            inicialLetter1 = separateCommand[0].charAt(0);
            word1 = separateCommand[0];
        }
        if (separateCommand.length >= 2) {
            inicialLetter2 = separateCommand[1].charAt(0);
            word2 = separateCommand[1];
        }
        if (separateCommand.length >= 3) {
            inicialLetter3 = separateCommand[2].charAt(0);
            word3 = separateCommand[2];
        }
//        System.out.println("TimerActivity.checkCommand letra 1 "+ inicialLetter1+ " letra2 "+ inicialLetter2+" letra3"+inicialLetter3 );

        command = command.toLowerCase();
        command = command.replace(" ", "");

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
         * If the command is in the command list or if it has the
         * initial letters 'm' and 'f' and has "mid" or "flash" as a word,
         * this is for it not to activate with "maria fernanda" for example
         * and that's why I will not detect lane and spell separately
         * */
        //Flash
        if (verifyCommands(command, commandsTopFlash) || ((inicialLetter1 == 't' && inicialLetter2 == 'f') && (word1.equals("top") || word2.equals("flash")))) {
            lane = TOP;
            spell = FLASH;
        } else if (verifyCommands(command, commandsJungleFlash) || ((inicialLetter1 == 'j' && inicialLetter2 == 'f') && (word1.equals("jungle") || word2.equals("flash")))) {
            lane = JUNGLE;
            spell = FLASH;
        } else if (verifyCommands(command, commandsMidFlash) || ((inicialLetter1 == 'm' && inicialLetter2 == 'f') && (word1.equals("mid") || word2.equals("flash")))) {
            lane = MID;
            spell = FLASH;
        } else if (verifyCommands(command, commandsAdcFlash) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'f') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("flash")))) {
            lane = ADC;
            spell = FLASH;
        } else if (verifyCommands(command, commandsSupportFlash) || ((inicialLetter1 == 's' && inicialLetter2 == 'f') && (word1 == "support" || word2.equals("flash")))) {
            lane = SUPPORT;
            spell = FLASH;
        }

        //Heal
        else if (verifyCommands(command, commandsTopHeal) || ((inicialLetter1 == 't' && inicialLetter2 == 'h') && (word1.equals("top") || word2.equals("heal")))) {
            lane = TOP;
            spell = HEAL;
        } else if (verifyCommands(command, commandsJungleHeal) || ((inicialLetter1 == 'j' && inicialLetter2 == 'h') && (word1.equals("jungle") || word2.equals("heal")))) {
            lane = JUNGLE;
            spell = HEAL;
        } else if (verifyCommands(command, commandsMidHeal) || ((inicialLetter1 == 'm' && inicialLetter2 == 'h') && (word1.equals("mid") || word2.equals("heal")))) {
            lane = MID;
            spell = HEAL;
        } else if (verifyCommands(command, commandsAdcHeal) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'h') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("heal")))) {
            lane = ADC;
            spell = HEAL;
        } else if (verifyCommands(command, commandsSupportHeal) || ((inicialLetter1 == 's' && inicialLetter2 == 'h') && (word1 == "support" || word2.equals("heal")))) {
            lane = SUPPORT;
            spell = HEAL;
        }

        //Ignite
        else if (verifyCommands(command, commandsTopIgnite) || ((inicialLetter1 == 't' && inicialLetter2 == 'i') && (word1.equals("top") || word2.equals("ignite")))) {
            lane = TOP;
            spell = IGNITE;
        } else if (verifyCommands(command, commandsJungleIgnite) || ((inicialLetter1 == 'j' && inicialLetter2 == 'i') && (word1.equals("jungle") || word2.equals("ignite")))) {
            lane = JUNGLE;
            spell = IGNITE;
        } else if (verifyCommands(command, commandsMidIgnite) || ((inicialLetter1 == 'm' && inicialLetter2 == 'i') && (word1.equals("mid") || word2.equals("ignite")))) {
            lane = MID;
            spell = IGNITE;
        } else if (verifyCommands(command, commandsAdcIgnite) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'i') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("ignite")))) {
            lane = ADC;
            spell = IGNITE;
        } else if (verifyCommands(command, commandsSupportIgnite) || ((inicialLetter1 == 's' && inicialLetter2 == 'i') && (word1 == "support" || word2.equals("ignite")))) {
            lane = SUPPORT;
            spell = IGNITE;
        }

        //Ghost
        else if (verifyCommands(command, commandsTopGhost) || ((inicialLetter1 == 't' && inicialLetter2 == 'g') && (word1.equals("top") || word2.equals("ghost")))) {
            lane = TOP;
            spell = GHOST;
        } else if (verifyCommands(command, commandsJungleGhost) || ((inicialLetter1 == 'j' && inicialLetter2 == 'g') && (word1.equals("jungle") || word2.equals("ghost")))) {
            lane = JUNGLE;
            spell = GHOST;
        } else if (verifyCommands(command, commandsMidGhost) || ((inicialLetter1 == 'm' && inicialLetter2 == 'g') && (word1.equals("mid") || word2.equals("ghost")))) {
            lane = MID;
            spell = GHOST;
        } else if (verifyCommands(command, commandsAdcGhost) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'g') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("ghost")))) {
            lane = ADC;
            spell = GHOST;
        } else if (verifyCommands(command, commandsSupportGhost) || ((inicialLetter1 == 's' && inicialLetter2 == 'g') && (word1 == "support" || word2.equals("ghost")))) {
            lane = SUPPORT;
            spell = GHOST;
        }

        //Teleport
        else if (verifyCommands(command, commandsTopTeleport) || ((inicialLetter1 == 't' && inicialLetter2 == 't') && (word1.equals("top") || word2.equals("teleport")))) {
            lane = TOP;
            spell = TELEPORT;
        } else if (verifyCommands(command, commandsJungleTeleport) || ((inicialLetter1 == 'j' && inicialLetter2 == 't') && (word1.equals("jungle") || word2.equals("teleport")))) {
            lane = JUNGLE;
            spell = TELEPORT;
        } else if (verifyCommands(command, commandsMidTeleport) || ((inicialLetter1 == 'm' && inicialLetter2 == 't') && (word1.equals("mid") || word2.equals("teleport")))) {
            lane = MID;
            spell = TELEPORT;
        } else if (verifyCommands(command, commandsAdcTeleport) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 't') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("teleport")))) {
            lane = ADC;
            spell = TELEPORT;
        } else if (verifyCommands(command, commandsSupportTeleport) || ((inicialLetter1 == 's' && inicialLetter2 == 't') && (word1 == "support" || word2.equals("teleport")))) {
            lane = SUPPORT;
            spell = TELEPORT;
        }

        //Barrier
        else if (verifyCommands(command, commandsTopBarrier) || ((inicialLetter1 == 't' && inicialLetter2 == 'b') && (word1.equals("top") || word2.equals("barrier")))) {
            lane = TOP;
            spell = BARRIER;
        } else if (verifyCommands(command, commandsJungleBarrier) || ((inicialLetter1 == 'j' && inicialLetter2 == 'b') && (word1.equals("jungle") || word2.equals("barrier")))) {
            lane = JUNGLE;
            spell = BARRIER;
        } else if (verifyCommands(command, commandsMidBarrier) || ((inicialLetter1 == 'm' && inicialLetter2 == 'b') && (word1.equals("mid") || word2.equals("barrier")))) {
            lane = MID;
            spell = BARRIER;
        } else if (verifyCommands(command, commandsAdcBarrier) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'b') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("barrier")))) {
            lane = ADC;
            spell = BARRIER;
        } else if (verifyCommands(command, commandsSupportBarrier) || ((inicialLetter1 == 's' && inicialLetter2 == 'b') && (word1 == "support" || word2.equals("barrier")))) {
            lane = SUPPORT;
            spell = BARRIER;
        }

        //Exhaust
        else if (verifyCommands(command, commandsTopExhaust) || ((inicialLetter1 == 't' && inicialLetter2 == 'e') && (word1.equals("top") || word2.equals("exhaust")))) {
            lane = TOP;
            spell = EXHAUST;
        } else if (verifyCommands(command, commandsJungleExhaust) || ((inicialLetter1 == 'j' && inicialLetter2 == 'e') && (word1.equals("jungle") || word2.equals("exhaust")))) {
            lane = JUNGLE;
            spell = EXHAUST;
        } else if (verifyCommands(command, commandsMidExhaust) || ((inicialLetter1 == 'm' && inicialLetter2 == 'e') && (word1.equals("mid") || word2.equals("exhaust")))) {
            lane = MID;
            spell = EXHAUST;
        } else if (verifyCommands(command, commandsAdcExhaust) || (((inicialLetter1 == 'a' || inicialLetter1 == 'e') && inicialLetter2 == 'e') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("exhaust")))) {
            lane = ADC;
            spell = EXHAUST;
        } else if (verifyCommands(command, commandsSupportExhaust) || ((inicialLetter1 == 's' && inicialLetter2 == 'e') && (word1 == "support" || word2.equals("exhaust")))) {
            lane = SUPPORT;
            spell = EXHAUST;
        }

        //Cleanse
        else if (verifyCommands(command, commandsTopCleanse) || ((inicialLetter1 == 't' && inicialLetter2 == 'c') && (word1.equals("top") || word2.equals("cleanse")))) {
            lane = TOP;
            spell = CLEANSE;
        } else if (verifyCommands(command, commandsJungleCleanse) || ((inicialLetter1 == 'j' && inicialLetter2 == 'c') && (word1.equals("jungle") || word2.equals("cleanse")))) {
            lane = JUNGLE;
            spell = CLEANSE;
        } else if (verifyCommands(command, commandsMidCleanse) || ((inicialLetter1 == 'm' && inicialLetter2 == 'c') && (word1.equals("mid") || word2.equals("cleanse")))) {
            lane = MID;
            spell = CLEANSE;
        } else if (verifyCommands(command, commandsAdcCleanse) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'c') && ((word1.equals("adc") || word1.equals("bot")) || word2.equals("cleanse")))) {
            lane = ADC;
            spell = CLEANSE;
        } else if (verifyCommands(command, commandsSupportCleanse) || ((inicialLetter1 == 's' && inicialLetter2 == 'c') && (word1 == "support" || word2.equals("cleanse")))) {
            lane = SUPPORT;
            spell = CLEANSE;
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (lane != 0 && spell != 0) {
            View containerTimer;
            CardView btnRune, btnBoots;
            String speakLane = "", speakSpell = "";
            containerTimer = getLayoutInflater().inflate(R.layout.view_timer, container, false);
            btnBoots = containerTimer.findViewById(R.id.btnBoots);


            int finalLane = lane;


            btnBoots.setOnTouchListener((view, motionEvent) -> {
                CountDownTimer btnTimer;
                System.out.println("TimerActivity.checkCommand clicou");


                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (finalLane == TOP) {
                        topBoots = !topBoots;
                    } else if (finalLane == JUNGLE) {
                        jungleBoots = !jungleBoots;
                    } else if (finalLane == MID) {
                        midBoots = !midBoots;
                    } else if (finalLane == ADC) {
                        adcBoots = !adcBoots;
                    }
                    if (finalLane == SUPPORT) {
                        supportBoots = !supportBoots;
                    }

                }
                return false;
            });
            ImageView laneIcon = containerTimer.findViewById(R.id.iconLane);
            ImageView spellIcon = containerTimer.findViewById(R.id.iconSpell);
            if (lane == TOP) {
                laneIcon.setImageDrawable(getDrawable(R.drawable.top));
                speakLane = "Top";
            } else if (lane == JUNGLE) {
                laneIcon.setImageDrawable(getDrawable(R.drawable.jungle));
                speakLane = "Jungle";
            } else if (lane == MID) {
                laneIcon.setImageDrawable(getDrawable(R.drawable.mid));
                speakLane = "Mid";
            } else if (lane == ADC) {
                laneIcon.setImageDrawable(getDrawable(R.drawable.adc));
                speakLane = "Adc";
            }
            if (lane == SUPPORT) {
                laneIcon.setImageDrawable(getDrawable(R.drawable.support));
                speakLane = "Support";
            }

            if (spell == FLASH) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.flash));
                speakSpell = "Flash";
            } else if (spell == HEAL) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.heal));
                speakSpell = "Heal";
            } else if (spell == GHOST) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.ghost));
                speakSpell = "Ghost";
            } else if (spell == BARRIER) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.barrier));
                speakSpell = "Barrier";
            } else if (spell == EXHAUST) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.exhaust));
                speakSpell = "Exhaust";
            } else if (spell == TELEPORT) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.teleport));
                speakSpell = "Teleport";
            } else if (spell == CLEANSE) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.cleanse));
                speakSpell = "Cleanse";
            } else if (spell == IGNITE) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.ignite));
                speakSpell = "Ignite";
            }
            createTimer(lane, spell, containerTimer, speakLane, speakSpell);
            container.addView(containerTimer);
        }
    }

    public void mute() {
        try {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE,
                    AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        } catch (IllegalArgumentException e) {

        }
    }


    public void unmute() {
        try {
            AudioManager audioManager =
                    (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE,
                    AudioManager.FLAG_VIBRATE);
        } catch (IllegalArgumentException e) {

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayout lp = findViewById(R.id.layoutPermission);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            lp.setVisibility(View.VISIBLE);
        } else
            lp.setVisibility(View.GONE);

        mute();
        StartListening();

//        System.out.println("TimerActivity.onResume timer aqui");
    }


    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("TimerActivity.onStop stop timer aqui");
        if (speechRecognizer != null)
            speechRecognizer.destroy();
        speechRecognizer = null;
        unmute();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (speechRecognizer != null)
            speechRecognizer.destroy();
        unmute();

    }
    //    public void timerStart(TimerActivity timerActivity) {
//        time = 1000;
//        timer = new CountDownTimer(time, time) {
//            @Override
//            public void onTick(long l) {
//                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(timerActivity);
//                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
////        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
////        srIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString());
//                speechRecognizer.setRecognitionListener(new RecognitionListener() {
//                    @Override
//                    public void onReadyForSpeech(Bundle bundle) {
//
//                    }
//
//                    @Override
//                    public void onBeginningOfSpeech() {
//
//                    }
//
//                    @Override
//                    public void onRmsChanged(float v) {
//
//                    }
//
//                    @Override
//                    public void onBufferReceived(byte[] bytes) {
//
//                    }
//
//                    @Override
//                    public void onEndOfSpeech() {
//                    }
//
//                    @Override
//                    public void onError(int i) {
////                System.out.println("TimerActivity.onError erro "+i);
//                        if (i == 7 || i == 8) {
//                            System.out.println("TimerActivity.onError on timer error " + i);
//                            if (speechRecognizer != null)
//                                speechRecognizer.destroy();
//                            speechRecognizer = null;
////                         StartListening();
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onResults(Bundle bundle) {
//                        ArrayList<String> command = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                        String result = command.get(0);
//                        String[] commandCheck = result.split(" ");
//                        System.out.println("TimerActivity.onResults comand " + result);
//                        if (commandCheck.length >= 3) {
//
////                    if (command != null)
////                        editText.setText(command.get(0));
//
//
//// o textToSpeak faz o delay entre comandos seguidos aumentar.
////                    textToSpeech.speak("Flash Mid Return", TextToSpeech.QUEUE_FLUSH, null, null);
//
//
//                        }
//                        if (command != null)
//                            editText.setText(command.get(0));
//                        if (speechRecognizer != null)
//                            speechRecognizer.destroy();
//                        speechRecognizer = null;
////                     StartListening();
//
//
////                for (int i = 0; i < Locale.getAvailableLocales().length; i++) {
////                    System.out.println("TimerActivity.onResults "+Locale.getAvailableLocales()[i]);
////                }
//
//                    }
//
//                    @Override
//                    public void onPartialResults(Bundle bundle) {
//
//                    }
//
//                    @Override
//                    public void onEvent(int i, Bundle bundle) {
//
//                    }
//
//                });
//                speechRecognizer.startListening(intent);
//            }
//
//            @Override
//            public void onFinish() {
//                timerStart(timerActivity);
//            }
//        }.start();
//    }

}