package com.thesis.dont.loyaltypointuser.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;

public class CardDetailActivity extends ActionBarActivity {

    int tabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();

        if(tabIndex == 0)
            tabIndex = i.getIntExtra(Global.TAB_INDEX, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        setTitle("card name");
        pager.setAdapter(new CardDetailTabPagerAdapter(getSupportFragmentManager(),"cardIdSample"));
        //Toast.makeText(this,shopId,Toast.LENGTH_LONG).show();

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTabPaddingLeftRight(70);
        tabs.setBackgroundResource(R.color.PrimaryColor);
        tabs.setTextColorResource(R.color.TextIconsHide);
        tabs.setIndicatorColorResource(R.color.TextIcons);
        tabs.setIndicatorHeight(tabs.getIndicatorHeight()+5);
        getSupportActionBar().setElevation(0);

        pager.setCurrentItem(tabIndex);
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}