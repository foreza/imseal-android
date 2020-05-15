package com.foreza.seal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.foreza.imseal.IMSEAL;
import com.foreza.imseal.IMSEALInterface;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements IMSEALInterface {

    IMSEAL imseal;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imseal = new IMSEAL();
        imseal.initialize(this,"hello world");

        timer = new Timer();
    }

    @Override
    public void initSuccess(int sessionId) {
                String msg = "initSuccess reported from activity, session id: " + sessionId;
                Log.d("Test", msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

                // Temporary

                imseal.recordAdRequest();
    }

    @Override
    public void initFail(String errString) {
                String msg = "init fail reported from activity with error: " + errString;
                Log.d("Test", msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startEventLogSuccess() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                imseal.recordAdLoaded();
            }
        }, 1000);


    }

    @Override
    public void startEventLogFail() {

    }

    @Override
    public void eventLogSuccess() {
        String msg = "eventLogSuccess";
        Log.d("Test", msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();

    }

    @Override
    public void eventLogFailure() {
        String msg = "eventLogFailure";
        Log.d("Test", msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
