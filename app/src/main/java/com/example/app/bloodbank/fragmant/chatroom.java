package com.example.app.bloodbank.fragmant;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.app.bloodbank.MessageAdapter;
import com.example.app.bloodbank.Messages;
import com.example.app.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.delight.android.location.SimpleLocation;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

/**
 * A simple {@link Fragment} subclass.
 */
public class chatroom extends Fragment {

    //***


    private SimpleLocation location;
     String latitude =" ";
     String longitude = " ";
     String message ;
     Context context ;

private  String chatuser ;
private  String currant_user_id;

private DatabaseReference mRootRef ;
private DatabaseReference mMessageRef ;
private FirebaseAuth mAuth ;

private ImageButton add;
private ImageButton send ;
private ImageButton sendGps ;
private EditText  Edit_massege ;

    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;

    private static final int GALLERY_PICK = 1;
    final int CAMERA_REQUEST = 1;
    final int PICK_FROM_GALLERY = 2;

    String encodedImage;
    byte[] imageInByteGlobal;


    // Storage Firebase
    private StorageReference mImageStorage;


    //New Solution
    private int itemPos = 0;

    private String mLastKey = "";
    private String mPrevKey = "";


    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;
    public chatroom() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chatroom, container, false);

        //*****camera
        //This code is Laa
       // Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
       // bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image = stream.toByteArray();
        imageInByteGlobal=image;
        String encodedString = Base64.encodeToString(image, 0);
        encodedImage=encodedString;


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mMessageRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currant_user_id = mAuth.getCurrentUser().getUid();
       chatuser = getArguments().getString("user_id");
        loadMessages();


        location = new SimpleLocation(getActivity());
        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(getActivity());
        }

       //**** inatiialize
        add = v.findViewById(R.id.icon_add);
        send = v.findViewById(R.id.icon_send);
        sendGps = v.findViewById(R.id.icon_add_gps);
        Edit_massege = v.findViewById(R.id.icon_massege);

        mAdapter = new MessageAdapter(messagesList);

        mMessagesList =  v.findViewById(R.id.messages_list);
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.message_swipe_layout);
        mLinearLayout = new LinearLayoutManager(getActivity());

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mRootRef.child("Chat").child(currant_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(chatuser)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + currant_user_id + "/" + chatuser, chatAddMap);
                    chatUserMap.put("Chat/" + chatuser + "/" + currant_user_id, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




send.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        sendMessege();
    }
});
add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT,1 );
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);

    }
});

sendGps.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     latitude = String.valueOf(location.getLatitude());
       Toast.makeText(getActivity(), latitude, Toast.LENGTH_SHORT).show();
    longitude = String.valueOf(location.getLongitude());
        Toast.makeText(getActivity(), longitude, Toast.LENGTH_SHORT).show();




//        Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+longitude+"?=15");
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        mapIntent.setPackage("com.google.android.apps.maps");
//        startActivity(mapIntent);

if(!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude) )
{
    Edit_massege.setText(latitude+"\n"+longitude);
    message = latitude+"\n"+longitude ;
}

        final String currant_user_ref = "messages/"+currant_user_id+"/"+chatuser;
        final String chat_user_ref = "messages/"+chatuser+"/"+currant_user_id;

        final DatabaseReference user_message_puth = mMessageRef.child("messages")
                .child(currant_user_id).child(chatuser).push();

        final String push_id = user_message_puth.getKey();


       // URI url = "http://google.com/maps/bylatlng?lat=" + latitude + "&lng=" + longitude;

        Map messageMap = new HashMap();
       messageMap.put("message",latitude+","+longitude);
        messageMap.put("seen", false);
        messageMap.put("type", "geo");
        messageMap.put("time", ServerValue.TIMESTAMP);
        messageMap.put("latitude", latitude);
        messageMap.put("longitude", longitude);
        messageMap.put("from", currant_user_id);

        Map messageUserMap = new HashMap();
        messageUserMap.put(currant_user_ref + "/" + push_id, messageMap);
        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

        Edit_massege.setText("");
        latitude=" ";
        longitude=" ";



        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError != null){
                    Log.d("chat_log",databaseError.getMessage().toString());

                  //  Toast.makeText(getActivity(),"succes",Toast.LENGTH_LONG).show();
                }
            }
        });









    }
});

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage++;

                itemPos = 0;

                loadMoreMessages();



            }
        });


        return  v ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                // start play with image uri
                final String currant_user_ref = "messages/"+currant_user_id+"/"+chatuser;
                final String chat_user_ref = "messages/"+chatuser+"/"+currant_user_id;

                final DatabaseReference user_message_puth = mMessageRef.child("messages")
                        .child(currant_user_id).child(chatuser).push();

                final String push_id = user_message_puth.getKey();


            final StorageReference filepath = mImageStorage.child("message_images").child( push_id + ".jpg");

                filepath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                          //  Toast.makeText(getActivity(), "filepath", Toast.LENGTH_SHORT).show();


                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                   // Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                                    String download_url = uri.toString();

                                    Map messageMap = new HashMap();
                                    messageMap.put("message", download_url);
                                    messageMap.put("seen", false);
                                    messageMap.put("type", "photo");
                                    messageMap.put("time", ServerValue.TIMESTAMP);
                                    messageMap.put("from", currant_user_id);

                                    Map messageUserMap = new HashMap();
                                    messageUserMap.put(currant_user_ref + "/" + push_id, messageMap);
                                    messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);



                                   // Edit_massege.setText("");

                                    mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                            if(databaseError != null){

                                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                            }

                                        }
                                    });



                                }
                            });



                        }


                         //   Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                    }
                });






            }
        }
    }

    private void sendMessege() {

        message = Edit_massege.getText().toString();
        if(!TextUtils.isEmpty(message))
        {


            String currant_user_ref = "messages/"+currant_user_id+"/"+chatuser;
            String chat_user_ref = "messages/"+chatuser+"/"+currant_user_id;

            DatabaseReference user_message_puth = mMessageRef.child("messages")
                    .child(currant_user_id).child(chatuser).push();


            String push_id = user_message_puth.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
//            messageMap.put("latitude", latitude);
//            messageMap.put("longitude", longitude);
            messageMap.put("from", currant_user_id);

            Map messageUserMap = new HashMap();
            messageUserMap.put(currant_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            Edit_massege.setText("");
            latitude=" ";
            longitude=" ";


            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        Log.d("chat_log",databaseError.getMessage().toString());

                       // Toast.makeText(getActivity(),"succes",Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }



    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootRef.child("messages").child(currant_user_id).child(chatuser);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Messages message = dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();

                if(!mPrevKey.equals(messageKey)){

                    messagesList.add(itemPos++, message);

                } else {

                    mPrevKey = mLastKey;

                }


                if(itemPos == 1) {

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                mAdapter.notifyDataSetChanged();

                mRefreshLayout.setRefreshing(false);

                mLinearLayout.scrollToPositionWithOffset(10, 0);

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

    private void loadMessages() {

        DatabaseReference messageRef = mRootRef.child("messages").child(currant_user_id).child(chatuser);

        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);


        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                itemPos++;

                if(itemPos == 1){

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size() - 1);

                mRefreshLayout.setRefreshing(false);

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



    @Override
    public void onResume() {
        super.onResume();

        // make the device update its location
  //     location.beginUpdates();

        // ...
    }

    @Override
    public void onPause() {
        // stop location updates (saves battery)
        location.endUpdates();

        // ...

        super.onPause();
    }







}
