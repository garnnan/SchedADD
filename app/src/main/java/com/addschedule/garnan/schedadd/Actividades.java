package com.addschedule.garnan.schedadd;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.addschedule.garnan.schedadd.Api.Clases.Activity;
import com.addschedule.garnan.schedadd.Api.Clases.Schedules;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Actividades.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Actividades#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Actividades extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Activity> activities;

    private OnFragmentInteractionListener mListener;

    private Properties ppt;

    public Actividades() {
        // Required empty public constructor

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Actividades.
     */
    // TODO: Rename and change types and number of parameters
    public static Actividades newInstance() {
        Actividades fragment = new Actividades();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ppt = new Properties();

        int idSchedule = 0;
        int parentID = 0;
        int sonID = 0;

        try {
            FileInputStream fi = getActivity().openFileInput(MainActivity.FILE_ACTIVITIES_SCHEDULE);
            ppt.loadFromXML(fi);
            fi.close();
            idSchedule = Integer.parseInt(ppt.getProperty("id"));
            parentID = Integer.parseInt(ppt.getProperty("parentID"));
            sonID = Integer.parseInt(ppt.getProperty("sonID"));

        }catch (Exception e){}

        final View v = inflater.inflate(R.layout.fragment_actividades, container, false);

        final int finalParentID = parentID;
        final int finalIdSchedule = idSchedule;
        class GetActivities extends AsyncTask<String,Void,String> {

            @Override
            protected String doInBackground(String... params) {

                try {
                    //no harcodear
                    return HttpRequest.get(params[0]).accept("application/json").basic("raglar","password1234").body();

                }catch (Exception e){
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.isEmpty())
                {
                    Toast.makeText(getActivity(),"No hay resultados",Toast.LENGTH_LONG);
                }
                else
                {

                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        activities = new ArrayList<>();

                        for (int i=0;i<jsonArray.length();i++)
                            if(jsonArray.getJSONObject(i).getInt("parentID")== finalParentID && jsonArray.getJSONObject(i).getInt("scheduleID") == finalIdSchedule) {
                                //System.out.println(jsonArray.getJSONObject(i).toString());
                                JSONObject obj = jsonArray.getJSONObject(i);
                                activities.add(new Activity(obj.getInt("id"),obj.getString("name"),obj.getString("description"),
                                        obj.getString("state"),obj.getString("steps"),obj.getString("imagePath"),obj.getString("date"),
                                        obj.getInt("duration"),obj.getInt("scheduleID"),obj.getInt("parentID")));
                            }

                        ExpandableListView expandableListView = (ExpandableListView) v.findViewById(R.id.actividadesList);
                        final List<String> actividades = new ArrayList<>();
                        final HashMap<String,List<String>> stringListHashMap = new HashMap<>();

                        for (int i=0;i<activities.size();i++)
                        {
                            String steps = activities.get(i).getSteps().replaceAll(";","\n");

                            actividades.add(activities.get(i).getName());
                            List<String> tmp = new ArrayList<>();

                            tmp.add(activities.get(i).getDescripcion()+"\n"+steps+"\n"+activities.get(i).getDate()+"\n"+activities.get(i).getDuracion());

                            stringListHashMap.put(actividades.get(i),tmp);
                        }

                        ExpandableListAdapter expandableListAdapter = new ExpandableAdapter(v.getContext(),actividades,stringListHashMap);

                        expandableListView.setAdapter(expandableListAdapter);

                        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                                DialogFragment dl = new Dialog_activity();

                                dl.show(getActivity().getFragmentManager(),stringListHashMap.get(actividades.get(groupPosition)).get(childPosition));

                                return true;
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        new GetActivities().execute("https://schedadd-api.herokuapp.com/activities/");

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

    private class ExpandableAdapter extends BaseExpandableListAdapter{

        private Context context;
        private List<String> list;
        private HashMap<String,List<String>> hashMap;

        public ExpandableAdapter(Context context, List<String> list, HashMap<String, List<String>> hashMap) {
            this.context = context;
            this.list = list;
            this.hashMap = hashMap;
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return hashMap.get(list.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return hashMap.get(list.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String header = (String) getGroup(groupPosition);
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listaexpandible,null);
            }

            TextView texto = (TextView) convertView.findViewById(R.id.textoExpandible);

            texto.setText(header);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String childtext = (String) getChild(groupPosition,childPosition);
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listaexpandible,null);
            }

            TextView texto = (TextView) convertView.findViewById(R.id.textoExpandible);
            texto.setTextSize(18);

            texto.setText(childtext);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
