package com.clothapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.clothapp.home_gallery.HomeMostRecentFragment;
import com.clothapp.resources.ApplicationSupport;
import com.clothapp.resources.Cloth;
import com.clothapp.resources.Image;
import com.clothapp.resources.MyCardListAdapter;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.clothapp.resources.ExceptionCheck.check;

public class ImageFragment extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    private boolean load=true;
    private int n;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;
    List<Image> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //prendo la lista delle immagini
        lista = getIntent().getParcelableArrayListExtra("lista");
        setContentView(R.layout.fragment_image);
        mPager = (ViewPager) findViewById(R.id.pager);



        //creo adattatore da pasare al ViewPager
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), lista.size());
        mPager.setAdapter(mPagerAdapter);
        // Set the current item based on the extra passed in to this activity
        final int extraCurrentItem = getIntent().getIntExtra("position", -1);
        if (extraCurrentItem != -1) {
            mPager.setCurrentItem(extraCurrentItem);
        }

        //se arrivo in fondo carico nuove foto
        n=mPager.getCurrentItem();
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //controllo di essere arrivata in fondo
                n = mPager.getCurrentItem();
                if (load && n == mPagerAdapter.getCount() - 1) {
                    load = false;
                    //aggiungo le nuove immagini
                    findMostRecent(mPagerAdapter.getCount(), 1);
                    mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), lista.size());
                    mPager.setAdapter(mPagerAdapter);
                    mPager.setCurrentItem(n);
                    load=true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private int npages;

        public ScreenSlidePagerAdapter(FragmentManager fm, int npages) {
            super(fm);
            this.npages = npages;
        }

        @Override
        public Fragment getItem(int position) {
            return new ImageDetailFragment().newInstance(lista.get(position).getObjectId(),getApplicationContext());
        }

        @Override
        public int getCount() {
            return npages;
        }


    }


//TODO:fare la query giusta
    public void findMostRecent(int start,int n){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Photo");
        ParseObject o= null;
        try {
            o = query.getFirst();
            System.out.println("nuova foto "+o);
            lista.add(new Image(o.getParseFile("thumbnail").getFile(), o.getObjectId(), o.getString("user"), o.getList("like")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
