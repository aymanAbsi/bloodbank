package com.example.app.bloodbank;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class howneed extends Fragment {
    List<test> users;
    ListView lvUsers;
    private DatabaseReference requestReference;
    private Query query;
    private DatabaseReference acceptReference;
    private DatabaseReference singleaccept;
    private FirebaseUser curranr_user ;
    ArrayAdapter<test> a;
    View v;
    String phone,name,email;
    String type ;
    private DatabaseReference databaseReference;
    private  FirebaseUser firebaseUser ;


    public howneed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_howneed, container, false);
        if(v == null)
        {
            requestReference = FirebaseDatabase.getInstance().getReference().child("req");
            query = FirebaseDatabase.getInstance().getReference().child("req");
            acceptReference = FirebaseDatabase.getInstance().getReference().child("accept");
            singleaccept = FirebaseDatabase.getInstance().getReference().child("reqq");
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








        }
        return v ;
    }

}
