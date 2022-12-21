package com.example.exe1_sub;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Menu_Activity extends AppCompatActivity {
    private MaterialButton menu_BTN_arrowsSlow;
    private MaterialButton menu_BTN_arrowsFast;
    private MaterialButton menu_BTN_playsensors;
    private MaterialButton menu_BTN_top10;
    private eGameType gameType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findView();
        initView();

    }

    private void initView() {
        menu_BTN_arrowsSlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameType=eGameType.ARROW_SLOW;
                openGame();
            }
        });
        menu_BTN_arrowsFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameType=eGameType.ARROW_FAST;
                openGame();
            }
        });
        menu_BTN_playsensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameType=eGameType.SENSORS;
                openGame();
            }
        });
        menu_BTN_top10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecords();

            }
        });
    }

    private void openRecords() {
        Intent intent = new Intent(this, RecordsActivity.class);
        startActivity(intent);
        finish();
    }

    private void openGame(){
        Intent intent = new Intent(this, Activity_Game.class);
        intent.putExtra(DataManager.GAME_TYPE,gameType.name());
        startActivity(intent);
        finish();
    }

    private void findView() {
        menu_BTN_arrowsFast=findViewById(R.id.menu_BTN_arrowsFast);
        menu_BTN_arrowsSlow=findViewById(R.id.menu_BTN_arrowsSlow);
        menu_BTN_playsensors=findViewById(R.id.menu_BTN_playsensors);
        menu_BTN_top10=findViewById(R.id.menu_BTN_top10);
    }
}