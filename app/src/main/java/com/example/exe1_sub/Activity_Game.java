package com.example.exe1_sub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Game extends AppCompatActivity {
    private ExtendedFloatingActionButton game_BTN_leftbtn;
    private ExtendedFloatingActionButton game_BTN_rightbtn;
    private MaterialButton game_BTN_start;
    private MaterialButton game_BTN_play;
    private MaterialButton game_BTN_pause;
    private RelativeLayout game_RTL_start;
    private ShapeableImageView[] game_icon_hearts;
    private final int ROWS=4;
    private final int COLS=3;
    private ShapeableImageView[][] game_IMG_bord;
    private ShapeableImageView[] game_IMG_players= new ShapeableImageView[COLS];
    private final int LIFE=3;
    private Timer timer;
    private final int DELAY = 1000;
    private GameManager gameManager=new GameManager(ROWS,COLS,LIFE);
    private int score=0;
    private ImageView game_IMG_background;
    private enum TIMER_STATUS {
        OFF,
        RUNNING,
    }
    private boolean isCrashed=false;
    private boolean isStartGame=false;

    private TIMER_STATUS timer_status = TIMER_STATUS.OFF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
//TODO backeground galaxy
        findViews();
        game_RTL_start.setVisibility(View.VISIBLE);
        initViews();
    }
       @Override
     protected void onPause() {
          super.onPause();
          stopTimer();
      }


    private void startTimer() {
        if(timer_status==TIMER_STATUS.OFF) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gameManager.nextStep();
                            score++;
                            refreshUI();
                        }
                    });
                }
            }, DELAY, DELAY);
            timer_status = TIMER_STATUS.RUNNING;
        }
        else return;
    }

    private void initViews() {

        game_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
                startTimer();
            }


        });
        game_BTN_leftbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clicked(eMoving.left);
            }


        });
        game_BTN_rightbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clicked(eMoving.right);
            }


        });
        game_BTN_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();
            }
        });
        game_BTN_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResume();
            }
        });
    }

    private void refreshUI() {
        int enemyCol;
        if(isCrashed){
            createPlayerBord();
            isCrashed=false;
        }

        for (int i = 0; i < LIFE-gameManager.getLife(); i++) {
            game_icon_hearts[i].setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                game_IMG_bord[i][j].setVisibility(View.INVISIBLE);
            }
        }

        for (int i = 0; i < ROWS; i++) {
            enemyCol=gameManager.findEnemyCol(i);
            if(enemyCol>=0) {
                game_IMG_bord[i][enemyCol].setVisibility(View.VISIBLE);
            }
        }
        isCrashed();
    }
    private void isCrashed() {
        if (gameManager.isCrashed()) {
            Glide.with(this).load(R.drawable.img_explosion).into(game_IMG_players[gameManager.getPlayer().col]);
            isCrashed = true;
            vibrate();

            if (gameManager.isLose()) {
                stopTimer();
                openScorePage(score);
            }
        }
    }
    private void stopTimer() {
        if(timer_status==TIMER_STATUS.RUNNING) {
            timer_status = TIMER_STATUS.OFF;
            timer.cancel();
        }
    }

    private void openScorePage(int score) {
        Intent intent= new Intent(this, Activity_Score.class);
        intent.putExtra(Activity_Score.KEY_SCORE,score);
        startActivity(intent);
        finish();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    private void startGame() {
        isStartGame=true;
        game_RTL_start.setVisibility(View.INVISIBLE);
        refreshUI();

    }

    private void clicked(eMoving move) {
        game_IMG_players[gameManager.getPlayer().col].setVisibility(View.INVISIBLE);
        gameManager.move(move);
        isCrashed();
        game_IMG_players[gameManager.getPlayer().getCol()].setVisibility(View.VISIBLE);
    }
    private void findViews() {
        game_IMG_background=findViewById(R.id.game_IMG_background);
        Glide.with(this).load(R.drawable.space).into(game_IMG_background);
        game_RTL_start=findViewById(R.id.game_RTL_start);
        game_BTN_start=findViewById(R.id.game_BTN_start);
        game_BTN_play=findViewById(R.id.game_BTN_play);
        game_BTN_pause=findViewById(R.id.game_BTN_pause);

        game_icon_hearts= new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
        game_BTN_leftbtn=findViewById(R.id.game_BTN_leftbtn);
        game_BTN_rightbtn=findViewById(R.id.game_BTN_rightbtn);
        createRockMat();
        createPlayerBord();
    }

    private void createRockMat() {
        game_IMG_bord= new ShapeableImageView[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                game_IMG_bord[i][j]=findViewById(getResources().getIdentifier("game_IMG_bord"+i+""+j, "id", getPackageName()));
                Glide.with(this).load(R.drawable.img_rock).into(game_IMG_bord[i][j]);
                game_IMG_bord[i][j].setVisibility(View.INVISIBLE);
            }
        }



    }
    private void createPlayerBord(){
        for (int i = 0; i < COLS; i++) {
            game_IMG_players[i]=findViewById(getResources().getIdentifier("game_IMG_player0"+i, "id", getPackageName()));
            Glide.with(this).load(R.drawable.img_rocketsheep).into(game_IMG_players[i]);
            game_IMG_players[i].setVisibility(View.INVISIBLE);
        }
        game_IMG_players[gameManager.getPlayer().col].setVisibility(View.VISIBLE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(isStartGame==true)
            startTimer();
    }


}