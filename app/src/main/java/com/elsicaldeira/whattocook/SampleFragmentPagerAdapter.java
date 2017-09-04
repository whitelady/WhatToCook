package com.elsicaldeira.whattocook;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Elsi on 18/08/2015.
 */
public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String recipeId;
    String sourceUrl;
    private Context context;
    private Fragment mCurrentFragment;

    public SampleFragmentPagerAdapter(FragmentManager fm, int NumOfTabs, String recipeId, String sourceUrl) {
        super(fm);
        //this.context = context;
        this.mNumOfTabs = NumOfTabs;
        this.recipeId = recipeId;
        this.sourceUrl = sourceUrl;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
               //IngredientsTab tab1 = new IngredientsTab();
                mCurrentFragment = IngredientsTab.newInstance(recipeId);
                return mCurrentFragment;
               //return tab1;
            case 1:
                /*DirectionsTab tab2 = new DirectionsTab();
                return tab2;*/
                mCurrentFragment = DirectionsTab.newInstance(sourceUrl);
                return mCurrentFragment;
            default:
                return null;
        }
    }


   /* @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }*/
}
