package com.example.app.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    TextView tv;
    EditText eetName,eetPassword;
CheckBox checkBoxx ;
    RadioButton radioButton;
//boolean bool = true ;
Button button ;
ProgressDialog mprogressDialog;
    //------- fire base auth
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_cost);
        tv = findViewById(R.id.textView);
        Animation x = AnimationUtils.loadAnimation(this, R.anim.blink);
        tv.startAnimation(x);
        eetName= findViewById(R.id.uName);
       eetPassword= findViewById(R.id.pass);
       checkBoxx =findViewById(R.id.checkBoxx);

   mprogressDialog = new ProgressDialog(this);
        //****fire base
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        //**************************************************


    }

  public void Log(View v)
    {
        String email= eetName.getText().toString();

        String pass= eetPassword.getText().toString();
        if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pass))
        {
            //**** progress dialog

            mprogressDialog.setTitle("Please wiat....... ");
            mprogressDialog.setMessage("uploading image");
            mprogressDialog.setCanceledOnTouchOutside(false);
            mprogressDialog.show();
            loguser(email,pass);
        }
        else {
            eetName.setError("enter email");
            eetPassword.setError("enter password");
        }

   }
    private void loguser(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    shred();
                    String currant_user_id = mAuth.getCurrentUser().getUid();
                    String deviceId = FirebaseInstanceId.getInstance().getToken();
                    databaseReference.child(currant_user_id).child("device_token").setValue(deviceId).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mprogressDialog.dismiss();
                            Intent intent = new Intent(login.this,MainScreen.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
                else
                {
                    mprogressDialog.hide();
                    Toast.makeText(login.this,"you got errore"+task.getException(),Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void reggg(View v) {


        Intent i = new Intent(this,register.class);

        startActivity(i);

    }

    @Override
    public void onBackPressed() {
      super.onBackPressed();
        finish();

    }

    public void shred() {

        SharedPreferences spp=   PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor pen=spp.edit();
        pen.putString("email", eetName.getText().toString());
        pen.putString("pass", eetPassword.getText().toString());
        pen.putBoolean("booll",checkBoxx.isChecked());
        pen.commit();

    }






}
