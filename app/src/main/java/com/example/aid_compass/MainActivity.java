package com.example.aid_compass;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickedCompass(View view){
        Intent intent = new Intent(this, compass_activity.class);
        startActivity(intent);

    }

    public void clickedAccelerometers(View view){
        Intent intent = new Intent(this, accelerometers_activity.class);
        startActivity(intent);

    }
}