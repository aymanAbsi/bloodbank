package com.example.app.bloodbank.fragmant;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.bloodbank.Config;
import com.example.app.bloodbank.MainScreen;
import com.example.app.bloodbank.NowNotification;
import com.example.app.bloodbank.R;
import com.example.app.bloodbank.test;
import com.example.app.bloodbank.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag2 extends Fragment {
//    Button how;
//    ProgressDialog pd;
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
    int i = 0;
    public frag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v== null) {
            v = inflater.inflate(R.layout.fragment_frag2, container, false);
//        how = v.findViewById(R.id.how);
            final SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            lvUsers = v.findViewById(R.id.lhow);
            users = new ArrayList<test>();
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

            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if(dataSnapshot.child("type_blood").getValue().toString().equals(type) && !dataSnapshot.getKey().equals(curranr_user.getUid())) {
                        test u = new test();
                        u.Id = dataSnapshot.getKey();
                        u.email = dataSnapshot.child("email").getValue().toString();
                        u.setTest(dataSnapshot.child("name").getValue().toString());
                        u.setAddress(dataSnapshot.child("donation_address").getValue().toString());
                        u.setAge(dataSnapshot.child("age").getValue().toString());
                        u.setType(dataSnapshot.child("type_blood").getValue().toString());
                        u.setDate(dataSnapshot.child("donation_id").getValue().toString());
                        u.setNumber(dataSnapshot.child("num").getValue().toString());

                        users.add(u);

                        a.notifyDataSetChanged();

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
 singleaccept.addChildEventListener(new ChildEventListener() {
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
                    users.add(u);

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
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("name").getValue().toString();
                    phone= dataSnapshot.child("phone").getValue().toString();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            a = new ArrayAdapter<test>(getActivity(), android.R.layout.simple_list_item_1, users);
            lvUsers.setAdapter(a);
            lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                    final String testid = users.get(position).Id;
                    test test = users.get(position);

                    SharedPreferences spp = PreferenceManager.getDefaultSharedPreferences(getContext());

                    Map<String, String> map = new HashMap<>();
                    map.put("name", test.getTest());
                    map.put("type_blood", test.getType());
                    map.put("donation_address", test.getAddress());
                    map.put("donation_id", test.getDate());
                    map.put("age", test.getAge());
                    map.put("num", test.getNumber());
                 map.put("email", spp.getString("email", "null"));
//                    map.put("phone", spp.getString("phone", "null"));
//                    map.put("provider_name", spp.getString("nname", "null"));
                    map.put("phone",phone);
                    map.put("provider_name", name);



                    acceptReference.child(md5(test.email)).child(curranr_user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            requestReference.child(testid).removeValue();
                            users.remove(position);
                            a.notifyDataSetChanged();
                        }
                    });


                }
            });



        }
        return v;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
