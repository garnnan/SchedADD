package com.addschedule.garnan.schedadd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by garnan on 12/11/2017.
 */
class LoadImage extends AsyncTask<String,Void,Bitmap>
{

    ImageView bmImageView;

    public LoadImage(ImageView bmImageView)
    {
        this.bmImageView = bmImageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap icon = null;
        try{
            InputStream in = new URL(params[0]).openStream();
            icon = BitmapFactory.decodeStream(in);
        }
        catch (Exception e)
        {

        }

        return icon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        bmImageView.setImageBitmap(bitmap);
    }
}