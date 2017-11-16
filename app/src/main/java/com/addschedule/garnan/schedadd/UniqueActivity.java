package com.addschedule.garnan.schedadd;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UniqueActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UniqueActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UniqueActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imagenActividad;

    private  JSONObject object_finale;

    private int ind;
    private JSONArray jsonArray;

    private static Properties ppt;

    private OnFragmentInteractionListener mListener;

    public UniqueActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UniqueActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static UniqueActivity newInstance(Properties p) {
        UniqueActivity fragment = new UniqueActivity();
        Bundle args = new Bundle();

        ppt = p;

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_unique, container, false);

        new GetActivity(v).execute("https://schedadd-api.herokuapp.com/activities/",
                ppt.getProperty("username"),ppt.getProperty("password"));

        FloatingActionButton done = (FloatingActionButton) v.findViewById(R.id.Done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Toast.makeText(getActivity(),"hecho",Toast.LENGTH_SHORT).show();

                new PutState(v).execute("https://schedadd-api.herokuapp.com/activities/",ppt.getProperty("username"),
                            ppt.getProperty("password"),"Hecho");
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    private class PutState extends AsyncTask<String,Void,String>
    {
        View v;

        public PutState(View v)
        {
            this.v = v;
        }



        @Override
        protected String doInBackground(final String... params) {

            InputStream inputStream = null;
            String result = "";
            try {

                HttpClient httpClient = new DefaultHttpClient();

                HttpPut httpPut = new HttpPut(params[0]+object_finale.getInt("id")+"/");

                object_finale.put("state",params[3]);

                StringEntity se = new StringEntity(object_finale.toString(), HTTP.UTF_8);

                httpPut.setEntity(se);

                String basic = params[1]+":"+params[2];

                String auth = "Basic " + new String(Base64.encode(basic.getBytes(),Base64.NO_WRAP));

                httpPut.addHeader("Accept", "application/json");
                httpPut.addHeader("Content-type", "application/json");
                httpPut.addHeader("Authorization",auth);

                HttpResponse httpResponse = httpClient.execute(httpPut);

                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null) {
                    int bytechar;

                    while ((bytechar = inputStream.read()) != -1) {
                        result += (char) bytechar;
                    }
                }
                else
                    result = "Did not work!";


                /*

                System.out.println(params[0]+object_finale.getInt("id"));
                object_finale.put("state",params[3]);
                //System.out.println(object_finale.toString());

                Map<String,String> forma_string = new HashMap<>();
                Map<String,Integer> forma_int = new HashMap<>();

                forma_int.put("id",object_finale.getInt("id"));
                forma_string.put("name",object_finale.getString("name"));
                forma_string.put("description",object_finale.getString("description"));
                forma_string.put("state",object_finale.getString("state"));
                forma_string.put("steps",object_finale.getString("steps"));
                forma_string.put("imagePath",object_finale.getString("imagePath"));
                forma_string.put("date",object_finale.getString("date"));
                forma_int.put("duration",object_finale.optInt("duration"));
                forma_int.put("scheduleID",object_finale.optInt("scheduleID"));
                forma_int.put("parentID",object_finale.optInt("parentID"));*/

                //return HttpRequest.put(params[0]+object_finale.getInt("id")).basic(params[1],params[2]).form(forma_int).form(forma_string).body();
                //return HttpRequest.put(params[0]+object_finale.getInt("id")).basic(params[1],params[2]).header(MainActivity.JSON_DATA,object_finale.toString()).body();
                /*return HttpRequest.put(params[0]+object_finale.getInt("id"),true,"id",object_finale.getInt("id"),"name",object_finale.getString("name"),
                        "description",object_finale.getString("description"),"state",object_finale.getString("state"),
                        "steps",object_finale.getString("steps"),"imagePath",object_finale.getString("imagePath"),
                        "date",object_finale.getString("date"),"duration",object_finale.optInt("duration"),
                        "scheduleID",object_finale.optInt("scheduleID"),"parentID",object_finale.optInt("parentID")).basic(params[1],params[2]).acceptJson().body();*/

                /*String basic = params[1]+":"+params[2];

                String auth = "Basic " + new String(Base64.encode(basic.getBytes(),Base64.NO_WRAP));

                String finale = object_finale.toString().replace("\\","");

                System.out.println(finale);

                URL url = new URL(params[0]+object_finale.getInt("id"));

                System.out.println(auth);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("PUT");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.addRequestProperty("Authorization",auth);
                httpURLConnection.addRequestProperty("Content-Type",MainActivity.JSON_DATA);
                httpURLConnection.connect();

                OutputStream dos = httpURLConnection.getOutputStream();
                dos.write(finale.getBytes());


                InputStream is = httpURLConnection.getInputStream();
                String result = "";

                int bytechar;

                while ((bytechar = is.read())!=-1){
                    result += (char) bytechar;
                }

                is.close();
                dos.close();
                httpURLConnection.disconnect();

                return result;*/





                /*return HttpRequest.put(params[0]+object_finale.getInt("id"))
                        .contentType("application/json")
                        .basic(params[1],params[2])
                        .
                        .body();*/

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            new GetActivity(v).execute("https://schedadd-api.herokuapp.com/activities/",
                    ppt.getProperty("username"),ppt.getProperty("password"));
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class GetActivity extends AsyncTask<String,Void,String>
    {
        private View v;

        public GetActivity(View v)
        {
            this.v = v;
        }

        @Override
        protected String doInBackground(String... params) {

            Map<String,String> pt = new HashMap<>();

            pt.put("name","jugar xbox");



            //System.out.println(params[0]+"1");

            //HttpRequest.put(params[0]+"1").form(pt).accept(MainActivity.JSON_DATA).basic(params[1],params[2]);

            return HttpRequest.get(params[0]).basic(params[1],params[2]).body();
        }


        private JSONObject Activo(JSONArray jsonArray) throws JSONException, ParseException {

            Date date = new Date();

            String date1 = new SimpleDateFormat("yyyy-MM-dd").format(date);

            String hour = new SimpleDateFormat("hh:mm:ss").format(date);

            String convertercomp = date1+"T"+hour+"Z";

            //System.out.println(convertercomp);

            //System.out.println(jsonArray.getJSONObject(0).getString("date"));

            for (int i=0;i<jsonArray.length();i++)
                if ((convertercomp.compareTo(jsonArray.getJSONObject(i).getString("date")))<=0
                        && jsonArray.getJSONObject(i).getString("state").equals("Pendiente")) {
                    ind = i;
                    return jsonArray.getJSONObject(i);

                }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                jsonArray = new JSONArray(s);

                List<JSONObject> lista = new ArrayList<>();

                for (int i=0;i<jsonArray.length();i++)
                {
                    lista.add(jsonArray.getJSONObject(i));
                }

                Collections.sort(lista, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        try {
                            return o1.getString("date").compareTo(o2.getString("date"));
                        } catch (JSONException e) {
                            return 0;
                        }
                    }
                });

                for (int i=0;i<lista.size();i++)
                    jsonArray.put(i,lista.get(i));


                final JSONObject jsonObject = Activo(jsonArray);

                object_finale = jsonObject;

                if(jsonObject!=null) {

                    TextView name = (TextView) v.findViewById(R.id.ActivityName);

                    name.setText("Nombre: "+jsonObject.getString("name"));

                    TextView description = (TextView) v.findViewById(R.id.Description2);

                    description.setText(jsonObject.getString("description"));

                    String st = jsonObject.getString("steps").replace(";", "\n");

                    TextView steps = (TextView) v.findViewById(R.id.ActivitySteps);

                    steps.setText(st);

                    Button panic = (Button) v.findViewById(R.id.PanicActivity);

                    panic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                new PanicCall().execute("https://schedadd-api.herokuapp.com/panicbuttoncalls/",
                                        ppt.getProperty("username"), ppt.getProperty("password"),
                                        String.valueOf(jsonObject.getInt("id")), ppt.getProperty("id_son"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + ppt.getProperty("cel")));
                            //startActivity(intent);

                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                                } else {
                                    startActivity(intent);
                                }
                            } else {
                                startActivity(intent);
                            }
                        }
                    });


                    new LoadImages((ImageView) v.findViewById(R.id.ActivityImage)).execute(jsonObject.getString("imagePath"));
                }

                else {



                    TextView name = (TextView) v.findViewById(R.id.ActivityName);

                    name.setEnabled(false);

                    TextView description = (TextView) v.findViewById(R.id.Description2);

                    description.setEnabled(false);

                    TextView steps = (TextView) v.findViewById(R.id.ActivitySteps);

                    steps.setEnabled(false);

                    Button panic = (Button) v.findViewById(R.id.PanicActivity);

                    panic.setEnabled(false);

                    ((ImageView) v.findViewById(R.id.ActivityImage)).setEnabled(false);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }



    private class PanicCall extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {

            Map<String,Integer> call = new HashMap<>();

            System.out.println(params[1]);

            call.put("activityID",Integer.parseInt(params[3]));
            call.put("sonID",Integer.parseInt(params[4]));

            return HttpRequest.post(params[0]).basic(params[1],params[2]).form(call).body();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
        }
    }

}
