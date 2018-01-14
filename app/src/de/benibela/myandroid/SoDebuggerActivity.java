package de.benibela.myandroid;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class SoDebuggerActivity extends MyAndroidBaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.sodebugger);

        configLoadAll();


        findButtonById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configSaveAll();

                System.load(getEditTextText(R.id.filename));
            }
        });

    }

}
