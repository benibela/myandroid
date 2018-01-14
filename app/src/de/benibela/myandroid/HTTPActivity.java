package de.benibela.myandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPActivity extends MyAndroidBaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.http);

        configLoadAll();


        findButtonById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configSaveAll();

                final String uri = getEditTextText(R.id.url);
                final String data = getEditTextText(R.id.data) + "\n";

                (new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            URL url = new URL(uri);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            try {
                                if (!data.isEmpty()) {
                                    urlConnection.setDoOutput(true);
                                    //urlConnection.setChunkedStreamingMode(0); DO NOT USE. It is in the android documentation, but it breaks things
                                    urlConnection.setRequestMethod("POST");

                                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

                                    OutputStream out = urlConnection.getOutputStream();
                                    out.write(data.getBytes());
                                    out.flush();
                                    out.close();
                                    Log.d("Post", data);
                                }

                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                char[] buf = new char[2048];
                                Reader r = new InputStreamReader(in, "UTF-8");
                                final StringBuilder s = new StringBuilder();
                                while (true) {
                                    int n = r.read(buf);
                                    if (n < 0)
                                        break;
                                    s.append(buf, 0, n);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(HTTPActivity.this, s, Toast.LENGTH_LONG).show();
                                    }
                                });

                            } finally {
                                urlConnection.disconnect();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }})).start();
            }
        });

    }

}
