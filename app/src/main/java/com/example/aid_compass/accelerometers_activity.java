package com.example.aid_compass;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class accelerometers_activity extends AppCompatActivity implements SensorEventListener {
    TextView XTV;
    TextView YTV;
    TextView ZTV;
    private SensorManager SensorManage;
    // attribut för färgändring och ljud
    private ConstraintLayout layout;
    private SoundPool soundPool;
    private int signal;
    private ImageView duck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometers_activity);

        SensorManage = (SensorManager) getSystemService(SENSOR_SERVICE);

        layout = (ConstraintLayout) findViewById(R.id.background);

        XTV = (TextView) findViewById(R.id.XTV);
        YTV = (TextView) findViewById(R.id.YTV);
        ZTV = (TextView) findViewById(R.id.ZTV);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool
                    .Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        signal = soundPool.load(this, R.raw.hejhopp,1);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        SensorManage.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // code for system's orientation sensor registered listeners
        SensorManage.registerListener(this, SensorManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = Math.round(event.values[0]);
        float y = Math.round(event.values[1]);
        float z = Math.round(event.values[2]);


        XTV.setText("X-axle: " + Float.toString(x) + " m/s^2");
        YTV.setText("Y-axle: " + Float.toString(y) + " m/s^2");
        ZTV.setText("Z-axle: " + Float.toString(z) + " m/s^2");

        layout = (ConstraintLayout) findViewById(R.id.background);
        duck = (ImageView) layout.getViewById(R.id.duck);

        if(x>6 || x< -6){
            layout.setBackgroundColor(Color.parseColor("#FF0000"));
        } else {
            layout.setBackgroundColor(Color.parseColor("#EDD5F1"));
        }

        if(y==8){
            soundPool.play(signal,1,1,0,0,1);
        } else if (y>= 8){
            duck.setVisibility(View.VISIBLE);
        } else{
            duck.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}