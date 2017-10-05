package com.addschedule.garnan.schedadd;

import android.content.Context;
import android.net.Uri;
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChildSelector.
     */
    // TODO: Rename and change types and number of parameters
    public static ChildSelector newInstance() {
        ChildSelector fragment = new ChildSelector();
        Bundle args = new Bundle();
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

       View v = inflater.inflate(R.layout.fragment_child_selector, container, false);

       /* Spinner sp = (Spinner) v.findViewById(R.id.children);

        List<String> list = new ArrayList<String>();

        list.add("Daniel");
        list.add("Ricardo");

        ArrayAdapter adapter = new KidsAdapter(getActivity(),R.layout.kids,list);

        //adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        sp.setAdapter(adapter);*/

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.ListaChild);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        RecyclerView.Adapter adapter = new RecyclerAdapter();

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);


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
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kids,null);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
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
