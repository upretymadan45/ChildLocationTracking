package com.example.test.childlocationtracking;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class ExtractLocation extends Service  implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    //private final String TAG = "MyAwesomeApp";

    protected GoogleApiClient mGoogleApiClient;

    protected Location mLastLocation;

    protected LocationRequest mLocationRequest;

    public static String Longitude="";
    public static String Latitude="";

    public static String ServiceStatus="";

    public ExtractLocation() {
    }

    @Override
    public void onCreate() {
        Toast.makeText(this,"Hi Service",Toast.LENGTH_LONG).show();
        //stopSelf();
        buildGoogleApiClient();
        // Connect the client.
        mGoogleApiClient.connect();
        //startLocationUpdates();
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this,"Service Destroyed",Toast.LENGTH_LONG).show();
        mGoogleApiClient.disconnect();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        ServiceStatus="Started";
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Latitude=String.valueOf(mLastLocation.getLatitude());
            Longitude=String.valueOf(mLastLocation.getLongitude());
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+64223616617", null, "locReply"+" "+ExtractLocation.Latitude+" "+ExtractLocation.Longitude, null, null);
            Toast.makeText(this, String.valueOf(mLastLocation.getLatitude()) +"Speed="+mLastLocation.getSpeed()+ "From Service", Toast.LENGTH_LONG).show();
            //stopSelf();
            startLocationUpdates();
        }
    }

    public void SendSMS(String message, String number)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Latitude=String.valueOf(location.getLatitude());
        Longitude=String.valueOf(location.getLongitude());
        Toast.makeText(this,String.valueOf(location.getSpeed()).toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
