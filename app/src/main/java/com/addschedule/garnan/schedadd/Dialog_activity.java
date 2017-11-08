package com.addschedule.garnan.schedadd;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by garnan on 23/10/2017.
 */

public class Dialog_activity extends DialogFragment {

    Button panic, Done;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.popup_fragment, container, false);

        Done = (Button) v.findViewById(R.id.Done_Button);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        final Bundle bundle = savedInstanceState;

        panic = (Button) v.findViewById(R.id.Panic_Button);

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PanicCall().execute("https://schedadd-api.herokuapp.com/panicbuttoncalls/","2","1");
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:123"));
                startActivity(intent);
            }
        });

        return v;
    }

    private class PanicCall extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {

            Map<String,Integer> call = new HashMap<>();

            call.put("activityID",Integer.parseInt(params[1]));
            call.put("sonID",Integer.parseInt(params[2]));

            return HttpRequest.post(params[0]).form(call).body();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
        }
    }

}
