package com.example.firebasenotificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SuscribeToTopic extends AppCompatActivity {
    EditText topic;
    Button suscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscribe_to_topic);

        topic=findViewById(R.id.topic);
        suscribe=findViewById(R.id.suscribe);
        suscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic_text=topic.getText().toString();
                suscribecode(topic_text);
            }
        });

    }

    private void suscribecode(final String topic_text) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic_text)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Suscribed to "+topic_text;
                        if (!task.isSuccessful()) {
                            msg = "Task Not successful ";
                        }
                        Log.d("myapp", msg);
                        Toast.makeText(SuscribeToTopic.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
