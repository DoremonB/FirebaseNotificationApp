package com.example.firebasenotificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SendByDeviceToken extends AppCompatActivity {

    ListView listView;
    EditText title,body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_by_device_token);

        title=findViewById(R.id.title);
        body=findViewById(R.id.body);

        listView=findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title_text=title.getText().toString();
                String body_text=body.getText().toString();
                sendFCMPush_device_token(title_text,body_text,((TextView)view.findViewById(android.R.id.text1)).getText().toString());
            }
        });

        displayTokeninAdapter();

    }

    private void sendFCMPush_device_token(String title_text,String body_text,String token_to_whom_we_have_tosend) {

        final String SERVER_KEY = Constants.SERVER_KEY;
        String msg = body_text;
        String title = title_text;

        //device token
//        String token = Constants.token;
        String token=token_to_whom_we_have_tosend;

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("return here>>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, Constants.FCM_URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("True", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("False", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

    private void displayTokeninAdapter() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Devices");

        //Getting all tokens

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int no_devices= (int) dataSnapshot.getChildrenCount();
                Log.d("myapp",String.valueOf(no_devices));

                String s[]=new String[no_devices];
                int i=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String token=snapshot.getValue(String.class);
                    Log.d("myapp",token);
                    s[i]=token;
                    i++;
                }

                ArrayList<String> planetList = new ArrayList<String>();
                planetList.addAll( Arrays.asList(s) );
                //Set array adapter
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, planetList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
