package com.example.app.bloodbank.fragmant;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.app.bloodbank.AcceptModel;
import com.example.app.bloodbank.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.app.bloodbank.fragmant.frag2.md5;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag5 extends Fragment {
    List<AcceptModel> users;
    ListView lvUsers;

    private DatabaseReference acceptReference;
    private DatabaseReference friend;
    private FirebaseUser currnat_user ;
    ArrayAdapter<AcceptModel> a;
    View v;
    SharedPreferences spp;

    int i ;
    public frag5() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (v== null) {
            // Inflate the layout for this fragment
            v = inflater.inflate(R.layout.fragment_frag5, container, false);
            lvUsers = v.findViewById(R.id.llaccept);
            users = new ArrayList<>();
            acceptReference = FirebaseDatabase.getInstance().getReference().child("accept");
            friend = FirebaseDatabase.getInstance().getReference().child("friend");
            currnat_user = FirebaseAuth.getInstance().getCurrentUser();
        spp  = PreferenceManager.getDefaultSharedPreferences(getContext());



            a = new ArrayAdapter<AcceptModel>(getActivity(), android.R.layout.simple_list_item_1, users);
            lvUsers.setAdapter(a);



                    acceptReference.child(md5(spp.getString("email", "null"))).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            AcceptModel acceptModel = new AcceptModel();
                            acceptModel.setName(dataSnapshot.child("provider_name").getValue().toString());
                            acceptModel.setEmail(dataSnapshot.child("email").getValue().toString());
                            acceptModel.setPhone(dataSnapshot.child("phone").getValue().toString());

                            acceptModel.setNum(dataSnapshot.child("num").getValue().toString());
                            acceptModel.setAge(dataSnapshot.child("age").getValue().toString());
                            acceptModel.setType(dataSnapshot.child("type_blood").getValue().toString());
                            acceptModel.setAddress(dataSnapshot.child("donation_address").getValue().toString());
                            acceptModel.setDate(dataSnapshot.child("donation_id").getValue().toString());

                            users.add(acceptModel);

                            a.notifyDataSetChanged();
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
                    lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                            acceptReference.child(md5(spp.getString("email", "null"))).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    final List<UserID> listuser = new ArrayList<>();
                                    UserID userID = new UserID();
                                    final String user_id = dataSnapshot.getKey();
                                    final String currant_Date = DateFormat.getDateTimeInstance().format(new Date());
                                    for(DataSnapshot ds : dataSnapshot.getChildren())
                                    {
                                    userID.setKeyy(ds.getKey());
                                    listuser.add(userID);

                                    }
                                    final List<String> list2 = new ArrayList<>();
                                    for(int i =0 ;i <= dataSnapshot.getChildrenCount();i++)
                                    {
                                        list2.add(i,dataSnapshot.getKey());

                                    }
                                    final String user_idd = list2.get(0);

                                    friend.child(currnat_user.getUid()).child(user_idd).child("Date").setValue(currant_Date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            friend.child(user_id).child(currnat_user.getUid()).child("Date").setValue(currant_Date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {


                                                }
                                            });

                                        }
                                    });


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
                    });





        }
        return v ;

    }

}
