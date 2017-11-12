package com.addschedule.garnan.schedadd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.addschedule.garnan.schedadd.Api.Clases.Son;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TabActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    Drawable mydraw;
    TabLayout tabLayout;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private int index_id;
    private String username;
    private String password;
    private int sons[];


    Properties ppt;

    Son son;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*index_id = getIntent().getIntExtra("id",0);
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        sons = getIntent().getIntArrayExtra("sons");*/

        ppt = new Properties();

        try {

            FileInputStream fi = openFileInput(MainActivity.TOKENS);
            ppt.loadFromXML(fi);
            fi.close();

            if(!ppt.getProperty("id").equals(""))
            {
                //Toast.makeText(TabActivity.this,ppt.toString(),Toast.LENGTH_LONG).show();
                son = new Son(Integer.parseInt(ppt.getProperty("id_son")),ppt.getProperty("name"),
                        ppt.getProperty("lastname"),ppt.getProperty("birth"),ppt.getProperty("gender"),
                        ppt.getProperty("code"),ppt.getProperty("cel"),Integer.parseInt(ppt.getProperty("id")),
                        new int[]{Integer.parseInt(ppt.getProperty("schedules"))});
            }

        } catch (FileNotFoundException e) {
            //Toast.makeText(MainActivity.this,"no existe el archivo",Toast.LENGTH_SHORT).show();
        } catch (InvalidPropertiesFormatException e) {
            Toast.makeText(TabActivity.this,"las propiedades no salieron",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(TabActivity.this,"error de lectura",Toast.LENGTH_SHORT).show();
        }


        int [] iconos = new int[]{R.drawable.ic_notifications_active_black_24dp,R.drawable.ic_event_note_black_24dp,R.drawable.ic_settings_black_24dp};

        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_event_note_black_24dp);

        for (int i=0;i<iconos.length;i++)
            tabLayout.getTabAt(i).setIcon(iconos[i]);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position)
            {
                /*case 0:
                    return ChildSelector.newInstance(index_id,sons,username,password);
                case 1:
                    return Actividades.newInstance();
                case 2:
                    return AvatarSelector.newInstance();
                case 3:
                    return Preferences.newInstance();*/
                case 0:
                    return UniqueActivity.newInstance(ppt);
                case 1:
                    return ListaActividades.newInstance(ppt);
                case 2:
                    return Preferences.newInstance(ppt);
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            SpannableStringBuilder sb;
            ImageSpan span;
            switch (position) {
                case 0:
                    return "Actividad";
                case 1:
                    return "ListaActividades";
                case 2:
                    return "Preferencias";
            }
            return null;
        }
    }
}
