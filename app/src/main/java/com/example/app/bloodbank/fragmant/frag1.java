package com.example.app.bloodbank.fragmant;



import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.example.app.bloodbank.R;
import com.example.app.bloodbank.login;
import com.example.app.bloodbank.user;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.james602152002.floatinglabelspinner.FloatingLabelSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.android.volley.VolleyLog.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag1 extends Fragment {
    ImageButton btn ;
    ProgressDialog pd;
    List<user> users;
    ListView lvUsers;
ImageButton logout ;
    //*********** one spiiner

    FloatingLabelSpinner sp ;
    String c = " " ,b = " " ;
    String []country={"Amman","Zarqa","Irbid","Aqaba","Balqaa","Jarash","Ajloun","Tafilah","Madba","Mafraq","Ma'an","Karak"};
    ArrayAdapter<String> mycountery;

    // ******** two spinner
    FloatingLabelSpinner sp2 ;
    String []mydate={"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    ArrayAdapter<String> mybold;

    public frag1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag1, container, false);

//onBackPressed(getActivity());


       // logout = v.findViewById(R.id.imageButton);

        //************************ one spinner
        sp = v.findViewById(R.id.floatingLabelSpinner);

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
        sp2 = v.findViewById(R.id.spinnerb);
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
  //    b = sp2.getSelectedItem().toString();




        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

      btn = v.findViewById(R.id.search);
      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (sp.getSelectedItem().toString().equals("Select City")) {
                  sp.setError("Please select city");

              } else if (sp2.getSelectedItem().toString().equals("Select Blood Type"))
                  sp2.setError("Please select Type Blood");


              else {
                  show sh = new show();
                  android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                  Bundle bundle = new Bundle();
                  bundle.putString("city", c);
                  bundle.putString("type", b);
                  sh.setArguments(bundle);
                  ft.replace(R.id.frag1, sh);
                  ft.addToBackStack(null);
                  ft.commit();
              }

          }
      });



        return v ;
    }



}
