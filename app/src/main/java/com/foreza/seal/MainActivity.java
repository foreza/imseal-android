package com.foreza.seal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.foreza.imseal.IMSEAL;


public class MainActivity extends AppCompatActivity {

    IMSEAL imseal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Test", "got here!");


        imseal = new IMSEAL();
        imseal.initialize("hello world");
        imseal.recordAdLoaded();

        Log.d("Test", "got here!");


    }
}
