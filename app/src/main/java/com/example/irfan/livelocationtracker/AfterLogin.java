package com.example.irfan.livelocationtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AfterLogin extends AppCompatActivity {

    ImageButton liveTracking, sendLocation,reportIncident;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        liveTracking = (ImageButton) findViewById(R.id.liveTrackButton);
        liveTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterLogin.this,LiveTrack.class);
                startActivity(intent);
            }
        });
        sendLocation = (ImageButton) findViewById(R.id.sendLocationButton);
        reportIncident = (ImageButton)findViewById(R.id.reportIncidentButton);
    }
}
