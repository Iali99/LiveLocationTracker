package com.example.irfan.livelocationtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    DatabaseReference onlineRef,currentUserRef,counterRef,locations;


    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RES_REQUEST = 7172;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 500;
    private static int FASTEST_INTERVAL = 300;

    private Timer locTimer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locTimer = new Timer();

        locations = FirebaseDatabase.getInstance().getReference("LiveLocations");
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        buildGoogleAPIClient();
        createLocationRequest();
        Global.alertCounter = 0;
        sendLocation();

        setupSystem();
        return START_STICKY;
    }

    private void setupSystem() {
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class)){
                    currentUserRef.onDisconnect().removeValue();
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),"Online"));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    Log.d("LOG",""+user.getEmail()+" is "+user.getStatus());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//        sendLocation();
//    }



    private void sendLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        Log.d("LOG","Inside sendLocation");
        mLastLocation =  LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            Log.d("LOG","Sending location to the database");
            double currentLat = mLastLocation.getLatitude();
            double currentLon = mLastLocation.getLongitude();
            if(distance(Global.prevLat,Global.prevLon,currentLat,currentLon) <= 1){
                Global.alertCounter ++;
                Global.prevAlert = (Global.alertCounter > 5) ? true : false;

                locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                currentLat,
                                currentLon,Global.alertCounter,Global.prevAlert));
            }
            else {
                Global.alertCounter = 0;
                Global.prevLat = currentLat;
                Global.prevLon = currentLon;
                Global.prevAlert = false;

                locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                currentLat,
                                currentLon,Global.alertCounter,Global.prevAlert));
            }


        }
//        else{
//            Log.d("LOG","Reached printing toast");
//            Toast.makeText(this,"Couldnt get the location",Toast.LENGTH_SHORT).show();
//        }
    }

    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    

    @Override
    public void onDestroy() {
        locTimer.cancel();
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onDestroy();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("LOG","Sending location to server");
                sendLocation();
            }
        },0,1000);
        startLocationUpdates();

    }

    private void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //mLastLocation = location;
        //sendLocation();
    }

    private static final int EARTH_RADIUS = 6371000; // Approx Earth radius in KM

    public static double distance(double startLat, double startLong,
                                  double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    public static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
