package com.example.exe1_sub;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class GameServices {
    public interface CallBack_changeDirection {
        void changeDirection(float y, float x);
    }

    private static GameServices _instance = null;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Sensor sensor;
    private Context context;
    private CallBack_changeDirection callBack_changeDirection;
    private MediaPlayer mediaPlayer;

    private GameServices(Context context) {
        this.context = context;
        mediaPlayer= MediaPlayer.create(this.context,R.raw.backround_music_space);
        mediaPlayer.setLooping(true);

    }
    public static void initHelper(Context context){
        if(_instance==null) {
            _instance = new GameServices(context);
        }
    }
    public GameServices sensorsUp(){
        sensorManager = (SensorManager) context.getSystemService(this.context.SENSOR_SERVICE);
        sensor= sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        activate_sensor();
        return this;
    }

    private void activate_sensor() {
        sensorEventListener= new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(callBack_changeDirection!= null){
                    callBack_changeDirection.changeDirection(sensorEvent.values[0], sensorEvent.values[1]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    public void register(){
        sensorManager.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void unregister() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    public GameServices setCallBack_changeDirection(CallBack_changeDirection callBack_changeDirection) {
        this.callBack_changeDirection = callBack_changeDirection;
        return this;
    }
    public static GameServices getGameServices(){
        return _instance;
    }

    public void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    public void soundOn(){
        mediaPlayer.start();
    }
    public void soundOff(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
    public void crashSound(){
        MediaPlayer crashSound=MediaPlayer.create(this.context,R.raw.crash_sound);
        crashSound.start();
    }
    public void coinSound(){
        MediaPlayer coinSound=MediaPlayer.create(this.context,R.raw.coin_sound);
        coinSound.start();
    }

}
