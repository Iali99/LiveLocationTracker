package com.example.irfan.livelocationtracker;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, incidentActivity;

    private final static int LOGIN_PERMISSION=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button)findViewById(R.id.btnSignIn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                        .build(),LOGIN_PERMISSION
                );
            }
        });

        incidentActivity = (Button)findViewById(R.id.incidentActivity);
        incidentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,IncidentReporting.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == LOGIN_PERMISSION){
            startNewActivity(resultCode,data);
        }
    }

    private void startNewActivity(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Intent intent = new Intent(MainActivity.this,AfterLogin.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Login Failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
