package com.foreza.seal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
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
        imseal.initialize(this,"hello world", "380000");
    }

    @Override
    public void initSuccess(String sessionId) {
                String msg = "initSuccess reported from activity, session id: " + sessionId;
                Log.d("Test", msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void initFail(String errString) {
                String msg = "init fail reported from activity with error: " + errString;
                Log.d("Test", msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void eventLogSuccess() {

    }

    @Override
    public void eventLogFailure() {

    }
}
