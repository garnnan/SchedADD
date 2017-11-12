package com.addschedule.garnan.schedadd;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.addschedule.garnan.schedadd.Api.Clases.Activity;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaActividades.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaActividades#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaActividades extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static Properties ppt;

    private OnFragmentInteractionListener mListener;

    public ListaActividades() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListaActividades.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaActividades newInstance(Properties p) {
        ListaActividades fragment = new ListaActividades();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        ppt = p;
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
        View v = inflater.inflate(R.layout.fragment_lista_actividades, container, false);

        new GetActivities(v).execute("https://schedadd-api.herokuapp.com/activities/",
                ppt.getProperty("username"),ppt.getProperty("password"));

        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    private class GetActivities extends AsyncTask<String,Void,String>
    {

        private View v;

        public GetActivities(View v){
            this.v = v;
        }

        @Override
        protected String doInBackground(String... params) {
            return HttpRequest.get(params[0]).basic(params[1],params[2]).body();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);

                List<Activity> objects = new ArrayList<>();

                for (int i=0;i<jsonArray.length();i++)
                    if(jsonArray.getJSONObject(i).getInt("scheduleID")==Integer.parseInt(ppt.getProperty("schedules")))
                        objects.add(new Activity(jsonArray.getJSONObject(i).getInt("id"),
                                jsonArray.getJSONObject(i).getString("name"),jsonArray.getJSONObject(i).getString("description"),
                                jsonArray.getJSONObject(i).getString("state"),jsonArray.getJSONObject(i).getString("steps")
                                ,jsonArray.getJSONObject(i).getString("imagePath"),
                                jsonArray.getJSONObject(i).getString("date"),jsonArray.getJSONObject(i).getInt("duration"),
                                jsonArray.getJSONObject(i).getInt("scheduleID"),jsonArray.getJSONObject(i).getInt("parentID")));


                //System.out.println(objects.get(0).getImage());

                Collections.sort(objects, new Comparator<Activity>() {
                    @Override
                    public int compare(Activity o1, Activity o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });


                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.ListadoActividades);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());

                recyclerView.setLayoutManager(layoutManager);

                RecyclerView.Adapter adapter = new RecyclerAdapter(objects);

                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


        List<Activity> list;

        public RecyclerAdapter (List<Activity> list) {
            this.list = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.actividad_single,null);
            ListaActividades.RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
            holder.name.setText(list.get(position).getName());
            //holder.imagepath = list.get(position).getImage();

            new LoadImages((ImageView) holder.itemView.findViewById(R.id.ImagenActividadLista)).execute(list.get(position).getImage());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView name;
            public String imagepath;

            //TextView name = (TextView) convertView.findViewById(R.id.name);

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.nombreActividadListada);
                //new LoadImages((ImageView) itemView.findViewById(R.id.ImagenActividadLista)).execute(imagepath);
            }


        }
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
}
