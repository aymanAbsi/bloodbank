package com.example.app.bloodbank.fragmant;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.app.bloodbank.R;
import com.example.app.bloodbank.test;
import com.example.app.bloodbank.test2;
import com.example.app.bloodbank.user;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class show extends Fragment {

    List<test2> users;
    ListView lvUsers;
    ArrayAdapter<test2> a;
    private FirebaseUser curranr_user ;
   ProgressDialog progressDialog ;
    ///******
    private RecyclerView recyclerView ;
    private Query mdatabaseReference;
    private DatabaseReference singledataref;
    private FirebaseUser firebaseUser ;
    String c ;
    String b ;


    public show() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show, container, false);

        Bundle bundle = getArguments();
        c = bundle.getString("city");
         b = bundle.getString("type");

        lvUsers = v.findViewById(R.id.lsShow);
        users = new ArrayList<test2>();





        return v ;
    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //*****
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // recyclerView= v.findViewById(R.id.recycler_list);
//        recyclerView.setHasFixedSize(true);
        //   recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
      //  mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("redio").equalTo("public");
        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        singledataref = FirebaseDatabase.getInstance().getReference().child("reqq");

        mdatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              // Toast.makeText(getActivity(), dataSnapshot+"", Toast.LENGTH_SHORT).show();
              if(dataSnapshot.child("city").getValue().toString().equals(c) &&
                      dataSnapshot.child("type").getValue().toString().equals(b)) {
                    test2 u = new test2();
//                    u.Id = dataSnapshot.getKey();
//                    u.email = dataSnapshot.child("email").getValue().toString();
                    u.setName(dataSnapshot.child("name").getValue().toString());
                    u.setCity(dataSnapshot.child("city").getValue().toString());
                    u.setUser(dataSnapshot.child("user").getValue().toString());
                    u.setType(dataSnapshot.child("type").getValue().toString());
                    u.setPhone(dataSnapshot.child("phone").getValue().toString());

                    users.add(u);
                    a = new ArrayAdapter<test2>(getActivity(), android.R.layout.simple_list_item_1, users);
                    lvUsers.setAdapter(a);
                    progressDialog.dismiss();
                    a.notifyDataSetChanged();
              }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


//    public void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerAdapter<user,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<user,UserViewHolder>(
//                user.class,
//                R.layout.showlayout,
//                UserViewHolder.class,
//                mdatabaseReference
//        ) {
//            @Override
//            protected void populateViewHolder(UserViewHolder viewHolder, user model, int position) {
//
//
//                viewHolder.setName(model.getName());
//                viewHolder.settype(model.getType());
//                viewHolder.setimage(model.getImage(),getContext());
//
//                final String user_id = getRef(position).getKey();
//                viewHolder.view.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String currant_id = firebaseUser.getUid();
//                        SharedPreferences value =   PreferenceManager.getDefaultSharedPreferences(getActivity());
//
//                        Map<String,String> map = new HashMap<>();
//                        map.put("name",value.getString("name","null"));
//                        map.put("type_blood", value.getString("type","null"));
//                        map.put("donation_address",value.getString("address","null"));
//                        map.put("donation_id", value.getString("date","null"));
//                        map.put("age", value.getString("age","null"));
//                        map.put("num",value.getString("num","null"));
//                        map.put("email",value.getString("email","null"));
//
//                        singledataref.child(currant_id).child(user_id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                             Toast.makeText(getActivity(),"sucses",Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//
//                    }
//                });
//            }
//        };
//        recyclerView.setAdapter(firebaseRecyclerAdapter);
//        progressDialog.dismiss();
//
//    }
//    public static class UserViewHolder extends RecyclerView.ViewHolder
//    {
//        TextView textView_name ;
//        TextView textView_type ;
//        CircleImageView circleImageView ;
//        View view ;
//        public UserViewHolder(View itemView) {
//            super(itemView);
//            view = itemView ;
//            textView_name = itemView.findViewById(R.id.show_name);
//            textView_type = itemView.findViewById(R.id.show_type);
//            circleImageView = itemView.findViewById(R.id.image_user);
//        }
//        public void setName(String name)
//        {
//
//            textView_name.setText(name);
//        }
//        public void settype(String type)
//        {
//
//            textView_type.setText(type);
//
//        }
//        public void setimage(String img, Context context)
//        {
//
//            Picasso.with(context).load(img).placeholder(R.drawable.ic_person).into(circleImageView);
//        }
//
//    }
}
