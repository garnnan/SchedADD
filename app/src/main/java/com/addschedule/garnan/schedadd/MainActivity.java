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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    CircularProgressButton login;

    ArrayList<User> users;

    EditText user;

    public final static String FILE_ACTIVITIES_SCHEDULE = "schedules.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.mailText);

        new GetUsers().execute("https://schedadd-api.herokuapp.com/users/");

        login = (CircularProgressButton) findViewById(R.id.Login);

    }

    public void setLogin(View view)
    {
        AsyncTask<String,String,String> demo = new AsyncTask<String, String, String>() {

            int index;

            @Override
            protected String doInBackground(String... params) {
                /*try{
                    Thread.sleep(3000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }*/

                String comp = user.getText().toString();
                String finale = "notdone";

                for (int i=0;i<users.size();i++)
                    if(users.get(i).getUsername().equals(comp)) {
                        finale = "done";
                        index = i;
                    }

                return finale;
            }

            @Override
            protected void onPostExecute(String s) {
                System.out.println(s);
                if(s.equals("done")){
                    login.startAnimation();

                    User u = users.get(index);

                    Intent i = new Intent(MainActivity.this,TabActivity.class);
                    i.putExtra("id",u.getId());
                    i.putExtra("username",u.getUsername());
                    i.putExtra("sons",u.getSons());
                    startActivity(i);
                    finish();
                }
                if(s.equals("notdone")){
                    login.stopAnimation();
                    Toast.makeText(MainActivity.this,"Login incorrecto",Toast.LENGTH_LONG).show();
                }
            }
        };

        demo.execute();
    }

    private class GetUsers extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            try {

                return HttpRequest.get(params[0]).accept("application/json").basic("raglar","password1234").body();
            }catch (Exception e){
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.isEmpty())
            {
                Toast.makeText(MainActivity.this,"No hay resultados",Toast.LENGTH_LONG);
            }
            else
            {
                //System.out.println(result);

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    users = new ArrayList<>();

                    for(int i=0;i<jsonArray.length();i++) {
                        System.out.println(jsonArray.get(i));
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //System.out.println(jsonObject.getString("username"));

                        JSONArray subarray = jsonObject.getJSONArray("sons");

                        int [] sons = new int [subarray.length()];

                        for (int j=0;j<subarray.length();j++) {
                            sons[j] = subarray.getInt(j);
                            //System.out.println(sons[j]);
                        }

                        users.add(new User(jsonObject.getInt("id"),jsonObject.getString("username")
                        ,jsonObject.getString("first_name"),jsonObject.getString("last_name")
                        ,jsonObject.getString("email"),sons));

                        System.out.println(users.get(i).getId());
                    }

                    //System.out.println(users.get(0).getFirst_name());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*ArrayList<User> Users = User.Users(result);
                ArrayList<User> AUX = new ArrayList<>();

                for (User u: Users) {
                    System.out.println(u);
                }*/
            }
        }
    }
}
