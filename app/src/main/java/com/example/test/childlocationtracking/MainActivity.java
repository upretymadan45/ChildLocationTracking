package com.example.test.childlocationtracking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText searchIn;
    private Button search;
    Geocoder geocoder;
    private GoogleMap mMap;
    private String radius;
    private DBHelper dbHelper;
    private String IdForGeofenceUpdateInDatabase="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new DBHelper(this);
        searchIn = (EditText)findViewById(R.id.editSearchIn);
        search = (Button)findViewById(R.id.btnSearch);

        search.setOnClickListener(searchButtonOnClickListener);
        try {
            Bundle bundle=getIntent().getExtras();
            IdForGeofenceUpdateInDatabase = bundle.getString("Id");
            Toast.makeText(this,IdForGeofenceUpdateInDatabase,Toast.LENGTH_LONG).show();
        }catch(Exception E){}
    }

    private void goToLocation(double lat,double lng,float zoom) {
       try {
               mMap = ((MapFragment) getFragmentManager().
                       findFragmentById(R.id.fragment)).getMap();
               mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
               LatLng ll = new LatLng(lat, lng);
           Marker TP = mMap.addMarker(new MarkerOptions().
                   position(ll).title("").snippet("please move if needed").draggable(true));

           CameraPosition cameraPosition = new CameraPosition.Builder().target(ll).zoom(14.0f).build();
           CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
           mMap.moveCamera(cameraUpdate);



           mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
               @Override
               public boolean onMarkerClick(final Marker marker) {
                   AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                   builder.setTitle("Enter radius in meter for circular geofence");

                    // Set up the input
                   final EditText input = new EditText(MainActivity.this);
                   // Specify the type of input expected
                   input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                   builder.setView(input);

                    // Set up the buttons
                   builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           geocoder = new Geocoder(MainActivity.this);
                           double lat=marker.getPosition().latitude;
                           double lng=marker.getPosition().longitude;
                           Address add;
                           try {
                               List<Address> list = geocoder.getFromLocation(lat, lng, 1);
                               add = list.get(0);
                               String street=add.getThoroughfare();
                               radius= input.getText().toString();
                               GetGeofenceDataFromDB gf=new GetGeofenceDataFromDB();
                               if(IdForGeofenceUpdateInDatabase=="") {
                                   gf.Address=street;
                                   gf.Latitude=String.valueOf(lat);
                                   gf.Longitude=String.valueOf(lng);
                                   gf.Radius=radius;
                                   gf.Id=0;
                                   if(dbHelper.addGeofence(gf)){
                                       sendGeofenceFromSMS(gf);
                                       Toast.makeText(MainActivity.this,"Geo-fence added successfully",Toast.LENGTH_LONG).show();
                                   }
                                   else{
                                       Toast.makeText(MainActivity.this,"Failed to add Geo-fence",Toast.LENGTH_LONG).show();
                                   }
                               }
                               else {
                                   Toast.makeText(MainActivity.this,"Updating..",Toast.LENGTH_LONG).show();
                                   gf.Id=Integer.parseInt(IdForGeofenceUpdateInDatabase);
                                   gf.Address=street;
                                   gf.Latitude=String.valueOf(lat);
                                   gf.Longitude=String.valueOf(lng);
                                   gf.Radius=radius;
                                   if(dbHelper.updateGeofence(gf))
                                   {
                                       sendGeofenceFromSMS(gf);
                                       Toast.makeText(MainActivity.this,"Geo-fence updated",Toast.LENGTH_LONG).show();
                                       IdForGeofenceUpdateInDatabase="";
                                   }
                                   else{
                                       Toast.makeText(MainActivity.this,"Failed to update Geo-fence",Toast.LENGTH_LONG).show();
                                   }
                               }
                           }catch(Exception e){}
                           Intent intent=new Intent(MainActivity.this,ManageGeofence.class);
                           startActivity(intent);
                       }
                   });
                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                       }
                   });

                   builder.show();
                   return true;
               }
           });

       } catch(Exception e){

       }
    }

    View.OnClickListener searchButtonOnClickListener =
            new View.OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    String location = searchIn.getText().toString();
                    try {
                        geocoder = new Geocoder(MainActivity.this);
                        List<Address> list=geocoder.getFromLocationName(location,1);
                        Address add=list.get(0);
                        String locality=add.getLocality();
                        Toast.makeText(MainActivity.this,locality,Toast.LENGTH_LONG).show();
                        double lat=add.getLatitude();
                        double lng=add.getLongitude();
                        goToLocation(lat,lng,15);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }};

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_openGeofence) {
            Intent intent=new Intent(this,ManageGeofence.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startGeofence(View view) {
        startService(new Intent(getBaseContext(), GeofenceService.class));
    }

    public void stopGeofence(View view) {
        stopService(new Intent(getBaseContext(), GeofenceService.class));
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void sendGeofenceFromSMS(GetGeofenceDataFromDB gf)
    {
        SmsManager smsManager=SmsManager.getDefault();
        if(gf.Id==0)
        {
            smsManager.sendTextMessage("+64223616617", null, "Insert " + gf.Address + " " + gf.Latitude + " " + gf.Longitude + " " + gf.Radius + "", null, null);
        }
        if(gf.Id!=0)
        {
            smsManager.sendTextMessage("+64223616617",null,"Update "+gf.Address+" "+gf.Latitude+" "+gf.Longitude+" "+gf.Radius+" "+gf.Id,null,null);
        }
    }
}
