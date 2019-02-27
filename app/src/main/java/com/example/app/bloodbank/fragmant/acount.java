package com.example.app.bloodbank.fragmant;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.bloodbank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class acount extends Fragment {

    TextView edName ;

TextView button ;
TextView change_image ;
    String name  ;
    String  age ;
    String  phone ;
    String  type;
    String  d ;
    String  city ;
    private CircleImageView circleImageView ;
    private static final int GALLARY_PIKE =1 ;
    final int PICK_FROM_GALLERY = 2;
    //**** fiere base
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser ;
    private StorageReference mStorageRef;

    //****progress dialoge
    private ProgressDialog mprogressDialog ;
    private ProgressDialog progressDialog;
    public acount() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View v =  inflater.inflate(R.layout.fragment_acount, container, false);
        shred(name,phone,type);
       edName = v.findViewById(R.id.name_ac);


        circleImageView = v.findViewById(R.id.circleImageView);
        change_image = v.findViewById(R.id.change_img);
        button = v.findViewById(R.id.button3);


        //*** progress dialoge
        mprogressDialog = new ProgressDialog(getActivity());
        mprogressDialog.setTitle("Please wiat....... ");
        mprogressDialog.setMessage("uploading image");
        mprogressDialog.show();

        //***fire base
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             String  image= dataSnapshot.child("image").getValue().toString();
                name = dataSnapshot.child("name").getValue().toString();

                age= dataSnapshot.child("user").getValue().toString();
                 phone= dataSnapshot.child("phone").getValue().toString();
                 type= dataSnapshot.child("type").getValue().toString();
                  d= dataSnapshot.child("redio").getValue().toString();
                 city= dataSnapshot.child("city").getValue().toString();

                Picasso.with(getContext()).load(image).placeholder(R.drawable.ic_person).into(circleImageView);
                SpannableString spannableStringObject= new SpannableString(name);
                spannableStringObject.setSpan(new UnderlineSpan(), 0, spannableStringObject.length(), 0);
                edName.setText(spannableStringObject);
                mprogressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag4 f= new frag4();
                Bundle bundle = new Bundle();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                bundle.putString("name",name);
                bundle.putString("age",age);
                bundle.putString("phone",phone);
                bundle.putString("pp",d);
                bundle.putString("city",city);
                bundle.putString("type",type);

                f.setArguments(bundle);
                ft.replace(R.id.content_acount,f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        change_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent= new Intent();
                gallaryIntent.setType("image/*");
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallaryIntent,"SELECT_IMAGE"), GALLARY_PIKE);

            }
        });



     return v ;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



            if (requestCode == GALLARY_PIKE && resultCode == RESULT_OK) {
                Uri imageUri = data.getData();


                // start cropping activity for pre-acquired image saved on the device
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start( getContext(),this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Please wiat....... ");
                    progressDialog.setMessage("uploading image");
                    progressDialog.show();
                    String curranr_user_id = firebaseUser.getUid();
                    Uri resultUri = result.getUri();

                    final StorageReference riversRef = mStorageRef.child("image_profile").child(curranr_user_id + ".jpg");
                    riversRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {

                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String downlode_url = uri.toString();
                                        databaseReference.child("image").setValue(downlode_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getActivity(), "the image uplode", Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "the image connt uplode", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(getActivity(), "the errore image connt uplode" + error, Toast.LENGTH_LONG).show();

                }
            }

    }
    public void shred(String name, String phone,String type) {

        SharedPreferences spp=   PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor pen=spp.edit();
        pen.putString("nname", name);
        pen.putString("phone", phone);
        pen.putString("type", type);

        pen.commit();

    }

}
