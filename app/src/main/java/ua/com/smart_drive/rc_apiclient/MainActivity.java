package ua.com.smart_drive.rc_apiclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button test = (Button) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL endpoint = new URL("http://rc-api.smart-drive.com.ua/test");
                            HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();

                            connection.setRequestProperty("User-Agent", "rc-api-client");
                            connection.setRequestProperty("Accept", "application/json");

                            if (connection.getResponseCode() == 200) {
                                InputStream data = connection.getInputStream();
                                InputStreamReader reader = new InputStreamReader(data, "UTF-8");
                                JsonReader jsonReader = new JsonReader(reader);
                                jsonReader.beginObject();

                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName();

                                    if (key.equals("data")) {
                                        String value = jsonReader.nextString();
                                        showToast(value);
                                        break;
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
