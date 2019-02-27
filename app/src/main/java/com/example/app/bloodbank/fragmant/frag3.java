package com.example.app.bloodbank.fragmant;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.app.bloodbank.MapsActivity;
import com.example.app.bloodbank.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag3 extends Fragment {
 EditText name,num ,date ,age;
 Button share ,send,location;
    ProgressDialog pd;
    String shareBody;
    private DatabaseReference requestReference;
    private FirebaseUser currant_user ;

    int MINIMUM_TIME_BETWEEN_UPDATES = 1000;
    int MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;


    private GoogleMap mMap;
    LocationManager locationManager;

    FloatingLabelSpinner sp ;
    String c = " " ,b = " " ;
    String []country={"Amman","Zarqa","Irbid","Aqaba","Balqaa","Jarash","Ajloun","Tafilah","Madba","Mafraq","Ma'an","Karak"};
    ArrayAdapter<String> mycountery;

    // ******** two spinner
    FloatingLabelSpinner sp2 ;
    String []mydate={"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    ArrayAdapter<String> mybold;



    public frag3() {
        // Required empty public constructor
    }


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v=  inflater.inflate(R.layout.fragment_frag3, container, false);
        //************************ one spinner
        sp = v.findViewById(R.id.floatingLabelSpinneraddress);

        mycountery = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,country);
        sp.setAdapter(mycountery);
        c= sp.getSelectedItem().toString();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object o  = parent.getItemAtPosition(position);
                c = String.valueOf(o) ;
                sp.setError(" ");
                sp.getError();
                sp.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //***************** two spinner
        sp2 = v.findViewById(R.id.floatingLabelSpinnertype);
        mybold = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,mydate);
        sp2.setAdapter(mybold);
        sp2.setAnimDuration(2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object o  = parent.getItemAtPosition(position);
                b = String.valueOf(o) ;
                sp2.setError(" ");
                sp2.dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


      requestReference = FirebaseDatabase.getInstance().getReference().child("req");
      currant_user = FirebaseAuth.getInstance().getCurrentUser();
    //  location = v.findViewById(R.id.location);
      name = v.findViewById(R.id.name);
      //  address = v.findViewById(R.id.address);
        //type = v.findViewById(R.id.type);
        date = v.findViewById(R.id.ddate);
      num = v.findViewById(R.id.num);
      age = v.findViewById(R.id.age);
      share = v.findViewById(R.id.share);
      share.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent share = new Intent(android.content.Intent.ACTION_SEND);
              share.setType("text/plain");
              //524288 is the flag of new task .. or task reset..Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK
              share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
              // Add data to the intent, the receiving app will decide
              // what to do with it. "http://www.codeofaninja.com"  "Share link!" "Title Of The Post"
              shareBody = "hay"+"\n"+" I need blood to "+name.getText().toString()+"\n"+" type blood "+b
                      +"\n"+" in "+c+"\n"+" on date "+date.getText().toString();
              share.putExtra(Intent.EXTRA_SUBJECT, shareBody);
              share.putExtra(Intent.EXTRA_TEXT, shareBody);

              startActivity(Intent.createChooser(share,"Share Via"));




          }
      });

           send = v.findViewById(R.id.send);
           send.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(TextUtils.isEmpty(name.getText().toString()))
                       name.setError("Empty");
                  else if (sp.getSelectedItem().toString().equals("Select City")) {
                   sp.setError("Please select city");
               } else if (sp2.getSelectedItem().toString().equals("Select Blood Type"))
                   sp2.setError("Please select Type Blood");
                  else if(TextUtils.isEmpty(date.getText().toString()))
                      date.setError("Empt");
                  else if(TextUtils.isEmpty(age.getText().toString()))
                      age.setError("Empty");
                  else if(TextUtils.isEmpty(num.getText().toString()))
                      num.setError("Empt");
                    else{
                   pd = new ProgressDialog(getActivity());
                   pd.setTitle("Loading");
                   pd.setMessage("Please wait...");
                   pd.show();
                   SharedPreferences spp=   PreferenceManager.getDefaultSharedPreferences(getContext());

                    savevalue(name.getText().toString(),b,c,
                    date.getText().toString(),age.getText().toString(),num.getText().toString(),spp.getString("email","null"));

                   Map<String,String> map = new HashMap<>();
                   map.put("name",name.getText().toString());
                   map.put("type_blood",b);
                   map.put("donation_address",c);
                   map.put("donation_id", date.getText().toString());
                   map.put("age", age.getText().toString());
                   map.put("num",num.getText().toString());
                   map.put("email", spp.getString("email","null"));


                   requestReference.child(currant_user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           pd.dismiss();
                       }
                   });
               }}
           });


      return  v;
    }




    private void savevalue(String s, String s1, String s2, String s3, String s4, String s5, String string) {
        SharedPreferences value =   PreferenceManager.getDefaultSharedPreferences(getContext());

        SharedPreferences.Editor data =value .edit();
        data.putString("name", s);
        data.putString("type", s1);
        data.putString("address", s2);
        data.putString("date",s3);
        data.putString("age",s4);
        data.putString("num",s5);
        data.putString("email",string);
        data.commit();




    }


}
