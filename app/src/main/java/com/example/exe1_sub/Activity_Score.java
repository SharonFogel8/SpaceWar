package com.example.exe1_sub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class Activity_Score extends AppCompatActivity {
    public static final String KEY_SCORE = "KEY_SCORE";

    private TextView score_LBL_score;
    private MaterialButton score_BTN_playagain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initViews();
        Intent previousIntent = getIntent();
        int score = previousIntent.getExtras().getInt(KEY_SCORE);
        score_LBL_score.setText("Score: " + score);
    }

    private void findViews() {
        score_LBL_score = findViewById(R.id.score_LBL_score);
        score_BTN_playagain = findViewById(R.id.score_BTN_playagain);
    }

    private void openGamePage() {
        Intent intent = new Intent(this, Activity_Game.class);
        startActivity(intent);
        finish();
    }

    private void initViews() {

        score_BTN_playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGamePage();
            }


        });
    }
}