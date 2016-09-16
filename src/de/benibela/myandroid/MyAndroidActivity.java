package de.benibela.myandroid;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.*;

import android.widget.EditText;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import java.lang.InternalError;
import java.lang.Override;
import java.util.Timer;
import java.util.TimerTask;

public class MyAndroidActivity extends MyAndroidBaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startmenu);

        findButtonById(R.id.buttonBeeper).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyAndroidActivity.this.startActivity(new Intent(MyAndroidActivity.this, BeeperActivity.class));
                    }
                }
        );
        findButtonById(R.id.buttonSO).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyAndroidActivity.this.startActivity(new Intent(MyAndroidActivity.this, SoDebuggerActivity.class));
                    }
                }
        );
        findButtonById(R.id.buttonEye).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyAndroidActivity.this.startActivity(new Intent(MyAndroidActivity.this, EyeTestActivity.class));
                    }
                }
        );

    }
}
