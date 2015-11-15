package com.example.test.childlocationtracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManageGeofence extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<GetGeofenceDataFromDB> listAdapter;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_geofence);
        listView=(ListView)findViewById(R.id.listViewGeofence);
        dbHelper=new DBHelper(this);
        loadListView();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> p, View v, final int po, long id) {

                GetGeofenceDataFromDB gf = (GetGeofenceDataFromDB) p.getItemAtPosition(po);
                Toast.makeText(ManageGeofence.this, gf.Address, Toast.LENGTH_LONG).show();
                if (dbHelper.deleteGeofence(String.valueOf(gf.Id))) {
                    deleteGeofenceFromSMS(String.valueOf(gf.Id));
                    Toast.makeText(ManageGeofence.this, "Successfully Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ManageGeofence.this, "Failed to delete", Toast.LENGTH_LONG).show();
                }
                loadListView();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                GetGeofenceDataFromDB gf=(GetGeofenceDataFromDB)adapter.getItemAtPosition(position);
                Intent intent=new Intent(ManageGeofence.this,MainActivity.class);
                intent.putExtra("Id",String.valueOf(gf.Id));
                startActivity(intent);
            }
        });
    }

    public void loadListView()
    {
        List<GetGeofenceDataFromDB> gf=new ArrayList<GetGeofenceDataFromDB>();
        gf=dbHelper.getAllGeofences();
        listAdapter=new ArrayAdapter<GetGeofenceDataFromDB>(this,android.R.layout.simple_list_item_1,gf);
        /*for(GetGeofenceDataFromDB gd:gf)
        {
            listAdapter.add(gd.Address);
        }*/
        listView.setAdapter(listAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_geofence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteGeofenceFromSMS(String Id)
    {
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(dbHelper.getContact("Child").toString(),null,"Delete "+Id+"",null,null);
    }
}
