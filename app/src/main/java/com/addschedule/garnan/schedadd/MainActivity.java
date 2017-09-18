package com.addschedule.garnan.schedadd;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    CircularProgressButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (CircularProgressButton) findViewById(R.id.Login);

    }

    public void setLogin(View view)
    {
        AsyncTask<String,String,String> demo = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try{
                    Thread.sleep(3000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return "done";
            }

            @Override
            protected void onPostExecute(String s) {
                if(s.equals("done")){
                    Intent i = new Intent(MainActivity.this,MainActivity.class);
                    startActivity(i);
                }
            }
        };

        login.startAnimation();
        demo.execute();
    }
}
