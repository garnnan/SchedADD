package com.addschedule.garnan.schedadd;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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

    private OnFragmentInteractionListener mListener;

    public Actividades() {
        // Required empty public constructor
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

        /*ExpandableListView expandableListView = (ExpandableListView) getActivity().findViewById(R.id.actividadesList);


        List<String> actividades = new ArrayList<>();
        HashMap<String,List<String>> stringListHashMap = new HashMap<>();

        actividades.add("actividad1");
        actividades.add("actividad2");

        List<String> subA = new ArrayList<>();
        subA.add("tas 1.1");

        List<String> subB = new ArrayList<>();
        subB.add("tas 2.1");
        subB.add("tas 2.3");

        stringListHashMap.put(actividades.get(0),subA);
        stringListHashMap.put(actividades.get(1),subB);

        ExpandableListAdapter expandableListAdapter = new ExpandableAdapter(getActivity(),actividades,stringListHashMap);

        expandableListView.setAdapter(expandableListAdapter);*/

        return inflater.inflate(R.layout.fragment_actividades, container, false);
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
            return 0;
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
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listaexpandible,null);
            }

            TextView texto = (TextView) convertView.findViewById(R.id.textoExpandible);

            texto.setText(childtext);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
