package com.addschedule.garnan.schedadd;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
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
        View v = inflater.inflate(R.layout.fragment_unique, container, false);

        new GetActivity(v).execute("https://schedadd-api.herokuapp.com/activities/",
                ppt.getProperty("username"),ppt.getProperty("password"));

        //new LoadImage((ImageView) v.findViewById(R.id.ActivityImage)).execute("http://papasabordo.com/Portal/papas-a-bordo/uploads/2015/03/consejos-tareas-1728x800_c.jpg");

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

            System.out.println(params[0]+"1");

            //HttpRequest.put(params[0]+"1").form(pt).accept(MainActivity.JSON_DATA).basic(params[1],params[2]);

            return HttpRequest.get(params[0]).basic(params[1],params[2]).body();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                final JSONArray jsonArray = new JSONArray(s);

                final JSONObject jsonObject = jsonArray.getJSONObject(0);

                TextView name = (TextView) v.findViewById(R.id.ActivityName);

                name.append(jsonObject.getString("name"));

                TextView description = (TextView) v.findViewById(R.id.Description2);

                description.setText(jsonObject.getString("description"));

                String st = jsonObject.getString("steps").replace(";","\n");

                TextView steps = (TextView) v.findViewById(R.id.ActivitySteps);

                steps.setText(st);

                Button panic = (Button) v.findViewById(R.id.PanicActivity);

                panic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            new PanicCall().execute("https://schedadd-api.herokuapp.com/panicbuttoncalls/",
                                    ppt.getProperty("username"),ppt.getProperty("password"),
                                    String.valueOf(jsonObject.getInt("id")),ppt.getProperty("id_son"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+ppt.getProperty("cel")));
                        //startActivity(intent);

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},1);
                            }
                            else
                            {
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            startActivity(intent);
                        }
                    }
                });


                new LoadImage((ImageView) v.findViewById(R.id.ActivityImage)).execute(jsonObject.getString("imagePath"));


            } catch (JSONException e) {
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
