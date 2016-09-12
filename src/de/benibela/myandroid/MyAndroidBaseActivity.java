package de.benibela.myandroid;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.*;

import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

public class MyAndroidBaseActivity extends SherlockActivity   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onPause() {
        if (MyAndroidApp.currentActivity == this) MyAndroidApp.currentActivity = null;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (MyAndroidApp.currentActivity == this) MyAndroidApp.currentActivity = null;
        super.onDestroy();
    }


    public boolean onOptionsItemSelected(Activity context, MenuItem item) {
        return onOptionsItemIdSelected(context, item.getItemId());
    }
    public boolean onOptionsItemIdSelected(Activity context, int id) {
        // Handle item selection
        Intent intent;
        switch (id) {
            case  android.R.id.home:
                configSaveAll();
                intent = new Intent(context, MyAndroidActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                return true;
        }
        return false;
    }

    //Util
    String getStringExtraSafe(String id){
        String r = getIntent().getStringExtra(id);
        if (r == null) return "";
        return  r;
    }

    public Button findButtonById(int id){
        return (Button)findViewById(id);
    }



    public void setTextViewText(int id, CharSequence text){
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
    }

    public String getTextViewText(int id){
        TextView tv = (TextView) findViewById(id);
        return tv.getText().toString();
    }

    public void setEditTextText(int id, CharSequence text){
        EditText tv = (EditText) findViewById(id);
        tv.setText(text);
    }

    public String getEditTextText(int id){
        EditText tv = (EditText) findViewById(id);
        return tv.getText().toString();
    }

    public void setCheckBoxChecked(int id, boolean text){
        CheckBox tv = (CheckBox) findViewById(id);
        tv.setChecked(text);
    }

    public boolean getCheckBoxChecked(int id){
        CheckBox tv = (CheckBox) findViewById(id);
        return tv.isChecked();
    }


    public String getViewName(View v)
    {
        return v.getResources().getResourceName(v.getId());
    }

    public void configAll(boolean load, Object config, String prefix, ViewGroup group)
    {
        SharedPreferences sp = load ? (SharedPreferences)config : null;
        SharedPreferences.Editor editor = !load ? (SharedPreferences.Editor)config : null;

        int n = group.getChildCount();
        for (int i = 0; i < n; i++) {
            View child = group.getChildAt(i);
            if (child instanceof ViewGroup) configAll(load, config, prefix, (ViewGroup)child);
            if (load) {
                if (child instanceof EditText)
                    ((EditText)child).setText(sp.getString(prefix + getViewName(child), ((EditText)child).getText().toString()));
            } else {
                if (child instanceof EditText)
                    editor.putString(prefix + getViewName(child), ((EditText)child).getText().toString());
            }
        }
    }

    public void configAll(boolean load){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String prefix = getClass().getName() + "_";
        if (load) configAll(load, sp, prefix, (ViewGroup) findViewById(android.R.id.content));
        else {
            SharedPreferences.Editor editor = sp.edit();
            configAll(load, editor, prefix, (ViewGroup) findViewById(android.R.id.content));
            editor.commit();
        }
    }

    public void configLoadAll(){ configAll(true); }
    public void configSaveAll(){ configAll(false); }

}
