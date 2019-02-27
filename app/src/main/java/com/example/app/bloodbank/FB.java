package com.example.app.bloodbank;

import android.content.SharedPreferences;
        import android.preference.PreferenceManager;
        import android.util.Log;
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
        import com.google.firebase.iid.FirebaseInstanceId;
        import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

import static android.content.ContentValues.TAG;

public class FB extends FirebaseInstanceIdService
{
    String deviceID ;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //  Log.d("", "Refreshed token: " + refreshedToken);
        deviceID=refreshedToken;
        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer();
        FirebaseMessaging.getInstance().subscribeToTopic("ug-all");

    }

    private void sendRegistrationToServer()
    {

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://10.0.2.2/chat/wsChatting.asmx/addDevice";

        StringRequest req = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                try {
                    JSONObject o=new JSONObject(s);
                    String data=o.getString("result");
                    if(data.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String errorDescription = "";
                if( volleyError instanceof NetworkError) {
                } else if( volleyError instanceof ServerError) {
                    errorDescription="Server Error";
                } else if( volleyError instanceof AuthFailureError) {
                    errorDescription="AuthFailureError";
                } else if( volleyError instanceof ParseError) {
                    errorDescription="Parse Error";
                } else if( volleyError instanceof NoConnectionError) {
                    errorDescription="No Conenction";
                } else if( volleyError instanceof TimeoutError) {
                    errorDescription="Time Out";
                }
                Toast.makeText(getApplicationContext(), errorDescription,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("deviceID", deviceID);
                param.put("UserID", "1");

                return param;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }
}
