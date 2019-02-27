package com.example.app.bloodbank;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.app.bloodbank.fragmant.acount;
import com.example.app.bloodbank.fragmant.chat;
import com.example.app.bloodbank.fragmant.frag1;
import com.example.app.bloodbank.fragmant.frag2;
import com.example.app.bloodbank.fragmant.frag3;
import com.example.app.bloodbank.fragmant.frag5;
import com.example.app.bloodbank.fragmant.friendList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class    MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private DatabaseReference requestReference;
    int i = 0;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(this);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        requestReference = FirebaseDatabase.getInstance().getReference().child("req");
        requestReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                test u = new test();
                u.Id = dataSnapshot.getKey();
                u.email = dataSnapshot.child("email").getValue().toString();
                u.setTest(dataSnapshot.child("name").getValue().toString());
                u.setAddress(dataSnapshot.child("donation_address").getValue().toString());
                u.setAge(dataSnapshot.child("age").getValue().toString());
                u.setType(dataSnapshot.child("type_blood").getValue().toString());
                u.setDate(dataSnapshot.child("donation_id").getValue().toString());
                u.setNumber(dataSnapshot.child("num").getValue().toString());


                if (!u.email.equalsIgnoreCase(spp.getString("email","null"))) {
                    NowNotification nowNotification = new NowNotification();
                    nowNotification.notify(getApplicationContext(), u.getTest() + " يحتاج " + u.getNumber() + " وحدات دم : " + u.getType(), i);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position ==0)
                return new frag1();
            else if(position == 1)
                return new frag3();
            else if(position == 2)
                return new frag2();

            else if(position == 3)
                return new frag5();
            else if(position == 4)
            {
                return new friendList();
            }
            else {
                return new acount();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 7;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
