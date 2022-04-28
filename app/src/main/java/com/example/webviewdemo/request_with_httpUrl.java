package com.example.webviewdemo;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telecom.Connection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


class RequestWithHttpUrl extends AppCompatActivity implements View.OnClickListener{


     TextView responseText;
     String url=null;
     EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
         editText =  (EditText)findViewById(R.id.city);
        Button button = (Button) findViewById(R.id.send_request);
        responseText =  (TextView) findViewById(R.id.response_request);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId()==R.id.send_request){
            url  = getUrl(editText.getText().toString());
            sendRequestWithHttpURLConnection(url);
        }
    }

    private void sendRequestWithHttpURLConnection(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    String result=null;
                    String strRead=null;
                    URL  url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    int response = conn.getResponseCode();
                    if(response == HttpURLConnection.HTTP_OK) {
                        InputStream is = conn.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        while ((strRead = reader.readLine()) != null) {
                            stringBuilder.append(strRead);
                        }
                        result = stringBuilder.toString();
                        showResponnse(result);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }

                }
            }
        }).start();
    }

    private void showResponnse(final String responnse){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(responnse);
            }
        });
    }
    private String getUrl(String city){
        return "http://php.weather.sina.com.cn/xml.php?city=%B1%B1%BE%A9&password=DJOYnieT8234jlsK&day=0"+city;
    }


}