package com.example.app.bloodbank;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private FirebaseUser firebaseUser ;
Context context ;
    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layoutcopyy,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage;
        public TextView messageTime;
        public ImageView gps;


        public MessageViewHolder(View view) {
            super(view);
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            messageText =  view.findViewById(R.id.message_text_layout);
            profileImage =  view.findViewById(R.id.message_profile_layout);
            displayName =  view.findViewById(R.id.name_text_layout);
            messageImage =  view.findViewById(R.id.message_image_layout);
            messageTime =  view.findViewById(R.id.time_text_layout);
   gps =  view.findViewById(R.id.gps);
   
   
          //  Uri gmmIntentUri ;


        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

     String currant_user_id = firebaseUser.getUid();
        final Messages c = mMessageList.get(i);
//        Toast.makeText(context, ""+c, Toast.LENGTH_SHORT).show();
        viewHolder.messageText.setText(c.getMessage());
        //viewHolder.gps.setText("Location"+c.getMessage());
//        viewHolder.messageText.setText(c.getLat());
//        viewHolder.messageText.setText(c.getLog());
        final String latitude = c.getLat();
        final String longitude = c.getLog();
      //  String url = "http://google.com/maps/bylatlng?lat=" + latitude + "&lng=" + longitude;
       // viewHolder.gps.setText(url);



       // viewHolder.messageTime.setText();
        String from_user = c.getFrom();
        String message_type = c.getType();
    Long time = c.getTime();
        viewHolder.messageTime.setText(Long.toString(time));

convTime convTime = new convTime();
String timeConv = convTime.convert(time);
        viewHolder.messageTime.setText(timeConv);

viewHolder.gps.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("geo:"+c.getMessage()+"?=15");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        viewHolder.gps.getContext().startActivity(mapIntent);
    }
});




        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                viewHolder.displayName.setText(name);

                Picasso.with(viewHolder.profileImage.getContext()).load(image)
                        .placeholder(R.drawable.ic_person).into(viewHolder.profileImage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(message_type.equals("geo"))
        {
                      Picasso.with(viewHolder.gps.getContext()).load(R.drawable.kareta)
                    .placeholder(R.drawable.ic_person).into(viewHolder.gps);

            viewHolder.messageText.setVisibility(View.INVISIBLE);
            viewHolder.messageImage.setVisibility(View.INVISIBLE);


        }

        if(message_type.equals("text")) {
           viewHolder.gps.setVisibility(View.INVISIBLE);
            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);
//            Picasso.with(viewHolder.gps.getContext()).load(R.drawable.googlemap)
//                    .placeholder(R.drawable.ic_person).into(viewHolder.gps);




        } else {
           // viewHolder.gps.setVisibility(View.INVISIBLE);
            viewHolder.messageText.setVisibility(View.INVISIBLE);
            Picasso.with(viewHolder.profileImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.ic_person).into(viewHolder.messageImage);

        }
//      if(message_type.equals("geo"))
//      {
//          viewHolder.gps.setOnClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View v) {
//                  Toast.makeText(context, "hiiiiiii", Toast.LENGTH_SHORT).show();
//              }
//          });
////            viewHolder.messageImage.setVisibility(View.INVISIBLE);
////            viewHolder.messageText.setVisibility(View.INVISIBLE);
////
//           // URL url = "http://google.com/maps/bylatlng?lat=" + c.getLat() + "&lng=" + c.getLog();
////            viewHolder.gps.setText(url);
////
////
//      }
        if(from_user.equals(currant_user_id))
        {
            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background_two);
            viewHolder.messageText.setTextColor(Color.BLACK);

        }
        else
        {
            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.BLACK);


        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}
