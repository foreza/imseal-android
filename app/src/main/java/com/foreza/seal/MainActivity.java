package com.foreza.seal;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.foreza.imseal.IMSEAL;
import com.foreza.imseal.IMSEALInterface;


public class MainActivity extends AppCompatActivity implements IMSEALInterface {

    IMSEAL imseal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imseal = new IMSEAL();
        imseal.initialize(this,"hello world");
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

        imseal.recordAdLoaded();
        imseal.recordAdNoFill("some nights...");


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
