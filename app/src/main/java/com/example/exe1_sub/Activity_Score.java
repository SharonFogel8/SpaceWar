package com.example.exe1_sub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class Activity_Score extends AppCompatActivity {
    public static final String KEY_SCORE = "KEY_SCORE";

    private TextView score_LBL_score;
    private MaterialButton score_BTN_playagain;
    private MaterialButton score_BTN_save;
    private EditText score_ETXT_name;
    private String name;
    private int score;
    private GPS my_gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        findViews();
        initViews();
        Intent previousIntent = getIntent();
        score = previousIntent.getExtras().getInt(KEY_SCORE);
        score_LBL_score.setText("Score: " + score);
        my_gps = new GPS(this);
    }

    private void findViews() {
        score_LBL_score = findViewById(R.id.score_LBL_score);
        score_BTN_playagain = findViewById(R.id.score_BTN_playagain);
        score_BTN_save= findViewById(R.id.score_BTN_save);
        score_ETXT_name= findViewById(R.id.score_ETXT_name);
    }

    private void openGamePage() {
        Intent intent = new Intent(this, Menu_Activity.class);
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
        score_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                score_BTN_save.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void save() {
        name= score_ETXT_name.getText().toString();
        UserDetails userDetails= new UserDetails().setName(name).setScore(score).setLat(my_gps.getLat()).setLag(my_gps.getLag());
        DataManager.getDataManager().updateTopRecords(userDetails);
    }
}