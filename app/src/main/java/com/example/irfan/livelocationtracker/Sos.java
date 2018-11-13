package com.example.irfan.livelocationtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sos extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    DatabaseReference sosDatabase,currentUserRef;

    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RES_REQUEST = 7172;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        sosDatabase =  FirebaseDatabase.getInstance().getReference("SOS");
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);
        }
        else{
            if(checkPlayServices()){
                buildGoogleApiClient();
                createLocationRequest();
                sendLocation();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(checkPlayServices()){
                        buildGoogleApiClient();
                        createLocationRequest();
                        sendLocation();
                    }
                }
            }
        }
    }

    public void sendLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("LOG", "Sending location to the database");
            sosDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            mLastLocation.getLatitude(),
                            mLastLocation.getLongitude(), 0, true));
        } else {
            Log.d("LOG", "Reached printing toast");
            Toast.makeText(this, "Couldnt get the location", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)){
                GoogleApiAvailability.getInstance().getErrorDialog(this,resultCode,PLAY_SERVICES_RES_REQUEST).show();
            }
            else{
                Toast.makeText(this,"This device is not supported",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        sendLocation();

    }



    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
