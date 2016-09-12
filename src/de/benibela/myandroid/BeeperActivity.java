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

public class BeeperActivity extends MyAndroidBaseActivity  {
    EditText baseBeep;
    EditText incBeep;
    int beeps;
    int multiBeep;
    Timer mTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.timer);

        configLoadAll();

        baseBeep = (EditText)findViewById(R.id.baseBeep);
        incBeep = (EditText)findViewById(R.id.incBeep);

        final ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,50);

        findButtonById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configSaveAll();

                beeps = 0;
                multiBeep = 1;
                //if (mTimer != null)
                stopTimer();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                mTimer = new Timer();
                final Timer myTimer = mTimer;
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mTimer != myTimer) { cancel(); return; } //wtf
                        beeps ++;
                        if (beeps >= Integer.parseInt(incBeep.getText().toString())) {
                            beeps = 0;
                            multiBeep++;
                        }
                        Log.i("MyAndroid", "beep: "+multiBeep);
                        for (int i=0;i<multiBeep;i++){
                            Log.i("MyAndroid", "single beep");
                            tone.startTone(ToneGenerator.TONE_PROP_ACK);
                            try {
                                Thread.sleep(75);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            tone.stopTone();
                            try {
                                Thread.sleep(75);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 0, Integer.parseInt(baseBeep.getText().toString()) * 1000);
            }
        });
        findButtonById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });


    }

    void stopTimer(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
        mTimer = null;
    }
}
