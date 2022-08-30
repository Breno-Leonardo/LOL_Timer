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

import com.onerb.timerlol.ui.main.MainViewModel;

import java.text.Normalizer;

public class CreateCustomCommands extends AppCompatActivity {
    private ScrollView scrollView;
    private long timeIcon, timeIcon2;
    private CountDownTimer timerIcon;
    private int rotation;
    private ImageView icon, imageOption;
    private float percentulTime;
    private Spinner dropdownLane;
    private int code;
    private Button btnCreate;
    private SharedPreferences sharedPref;
    private TextView txtOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_create_custom_commands);
        rotateIcon();

        sharedPref = this.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        imageOption = findViewById(R.id.customOption);
        txtOption=findViewById(R.id.txtOption);

        dropdownLane = findViewById(R.id.spinnerLane);
        dropdownLane.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (((TextView) parentView.getChildAt(0)) != null) {
                    ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);
                    ((TextView) parentView.getChildAt(0)).setTextSize(20);
                    ((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
                }
                changeOption(position);
                code = position + 2;
                getCommands(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                code = 2;
            }
        });
        String[] items = new String[]{"TOP", "JUNGLE", "MID", "BOT", "SUPPORT","FLASH", "IGNITE", "HEAL", "GHOST", "BARRIER", "EXHAUST", "TELEPORT", "CLEANSE", "BOOTS","ZHONYAS","RUNE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownLane.setAdapter(adapter);




        btnCreate = findViewById(R.id.btnCreateCommand);
        EditText editTextCommand = findViewById(R.id.editTextCreateCommand);
        btnCreate.setOnClickListener(view -> {
            putCommands( code, editTextCommand.getText().toString());
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

    public void changeOption(int position) {
        position += 2;
        if (position == TimerActivity.TOP) {
            imageOption.setImageDrawable(getDrawable(R.drawable.top));
        } else if (position == TimerActivity.JUNGLE) {
            imageOption.setImageDrawable(getDrawable(R.drawable.jungle));
        } else if (position == TimerActivity.MID) {
            imageOption.setImageDrawable(getDrawable(R.drawable.mid));
        } else if (position == TimerActivity.ADC) {
            imageOption.setImageDrawable(getDrawable(R.drawable.adc));
        } else if (position == TimerActivity.SUPPORT) {
            imageOption.setImageDrawable(getDrawable(R.drawable.support));
        }
        else if (position == TimerActivity.FLASH) {
            imageOption.setImageDrawable(getDrawable(R.drawable.flash));
        } else if (position == TimerActivity.HEAL) {
            imageOption.setImageDrawable(getDrawable(R.drawable.heal));
        } else if (position == TimerActivity.IGNITE) {
            imageOption.setImageDrawable(getDrawable(R.drawable.ignite));
        } else if (position == TimerActivity.GHOST) {
            imageOption.setImageDrawable(getDrawable(R.drawable.ghost));
        } else if (position == TimerActivity.BARRIER) {
            imageOption.setImageDrawable(getDrawable(R.drawable.barrier));
        } else if (position == TimerActivity.CLEANSE) {
            imageOption.setImageDrawable(getDrawable(R.drawable.cleanse));
        } else if (position == TimerActivity.EXHAUST) {
            imageOption.setImageDrawable(getDrawable(R.drawable.exhaust));
        } else if (position == TimerActivity.TELEPORT) {
            imageOption.setImageDrawable(getDrawable(R.drawable.teleport));
        } else if (position == TimerActivity.BOOTS) {
            imageOption.setImageDrawable(getDrawable(R.drawable.boots));
        }
        else if (position == TimerActivity.ZHONYAS) {
            imageOption.setImageDrawable(getDrawable(R.drawable.zhonyas));
        }
        else if (position == TimerActivity.RUNE) {
            imageOption.setImageDrawable(getDrawable(R.drawable.cosmic_insight));
        }
    }





    public void putCommands(int code, String command) {
        String commands, originalCommands, existingCommands;
        String commandsCode, originalCommandsCode;
        command=command.trim();
        command=command.toLowerCase();
//        command=removerAcentos(command);
        String name = "";
        if (code == TimerActivity.FLASH) {
            name = "Flash";
        } else if (code == TimerActivity.HEAL) {
            name = "Heal";

        } else if (code == TimerActivity.IGNITE) {
            name = "Ignite";

        } else if (code == TimerActivity.GHOST) {
            name = "Ghost";

        } else if (code == TimerActivity.BARRIER) {
            name = "Barrier";

        } else if (code == TimerActivity.CLEANSE) {
            name = "Cleanse";

        } else if (code == TimerActivity.EXHAUST) {
            name = "Exhaust";

        } else if (code == TimerActivity.TELEPORT) {
            name = "Teleport";

        } else if (code == TimerActivity.BOOTS) {
            name = "Boots";

        }
        else if (code == TimerActivity.ZHONYAS) {
            name = "Zhonyas";

        }
        else if (code == TimerActivity.RUNE) {
            name = "Rune";

        }

        if (code == TimerActivity.TOP) {
            name = "Top";
        } else if (code == TimerActivity.JUNGLE) {
            name = "Jungle";

        } else if (code == TimerActivity.MID) {
            name = "Mid";

        } else if (code == TimerActivity.ADC) {
            name = "Adc";

        } else if (code == TimerActivity.SUPPORT) {
            name = "Support";

        }
        commandsCode = name + "Commands";
        originalCommandsCode =  name + "OriginalCommands";
        commands = sharedPref.getString(commandsCode, null);
        originalCommands = sharedPref.getString(originalCommandsCode, null);
        existingCommands = sharedPref.getString("existingCommands", "");
        System.out.println("CreateCustomCommands.putCommands shared all:"+sharedPref.getAll());


        String separator = "&7&";
        SharedPreferences.Editor editor = sharedPref.edit();

        String originalCommand = command;
        command = command.replace(" ", "");
        for (String s:
                existingCommands.split(" ")) {
            if(s.equalsIgnoreCase(command) && !s.equalsIgnoreCase("") && !s.equalsIgnoreCase(" ")) {
                Toast toast= Toast.makeText(getApplicationContext(),getString(R.string.command_exist), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
        Boolean contains = false;
        if (commands != null) {
            for (String s :
                    commands.split(" ")) {
                if (s.equalsIgnoreCase(command))
                    contains = true;
            }
        }
        if (!contains && !originalCommand.equalsIgnoreCase("") && !originalCommand.equalsIgnoreCase("\n") && !originalCommand.equalsIgnoreCase(" ") && !originalCommand.equalsIgnoreCase("  ")) {
            if (commands == null)
                commands = "";
            if (originalCommands == null)
                originalCommands = "";
            editor.putString(commandsCode, commands + " " + command);// add command
            editor.putString("existingCommands", existingCommands+ " "+ command);
            if (originalCommands.equalsIgnoreCase(""))
                editor.putString(originalCommandsCode, originalCommand);
            else
                editor.putString(originalCommandsCode, originalCommands + separator  + originalCommand);

        }
        editor.commit();
        getCommands( code);

    }

    public void getCommands(int code) {
        String commands, originalCommands;
        String commandsCode, originalCommandsCode;
        LinearLayout containerCustomCommandsExisting = findViewById(R.id.containerCustomCommandsExistings);
        containerCustomCommandsExisting.removeAllViews();
        System.out.println("CreateCustomCommands.getCommands commands " + sharedPref.getAll());
        String name = "";
        if (this.code == TimerActivity.TOP) {
            name = "Top";
        } else if (this.code == TimerActivity.JUNGLE) {
            name = "Jungle";

        } else if (this.code == TimerActivity.MID) {
            name = "Mid";

        } else if (this.code == TimerActivity.ADC) {
            name = "Adc";

        } else if (this.code == TimerActivity.SUPPORT) {
            name = "Support";

        }
        else if (this.code == TimerActivity.FLASH) {
            name = "Flash";
        } else if (this.code == TimerActivity.HEAL) {
            name = "Heal";

        } else if (this.code == TimerActivity.IGNITE) {
            name = "Ignite";

        } else if (this.code == TimerActivity.GHOST) {
            name = "Ghost";

        } else if (this.code == TimerActivity.BARRIER) {
            name = "Barrier";

        } else if (this.code == TimerActivity.CLEANSE) {
            name = "Cleanse";

        } else if (this.code == TimerActivity.EXHAUST) {
            name = "Exhaust";

        } else if (this.code == TimerActivity.TELEPORT) {
            name = "Teleport";

        } else if (this.code == TimerActivity.BOOTS) {
            name = "Boots";

        }
        else if (this.code == TimerActivity.ZHONYAS) {
            name = "Zhonyas";

        }
        else if (this.code == TimerActivity.RUNE) {
            name = "Rune";

        }


        commandsCode = name + "Commands";
        originalCommandsCode =  name + "OriginalCommands";
        commands = sharedPref.getString(commandsCode, null);
        originalCommands = sharedPref.getString(originalCommandsCode, null);
//        System.out.println("CreateCustomCommands.getCommands  new original commands " + originalCommands);
//        System.out.println("CreateCustomCommands.getCommands  new  commands " + commands);
        if (commands != null) {
            String[] aux = originalCommands.split("&7&");
            for (int i = 0; i < aux.length; i++) {


                CardView containerCard = (CardView) getLayoutInflater().inflate(R.layout.view_commands, containerCustomCommandsExisting, false);
                TextView textCommand = containerCard.findViewById(R.id.textCommand);
                Button btnRemove = containerCard.findViewById(R.id.btnRemoveCommand);
                btnRemove.setOnTouchListener((view, motionEvent) -> {

                    String[] a1 = sharedPref.getString(originalCommandsCode, null).split("&7&");
                    String[] a2 = sharedPref.getString(commandsCode, null).split(" ");
                    String[] a3 = sharedPref.getString("existingCommands", null).split(" ");
                    TextView txtCommand = containerCard.findViewById(R.id.textCommand);
                    String newOriginalCommand = "", newCommand = "", newExistingCommand="";

                    for (int j = 0; j < a1.length; j++) {
                        if (!a1[j].equalsIgnoreCase(txtCommand.getText().toString()) && !a1[j].equalsIgnoreCase("") && !a1[j].equalsIgnoreCase(" ") )
                            newOriginalCommand = newOriginalCommand + a1[j] + "&7&";
                    }


                    for (int j = 0; j < a2.length; j++) {
                        if (!a2[j].equalsIgnoreCase(txtCommand.getText().toString().replace(" ", ""))&& !a2[j].equalsIgnoreCase("") && !a2[j].equalsIgnoreCase(" "))
                            newCommand = newCommand + a2[j] + " ";
                    }

                    for (int j = 0; j < a3.length; j++) {
                        if (!a3[j].equalsIgnoreCase(txtCommand.getText().toString().replace(" ", "")) && !a3[j].equalsIgnoreCase("") && !a3[j].equalsIgnoreCase(" "))
                            newExistingCommand = newExistingCommand + a3[j] + " ";
                    }

                    containerCustomCommandsExisting.removeView(containerCard);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(commandsCode, newCommand);// add command
                    editor.putString(originalCommandsCode, newOriginalCommand);
                    editor.putString("existingCommands", newExistingCommand);
                    editor.commit();


                    return true;
                });
                textCommand.setText(aux[i]);
                if (aux[i] != "")
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