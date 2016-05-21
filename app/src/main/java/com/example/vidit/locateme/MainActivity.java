package com.example.vidit.locateme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    Button button ;
    GPSTracker gps;
    double lat;
    double longi;
    String msg = "Your ward in danger check app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.trigger);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent i = new Intent(MainActivity.this,GetCurrentLocation.class);
                startActivity(i);
                finish();*/
                //String[] loc = new String[];
                int timer = 0;
                int count = 0;
                //while(timer==0) {
                    gps = new GPSTracker(MainActivity.this);
                    count++;
                    // check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        // \n is for new line
                        lat = latitude;
                        longi = longitude;
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                    int not = 30 * 60 * 1000;
                  //  while (not > 0) {
                    //    not--;
                   // }
                   // if(count==5){
                    //    callTOvolley();
                     //   break;
                 //   }
               // }
                callTOvolley(lat,longi);
                //sendSMSMessage();
            }
        });

    }
    protected void sendSMSMessage() {
        Log.i("Send SMS", "");
        String phoneNo = "9718455700";
        String message = msg;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private void callTOvolley(final double lat, final double longi) {
        String tag_string_req = "req_upload";
        final String lat1 = Double.toString(lat);
        final String long1 = Double.toString(longi);
        //pDialog.setMessage("Logging in ...");
       // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOCATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                //hideDialog();
                    /*Intent i = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(i);
                    finish();*/
                //VolleySingleton volleysingleton = VolleySingleton.getmInstance();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Log.d(TAG,"Successfully Transmitted");
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
              //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("location[lat]", lat1);
                params.put("location[long]",long1 );
                params.put("location[user_id]", "1");


                return params;
            }

        };

        // Adding request to request queue
        strReq.setTag(tag_string_req);
        // RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue =  Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);

    }
}
