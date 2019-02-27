package com.example.app.bloodbank.fragmant;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.bloodbank.AcceptModel;
import com.example.app.bloodbank.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import in.shadowfax.proswipebutton.ProSwipeButton;

import static com.example.app.bloodbank.fragmant.frag2.md5;


/**
 * A simple {@link Fragment} subclass.
 */
public class accept extends Fragment {
    private RecyclerView recyclerView ;
    private DatabaseReference acceptReference;
    private DatabaseReference friend;
    private FirebaseUser currnat_user ;
    SharedPreferences spp;

    public accept() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_accept, container, false);

        recyclerView= v.findViewById(R.id.acceptList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        acceptReference = FirebaseDatabase.getInstance().getReference().child("accept");
        friend = FirebaseDatabase.getInstance().getReference().child("friend");
        currnat_user = FirebaseAuth.getInstance().getCurrentUser();
        spp  = PreferenceManager.getDefaultSharedPreferences(getContext());



        return v ;
    }
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AcceptModel,accept.UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AcceptModel,accept.UserViewHolder>(
                AcceptModel.class,
                R.layout.showuser,
                accept.UserViewHolder.class,
                acceptReference.child(md5(spp.getString("email", "null")))

        ) {
            @Override
            protected void populateViewHolder(final accept.UserViewHolder viewHolder, AcceptModel model, int position) {
                viewHolder.setName(model.getProvider_name());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setPhone(model.getPhone());
                Toast.makeText(getActivity(),model.getEmail()+model.getName()+model.getPhone()+"",Toast.LENGTH_LONG).show();
                final String user_id = getRef(position).getKey();
                final String currant_id = currnat_user.getUid();




//                viewHolder.cahtBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//
//                    }
//                });

                viewHolder.proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
                    @Override
                    public void onSwipeConfirm() {
                        // user has swiped the btn. Perform your async operation now
                        final String currant_Date = DateFormat.getDateTimeInstance().format(new Date());

                        //acceptReference.child(currnat_user.getUid()).child(user_id).removeValue();
                        friend.child(currant_id).child(user_id).child("Date").setValue(currant_Date).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                friend.child(user_id).child(currant_id).child("Date").setValue(currant_Date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        acceptReference.child(md5(spp.getString("email", "null"))).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getActivity(), "secces", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });

                            }
                        });

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // task success! show TICK icon in ProSwipeButton
                                viewHolder.proSwipeBtn.showResultIcon(true); // false if task failed
                            }
                        }, 2000);
                    }
                });








            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }
    public static class UserViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView_name ;
        TextView textView_email ;
        TextView textView_phone ;
       // Button cahtBtn  ;
        ProSwipeButton proSwipeBtn ;


        View view ;
        public UserViewHolder(View itemView) {
            super(itemView);
            view = itemView ;
            textView_name = itemView.findViewById(R.id.Susername);
            textView_email = itemView.findViewById(R.id.Suseremail);
            textView_phone = itemView.findViewById(R.id.Suserphone);
            //cahtBtn = itemView.findViewById(R.id.cahtBtn);
        proSwipeBtn = itemView.findViewById(R.id.cahtBtn);




        }
        public void setName(String name)
        {

            textView_name.setText(name);
        }
        public void setEmail(String email)
        {

            textView_email.setText(email);

        }

  public void setPhone(String phone)
        {

            textView_phone.setText(phone);

        }


    }
}
