package com.example.exe1_sub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_Game extends AppCompatActivity {
    private ExtendedFloatingActionButton game_BTN_leftbtn;
    private ExtendedFloatingActionButton game_BTN_rightbtn;
    private MaterialButton game_BTN_play;
    private MaterialButton game_BTN_pause;
    private RelativeLayout game_RTL_arrows;
    private ShapeableImageView[] game_icon_hearts;
    private final int ROWS = 6;
    private final int COLS = 5;
    private ShapeableImageView[][] game_IMG_bord;
    private ShapeableImageView[][] game_IMG_coins;
    private ShapeableImageView[] game_IMG_players = new ShapeableImageView[COLS];
    private final int LIFE = 3;
    private Timer timer;
    private final int FAST_DELAY = 500;
    private final int SLOW_DELAY = 1000;
    private int delay;
    private GameManager gameManager = new GameManager(ROWS, COLS, LIFE);
    private int score = 0;
    private ImageView game_IMG_background;
    private float temp=0;
    private MaterialTextView game_LBL_score;
    private enum TIMER_STATUS {
        OFF,
        RUNNING,
    }

    private eGameType gameType;
    private boolean isCrashed = false;
    private boolean isStartGame = false;


    private TIMER_STATUS timer_status = TIMER_STATUS.OFF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent previousIntent = getIntent();
        String enum_name = previousIntent.getExtras().getString(DataManager.GAME_TYPE);
        gameType = eGameType.valueOf(enum_name);

        findViews();
        initViews();

        if (gameType == eGameType.ARROW_SLOW || gameType == eGameType.ARROW_FAST) {
            initViewsArrow();
        } else if (gameType == eGameType.SENSORS) {
            initViewsSensors();
        }
        startGame();
        startTimer();


    }

    private void initViewsArrow() {
        game_BTN_leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMove(eMoving.left);
            }


        });
        game_BTN_rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMove(eMoving.right);
            }


        });
        if (gameType == eGameType.ARROW_SLOW)
            delay = SLOW_DELAY;
        else if (gameType == eGameType.ARROW_FAST) {
            delay = FAST_DELAY;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        if (gameType == eGameType.SENSORS)
            GameServices.getGameServices().unregister();
        GameServices.getGameServices().soundOff();
    }


    private void startTimer() {
        if (timer_status == TIMER_STATUS.OFF) {
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
            }, delay, delay);
            timer_status = TIMER_STATUS.RUNNING;
        } else return;
    }

    private void initViews() {

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

    private void initViewsSensors() {
        game_RTL_arrows.setVisibility(View.INVISIBLE);
        delay=SLOW_DELAY;
        GameServices.getGameServices().setCallBack_changeDirection(callBack_changeDirection);
        GameServices.getGameServices().sensorsUp();
    }

    private void refreshUI() {
        int enemyCol;
        int coinCol;
        game_LBL_score.setText(" "+score);
        if (isCrashed) {
            createPlayerBord();
            isCrashed = false;
        }

        for (int i = 0; i < LIFE - gameManager.getLife(); i++) {
            game_icon_hearts[i].setVisibility(View.INVISIBLE);
        }
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                game_IMG_bord[i][j].setVisibility(View.INVISIBLE);
                game_IMG_coins[i][j].setVisibility(View.INVISIBLE);
            }
        }

        for (int i = 0; i < ROWS; i++) {
            enemyCol = gameManager.findEnemyCol(i);
            if (enemyCol >= 0) {
                game_IMG_bord[i][enemyCol].setVisibility(View.VISIBLE);
            }
        }
        for (int i = 0; i < ROWS; i++) {
            coinCol = gameManager.findCoinCol(i);
            if (coinCol >= 0) {
                game_IMG_coins[i][coinCol].setVisibility(View.VISIBLE);
            }
        }
        isCrashed();
        isSucceededCoin();

    }

    private void isSucceededCoin() {
        if(gameManager.isSucceededCoin()){
            score=score+5;
            GameServices.getGameServices().coinSound();
        }

    }

    private void isCrashed() {
        if (gameManager.isCrashed()) {
            Glide.with(this).load(R.drawable.img_explosion).into(game_IMG_players[gameManager.getPlayer().col]);
            isCrashed = true;
           GameServices.getGameServices().vibrate();
           GameServices.getGameServices().crashSound();
            if (gameManager.isLose()) {
                stopTimer();
                openScorePage(score);
            }
        }
    }

    private void stopTimer() {
        if (timer_status == TIMER_STATUS.RUNNING) {
            timer_status = TIMER_STATUS.OFF;
            timer.cancel();
        }
    }

    private void openScorePage(int score) {
        Intent intent = new Intent(this, Activity_Score.class);
        intent.putExtra(Activity_Score.KEY_SCORE, score);
        startActivity(intent);
        finish();
    }



    private void startGame() {
        isStartGame = true;
        refreshUI();

    }

    private void updateMove(eMoving move) {
        game_IMG_players[gameManager.getPlayer().col].setVisibility(View.INVISIBLE);
        gameManager.move(move);
        isCrashed();
        game_IMG_players[gameManager.getPlayer().getCol()].setVisibility(View.VISIBLE);
    }

    private void findViews() {
        game_IMG_bord = new ShapeableImageView[ROWS][COLS];
        game_IMG_coins = new ShapeableImageView[ROWS][COLS];
        game_RTL_arrows = findViewById(R.id.game_RTL_arrows);
        game_IMG_background = findViewById(R.id.game_IMG_background);
        Glide.with(this).load(R.drawable.space).into(game_IMG_background);
        game_BTN_play = findViewById(R.id.game_BTN_play);
        game_BTN_pause = findViewById(R.id.game_BTN_pause);
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_icon_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
        game_BTN_leftbtn = findViewById(R.id.game_BTN_leftbtn);
        game_BTN_rightbtn = findViewById(R.id.game_BTN_rightbtn);
        createMat(game_IMG_bord,"game_IMG_bord",R.drawable.img_meteor);
        createMat(game_IMG_coins,"game_IMG_coins",R.drawable.img_coins);
        createPlayerBord();
    }

    private void createMat(ShapeableImageView[][] mat, String matName, int img) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                mat[i][j] = findViewById(getResources().getIdentifier( matName+ i + "" + j, "id", getPackageName()));
                Glide.with(this).load(img).into(mat[i][j]);
                mat[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void createPlayerBord() {
        for (int i = 0; i < COLS; i++) {
            game_IMG_players[i] = findViewById(getResources().getIdentifier("game_IMG_player0" + i, "id", getPackageName()));
            Glide.with(this).load(R.drawable.img_rocketsheep).into(game_IMG_players[i]);
            game_IMG_players[i].setVisibility(View.INVISIBLE);
        }
        game_IMG_players[gameManager.getPlayer().col].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStartGame == true)
            startTimer();
        if (gameType == eGameType.SENSORS)
            GameServices.getGameServices().register();
        GameServices.getGameServices().soundOn();
    }

    GameServices.CallBack_changeDirection callBack_changeDirection = (y, x) -> {
        Log.d("myLog", "x=" + x + "y=" + y + "_____");


        if(x>temp && x>0.05) {
            updateMove(eMoving.right);
        }
        else if(x<temp &&  x<-0.05) {
            updateMove(eMoving.left);
        }
        temp=x;
    };

}