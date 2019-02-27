package com.example.app.bloodbank.fragmant;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.bloodbank.R;
import com.example.app.bloodbank.friendclass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class friendList extends Fragment {
    private RecyclerView recyclerView ;
    private DatabaseReference mdatabaseReference;
    private DatabaseReference userdatabase ;
     private String currant_user_id ;



    public friendList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.friendlist, container, false);
        recyclerView= v.findViewById(R.id.user_rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        currant_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("friend").child(currant_user_id);
        userdatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        return v ;
    }
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<friendclass,UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<friendclass,UserViewHolder>(
                friendclass.class,
                R.layout.showlayout,
                UserViewHolder.class,
                mdatabaseReference

        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, friendclass model, int position) {

                viewHolder.setDate(model.getDate());
                final String list_user_id = getRef(position).getKey();
                userdatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String username= dataSnapshot.child("name").getValue().toString();
                        String userimage= dataSnapshot.child("image").getValue().toString();
                        viewHolder.setName(username);
                        viewHolder.setimage(userimage,getContext());
                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chatroom sh = new chatroom();
                               android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                Bundle bundle =new Bundle();
                                bundle.putString("user_id",list_user_id);
                                sh.setArguments(bundle);
                                ft.replace(R.id.friendlistxml,sh);
                                ft.addToBackStack(null);
                                ft.commit();






                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder
    {


        View view ;
        public UserViewHolder(View itemView) {
            super(itemView);
            view = itemView ;

        }
        public void setDate(String Date)
        {

          TextView  textView_Date = itemView.findViewById(R.id.show_type);
          textView_Date.setText(Date);
        }
  public void setName(String name)
        {

          TextView  textView_name = itemView.findViewById(R.id.show_name);
          textView_name.setText(name);
        }
        public void setimage(String img, Context context)
        {
            CircleImageView  circleImageView = itemView.findViewById(R.id.image_user);
            Picasso.with(context).load(img).placeholder(R.drawable.ic_person).into(circleImageView);
        }

    }

}
