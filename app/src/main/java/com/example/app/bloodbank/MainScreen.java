
package com.example.app.bloodbank;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.app.bloodbank.fragmant.accept;
import com.example.app.bloodbank.fragmant.acount;
import com.example.app.bloodbank.fragmant.frag1;
import com.example.app.bloodbank.fragmant.frag2;
import com.example.app.bloodbank.fragmant.frag3;
import com.example.app.bloodbank.fragmant.friendList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.app.bloodbank.fragmant.frag2.md5;

public class MainScreen extends AppCompatActivity {
    private DatabaseReference requestReference;
    private FirebaseUser curranr_user ;
    int i = 0;
    private DatabaseReference databaseReference;
    private  FirebaseUser firebaseUser ;
    String type ;

    private DatabaseReference acceptReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Blood Bank");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        curranr_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(curranr_user.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                type= dataSnapshot.child("type").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(this);
        requestReference = FirebaseDatabase.getInstance().getReference().child("req");
        requestReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("type_blood").getValue().toString().equals(type) && !dataSnapshot.getKey().equals(curranr_user.getUid())) {

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
                    nowNotification.notify(getApplicationContext(), u.getTest() + " يحتاج " +"\n"+ u.getNumber() + " وحدات دم : " + u.getType(), i);
                }
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
        acceptReference = FirebaseDatabase.getInstance().getReference().child("accept");




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent intent = new Intent(MainScreen.this,login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void  move_search_page(View v)
    {
        frag1 f= new frag1();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentM,f);
        ft .addToBackStack(null);
        ft.commit();

    }

public void  Mform(View v)
    {
        frag3 f= new frag3();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentM,f);
        ft .addToBackStack(null);
        ft.commit();

    }

public void  Mfrind(View v)
    {
        friendList f= new friendList();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentM,f);
        ft .addToBackStack(null);
        ft.commit();

    }

public void  Maccept(View v)
    {
        accept f= new accept();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentM,f);
        ft .addToBackStack(null);
        ft.commit();

    }

    public void  Mhowneed(View v)
    {
        frag2 f= new frag2();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentM,f);
        ft .addToBackStack(null);
        ft.commit();

    }


 public void  Mdetails(View v)
    {
        acount f= new acount();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.contentM,f);
        ft .addToBackStack(null);
        ft.commit();

    }
    



}
