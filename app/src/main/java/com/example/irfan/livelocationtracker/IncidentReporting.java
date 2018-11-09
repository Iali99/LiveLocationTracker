package com.example.irfan.livelocationtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.widget.Toast.LENGTH_SHORT;

public class IncidentReporting extends AppCompatActivity {

    DatabaseReference incidents;

    EditText incidentType,incidentIntensity,incidentLat,incidentLon;
    Button report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_reporting);


        incidents = FirebaseDatabase.getInstance().getReference("Incidents");
        incidentType = (EditText)findViewById(R.id.incidentType);
        incidentIntensity = (EditText)findViewById(R.id.incidentIntensity);
        incidentLat = (EditText)findViewById(R.id.incidentLat);
        incidentLon = (EditText)findViewById(R.id.incidentLon);

        report = (Button) findViewById(R.id.reportButton);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllFieldsDone()){
                    incidents.child(String.valueOf(Global.incidentCounter))
                            .setValue(new IncidentClass(incidentType.getText().toString(),Integer.parseInt(incidentIntensity.getText().toString()),
                                    Double.parseDouble(incidentLat.getText().toString()),
                                    Double.parseDouble((incidentLon.getText().toString()))));
                    Global.incidentCounter++;
                    Intent intent = new Intent(IncidentReporting.this,IncidentReportDone.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(IncidentReporting.this,"Fill all the fields",LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkAllFieldsDone(){
        if(incidentType.getText() == null)
                return false;
        if(incidentIntensity.getText() == null)
            return false;
        if(incidentType.getText() == null)
            return false;
        if(incidentType.getText() == null)
            return false;
        return true;

    }
}
