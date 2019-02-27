package com.example.app.bloodbank.fragmant;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.app.bloodbank.Config;
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
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag4 extends Fragment {
    EditText age,edName,edPhone ;
    TextView edType,edAddress ;
    RadioGroup radioGroup ;
    RadioButton r1,r2 ;
    Button update ;
    String radio;

    FloatingLabelSpinner sp ;
    String c = " " ,b = " " ;
    String []country={"Amman","Zarqa","Irbid","Aqaba","Balqaa","Jarash","Ajloun","Tafilah","Madba","Mafraq","Ma'an","Karak"};
    ArrayAdapter<String> mycountery;

    // ******** two spinner
    FloatingLabelSpinner sp2 ;
    String []mydate={"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    ArrayAdapter<String> mybold;


    //**** fiere base
    private DatabaseReference databaseReference;
    private  FirebaseUser firebaseUser ;

    //****progress dialoge
    private ProgressDialog progressDialog ;
    public frag4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.fragment_frag4, container, false);
        radioGroup = v.findViewById(R.id.radioGroup);
        //EditText edUser,edName,edPass,edMail,edPhone ;
     age = v.findViewById(R.id.f_age);
       edName = v.findViewById(R.id.edName);
        edAddress = v.findViewById(R.id.addressCity);
        edType = v.findViewById(R.id.Bloodtype);
        edPhone = v.findViewById(R.id.edPhone);
        r1 = v.findViewById(R.id.radioButton);
        r2 = v.findViewById(R.id.radioButton2);
        SpannableString spannableStringObject= new SpannableString(getArguments().getString("name"));
        spannableStringObject.setSpan(new UnderlineSpan(), 0, spannableStringObject.length(), 0);

//        SpannableString spannableStringObject2= new SpannableString(getArguments().getString("age"));
//        spannableStringObject2.setSpan(new UnderlineSpan(), 0, spannableStringObject.length(), 0);



//        edMail.setText(getArguments().getString("age"));
        age.setText(getArguments().getString("age"));
        edName.setText(spannableStringObject);
        edPhone.setText(getArguments().getString("phone"));
     radio = getArguments().getString("pp");
     edAddress.setText(getArguments().getString("city"));
     edType.setText(getArguments().getString("type"));

        if(radio.equals("public"))
        {
           r1.setChecked(true);
        }else {
            r1.setChecked(false);
        }


        //************************ one spinner
        sp = v.findViewById(R.id.spinner22);

        mycountery = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,country);
        sp.setAdapter(mycountery);
       // c= sp.getSelectedItem().toString();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    c = getArguments().getString("city");

                Object o  = parent.getItemAtPosition(position);
                c = String.valueOf(o) ;
                edAddress.setText(c);
                sp.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //***************** two spinner
        sp2 = v.findViewById(R.id.address);
        mybold = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mydate);
        sp2.setAdapter(mybold);
        sp2.setAnimDuration(2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                    b = getArguments().getString("type");


                Object o  = parent.getItemAtPosition(position);
                b = String.valueOf(o) ;
              edType.setText(b);
                sp2.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //***fire base
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        update = v.findViewById(R.id.updateee);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                final HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("name",edName.getText().toString());
                hashMap.put("user",age.getText().toString());
                hashMap.put("phone",edPhone.getText().toString());
                hashMap.put("redio",radio);
                hashMap.put("city",c);
                hashMap.put("type",b);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("name").setValue(edName.getText().toString());
                        dataSnapshot.getRef().child("user").setValue(age.getText().toString());
                        dataSnapshot.getRef().child("phone").setValue(edPhone.getText().toString());
                        dataSnapshot.getRef().child("redio").setValue(radio);
                        dataSnapshot.getRef().child("city").setValue(edAddress.getText().toString());
                        dataSnapshot.getRef().child("type").setValue(edType.getText().toString());
                        progressDialog.dismiss();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//             databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                 @Override
//                 public void onComplete(@NonNull Task<Void> task) {
//                     if(task.isSuccessful()){
//                progressDialog.dismiss();
//                     Toast.makeText(getActivity(),hashMap+"succes",Toast.LENGTH_LONG).show();}
//                     else {
//                         Toast.makeText(getActivity(),hashMap+"erroe",Toast.LENGTH_LONG).show();}
//
//                 }
//
//
//             });
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               if(r1.isChecked())
                   radio = "public";
               else
                   radio = "private";
            }
        });

        return v ;
    }



}
