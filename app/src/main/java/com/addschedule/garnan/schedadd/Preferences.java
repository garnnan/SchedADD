package com.addschedule.garnan.schedadd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Preferences.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Preferences#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Preferences extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static Properties ppt;

    private OnFragmentInteractionListener mListener;

    public Preferences() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Preferences.
     */
    // TODO: Rename and change types and number of parameters
    public static Preferences newInstance(Properties p) {
        Preferences fragment = new Preferences();
        Bundle args = new Bundle();
        ppt = p;
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
        View v = inflater.inflate(R.layout.fragment_preferences, container, false);

        Button b = (Button) v.findViewById(R.id.logout);

        TextView nombre = (TextView) v.findViewById(R.id.Nombre_Pre);
        TextView cumple = (TextView) v.findViewById(R.id.cumple_pre);
        TextView sexo = (TextView) v.findViewById(R.id.genero_pre);

        nombre.append(ppt.getProperty("name")+" "+ppt.getProperty("lastname"));
        cumple.append(ppt.getProperty("birth"));
        sexo.append(ppt.getProperty("gender"));


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                View v1 = getActivity().getLayoutInflater().inflate(R.layout.codeinput,null);

                final EditText code = (EditText) v1.findViewById(R.id.codeInput);

                Button accp = (Button) v1.findViewById(R.id.LogCode);

                accp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(code.getText().toString().equals(ppt.getProperty("code")))
                        {
                            ppt.put("id","");

                            try {
                                FileOutputStream fos = getActivity().openFileOutput(MainActivity.TOKENS,getContext().MODE_PRIVATE);
                                ppt.storeToXML(fos,null);
                                fos.close();


                                Intent i = new Intent(getContext(),MainActivity.class);
                                startActivity(i);
                                getActivity().finish();

                            } catch (FileNotFoundException e) {
                                Toast.makeText(getActivity(),"Error al guardar la sesion",Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(getActivity(),"Error al guardar la el archivo",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getActivity(),"Codigo incorrecto para cerrar sesion",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setView(v1);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


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
