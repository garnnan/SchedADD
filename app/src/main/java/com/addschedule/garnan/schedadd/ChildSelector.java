package com.addschedule.garnan.schedadd;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.addschedule.garnan.schedadd.Api.Clases.Schedules;
import com.addschedule.garnan.schedadd.Api.Clases.User;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChildSelector.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChildSelector#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildSelector extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;

    public ChildSelector() {
        // Required empty public constructor
    }


    private int id;
    private int [] sons;
    private ArrayList<Schedules> schedules;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChildSelector.
     */
    // TODO: Rename and change types and number of parameters
    public static ChildSelector newInstance(int id,int [] sons) {
        ChildSelector fragment = new ChildSelector();
        Bundle args = new Bundle();
        args.putInt("id",id);
        args.putIntArray("sons",sons);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        this.id = getArguments().getInt("id");
        this.sons = getArguments().getIntArray("sons");

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_child_selector, container, false);

        class GetSchedules extends AsyncTask<String,Void,String> {

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
                    Toast.makeText(getActivity(),"No hay resultados",Toast.LENGTH_LONG);
                }
                else
                {

                    try {
                        JSONArray jsonArray = new JSONArray(result);

                        schedules = new ArrayList<>();

                        for (int i=0;i<jsonArray.length();i++){
                            if(jsonArray.getJSONObject(i).getInt("parentID")==id) {

                                JSONArray sub = jsonArray.getJSONObject(i).getJSONArray("activities");

                                JSONObject obj = jsonArray.getJSONObject(i);

                                int activities [] = new int[sub.length()];

                                for (int j=0;j<sub.length();j++)
                                    activities[j] = sub.getInt(j);

                                schedules.add(new Schedules(obj.getInt("id"),obj.getInt("sonID"),obj.getString("name"),activities));

                                System.out.println(schedules.get(i).getName());
                            }
                        }

                        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.ListaChild);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());

                        recyclerView.setLayoutManager(layoutManager);

                        RecyclerView.Adapter adapter = new RecyclerAdapter();

                        recyclerView.setAdapter(adapter);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        new GetSchedules().execute("https://schedadd-api.herokuapp.com/schedules/");

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

    private class KidsAdapter extends ArrayAdapter<String>
    {

        public KidsAdapter(Context context, int resource, List<String> lista) {
            super(context, resource,lista);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
            {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.kids,null);

            }

            TextView name = (TextView) convertView.findViewById(R.id.name);

            name.setText(getItem(position));

            return convertView;

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            if(convertView==null)
            {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.kids,null);

            }

            TextView name = (TextView) convertView.findViewById(R.id.name);

            name.setText(getItem(position));

            return convertView;
        }
    }



    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


        List<String> list = new ArrayList<String>();

        public RecyclerAdapter () {
            list.add("Daniel");
            list.add("Ricardo");
            list.add("romulo");
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kids,null);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(schedules.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return schedules.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView name;


            //TextView name = (TextView) convertView.findViewById(R.id.name);

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);

            }
        }
    }
}
