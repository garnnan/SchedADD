package com.addschedule.garnan.schedadd;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.addschedule.garnan.schedadd.Api.Clases.Son;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    CircularProgressButton login;

    User AdUser;

    EditText user;
    EditText password;

    List<Son> sons;

    Properties ppt;

    public final static String FILE_ACTIVITIES_SCHEDULE = "schedules.xml",
    JSON_DATA = "application/json",TOKENS = "tokens.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ppt = new Properties();

        try {

            FileInputStream fi = openFileInput(TOKENS);
            ppt.loadFromXML(fi);
            fi.close();

            if(!ppt.getProperty("id").equals(""))
            {
                Intent i = new Intent(MainActivity.this,TabActivity.class);
                startActivity(i);
            }

        } catch (FileNotFoundException e) {
            //Toast.makeText(MainActivity.this,"no existe el archivo",Toast.LENGTH_SHORT).show();
        } catch (InvalidPropertiesFormatException e) {
            Toast.makeText(MainActivity.this,"las propiedades no salieron",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"error de lectura",Toast.LENGTH_SHORT).show();
        }

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

    private int BuscarChild(String code)
    {
        for (int i=0;i<sons.size();i++)
            if(sons.get(i).getCode().equals(code))
                return i;
        return -1;
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


                new GetSons().execute("https://schedadd-api.herokuapp.com/sons/",id,user,pass);

            } catch (JSONException e) {
                //e.printStackTrace();
                login.revertAnimation();
            }


        }
    }

    private class GetSons extends AsyncTask<String,Void,String>
    {

        private String id,user,pass;

        @Override
        protected String doInBackground(String... params) {
            id = params[1];
            user = params[2];
            pass = params[3];
            return HttpRequest.get(params[0]).basic(user,pass).body();
        }

        @Override
        protected void onPostExecute(String s) {

            sons = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i=0;i<jsonArray.length();i++){

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int sche [] = new int[jsonObject.getJSONArray("schedules").length()];

                    for (int j=0;j<sche.length;j++)
                        sche[j] = jsonObject.getJSONArray("schedules").getInt(j);

                    sons.add(new Son(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getString("lastName"),
                            jsonObject.getString("birthday"),jsonObject.getString("gender"),jsonObject.getString("code"),
                            jsonObject.getString("cellphone"),jsonObject.getInt("parentID"),sche));

                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                View v = getLayoutInflater().inflate(R.layout.codeinput,null);

                final EditText code = (EditText) v.findViewById(R.id.codeInput);

                Button accp = (Button) v.findViewById(R.id.LogCode);

                accp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int codex = BuscarChild(code.getText().toString());
                        if(codex!=-1)
                        {
                            //Toast.makeText(MainActivity.this,"se concecto con exito",Toast.LENGTH_SHORT).show();
                            //ppt.put("user",AdUser);
                            //ppt.put("son",sons.get(codex));

                            Son s = sons.get(codex);

                            ppt.put("id",AdUser.getId()+"");
                            ppt.put("id_son",s.getId()+"");
                            ppt.put("code",s.getCode()+"");
                            ppt.put("schedules",s.getSchedules()[0]+"");
                            ppt.put("cel",s.getCellphone()+"");
                            ppt.put("name",s.getName()+"");
                            ppt.put("gender",s.getGender());
                            ppt.put("lastname",s.getLastName());
                            ppt.put("birth",s.getBithday()+"");

                            ppt.put("username",user);
                            ppt.put("password",pass);


                            //Toast.makeText(MainActivity.this,ppt.toString(),Toast.LENGTH_SHORT).show();

                            try {
                                FileOutputStream fos = openFileOutput(TOKENS,MODE_PRIVATE);
                                ppt.storeToXML(fos,null);
                                fos.close();


                                Intent i = new Intent(MainActivity.this,TabActivity.class);

                                startActivity(i);


                            } catch (FileNotFoundException e) {
                                Toast.makeText(MainActivity.this,"Error al guardar la sesion",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(MainActivity.this,"Error al guardar la el archivo",Toast.LENGTH_SHORT).show();
                            }


                        }
                        else
                            Toast.makeText(MainActivity.this,"error con el codigo",Toast.LENGTH_LONG).show();
                    }
                });

                builder.setView(v);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                login.revertAnimation();


            } catch (JSONException e) {
                e.printStackTrace();
                login.revertAnimation();
            }

        }
    }
}
