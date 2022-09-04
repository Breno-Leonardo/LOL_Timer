package com.onerb.timerlol;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onerb.timerlol.api.SummonerInfos;
import com.onerb.timerlol.ui.main.MainViewModel;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private static final int RECORD_AUDIO_REQUEST_CODE = 0;

    public static final int TOP = 2;
    public static final int JUNGLE = 3;
    public static final int MID = 4;
    public static final int ADC = 5;
    public static final int SUPPORT = 6;

    public static final int FLASH = 7;
    public static final int IGNITE = 8;
    public static final int HEAL = 9;
    public static final int GHOST = 10;
    public static final int BARRIER = 11;
    public static final int EXHAUST = 12;
    public static final int TELEPORT = 13;
    public static final int CLEANSE = 14;
    public static final int BOOTS = 15;
    public static final int ZHONYAS = 16;
    public static final int RUNE = 17;

    private static final int KINDRED = 1;
    private static final int ANIVIA = 18;
    private static final int ANIVIA_TIME = 240;
    private static final int NORMAL_TIMER = 19;


    private static final int FLASH_TIME = 300;
    private static final int IGNITE_TIME = 180;
    private static final int HEAL_TIME = 240;
    private static final int GHOST_TIME = 210;
    private static final int BARRIER_TIME = 180;
    private static final int EXHAUST_TIME = 210;
    private static final int TELEPORT_TIME = 360;
    private static final int CLEANSE_TIME = 210;
    private static final int ZHONYAS_TIME = 120;

    private static final int RUNE_HASTE = 18;
    private static final int BOOT_HASTE = 12;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //put the commands glued
    //FLASH
    private String commandsTopFlash = " topflash topfashion  flashtop topfashion";
    private String commandsJungleFlash = " jungleflash djangoflash junglefashion jogodoflash jogodeflash  flashjungle flashdjango fashionjungle ";
    private String commandsMidFlash = " midflash mediaflash midiflash midfashion miniflash minifashion  flashmid flashmedia flashmidi fashionmid flashmini fashionmini ";
    private String commandsAdcFlash = " adcflash  botflash abcflash adcfashion botfashion abcfashion  flashadc  flashbot flashabc fashionadc fashionbot fashionabc   ";
    private String commandsSupportFlash = " supportflash suporteflash supportfashion suportefashion supflash supfashion  flashsupport flashsuporte fashionsupport fashionsuporte flashsup fashionsup";

    //HEAL
    private String commandsTopHeal = " topheal topcurar toprio ht healtop curartop riotop  topheal";
    private String commandsJungleHeal = " jungleheal djangoheal junglecurar junglerio djangorio hj healjungle healdjango curarjungle riojungle riodjango";
    private String commandsMidHeal = " midheal midcurar midrio mediaheal mediacurar mediario midiheal midicurar midirio minicurar minirio miniheal  healmid curarmid riomid healmidi curarmedia riomedia healmidi curarmidi riomidi curarmini riomini healmini";
    private String commandsAdcHeal = "ah adcheal bh botheal adcrio botrio adccurar botcurar abcrio abccurar abcheal  healadc  healbot rioadc riobot curaradc curarbot rioabc curarabc healabc";
    private String commandsSupportHeal = " supportheal suprio supcurar suporterio suportecurar suporteheal  healsupport riosup curarsup riosuporte curarsuporte healsuporte ";

    //IGNITE
    private String commandsTopIgnite = " topignite  ignitetop";
    private String commandsJungleIgnite = " jungleignite djangoignite  ignitejungle ignitedjango  ";
    private String commandsMidIgnite = " midignite mediaignite modnight midnight miniiginite mininight  ignitemid ignitemedia nightmod nightmid ignitemini nightmini ";
    private String commandsAdcIgnite = " adcignite  botignite abcignite  igniteadc  ignitebot igniteabc ";
    private String commandsSupportIgnite = " supportignite  suporteignite supignite  ignitesupport ignitesuporte ignitesup ";

    //BARRIER
    private String commandsTopBarrier = " topbarrier topbarreira  barreiratop barriertop ";
    private String commandsJungleBarrier = " junglebarrier djangobarrier junglebarriera djangobarriera  barrierjungle barrierdjango barreirajungle barreiradjango ";
    private String commandsMidBarrier = " midbarrier mediabarrier midibarrier minibarrier midbarriera mediabarriera midibarriera minibarreira  barriermid barriermedia barriermidi barriermini barreiramid barreiramini barreiramedia barreiramidi barreiramini ";
    private String commandsAdcBarrier = " adcbarrier  botbarrier adcbarriera  botbarriera abcbarrier abcbarreira   barrieradc bb barrierbot barreiraadc ";
    private String commandsSupportBarrier = " supportbarrier suportebarrier suportebarreira supbarrier supbarreira suportebehringer suportbarreira   barriersupport barriersuporte barreirasupport barriersup barreirasup behringersuporte barreirasuporte";

    //GHOST
    private String commandsTopGhost = " topghost topfantasma  ghosttop fantasmatop ";
    private String commandsJungleGhost = " jungleghost djangoghost junglefantasma  ghostjungle ghostdjango fantasmajungle";
    private String commandsMidGhost = " midghost midfantasma mediaghost mediafantasma midighost midifantasma minighost minifantasma  ghostmid fantasmamid ghostmedia fantasmamedia ghostmidi fantasmamid ghostmini fantasmamini";
    private String commandsAdcGhost = " adcghost  botghost adcfantasma botfantasma abcghost abcfantasma  ghostadc  ghostbot fantasmaadc fantasmabot ghostabc fantasmaabc";
    private String commandsSupportGhost = " supportghost suportfantasma suporteghost suportefantasma supghost supfantasma";

    //EXHAUST
    private String commandsTopExhaust = " topexhaust topexausto   exhausttop exaustotop";
    private String commandsJungleExhaust = " jungleexhaust djangoexhaust jungleexausto djangoexausto  exhaustjungle exhaustdjango exaustojungle exaustodjango ";
    private String commandsMidExhaust = " midexhaust midexausto mediaexausto mediaexhaust midiexausto  midiexhaust miniexhaust miniexausto  exhaustmid exaustomid exuastomedia  mediaexhaust exaustomidi exhaustmini exhaustmidi";
    private String commandsAdcExhaust = " adcexhaust  botexhaust adcexausto botexausto abcexausto abcexaust";
    private String commandsSupportExhaust = " supportexhaust suporteexhaust suporteexausto supportexausto supexaust supexausto  exhaustsupport exhaustsuporte exaustosuport exaustosuporte exhaustsup exaustosup";

    //TELEPORT
    private String commandsTopTeleport = " topteleport topteleporte teleporttop teleportetop";
    private String commandsJungleTeleport = " jungleteleport djangoteleport jungleteleporte djangoteleporte  teleportjungle teleportedjango teleportejungle teleportedjango";
    private String commandsMidTeleport = "  midteleport mediateleport miditeleport miniteleport miniteleporte meteleporte";
    private String commandsAdcTeleport = " adcteleport  botteleport abcteleport abcteleporte adcteleporte botteleporte abcteleport  teleportadc  teleportbot teleportabc teleporteabc teleportebot";
    private String commandsSupportTeleport = " supportteleport  suporteteleporte supteleport supteleporte  teleportsupport teleportesuporte teleportsup teleportesup";

    //CLEANSE
    private String commandsTopCleanse = " topcleanse toppurificar topklinse  cleansetop purificartop klinsetop ";
    private String commandsJungleCleanse = " junglecleanse djangocleanse junglepurificar  djangopurificar jungleklinse  cleansejungle cleansedjango purificarjungle purificardjango klinsejungle ";
    private String commandsMidCleanse = " midcleanse midpurificar mediacleanse mediapurificar mediaklinse midklinse midicleanse midipurificar mediacleanse mediapurificar mediaklinse midiklinse midlince miniprince minicleanse minipurificar miniklinse  cleansemid purificarmid cleansemedia purificarmedia klinsemedia klinsemid cleansemidi purificarmidi cleansemedia purificarmedia klinsemedia klinsemid  lincemid princemini cleansemini purificarmini klinsemini ";
    private String commandsAdcCleanse = "= adccleanse  botcleanse botpurificar botklinse adcklinse abccleanse adcpurificar abcpurificar abcklinse  cleanseadc  cleansebot purificarbot klinsebot klinseadc cleanseabc purificaradc purificarabc klinseabc ";
    private String commandsSupportCleanse = "= supportcleanse suportepurificar suporteklinse suportecleanse suportpurificar suportklinse  cleansesupport purificarsuporte klinsesuporte cleansesuporte purificarsuport klinsesuport";

    //Kindred Mark
    private String commandsKindred = "k30 k45 kindred45 kindred30 ca30 ca45 ";

    //Anivia Passive
    private String commandsAnivia = "aniviapassive aniviaegg aniviaovo ";

    //BOOTS
    private String commandsTopBoots = "bootstop bottop boottop bootstrap footstop butstop multitop topboots topbot topboot   ";
    private String commandsJungleBoots = "bootsjungle botjungle bootjungle  bootsdjango botdjango bootdjango butsjungle butsdjango bootjango bootsjango botjango multijungle multidjango jungleboots junglebot jungleboot djangoboots djangobot djangoboot junglebuts djangobuts jangoboot jangobot jangoboots ";
    private String commandsMidBoots = "bootsmid botmid bootmid  bootsmedia botmedia bootmedia bootmidi botmidi bootsmidi botmini bootsmini bootmini multimid midboots midboot midbot  mediaboot mediabots mediabots  minibot miniboots miniboot ";
    private String commandsAdcBoots = "bootsbot botbot bootbot bootsadc botadc bootadc ba botabc bootabc bootsabc butibuti multibot botboots botbot botboot adcboots adcbot adcboot abcboot abcbot abcboots ";
    private String commandsSupportBoots = "botsuport botsuporte bootssuport bootssuporte botsup bootsup bootssup bootsuporte bootsuporte multisuporte multisuport suportbot suportboots suportboot suportebot suporteboot suporteboots supbot supboot supboots";
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //EXTRA LANGUAGES
    // new method separate commands
    // ingles en 0, portugues pt 1, alemao de 2, espanhol es 3, frances fr 4, italiano it 5, polones pl 6, grego el 7, romeno ro 8, hungaro hu 9, tcheco cs 10
    // ,japones ja 11,russo ru 12, turco tr 13, koreano ko 14, chines zh 15, comandos extras para melhorar deteccao
    public static String laneTopExtra = "top topo OBERE SUPERIOR HAUT SUPERIORE GÓRNA Πάνω SUS FELSŐ HORNÍ トップ ВЕРХНЯЯ ÜST 상단 上路";
    public static String laneJungleExtra = "jungle caçador DSCHUNGEL JUNGLA JUNGLE GIUNGLA DŻUNGLA Ζούγκλα JUNGLĂ DZSUNGEL DŽUNGLE ジャングル ЛЕС ORMANCI 정글 打野 django";
    public static String laneMidExtra = "mid meio MITTLERE CENTRAL MILIEU CENTRALE ŚRODKOWA Μεσαία MIJLOC KÖZÉPSŐ STŘEDOVÁ ミッド СРЕДНЯЯ ORTA 중단 中路 midi";
    public static String laneAdcExtra = "bot atirador UNTERE INFERIOR BAS INFERIORE DOLNA Κάτω JOS ALSÓ SPODNÍ ボット НИЖНЯЯ ALT 하단 远程物理输出 adc";
    public static String laneSupportExtra = "support suporte SUPPORTER APOYO SUPPORT SUPPORTO WSPARCIE Υποστηρικτής SUPORT TÁMOGATÓ PODPORA サポート ПОДДЕРЖКА DESTEK 서포터 辅助 sup super";

    public static String flashExtra = "FLASH FLASH Blitz Destello Sautéclair Flash Błysk Φλας Flash Átvillanás Skok フラッシュ Скачок Sıçra 점멸 閃現";
    public static String igniteExtra = "IGNITE INCENDIAR Entzünden Prender Embrasement Ustione Podpalenie Ανάφλεξη Igniție Égetés Vznítit イグナイト Воспламенение Tutuştur 점화 點燃";
    public static String exhaustExtra = "EXHAUST EXAUSTÃO Erschöpfen Extenuación Fatigue Sfinimento Wyczerpanie Εξάντληση Epuizare Kifárasztás Vyčerpat イグゾースト Изнурение Bitkinlik 탈진 虛弱 exausto";
    public static String healExtra = "HEAL CURAR Heilen Curar Soins Guarigione Uzdrowienie Θεραπεία Vindecare Gyógyítás Vyléčit ヒール Исцеление Şifa 회복 治癒";
    public static String teleportExtra = "TELEPORT TELEPORTE Teleportation Teleportar Téléportation Teletrasporto Teleportacja Τηλεμεταφορά Teleportare Teleport Teleport テレポート Телепорт Işınlan 순간이동 傳送 TP";
    public static String barrierExtra = "BARRIER BARREIRA Barriere Barrera Barrière Barriera Bariera Φράγμα Barieră Pajzs Bariéra バリア Барьер Bariyer 방어막 光盾";
    public static String ghostExtra = "GHOST FANTASMA Geist Fantasmal Fantôme Spettralità Duch Φάντασμα Fantomă Szellem Duch ゴースト Призрак Hayalet 유체화 鬼步";
    public static String cleanseExtra = "CLEANSE PURIFICAR Läuterung Limpiar Purge Purificazione Oczyszczenie Εξαγνισμός Purificare Megtisztulás Očista クレンズ Очищение Arındır 정화 淨化 klinse";
    public static String bootExtra = "BOOT BOTA Stiefel Bota Botte Avvio Uruchomić Μπότα Boot Csomagtartó Bota ブート Ботинок Bot 신병 引导 引導";
    public static String bootsExtra = "BOOTS BOTAS Stiefel Botas Bottes Stivali Buty Μπότες Cizme Csizma Boty ブーツ Сапоги botayakkabı 부츠 靴子 bots";
    public static String zhonyasExtra = "ZH Zzz Zho Zhon Zy Zhonyas Z Z Z Z Z Z Z Z Z Z ZZ";
    public static String runeExtra = "rune Runa Rune runa rune runa runa γράμματουρουνικούαλφάβητου rune rúna runa ルーン руна rune 룬 符文";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean topBoots = false;
    private boolean jungleBoots = false;
    private boolean midBoots = false;
    private boolean adcBoots = false;
    private boolean supportBoots = false;

    private boolean topRune = false;
    private boolean jungleRune = false;
    private boolean midRune = false;
    private boolean adcRune = false;
    private boolean supportRune = false;

    private int topHaste = 0;
    private int jungleHaste = 0;
    private int midHaste = 0;
    private int adcHaste = 0;
    private int supportHaste = 0;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private TextView textCommand, tipText;
    private ScrollView scrollView;
    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private Intent intent;
    private CountDownTimer timer2;
    private LinearLayout container, containerInfos;
    private CardView currentTouch;//to know which timer is playing
    private HashMap<CardView, CountDownTimer> timers = new HashMap<>();//to cancel the timer by dragging to the right
    private SummonerInfos.Participants[] participantsInfos = null;
    private SharedPreferences sharedPref;
    private int apiRespCode = -1;
    private String language;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();

//        MatchApiUtil matchApiUtil = new MatchApiUtil(getViewModel(), "the kindred ", InfosGameApiUtil.BRAZIL_ROUTE);
//
//
//        try {
//            matchApiUtil.execute().get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        participantsInfos = matchApiUtil.getParticipantsInfos();
//        System.out.println("TimerActivity.onCreate participantsInfos" + participantsInfos);
//        apiRespCode = matchApiUtil.getRespCode();
//        System.out.println("TimerActivity.onCreate coderesposta " + apiRespCode);
//        if (participantsInfos != null) {
//
//        }else if(apiRespCode == 200 && participantsInfos==null ){
//            System.out.println("Game not begin");
//        }
//        else if (apiRespCode == 404) {
//            System.out.println("TimerActivity.onCreate summoner not found");
//        }


//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_timer);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adViewTimerActivity);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        sharedPref = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        // add commands
        addCustomCommands();

        container = findViewById(R.id.timersContainer);
        containerInfos = findViewById(R.id.containerInfos);
        textCommand = findViewById(R.id.textTimer);
        tipText = findViewById(R.id.tipText);
        tipText.setText(getResources().getString(R.string.tipStart) + " Lane + Spell, " + getResources().getString(R.string.like_for_example) + " \"Mid Flash \". \n " + "\n" + getResources().getString(R.string.tipEnd) + "\n" + "\n" + getResources().getString(R.string.tipCustomizeCommands));
        scrollView = findViewById(R.id.scrollViewActivityTimer);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = event.getX();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    if (currentTouch != null) {
                        layoutContainerTimer = currentTouch.findViewById(R.id.layoutContainerTimer);
                        currentTouch.setTranslationX(currentTouch.getTranslationX() + (event.getX() - lastX));
                        if (currentTouch.getTranslationX() >= currentTouch.getWidth() * 0.4f)
                            layoutContainerTimer.setBackgroundColor(Color.RED);
                        else
                            layoutContainerTimer.setBackgroundColor(Color.WHITE);
                    }

                    lastX = event.getX();
                } else if (action == MotionEvent.ACTION_UP) {
                    if (currentTouch != null) {
                        if (currentTouch.getTranslationX() >= currentTouch.getWidth() * 0.4f) {
                            container.removeView(currentTouch);
                            if (timers.get(currentTouch) != null)
                                timers.get(currentTouch).cancel();
                            timers.remove(currentTouch);
                        }
                        currentTouch.setTranslationX(0);
                        currentTouch = null;
                    }
                }
                return false;
            }
        });
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

        new CountDownTimer(1000, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                StartListening();

            }
        };

    }

    public MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    private void StartListening() {
        mute();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        language = sharedPref.getString("language", Locale.getDefault().toString());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", language);

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
//                command = removerAcentos(command);
                checkCommand(command);

                if (command != null) {// set max length text command in screen
                    String[] commandMax = command.split(" ");
                    String text = "";
                    for (int i = 0; i < 3; i++) {
                        if (i < commandMax.length)
                            text += (" " + commandMax[i]);
                    }
                    textCommand.setText(getString(R.string.command) + ": " + text);
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

    public void createTimer(int lane, int spell, CardView containerTimer, String speakLane, String speakSpell) {
        TextView textView = containerTimer.findViewById(R.id.textTimer);
        ImageView iconRune = containerTimer.findViewById(R.id.iconRune);
        ImageView iconBoot = containerTimer.findViewById(R.id.iconBoots);
        CountDownTimer timer;
        final double[] haste = {0};
        final CountDownTimer[] timerSpeaker = new CountDownTimer[2];
        final long[] time = new long[1];
        final long[] originalTime = new long[1];
        final long[] tempoDecorrido = new long[1];
        tempoDecorrido[0] = 0;

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
        if (haste[0] >= 18)
            iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight));
        else
            iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight_disable));


        if (spell == FLASH) {
            originalTime[0] = FLASH_TIME;
        } else if (spell == HEAL) {
            originalTime[0] = HEAL_TIME;
        } else if (spell == GHOST) {
            originalTime[0] = GHOST_TIME;
        } else if (spell == BARRIER) {
            originalTime[0] = BARRIER_TIME;
        } else if (spell == EXHAUST) {
            originalTime[0] = EXHAUST_TIME;
        } else if (spell == TELEPORT) {
            originalTime[0] = TELEPORT_TIME;
        } else if (spell == CLEANSE) {
            originalTime[0] = CLEANSE_TIME;
        } else if (spell == IGNITE) {
            originalTime[0] = IGNITE_TIME;
        } else if (spell == ZHONYAS) {
            originalTime[0] = ZHONYAS_TIME;
        }

        if (lane == KINDRED || lane == NORMAL_TIMER) {
            originalTime[0] = -spell;
        } else if (lane == ANIVIA) {
            originalTime[0] = ANIVIA_TIME;
        }

        time[0] = originalTime[0] * 1000;

        timer = new CountDownTimer(time[0], 1000) {

            boolean finishing = false;//to not have an onFinish() loop
            boolean changeBoot = false;
            boolean changeRune = false;

            @Override
            public void onTick(long l) {
                if (lane == TOP) {
                    if (changeBoot && !topBoots) {
                        if ((topHaste == BOOT_HASTE || topHaste == (RUNE_HASTE + BOOT_HASTE)))
                            topHaste -= BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        changeBoot = false;

                    } else if (!changeBoot && topBoots) {
                        if (topHaste != BOOT_HASTE && topHaste != (RUNE_HASTE + BOOT_HASTE))
                            topHaste += BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots));
                        changeBoot = true;
                    }


                } else if (lane == JUNGLE ) {
                    if (changeBoot && !jungleBoots) {
                        if ((jungleHaste == BOOT_HASTE || jungleHaste == (RUNE_HASTE + BOOT_HASTE)))
                            jungleHaste -= BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        changeBoot = false;

                    } else if (!changeBoot && jungleBoots) {
                        if (jungleHaste != BOOT_HASTE && jungleHaste != (RUNE_HASTE + BOOT_HASTE))
                            jungleHaste += BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots));
                        changeBoot = true;
                    }

                } else if (lane == MID ) {
                    if (changeBoot && !midBoots) {
                        if ((midHaste == BOOT_HASTE || midHaste == (RUNE_HASTE + BOOT_HASTE)))
                            midHaste -= BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        changeBoot = false;

                    } else if (!changeBoot && midBoots) {
                        if (midHaste != BOOT_HASTE && midHaste != (RUNE_HASTE + BOOT_HASTE))
                            midHaste += BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots));
                        changeBoot = true;
                    }

                } else if (lane == ADC ) {
                    if (changeBoot && !adcBoots) {
                        if ((adcHaste == BOOT_HASTE || adcHaste == (RUNE_HASTE + BOOT_HASTE)))
                            adcHaste -= BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        changeBoot = false;

                    } else if (!changeBoot && adcBoots) {
                        if (adcHaste != BOOT_HASTE && adcHaste != (RUNE_HASTE + BOOT_HASTE))
                            adcHaste += BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots));
                        changeBoot = true;
                    }


                } else if (lane == SUPPORT ) {
                    if (changeBoot && !supportBoots) {
                        if ((supportHaste == BOOT_HASTE || supportHaste == (RUNE_HASTE + BOOT_HASTE)))
                            supportHaste -= BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots_disable));
                        changeBoot = false;

                    } else if (!changeBoot && supportBoots) {
                        if (supportHaste != BOOT_HASTE && supportHaste != (RUNE_HASTE + BOOT_HASTE))
                            supportHaste += BOOT_HASTE;
                        iconBoot.setImageDrawable(getDrawable(R.drawable.boots));
                        changeBoot = true;
                    }


                }

                if (lane == TOP ) {

                    if (changeRune && !topRune) {
                        if ((topHaste == RUNE_HASTE || topHaste == (RUNE_HASTE + BOOT_HASTE)))
                            topHaste -= RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight_disable));
                        changeRune = false;

                    } else if (!changeRune && topRune) {
                        if (topHaste != RUNE_HASTE && topHaste != (RUNE_HASTE + BOOT_HASTE))
                            topHaste += RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight));
                        changeRune = true;
                    }

                } else if (lane == JUNGLE ) {
                    if (changeRune && !jungleRune) {
                        if ((jungleHaste == RUNE_HASTE || jungleHaste == (RUNE_HASTE + BOOT_HASTE)))
                            jungleHaste -= RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight_disable));
                        changeRune = false;

                    } else if (!changeRune && jungleRune) {
                        if (jungleHaste != RUNE_HASTE && jungleHaste != (RUNE_HASTE + BOOT_HASTE))
                            jungleHaste += RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight));
                        changeRune = true;
                    }



                } else if (lane == MID ) {
                    if (changeRune && !midRune) {
                        if ((midHaste == RUNE_HASTE || midHaste == (RUNE_HASTE + BOOT_HASTE)))
                            midHaste -= RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight_disable));
                        changeRune = false;

                    } else if (!changeRune && midRune) {
                        if (midHaste != RUNE_HASTE && midHaste != (RUNE_HASTE + BOOT_HASTE))
                            midHaste += RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight));
                        changeRune = true;
                    }


                } else if (lane == ADC ) {
                    if (changeRune && !adcRune) {
                        if ((adcHaste == RUNE_HASTE || adcHaste == (RUNE_HASTE + BOOT_HASTE)))
                            adcHaste -= RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight_disable));
                        changeRune = false;

                    } else if (!changeRune && adcRune) {
                        if (adcHaste != RUNE_HASTE && adcHaste != (RUNE_HASTE + BOOT_HASTE))
                            adcHaste += RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight));
                        changeRune = true;
                    }


                } else if (lane == SUPPORT ) {
                    if (changeRune && !supportRune) {
                        if ((supportHaste == RUNE_HASTE || supportHaste == (RUNE_HASTE + BOOT_HASTE)))
                            supportHaste -= RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight_disable));
                        changeRune = false;

                    } else if (!changeRune && supportRune) {
                        if (supportHaste != RUNE_HASTE && supportHaste != (RUNE_HASTE + BOOT_HASTE))
                            supportHaste += RUNE_HASTE;
                        iconRune.setImageDrawable(getDrawable(R.drawable.cosmic_insight));
                        changeRune = true;
                    }


                }


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
                TextView textTi = containerTimer.findViewById(R.id.textTimer);
                if (textTi == null) {
                    cancel();
                    System.out.println("TimerActivity.onTick cancel timer");
                }
                if (lane != KINDRED && lane != NORMAL_TIMER && lane != ANIVIA && lane != ZHONYAS) {
                    double cdr = haste[0] / (haste[0] + 100) * 100;
                    double cdrEmTempo = (originalTime[0] * 1000 * cdr / 100);
//                    System.out.println("TimerActivity.onTick cdr"+cdr+" cdremtempo"+cdrEmTempo);
//                    System.out.println("TimerActivity.onTick entrou aq cdr:"+cdr);
                    time[0] = (long) (originalTime[0] * 1000 - cdrEmTempo - tempoDecorrido[0]);
                }
                if (lane == ZHONYAS) {
                    if (haste[0] == RUNE_HASTE || haste[0] == (RUNE_HASTE + BOOT_HASTE)) {
                        double cdr = 10 / (10 + 100) * 100;
                        double cdrEmTempo = (originalTime[0] * 1000 * cdr / 100);
                        time[0] = (long) (originalTime[0] * 1000 - cdrEmTempo - tempoDecorrido[0]);
                    }

                }


                if (time[0] <= 0 && !finishing) {
                    onFinish();
                    finishing = true;
                    cancel();
                } else if (time[0] > 0) {
                    time[0] -= 1000;
                    tempoDecorrido[0] += 1000;
                }
                updateTextTime(time[0], textView);
            }

            @Override
            public void onFinish() {
                if (speechRecognizer != null)
                    speechRecognizer.destroy();
                speechRecognizer = null;
                TextView txtReturnSpell = findViewById(R.id.spellReturn);
                String speak = speakLane + " " + speakSpell + " return ";

                txtReturnSpell.setText(speak);
                txtReturnSpell.setVisibility(View.VISIBLE);

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (sharedPref.getBoolean("vibrateFinish", true)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(1000);
                    }
                }
                final boolean[] secondVibrate = {true};
                final TextView[] txtHistory = {null};
                if (lane != NORMAL_TIMER) {
                    textToSpeech.speak(speak + speak, TextToSpeech.QUEUE_FLUSH, null, null);

                } else if (lane == NORMAL_TIMER) {
                    textToSpeech.speak("Timer Is Over. Timer Is Over", TextToSpeech.QUEUE_FLUSH, null, null);
                    txtReturnSpell.setText("Timer Is Over");

                }


                timerSpeaker[0] = new CountDownTimer(4000, 300) {
                    @Override
                    public void onTick(long l) {
                        if (textView.getAlpha() > 0) {
                            textView.animate().alpha(0).setDuration(250);
                            txtReturnSpell.animate().alpha(0).setDuration(250);
                        } else {
                            textView.animate().alpha(1).setDuration(250);
                            txtReturnSpell.animate().alpha(1).setDuration(250);
                        }
                        if (l <= 1500 && secondVibrate[0]) {
                            secondVibrate[0] = false;
                            if (sharedPref.getBoolean("vibrateFinish", true)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                                } else {
                                    v.vibrate(1000);
                                }
                            }
                        }
                    }


                    @Override
                    public void onFinish() {
                        containerTimer.animate().alpha(0).setDuration(500);
                        StartListening();
                        txtReturnSpell.setVisibility(View.GONE);
                        txtHistory[0] = new TextView(getApplicationContext());
                        txtHistory[0].setText(speak);
                        txtHistory[0].setTextSize(20);
                        txtHistory[0].setGravity(Gravity.CENTER);
                        txtHistory[0].setTypeface(txtHistory[0].getTypeface(), Typeface.BOLD);
                        txtHistory[0].setVisibility(View.VISIBLE);
                        txtHistory[0].setTextColor(Color.BLACK);
                        containerInfos.addView(txtHistory[0]);

                        timerSpeaker[1] = new CountDownTimer(11000, 1000) {


                            @Override
                            public void onTick(long l) {
                                if (l < 1100)
                                    txtHistory[0].animate().alpha(0).setDuration(1000);
                            }


                            @Override
                            public void onFinish() {
                                if (txtHistory[0] != null) {

                                    containerInfos.removeView(txtHistory[0]);
                                    System.out.println("TimerActivity.onFinish removi");
                                }

                            }
                        }.start();
                        container.removeView(containerTimer);
                        cancel();

                    }
                }.start();
                TextView finalTxtHistory = txtHistory[0];

                timers.remove(currentTouch);

            }
        }.start();
        timers.put(containerTimer, timer);
    }

    public boolean verifyCommands(String command, String commands) {

        boolean in = false;
        if (commands != null && command != "") {

            String[] laneCommands = commands.split(" ");
            for (String c :
                    laneCommands) {
                if (c.equalsIgnoreCase(command))
                    in = true;
            }
        }
        return in;
    }

    public void checkCommand(String command) {
        int lane = 0;
        int spell = 0;
        char inicialLetter1 = ' ', inicialLetter2 = ' ', inicialLetter3 = ' ';
        String word1 = "", word2 = "", word3 = "";
        String[] separateCommand = command.toLowerCase().split(" ");

        System.out.println("TimerActivity.checkCommand command " + command);
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
//        int laneSpell[] = verifyCustomCommands(command);
//        lane = laneSpell[0];
//        spell = laneSpell[1];
//        if (spell == 15) {
//            if (lane == 2) {
//
//                topBoots = !topBoots;
//                return;
//            } else if (lane == 3) {
//
//                jungleBoots = !jungleBoots;
//                return;
//
//            } else if (lane == 4) {
//
//                midBoots = !midBoots;
//                return;
//
//            } else if (lane == 5) {
//
//                adcBoots = !adcBoots;
//                return;
//
//            } else if (lane == 6) {
//
//                supportBoots = !supportBoots;
//                return;
//
//            }
//        }
        if (lane == 0 || spell == 0) {
            //Boots

            if (verifyCommands(command, commandsTopBoots)) {

                topBoots = !topBoots;
                return;
            } else if (verifyCommands(command, commandsJungleBoots)) {

                jungleBoots = !jungleBoots;
                return;

            } else if (verifyCommands(command, commandsMidBoots)) {

                midBoots = !midBoots;
                return;

            } else if (verifyCommands(command, commandsAdcBoots)) {

                adcBoots = !adcBoots;
                return;

            } else if (verifyCommands(command, commandsSupportBoots)) {

                supportBoots = !supportBoots;
                return;

            }
            //Flash
            else if (verifyCommands(command, commandsTopFlash) || ((inicialLetter1 == 't' && inicialLetter2 == 'f') && (word1.equalsIgnoreCase("top") || word2.equalsIgnoreCase("flash")))) {
                lane = TOP;
                spell = FLASH;
            } else if (verifyCommands(command, commandsJungleFlash) || ((inicialLetter1 == 'j' && inicialLetter2 == 'f') && (word1.equalsIgnoreCase("jungle") || word2.equalsIgnoreCase("flash")))) {
                lane = JUNGLE;
                spell = FLASH;
            } else if (verifyCommands(command, commandsMidFlash) || ((inicialLetter1 == 'm' && inicialLetter2 == 'f') && (word1.equalsIgnoreCase("mid") || word2.equalsIgnoreCase("flash")))) {
                lane = MID;
                spell = FLASH;
            } else if (verifyCommands(command, commandsAdcFlash) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'f') && ((word1.equalsIgnoreCase("adc") || word1.equalsIgnoreCase("bot")) || word2.equalsIgnoreCase("flash")))) {
                lane = ADC;
                spell = FLASH;
            } else if (verifyCommands(command, commandsSupportFlash) || ((inicialLetter1 == 's' && inicialLetter2 == 'f') && (word1 == "support" || word2.equalsIgnoreCase("flash")))) {
                lane = SUPPORT;
                spell = FLASH;
            }

            //Heal
            else if (verifyCommands(command, commandsTopHeal) || ((inicialLetter1 == 't' && inicialLetter2 == 'h') && (word1.equalsIgnoreCase("top") || word2.equalsIgnoreCase("heal")))) {
                lane = TOP;
                spell = HEAL;
            } else if (verifyCommands(command, commandsJungleHeal) || ((inicialLetter1 == 'j' && inicialLetter2 == 'h') && (word1.equalsIgnoreCase("jungle") || word2.equalsIgnoreCase("heal")))) {
                lane = JUNGLE;
                spell = HEAL;
            } else if (verifyCommands(command, commandsMidHeal) || ((inicialLetter1 == 'm' && inicialLetter2 == 'h') && (word1.equalsIgnoreCase("mid") || word2.equalsIgnoreCase("heal")))) {
                lane = MID;
                spell = HEAL;
            } else if (verifyCommands(command, commandsAdcHeal) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'h') && ((word1.equalsIgnoreCase("adc") || word1.equalsIgnoreCase("bot")) || word2.equalsIgnoreCase("heal")))) {
                lane = ADC;
                spell = HEAL;
            } else if (verifyCommands(command, commandsSupportHeal) || ((inicialLetter1 == 's' && inicialLetter2 == 'h') && (word1 == "support" || word2.equalsIgnoreCase("heal")))) {
                lane = SUPPORT;
                spell = HEAL;
            }

            //Ignite
            else if (verifyCommands(command, commandsTopIgnite) || ((inicialLetter1 == 't' && inicialLetter2 == 'i') && (word1.equalsIgnoreCase("top") || word2.equalsIgnoreCase("ignite")))) {
                lane = TOP;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsJungleIgnite) || ((inicialLetter1 == 'j' && inicialLetter2 == 'i') && (word1.equalsIgnoreCase("jungle") || word2.equalsIgnoreCase("ignite")))) {
                lane = JUNGLE;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsMidIgnite) || ((inicialLetter1 == 'm' && inicialLetter2 == 'i') && (word1.equalsIgnoreCase("mid") || word2.equalsIgnoreCase("ignite")))) {
                lane = MID;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsAdcIgnite) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'i') && ((word1.equalsIgnoreCase("adc") || word1.equalsIgnoreCase("bot")) || word2.equalsIgnoreCase("ignite")))) {
                lane = ADC;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsSupportIgnite) || ((inicialLetter1 == 's' && inicialLetter2 == 'i') && (word1 == "support" || word2.equalsIgnoreCase("ignite")))) {
                lane = SUPPORT;
                spell = IGNITE;
            }

            //Ghost
            else if (verifyCommands(command, commandsTopGhost) || ((inicialLetter1 == 't' && inicialLetter2 == 'g') && (word1.equalsIgnoreCase("top") || word2.equalsIgnoreCase("ghost")))) {
                lane = TOP;
                spell = GHOST;
            } else if (verifyCommands(command, commandsJungleGhost) || ((inicialLetter1 == 'j' && inicialLetter2 == 'g') && (word1.equalsIgnoreCase("jungle") || word2.equalsIgnoreCase("ghost")))) {
                lane = JUNGLE;
                spell = GHOST;
            } else if (verifyCommands(command, commandsMidGhost) || ((inicialLetter1 == 'm' && inicialLetter2 == 'g') && (word1.equalsIgnoreCase("mid") || word2.equalsIgnoreCase("ghost")))) {
                lane = MID;
                spell = GHOST;
            } else if (verifyCommands(command, commandsAdcGhost) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'g') && ((word1.equalsIgnoreCase("adc") || word1.equalsIgnoreCase("bot")) || word2.equalsIgnoreCase("ghost")))) {
                lane = ADC;
                spell = GHOST;
            } else if (verifyCommands(command, commandsSupportGhost) || ((inicialLetter1 == 's' && inicialLetter2 == 'g') && (word1 == "support" || word2.equalsIgnoreCase("ghost")))) {
                lane = SUPPORT;
                spell = GHOST;
            }

            //Teleport
            else if (verifyCommands(command, commandsTopTeleport) || ((inicialLetter1 == 't' && inicialLetter2 == 't') && (word1.equalsIgnoreCase("top") || word2.equalsIgnoreCase("teleport")))) {
                lane = TOP;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsJungleTeleport) || ((inicialLetter1 == 'j' && inicialLetter2 == 't') && (word1.equalsIgnoreCase("jungle") || word2.equalsIgnoreCase("teleport")))) {
                lane = JUNGLE;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsMidTeleport) || ((inicialLetter1 == 'm' && inicialLetter2 == 't') && (word1.equalsIgnoreCase("mid") || word2.equalsIgnoreCase("teleport")))) {
                lane = MID;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsAdcTeleport) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 't') && ((word1.equalsIgnoreCase("adc") || word1.equalsIgnoreCase("bot")) || word2.equalsIgnoreCase("teleport")))) {
                lane = ADC;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsSupportTeleport) || ((inicialLetter1 == 's' && inicialLetter2 == 't') && (word1 == "support" || word2.equalsIgnoreCase("teleport")))) {
                lane = SUPPORT;
                spell = TELEPORT;
            }

            //Barrier
            else if (verifyCommands(command, commandsTopBarrier)) {
                lane = TOP;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsJungleBarrier)) {
                lane = JUNGLE;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsMidBarrier)) {
                lane = MID;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsAdcBarrier)) {
                lane = ADC;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsSupportBarrier)) {
                lane = SUPPORT;
                spell = BARRIER;
            }

            //Exhaust
            else if (verifyCommands(command, commandsTopExhaust) || ((inicialLetter1 == 't' && inicialLetter2 == 'e') && (word1.equalsIgnoreCase("top") || word2.equalsIgnoreCase("exhaust")))) {
                lane = TOP;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsJungleExhaust) || ((inicialLetter1 == 'j' && inicialLetter2 == 'e') && (word1.equalsIgnoreCase("jungle") || word2.equalsIgnoreCase("exhaust")))) {
                lane = JUNGLE;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsMidExhaust) || ((inicialLetter1 == 'm' && inicialLetter2 == 'e') && (word1.equalsIgnoreCase("mid") || word2.equalsIgnoreCase("exhaust")))) {
                lane = MID;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsAdcExhaust) || (((inicialLetter1 == 'a' || inicialLetter1 == 'e') && inicialLetter2 == 'e') && ((word1.equalsIgnoreCase("adc") || word1.equalsIgnoreCase("bot")) || word2.equalsIgnoreCase("exhaust")))) {
                lane = ADC;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsSupportExhaust) || ((inicialLetter1 == 's' && inicialLetter2 == 'e') && (word1 == "support" || word2.equalsIgnoreCase("exhaust")))) {
                lane = SUPPORT;
                spell = EXHAUST;
            }

            //Cleanse
            else if (verifyCommands(command, commandsTopCleanse) || ((inicialLetter1 == 't' && inicialLetter2 == 'c') && (word1.equalsIgnoreCase("top") || word2.equalsIgnoreCase("cleanse")))) {
                lane = TOP;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsJungleCleanse) || ((inicialLetter1 == 'j' && inicialLetter2 == 'c') && (word1.equalsIgnoreCase("jungle") || word2.equalsIgnoreCase("cleanse")))) {
                lane = JUNGLE;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsMidCleanse) || ((inicialLetter1 == 'm' && inicialLetter2 == 'c') && (word1.equalsIgnoreCase("mid") || word2.equalsIgnoreCase("cleanse")))) {
                lane = MID;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsAdcCleanse) || (((inicialLetter1 == 'a' || inicialLetter1 == 'b') && inicialLetter2 == 'c') && ((word1.equalsIgnoreCase("adc") || word1.equalsIgnoreCase("bot")) || word2.equalsIgnoreCase("cleanse")))) {
                lane = ADC;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsSupportCleanse) || ((inicialLetter1 == 's' && inicialLetter2 == 'c') && (word1 == "support" || word2.equalsIgnoreCase("cleanse")))) {
                lane = SUPPORT;
                spell = CLEANSE;
            }

            // REVERSE ORDER
            //Flash
            if (verifyCommands(command, commandsTopFlash) || ((inicialLetter2 == 't' && inicialLetter1 == 'f') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("flash")))) {
                lane = TOP;
                spell = FLASH;
            } else if (verifyCommands(command, commandsJungleFlash) || ((inicialLetter2 == 'j' && inicialLetter1 == 'f') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("flash")))) {
                lane = JUNGLE;
                spell = FLASH;
            } else if (verifyCommands(command, commandsMidFlash) || ((inicialLetter2 == 'm' && inicialLetter1 == 'f') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("flash")))) {
                lane = MID;
                spell = FLASH;
            } else if (verifyCommands(command, commandsAdcFlash) || (((inicialLetter2 == 'a' || inicialLetter2 == 'b') && inicialLetter1 == 'f') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("flash")))) {
                lane = ADC;
                spell = FLASH;
            } else if (verifyCommands(command, commandsSupportFlash) || ((inicialLetter2 == 's' && inicialLetter1 == 'f') && (word2 == "support" || word1.equalsIgnoreCase("flash")))) {
                lane = SUPPORT;
                spell = FLASH;
            }

//Heal
            else if (verifyCommands(command, commandsTopHeal) || ((inicialLetter2 == 't' && inicialLetter1 == 'h') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("heal")))) {
                lane = TOP;
                spell = HEAL;
            } else if (verifyCommands(command, commandsJungleHeal) || ((inicialLetter2 == 'j' && inicialLetter1 == 'h') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("heal")))) {
                lane = JUNGLE;
                spell = HEAL;
            } else if (verifyCommands(command, commandsMidHeal) || ((inicialLetter2 == 'm' && inicialLetter1 == 'h') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("heal")))) {
                lane = MID;
                spell = HEAL;
            } else if (verifyCommands(command, commandsAdcHeal) || (((inicialLetter2 == 'a' || inicialLetter2 == 'b') && inicialLetter1 == 'h') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("heal")))) {
                lane = ADC;
                spell = HEAL;
            } else if (verifyCommands(command, commandsSupportHeal) || ((inicialLetter2 == 's' && inicialLetter1 == 'h') && (word2 == "support" || word1.equalsIgnoreCase("heal")))) {
                lane = SUPPORT;
                spell = HEAL;
            }

//Ignite
            else if (verifyCommands(command, commandsTopIgnite) || ((inicialLetter2 == 't' && inicialLetter1 == 'i') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("ignite")))) {
                lane = TOP;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsJungleIgnite) || ((inicialLetter2 == 'j' && inicialLetter1 == 'i') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("ignite")))) {
                lane = JUNGLE;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsMidIgnite) || ((inicialLetter2 == 'm' && inicialLetter1 == 'i') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("ignite")))) {
                lane = MID;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsAdcIgnite) || (((inicialLetter2 == 'a' || inicialLetter2 == 'b') && inicialLetter1 == 'i') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("ignite")))) {
                lane = ADC;
                spell = IGNITE;
            } else if (verifyCommands(command, commandsSupportIgnite) || ((inicialLetter2 == 's' && inicialLetter1 == 'i') && (word2 == "support" || word1.equalsIgnoreCase("ignite")))) {
                lane = SUPPORT;
                spell = IGNITE;
            }

//Ghost
            else if (verifyCommands(command, commandsTopGhost) || ((inicialLetter2 == 't' && inicialLetter1 == 'g') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("ghost")))) {
                lane = TOP;
                spell = GHOST;
            } else if (verifyCommands(command, commandsJungleGhost) || ((inicialLetter2 == 'j' && inicialLetter1 == 'g') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("ghost")))) {
                lane = JUNGLE;
                spell = GHOST;
            } else if (verifyCommands(command, commandsMidGhost) || ((inicialLetter2 == 'm' && inicialLetter1 == 'g') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("ghost")))) {
                lane = MID;
                spell = GHOST;
            } else if (verifyCommands(command, commandsAdcGhost) || (((inicialLetter2 == 'a' || inicialLetter2 == 'b') && inicialLetter1 == 'g') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("ghost")))) {
                lane = ADC;
                spell = GHOST;
            } else if (verifyCommands(command, commandsSupportGhost) || ((inicialLetter2 == 's' && inicialLetter1 == 'g') && (word2 == "support" || word1.equalsIgnoreCase("ghost")))) {
                lane = SUPPORT;
                spell = GHOST;
            }

//Teleport
            else if (verifyCommands(command, commandsTopTeleport) || ((inicialLetter2 == 't' && inicialLetter1 == 't') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("teleport")))) {
                lane = TOP;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsJungleTeleport) || ((inicialLetter2 == 'j' && inicialLetter1 == 't') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("teleport")))) {
                lane = JUNGLE;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsMidTeleport) || ((inicialLetter2 == 'm' && inicialLetter1 == 't') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("teleport")))) {
                lane = MID;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsAdcTeleport) || (((inicialLetter2 == 'a' || inicialLetter2 == 'b') && inicialLetter1 == 't') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("teleport")))) {
                lane = ADC;
                spell = TELEPORT;
            } else if (verifyCommands(command, commandsSupportTeleport) || ((inicialLetter2 == 's' && inicialLetter1 == 't') && (word2 == "support" || word1.equalsIgnoreCase("teleport")))) {
                lane = SUPPORT;
                spell = TELEPORT;
            }

//Barrier
            else if (verifyCommands(command, commandsTopBarrier) || ((inicialLetter2 == 't' && inicialLetter1 == 'b') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("barrier")))) {
                lane = TOP;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsJungleBarrier) || ((inicialLetter2 == 'j' && inicialLetter1 == 'b') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("barrier")))) {
                lane = JUNGLE;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsMidBarrier) || ((inicialLetter2 == 'm' && inicialLetter1 == 'b') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("barrier")))) {
                lane = MID;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsAdcBarrier) || (((inicialLetter2 == 'a' || inicialLetter2 == 'b') && inicialLetter1 == 'b') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("barrier")))) {
                lane = ADC;
                spell = BARRIER;
            } else if (verifyCommands(command, commandsSupportBarrier) || ((inicialLetter2 == 's' && inicialLetter1 == 'b') && (word2 == "support" || word1.equalsIgnoreCase("barrier")))) {
                lane = SUPPORT;
                spell = BARRIER;
            }

//Exhaust
            else if (verifyCommands(command, commandsTopExhaust) || ((inicialLetter2 == 't' && inicialLetter1 == 'e') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("exhaust")))) {
                lane = TOP;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsJungleExhaust) || ((inicialLetter2 == 'j' && inicialLetter1 == 'e') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("exhaust")))) {
                lane = JUNGLE;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsMidExhaust) || ((inicialLetter2 == 'm' && inicialLetter1 == 'e') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("exhaust")))) {
                lane = MID;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsAdcExhaust) || (((inicialLetter2 == 'a' || inicialLetter2 == 'e') && inicialLetter1 == 'e') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("exhaust")))) {
                lane = ADC;
                spell = EXHAUST;
            } else if (verifyCommands(command, commandsSupportExhaust) || ((inicialLetter2 == 's' && inicialLetter1 == 'e') && (word2 == "support" || word1.equalsIgnoreCase("exhaust")))) {
                lane = SUPPORT;
                spell = EXHAUST;
            }

//Cleanse
            else if (verifyCommands(command, commandsTopCleanse) || ((inicialLetter2 == 't' && inicialLetter1 == 'c') && (word2.equalsIgnoreCase("top") || word1.equalsIgnoreCase("cleanse")))) {
                lane = TOP;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsJungleCleanse) || ((inicialLetter2 == 'j' && inicialLetter1 == 'c') && (word2.equalsIgnoreCase("jungle") || word1.equalsIgnoreCase("cleanse")))) {
                lane = JUNGLE;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsMidCleanse) || ((inicialLetter2 == 'm' && inicialLetter1 == 'c') && (word2.equalsIgnoreCase("mid") || word1.equalsIgnoreCase("cleanse")))) {
                lane = MID;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsAdcCleanse) || (((inicialLetter2 == 'a' || inicialLetter2 == 'b') && inicialLetter1 == 'c') && ((word2.equalsIgnoreCase("adc") || word2.equalsIgnoreCase("bot")) || word1.equalsIgnoreCase("cleanse")))) {
                lane = ADC;
                spell = CLEANSE;
            } else if (verifyCommands(command, commandsSupportCleanse) || ((inicialLetter2 == 's' && inicialLetter1 == 'c') && (word2 == "support" || word1.equalsIgnoreCase("cleanse")))) {
                lane = SUPPORT;
                spell = CLEANSE;
            }

        }


        //Kindred Mark
        String timeMark = command.replace("k", "");
        if (verifyCommands(command, commandsKindred) || (inicialLetter1 == 'k' && timeMark.matches("[+-]?\\d*(\\.\\d+)?"))) {
            lane = KINDRED;
            spell = -(Integer.parseInt(timeMark));

        }
        //Anivia Egg
        else if (verifyCommands(command, commandsAnivia) || (word1.equalsIgnoreCase("anivia") || word2.equalsIgnoreCase("passive"))) {
            lane = ANIVIA;
            spell = ANIVIA_TIME;

        }

        //Timer
        if (command.length() > 0) {
            if (command.substring(command.length() - 1).equalsIgnoreCase("s")) {
                String normalTimer = command.replace("s", "");
                if (normalTimer.matches("[+-]?\\d*(\\.\\d+)?") && normalTimer != "") {
                    lane = NORMAL_TIMER;
                    spell = -(Integer.parseInt(normalTimer));
                }
            }
        } else if ((word2.equalsIgnoreCase("segundos") || word2.equalsIgnoreCase("seconds"))) {
            if (word1.matches("[+-]?\\d*(\\.\\d+)?")) {
                lane = NORMAL_TIMER;
                spell = -(Integer.parseInt(word1));
            }


        }

        //extra languages
        if (verifyCommands(word1, laneTopExtra) || verifyCommands(word2, laneTopExtra))
            lane = TOP;
        else if (verifyCommands(word1, laneJungleExtra) || verifyCommands(word2, laneJungleExtra))
            lane = JUNGLE;
        else if (verifyCommands(word1, laneMidExtra) || verifyCommands(word2, laneMidExtra))
            lane = MID;
        else if (verifyCommands(word1, laneAdcExtra) || verifyCommands(word2, laneAdcExtra))
            lane = ADC;
        else if (verifyCommands(word1, laneSupportExtra) || verifyCommands(word2, laneSupportExtra))
            lane = SUPPORT;

        if (verifyCommands(word1, flashExtra) || verifyCommands(word2, flashExtra))
            spell = FLASH;
        else if (verifyCommands(word1, teleportExtra) || verifyCommands(word2, teleportExtra))
            spell = TELEPORT;
        else if (verifyCommands(word1, igniteExtra) || verifyCommands(word2, igniteExtra))
            spell = IGNITE;
        else if (verifyCommands(word1, healExtra) || verifyCommands(word2, healExtra))
            spell = HEAL;
        else if (verifyCommands(word1, ghostExtra) || verifyCommands(word2, ghostExtra))
            spell = GHOST;
        else if (verifyCommands(word1, exhaustExtra) || verifyCommands(word2, exhaustExtra))
            spell = EXHAUST;
        else if (verifyCommands(word1, barrierExtra) || verifyCommands(word2, barrierExtra))
            spell = BARRIER;
        else if (verifyCommands(word1, cleanseExtra) || verifyCommands(word2, cleanseExtra))
            spell = CLEANSE;
        else if (verifyCommands(word1, zhonyasExtra) || verifyCommands(word2, zhonyasExtra))
            spell = ZHONYAS;
        else if (verifyCommands(word1, bootExtra) || verifyCommands(word2, bootExtra)) {
            if (lane == 2) {

                topBoots = !topBoots;
                return;
            } else if (lane == 3) {

                jungleBoots = !jungleBoots;
                return;

            } else if (lane == 4) {

                midBoots = !midBoots;
                return;

            } else if (lane == 5) {

                adcBoots = !adcBoots;
                return;

            } else if (lane == 6) {

                supportBoots = !supportBoots;
                return;

            }
        } else if (verifyCommands(word1, bootsExtra) || verifyCommands(word2, bootsExtra)) {
            if (lane == 2) {

                topBoots = !topBoots;
                return;
            } else if (lane == 3) {

                jungleBoots = !jungleBoots;
                return;

            } else if (lane == 4) {

                midBoots = !midBoots;
                return;

            } else if (lane == 5) {

                adcBoots = !adcBoots;
                return;

            } else if (lane == 6) {

                supportBoots = !supportBoots;
                return;

            }
        } else if (verifyCommands(word1, runeExtra) || verifyCommands(word2, runeExtra)) {
            if (lane == 2) {

                topRune = !topRune;
                return;
            } else if (lane == 3) {

                jungleRune = !jungleRune;
                return;

            } else if (lane == 4) {

                midRune = !midRune;
                return;

            } else if (lane == 5) {

                adcRune = !adcRune;
                return;

            } else if (lane == 6) {

                supportRune = !supportRune;
                return;

            }
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (lane != 0 && spell != 0) {
            CardView containerTimer;
            CardView btnBoots, btnRune;
            String speakLane = "", speakSpell = "";
            containerTimer = (CardView) getLayoutInflater().inflate(R.layout.view_timer, container, false);
            btnBoots = containerTimer.findViewById(R.id.btnBoots);
            btnRune = containerTimer.findViewById(R.id.btnRune);

            containerTimer.setOnTouchListener((view, motionEvent) -> {
                System.out.println("TimerActivity.checkCommand containertimer" + motionEvent.getAction());
                currentTouch = containerTimer;
                return false;
            });
            ImageView iconRune = containerTimer.findViewById(R.id.iconRune);
            ImageView iconBoot = containerTimer.findViewById(R.id.iconBoots);

            int finalLane = lane;
            btnBoots.setOnClickListener(view -> {
                if (finalLane == TOP) {
                    topBoots = !topBoots;
                } else if (finalLane == JUNGLE) {
                    jungleBoots = !jungleBoots;
                } else if (finalLane == MID) {
                    midBoots = !midBoots;
                } else if (finalLane == ADC) {
                    adcBoots = !adcBoots;
                } else if (finalLane == SUPPORT) {
                    supportBoots = !supportBoots;
                }
                return;
            });
            btnRune.setOnClickListener(view -> {


                if (finalLane == TOP) {
                    topRune = !topRune;
                } else if (finalLane == JUNGLE) {
                    jungleRune = !jungleRune;
                } else if (finalLane == MID) {
                    midRune = !midRune;
                } else if (finalLane == ADC) {
                    adcRune = !adcRune;
                } else if (finalLane == SUPPORT) {
                    supportRune = !supportRune;
                }


                return;
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
            } else if (lane == SUPPORT) {
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
            } else if (spell == ZHONYAS) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.zhonyas));
                speakSpell = "Zhonyas";
            } else if (lane == KINDRED) {
                laneIcon.setImageDrawable(getDrawable(R.drawable.kindred));
                speakLane = "Kindred";
                spellIcon.setImageDrawable(getDrawable(R.drawable.kindred_passive));
                speakSpell = "Mark";
                containerTimer.findViewById(R.id.layoutBootsAndRune).setVisibility(View.GONE);
            } else if (lane == ANIVIA) {
                laneIcon.setImageDrawable(getDrawable(R.drawable.anivia));
                speakLane = "Anivia";
                spellIcon.setImageDrawable(getDrawable(R.drawable.anivia_passive));
                speakSpell = "Passive";
                containerTimer.findViewById(R.id.layoutBootsAndRune).setVisibility(View.GONE);

            } else if (lane == NORMAL_TIMER) {
                spellIcon.setImageDrawable(getDrawable(R.drawable.icone_verde));
                containerTimer.findViewById(R.id.iconLane).setVisibility(View.GONE);
                containerTimer.findViewById(R.id.layoutBootsAndRune).setVisibility(View.GONE);

            }


            createTimer(lane, spell, containerTimer, speakLane, speakSpell);
            container.addView(containerTimer);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (sharedPref.getBoolean("vibrateCreate", true)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(1000);
                }
            }
        }
    }

//    public String getCommands(int code) {
//        String commands;
//        String commandsCode;
//        String name = "", laneName = "";
//        if (code == TimerActivity.FLASH) {
//            name = "Flash";
//        } else if (code == TimerActivity.HEAL) {
//            name = "Heal";
//
//        } else if (code == TimerActivity.IGNITE) {
//            name = "Ignite";
//
//        } else if (code == TimerActivity.GHOST) {
//            name = "Ghost";
//
//        } else if (code == TimerActivity.BARRIER) {
//            name = "Barrier";
//
//        } else if (code == TimerActivity.CLEANSE) {
//            name = "Cleanse";
//
//        } else if (code == TimerActivity.EXHAUST) {
//            name = "Exhaust";
//
//        } else if (code == TimerActivity.TELEPORT) {
//            name = "Teleport";
//
//        } else if (code == TimerActivity.BOOTS) {
//            name = "Boots";
//
//        }
//
//        if (code == TimerActivity.TOP) {
//            laneName = "Top";
//        } else if (code == TimerActivity.JUNGLE) {
//            laneName = "Jungle";
//
//        } else if (code == TimerActivity.MID) {
//            laneName = "Mid";
//
//        } else if (code == TimerActivity.ADC) {
//            laneName = "Adc";
//
//        } else if (code == TimerActivity.SUPPORT) {
//            laneName = "Support";
//
//        }
//        commandsCode = laneName + name + "Commands";
//
//        commands = sharedPref.getString(commandsCode, null);
//        return commands;
//    }

//    public int[] verifyCustomCommands(String command) {
//
//        for (int i = 2; i < 7; i++) {//lane
//            for (int j = 0; j < 17; j++) {//spell
//                if (verifyCommands(command, getCommands(i, j)))
//                    return new int[]{i, j};
//            }
//        }
//
//        return new int[]{0, 0};
//    }

    public void addCustomCommands() {
        laneTopExtra += " " + sharedPref.getString("TopCommands", "");
        laneJungleExtra += " " + sharedPref.getString("JungleCommands", "");
        laneMidExtra += " " + sharedPref.getString("MidCommands", "");
        laneAdcExtra += " " + sharedPref.getString("AdcCommands", "");
        laneSupportExtra += " " + sharedPref.getString("SupportCommands", "");

        flashExtra += " " + sharedPref.getString("FlashCommands", "");
        igniteExtra += " " + sharedPref.getString("IgniteCommands", "");
        exhaustExtra += " " + sharedPref.getString("ExhaustCommands", "");
        teleportExtra += " " + sharedPref.getString("TeleportCommands", "");
        healExtra += " " + sharedPref.getString("HealCommands", "");
        cleanseExtra += " " + sharedPref.getString("CleanseCommands", "");
        ghostExtra += " " + sharedPref.getString("GhostCommands", "");
        bootsExtra += " " + sharedPref.getString("BootsCommands", "");
        barrierExtra += " " + sharedPref.getString("BarrierCommands", "");
        zhonyasExtra += " " + sharedPref.getString("ZhonyasCommands", "");
        runeExtra += " " + sharedPref.getString("RuneCommands", "");
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


    private float lastX;
    private LinearLayout layoutContainerTimer;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            lastX = event.getX();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (currentTouch != null) {
                layoutContainerTimer = currentTouch.findViewById(R.id.layoutContainerTimer);
                currentTouch.setTranslationX(currentTouch.getTranslationX() + (event.getX() - lastX));
                if (currentTouch.getTranslationX() >= currentTouch.getWidth() * 0.4f)
                    layoutContainerTimer.setBackgroundColor(Color.RED);
                else
                    layoutContainerTimer.setBackgroundColor(Color.WHITE);
            }

            lastX = event.getX();
        } else if (action == MotionEvent.ACTION_UP) {
            if (currentTouch != null) {
                if (currentTouch.getTranslationX() >= currentTouch.getWidth() * 0.4f) {
                    container.removeView(currentTouch);
                    if (timers.get(currentTouch) != null)
                        timers.get(currentTouch).cancel();
                    timers.remove(currentTouch);
                }
                currentTouch.setTranslationX(0);
                currentTouch = null;
            }
        }
//        System.out.println("TimerActivity.onTouchEvent touching " + touchingTimer);
//        System.out.println("current: " + currentTouch);

        return super.onTouchEvent(event);
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