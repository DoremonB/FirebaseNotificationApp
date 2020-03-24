package com.example.firebasenotificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendNotifToByTopic extends AppCompatActivity {

    String TAG="myapp";
    Button notify;
    EditText title,body,topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notif_to_by_topic);

        notify=findViewById(R.id.notify);
        title=findViewById(R.id.title);
        body=findViewById(R.id.body);
        topic=findViewById(R.id.topic);


        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_text=title.getText().toString();
                String body_text=body.getText().toString();
                String topic_text=topic.getText().toString();
                SendNotificationMethod_topic(title_text,body_text,topic_text);
            }
        });


    }

    private void SendNotificationMethod_topic(String title_text,String body_text,String topic_text){
        JSONObject mainObject=new JSONObject();
        try {
            mainObject.put("to","/topics/"+topic_text);

            JSONObject notificationObject=new JSONObject();
            notificationObject.put("title",title_text);
            notificationObject.put("body",body_text);

            mainObject.put("notification",notificationObject);

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, Constants.FCM_URL, mainObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("myapp","response " +response.toString());
                    Toast.makeText(SendNotifToByTopic.this, "Successful", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("myapp",error.toString());
                    Toast.makeText(SendNotifToByTopic.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header=new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key="+Constants.SERVER_KEY);
                    return header;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            int socketTimeout = 1000 * 60;// 60 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
