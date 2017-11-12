package com.addschedule.garnan.schedadd;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.addschedule.garnan.schedadd.Api.Clases.User;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    CircularProgressButton login;

    User AdUser;

    EditText user;
    EditText password;

    Properties ppt;

    public final static String FILE_ACTIVITIES_SCHEDULE = "schedules.xml",
    JSON_DATA = "application/json",TOKENS = "tokens.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ppt = new Properties();

        try {

            FileInputStream fi = new FileInputStream(TOKENS);
            ppt.loadFromXML(fi);
            fi.close();

            new GetUsers().execute("https://schedadd-api.herokuapp.com/users/",ppt.getProperty("user"),ppt.getProperty("password"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        user = (EditText) findViewById(R.id.mailText);
        password = (EditText) findViewById(R.id.PasswordID);

        login = (CircularProgressButton) findViewById(R.id.Login);

    }

    public void setLogin(View view)
    {
        String username = user.getText().toString(),pass = password.getText().toString();

        login.startAnimation();

        new GetToken().execute("https://schedadd-api.herokuapp.com/get-token/",username,pass);
    }


    private class GetToken extends AsyncTask<String,Void,String>{

        private String user,pass;

        @Override
        protected String doInBackground(String... params) {

            Map<String,String> data = new HashMap<>();

            user = params[1];
            pass = params[2];

            data.put("username",user);
            data.put("password",pass);

           return HttpRequest.post(params[0]).form(data).body();
        }

        @Override
        protected void onPostExecute(String result) {
            //System.out.println(result);
            /*try {

                JSONObject jsonObject = new JSONObject();
                ppt.put("token",jsonObject.getString("token"));
                ppt.put("id",Integer.toString(jsonObject.getInt("id")));
                ppt.put("password",pass);
                ppt.put("user",user);

                FileOutputStream fo = openFileOutput(TOKENS,MODE_PRIVATE);

                ppt.storeToXML(fo,null);

                fo.close();

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                Toast.makeText(MainActivity.this,"Token no encontrado",Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(MainActivity.this,"Error al guardar sesion",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            try {
                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.getString("id") != null)
                    new GetUsers().execute("https://schedadd-api.herokuapp.com/users/",user,pass,jsonObject.getString("id"));

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"error de login",Toast.LENGTH_SHORT).show();
                login.revertAnimation();
            }


            //new GetUsers().execute("https://schedadd-api.herokuapp.com/users/",)
        }
    }

    private class GetUsers extends AsyncTask<String,Void,String>{

        private String id,user,pass;

        @Override
        protected String doInBackground(String... params) {

            user = params[1];
            pass = params[2];
            id = params[3];

            return HttpRequest.get(params[0]).accept("application/json").basic(params[1],params[2]).body();
        }

        @Override
        protected void onPostExecute(String result) {

            //Toast.makeText(MainActivity.this,id+" "+result,Toast.LENGTH_LONG).show();

            try {
                JSONArray jsonArray = new JSONArray(result);

                JSONObject jsonObject = new JSONObject(jsonArray.get(Integer.parseInt(id)-1).toString());

                int sons [] = new int[jsonObject.getJSONArray("sons").length()];

                for (int i=0;i<sons.length;i++)
                    sons[i] = jsonObject.getJSONArray("sons").getInt(i);

                AdUser = new User(jsonObject.getInt("id"),jsonObject.getString("username"),
                        jsonObject.getString("first_name"),
                        jsonObject.getString("last_name"),jsonObject.getString("email"),sons);

                //Toast.makeText(MainActivity.this,AdUser.getSons()[0]+" ",Toast.LENGTH_LONG).show();




            } catch (JSONException e) {
                //e.printStackTrace();
                login.revertAnimation();
            }

            login.revertAnimation();
        }
    }

    private class GetSons extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
