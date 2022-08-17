package com.onerb.timerlol;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.onerb.timerlol.api.InfosGameApiUtil;
import com.onerb.timerlol.ui.main.MainViewModel;

import java.text.Normalizer;
import java.util.Map;

public class CreateCustomCommands extends AppCompatActivity {
    private ScrollView scrollView;
    private long timeIcon, timeIcon2;
    private CountDownTimer timerIcon;
    private int rotation;
    private ImageView icon, imageLane, imageSpell;
    private float percentulTime;
    private Spinner dropdownLane;
    private Spinner dropdownSpell;
    private int lane, spell;
    private Button btnCreate;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_create_custom_commands);
        rotateIcon();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        imageLane = findViewById(R.id.createCustomLane);
        imageSpell = findViewById(R.id.createCustomSpell);

        dropdownLane = findViewById(R.id.spinnerLane);
        dropdownLane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parentView.getChildAt(0)).setTextSize(20);
                    ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                }
                changeLane(position);
                lane = position + 2;
                getCommands(lane, spell);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                lane = 2;
            }
        });
        String[] items = new String[]{"TOP", "JUNGLE", "MID", "ADC | BOT", "SUPPORT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownLane.setAdapter(adapter);

        dropdownSpell = findViewById(R.id.spinnerSpell);
        dropdownSpell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parentView.getChildAt(0)).setTextSize(20);
                    ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                }
                changeSpell(position);
                spell = position + 7;
                getCommands(lane, spell);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spell = 7;
            }
        });
        String[] items2 = new String[]{"FLASH", "IGNITE", "HEAL", "GHOST", "BARRIER", "EXHAUST", "TELEPORT", "CLEANSE", "BOOTS"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdownSpell.setAdapter(adapter2);
        btnCreate = findViewById(R.id.btnCreateCommand);
        EditText editTextCommand = findViewById(R.id.editTextCreateCommand);
        btnCreate.setOnTouchListener((view, motionEvent) -> {
            putCommands(lane, spell, editTextCommand.getText().toString());
            System.out.println("CreateCustomCommands.onCreate shared all " + sharedPref.getAll());
            return true;
        });


        scrollView = findViewById(R.id.scrollViewCreateCustomCommands);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                } else if (action == MotionEvent.ACTION_MOVE) {


                } else if (action == MotionEvent.ACTION_UP) {

                }
                return false;
            }
        });

    }

    public void changeLane(int position) {
        position += 2;
        if (position == TimerActivity.TOP) {
            imageLane.setImageDrawable(getDrawable(R.drawable.top));
        } else if (position == TimerActivity.JUNGLE) {
            imageLane.setImageDrawable(getDrawable(R.drawable.jungle));
        } else if (position == TimerActivity.MID) {
            imageLane.setImageDrawable(getDrawable(R.drawable.mid));
        } else if (position == TimerActivity.ADC) {
            imageLane.setImageDrawable(getDrawable(R.drawable.adc));
        } else if (position == TimerActivity.SUPPORT) {
            imageLane.setImageDrawable(getDrawable(R.drawable.support));
        }


    }

    public void changeSpell(int position) {
        position += 7;
        if (position == TimerActivity.FLASH) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.flash));
        } else if (position == TimerActivity.HEAL) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.heal));
        } else if (position == TimerActivity.IGNITE) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.ignite));
        } else if (position == TimerActivity.GHOST) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.ghost));
        } else if (position == TimerActivity.BARRIER) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.barrier));
        } else if (position == TimerActivity.CLEANSE) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.cleanse));
        } else if (position == TimerActivity.EXHAUST) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.exhaust));
        } else if (position == TimerActivity.TELEPORT) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.teleport));
        } else if (position == TimerActivity.BOOTS) {
            imageSpell.setImageDrawable(getDrawable(R.drawable.boots));
        }


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

    public void putCommands(int lane, int spell, String command) {
        String commands, originalCommands;
        String commandsCode, originalCommandsCode;

        String spellName = "", laneName = "";
        if (spell == TimerActivity.FLASH) {
            spellName = "Flash";
        } else if (spell == TimerActivity.HEAL) {
            spellName = "Heal";

        } else if (spell == TimerActivity.IGNITE) {
            spellName = "Ignite";

        } else if (spell == TimerActivity.GHOST) {
            spellName = "Ghost";

        } else if (spell == TimerActivity.BARRIER) {
            spellName = "Barrier";

        } else if (spell == TimerActivity.CLEANSE) {
            spellName = "Cleanse";

        } else if (spell == TimerActivity.EXHAUST) {
            spellName = "Exhaust";

        } else if (spell == TimerActivity.TELEPORT) {
            spellName = "Teleport";

        } else if (spell == TimerActivity.BOOTS) {
            spellName = "Boots";

        }

        if (lane == TimerActivity.TOP) {
            laneName = "Top";
        } else if (lane == TimerActivity.JUNGLE) {
            laneName = "Jungle";

        } else if (lane == TimerActivity.MID) {
            laneName = "Mid";

        } else if (lane == TimerActivity.ADC) {
            laneName = "Adc";

        } else if (lane == TimerActivity.SUPPORT) {
            laneName = "Support";

        }
        commandsCode = laneName + spellName + "Commands";
        originalCommandsCode = laneName + spellName + "OriginalCommands";
        commands = sharedPref.getString(commandsCode, null);
        originalCommands = sharedPref.getString(originalCommandsCode, null);
        String separator = "&7&";
        SharedPreferences.Editor editor = sharedPref.edit();

        String originalCommand = command;
        command = command.replace(" ", "");
        Boolean contains = false;
        if (commands != null) {
            for (String s :
                    commands.split(" ")) {
                if (s.equals(command))
                    contains = true;
            }
        }
        if (!contains && !originalCommand.equals("") && !originalCommand.equals("\n") && !originalCommand.equals(" ") && !originalCommand.equals("  ")) {
            if (commands == null)
                commands = "";
            if (originalCommands == null)
                originalCommands = "";
            editor.putString(commandsCode, commands + " " + command);// add command
            if (originalCommands.equals(""))
                editor.putString(originalCommandsCode, originalCommand);
            else
                editor.putString(originalCommandsCode, originalCommands + " " + separator + " " + originalCommand);

        }
        editor.apply();
        getCommands(lane, spell);

    }

    public void getCommands(int lane, int spell) {
        String commands, originalCommands;
        String commandsCode, originalCommandsCode;
        LinearLayout containerCustomCommandsExisting = findViewById(R.id.containerCustomCommandsExistings);
        containerCustomCommandsExisting.removeAllViews();
        System.out.println("CreateCustomCommands.getCommands commands "+sharedPref.getAll());
        String spellName = "", laneName = "";
        if (spell == TimerActivity.FLASH) {
            spellName = "Flash";
        } else if (spell == TimerActivity.HEAL) {
            spellName = "Heal";

        } else if (spell == TimerActivity.IGNITE) {
            spellName = "Ignite";

        } else if (spell == TimerActivity.GHOST) {
            spellName = "Ghost";

        } else if (spell == TimerActivity.BARRIER) {
            spellName = "Barrier";

        } else if (spell == TimerActivity.CLEANSE) {
            spellName = "Cleanse";

        } else if (spell == TimerActivity.EXHAUST) {
            spellName = "Exhaust";

        } else if (spell == TimerActivity.TELEPORT) {
            spellName = "Teleport";

        } else if (spell == TimerActivity.BOOTS) {
            spellName = "Boots";

        }

        if (lane == TimerActivity.TOP) {
            laneName = "Top";
        } else if (lane == TimerActivity.JUNGLE) {
            laneName = "Jungle";

        } else if (lane == TimerActivity.MID) {
            laneName = "Mid";

        } else if (lane == TimerActivity.ADC) {
            laneName = "Adc";

        } else if (lane == TimerActivity.SUPPORT) {
            laneName = "Support";

        }
        commandsCode = laneName + spellName + "Commands";
        originalCommandsCode = laneName + spellName + "OriginalCommands";
        commands = sharedPref.getString(commandsCode, null);
        originalCommands = sharedPref.getString(originalCommandsCode, null);
        if (commands != null) {
            String[] aux = originalCommands.split("&7&");
            String[] aux2 = originalCommands.split(" ");
            for (int i = 0; i < aux.length; i++) {


                CardView containerCard = (CardView) getLayoutInflater().inflate(R.layout.view_commands, containerCustomCommandsExisting, false);
                TextView textCommand = containerCard.findViewById(R.id.textCommand);
                Button btnRemove = containerCard.findViewById(R.id.btnRemoveCommand);
                btnRemove.setOnTouchListener((view, motionEvent) -> {
                    String[] a1 = originalCommands.split("&7&");
                    String[] a2 = originalCommands.split(" ");
                    TextView txtCommand = containerCard.findViewById(R.id.textCommand);
                    String newOriginalCommand = "", newCommand = "";

                    for (int j = 0; j < a1.length; j++) {
                        if(!a1[j].equals(txtCommand.getText().toString()))
                        newOriginalCommand = newOriginalCommand + a1[j] + "&7&";
                    }


                    for (int j = 0; j < a2.length; j++) {
                        if(!a2[j].equals(txtCommand.getText().toString().replace(" ","")))
                        newCommand = newCommand + a2[j] + " ";
                    }

                    containerCustomCommandsExisting.removeView(containerCard);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(commandsCode, newCommand);// add command
                    editor.putString(originalCommandsCode, newOriginalCommand);
                    editor.apply();

                    return true;
                });
                textCommand.setText(aux[i]);
                containerCustomCommandsExisting.addView(containerCard);
            }
        }
    }

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


    public MainViewModel getViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onStop() {
        super.onStop();
//        System.out.println("TimerActivity.onStop stop timer aqui");


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();


    }


}