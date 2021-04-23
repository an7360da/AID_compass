package com.example.aid_compass;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class compass_activity extends AppCompatActivity implements SensorEventListener {

    // device sensor manager
    private SensorManager SensorManage;

    // define the compass picture that will be use
    private ImageView compassImage;

    // record the angle turned of the compass picture
    private float DegreeStart = 0f;
    TextView DegreeTV;
    // attribut som är tillagda för att stödja vibration/ljud

    private SoundPool soundPool;
    private int signal;
    private Vibrator vibrator;
    private KonfettiView konfettiView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_activity);
        compassImage = (ImageView) findViewById(R.id.compass_image);

        // TextView that will display the degree
        DegreeTV = (TextView) findViewById(R.id.DegreeTV);

        // initialize your android device sensor capabilities
        SensorManage = (SensorManager) getSystemService(SENSOR_SERVICE);

        // skapar ljud + vibration
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        konfettiView = findViewById(R.id.konfettiView);

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

        signal = soundPool.load(this, R.raw.chime_up,1);

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
        SensorManage.registerListener(this, SensorManage.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        DegreeTV.setText("Heading: " + Float.toString(degree) + " degrees");

        // rotation animation - reverse turn degree degrees
        RotateAnimation ra = new RotateAnimation(
                DegreeStart,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // set the compass animation after the end of the reservation status
        ra.setFillAfter(true);

        // set how long the animation for the compass image will take place
        ra.setDuration(210);

        // Start animation of compass image
        compassImage.startAnimation(ra);
        DegreeStart = -degree;

        //check för att se om kompassen kollar mot norr

       if (345 < degree || degree < 15) {

        vibrator.vibrate(100);

       }

       if((int) degree == 0){
           soundPool.play(signal,1,1,0,0,1);
           konfettiView.build()
                   .addColors(Color.parseColor("#799C27B0"), Color.parseColor("#e75480"), Color.parseColor("#DAA520"))
                   .setDirection(0.0, 359.0)
                   .setSpeed(1f, 5f)
                   .setFadeOutEnabled(true)
                   .setTimeToLive(2000L)
                   .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                   .addSizes(new Size(12, 5f))
                   .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                   .streamFor(300, 500L);
       }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}