package com.example.app.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    FloatingLabelSpinner sp;
    String c = " ", b = " ";
    String[] country = {"Amman", "Zarqa", "Irbid", "Aqaba", "Balqaa", "Jarash", "Ajloun", "Tafilah", "Madba", "Mafraq", "Ma'an", "Karak"};
    ArrayAdapter<String> mycountery;

    // ******** two spinner
    FloatingLabelSpinner sp2;
    String[] mydate = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    ArrayAdapter<String> mybold;


    String radio = "public";

    EditText edName, edPassword, edUser, edMail, edAdress, edPhone;

    Button button;
    RadioButton publicR, privateR;

    //------- fire base auth
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabaseReference;
    private DatabaseReference mdatabaseReferenceWithType;

    //***** prgress dialog
    private ProgressDialog mprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sing_up);
//        ccity =getResources().getStringArray(R.array.selectcity);
//        mydate =getResources().getStringArray(R.array.selectbloodgroup);
        edName = findViewById(R.id.edt1);
        edPassword = findViewById(R.id.edt6);
        edUser = findViewById(R.id.edt3);
        edMail = findViewById(R.id.edt5);
        edPhone = findViewById(R.id.edt4);
        button = findViewById(R.id.rigCust);
        publicR = findViewById(R.id.radio2);
        privateR = findViewById(R.id.radio1);

        mprogressDialog = new ProgressDialog(this);


        //************************ one spinner
        sp = findViewById(R.id.spinner1);

        mycountery = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);
        sp.setAdapter(mycountery);
        c = sp.getSelectedItem().toString();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                c = String.valueOf(o);
                sp.setError(" ");
                sp.getError();
                sp.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //***************** two spinner
        sp2 = findViewById(R.id.spinner2);
        mybold = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mydate);
        sp2.setAdapter(mybold);
        sp2.setAnimDuration(2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object o = parent.getItemAtPosition(position);
                b = String.valueOf(o);
                sp2.setError(" ");
                sp2.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //****fire base


        mAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                String Password = edPassword.getText().toString();
                String User = edUser.getText().toString();
                String Mail = edMail.getText().toString();
                String Phone = edPhone.getText().toString();

//                if(!TextUtils.isEmpty(name)|| !TextUtils.isEmpty(Mail) || !TextUtils.isEmpty(Mail) || !TextUtils.isEmpty(Password))
//                {

//
//
//                }
                register_user(name, Password, Mail, User, Phone);

            }
        });
    }


    //    public void r(View view) {
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.radio1:
//                if (checked)
//                    radio = "private";
//                    break;
//            case R.id.radio2:
//                if (checked)
//                    radio = "public";
//                    break;
//        }
//
//    }
    private void register_user(final String name, final String pass, final String mail, final String user, final String phone) {

        if (TextUtils.isEmpty(edName.getText().toString()))
            edName.setError("Accept Name ");
        else if (TextUtils.isEmpty(edUser.getText().toString()))
            edUser.setError("Accept Age");
        else if (edPassword.getText().toString().length() < 6)
            edPassword.setError("pass must 6 or more digit ");
        else if (edMail.getText().toString().isEmpty())
            edMail.setError("Accept pass ");
        else if (edPhone.getText().toString().length() < 10)
            edPhone.setError("error number ");
        else if (sp.getSelectedItem().toString().equals("Select City"))
            sp.setError("Please select city");
         else if (sp2.getSelectedItem().toString().equals("Select Blood Type"))
            sp2.setError("Please select Type Blood");
        else if (publicR.isChecked())
            radio = "public";
        else if (privateR.isChecked())
            radio = "private";
        else
            mprogressDialog.setTitle("Loading");
            mprogressDialog.setMessage("Please wait...");
            mprogressDialog.setCanceledOnTouchOutside(false);
            mprogressDialog.show();
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {


                        FirebaseUser currant_user = FirebaseAuth.getInstance().getCurrentUser();
                        final String uid = currant_user.getUid();
                        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                        final HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("name", name);
                        hashMap.put("user", user);
                        hashMap.put("phone", phone);
                        hashMap.put("city", c);
                        hashMap.put("type", b);
                        hashMap.put("redio", radio);
                        hashMap.put("image", "default");
                        mdatabaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                    mdatabaseReferenceWithType = FirebaseDatabase.getInstance().getReference().child(spinnerr).child(uid);
//                                    mdatabaseReferenceWithType.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if(task.isSuccessful()){
//
//                                        }}
//                                    });

                                    mprogressDialog.dismiss();
                                    Intent intent = new Intent(register.this, MainScreen.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        });


                    } else {
                        mprogressDialog.hide();
                        Toast.makeText(register.this, "you got errore" + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }









