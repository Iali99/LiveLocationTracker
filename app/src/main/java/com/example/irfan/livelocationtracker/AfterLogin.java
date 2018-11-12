package com.example.irfan.livelocationtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AfterLogin extends AppCompatActivity {

    Button liveTracking, sendLocation,reportIncident;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        liveTracking = (Button)findViewById(R.id.liveTrackButton);
        liveTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterLogin.this,LiveTrack.class);
                startActivity(intent);
            }
        });
        sendLocation = (Button)findViewById(R.id.sendLocationButton);
        reportIncident = (Button)findViewById(R.id.reportIncidentButton);
    }
}
