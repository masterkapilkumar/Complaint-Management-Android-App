package com.cyberthieves.complaintapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import com.cyberthieves.complaintapp.fragments.*;

//This activity sets the fragments to display the details of complains. There are 3 fragments to display
//the complaint details: to view all complaints, to view unresolved complaints and to view complaints
//belonging to the logged in user
public class ComplaintDetails extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String hostel;
    private int person_id;
    private Toolbar toolbar;
    static private final String TAG = "Complaints-App";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hostel = getIntent().getExtras().getString("hostel");
        person_id = getIntent().getExtras().getInt("person_id");
        Log.d(TAG, "hostel set in ComplaintDetails:  "+hostel);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //set up the tabs to switch between fragments
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    //this function sets up the fragments
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        int id = getIntent().getExtras().getInt("item_id");
        int type = getIntent().getExtras().getInt("type");
        All all = new All();
        all.hostel = hostel;
        all.item_id=id;
        all.type=type;
        all.person_id = person_id;

        MyComplaints mycomplaints = new MyComplaints();
        mycomplaints.hostel = hostel;
        mycomplaints.item_id=id;
        mycomplaints.type=type;
        mycomplaints.person_id = person_id;

        Unresolved unresolved = new Unresolved();
        unresolved.hostel = hostel;
        unresolved.item_id=id;
        unresolved.type=type;
        unresolved.person_id=person_id;

        adapter.addFrag(all, "All");
        adapter.addFrag(unresolved, "Unresolved");

        if(id!=1) {
            adapter.addFrag(mycomplaints, "My Complaints");
        }

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
