package com.example.test.childlocationtracking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManageContact extends AppCompatActivity {
    private Spinner spinner;
    private EditText editTextNumber;
    private Button btnCreateContact;
    private ListView listView;
    private DBHelper dbHelper;
    private ArrayAdapter<ContactDAO> contactArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contact);

        dbHelper=new DBHelper(this);

        spinner=(Spinner)findViewById(R.id.contactTypeSpinner);
        String[] items = new String[]{"Parent", "Child"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        editTextNumber=(EditText)findViewById(R.id.editTextNumber);

        btnCreateContact=(Button)findViewById(R.id.btnAddContact);
        btnCreateContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dbHelper.createContact(spinner.getSelectedItem().toString(),editTextNumber.getText().toString());
                loadListView();
            }
        });

        listView=(ListView)findViewById(R.id.listViewContact);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> p, View v, final int po, long id) {

                ContactDAO contactDAO = (ContactDAO) p.getItemAtPosition(po);
                if (dbHelper.deleteContact(String.valueOf(contactDAO.Id))) {
                    Toast.makeText(ManageContact.this, "Successfully Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ManageContact.this, "Failed to delete", Toast.LENGTH_LONG).show();
                }
                loadListView();
                return true;
            }
        });

        loadListView();

    }

    public void loadListView()
    {
        List<ContactDAO> contact=new ArrayList<ContactDAO>();
        contact=dbHelper.getAllContacts();
        contactArrayAdapter=new ArrayAdapter<ContactDAO>(this,android.R.layout.simple_list_item_1,contact);
        /*for(GetGeofenceDataFromDB gd:gf)
        {
            listAdapter.add(gd.Address);
        }*/
        listView.setAdapter(contactArrayAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            new AlertDialog.Builder(ManageContact.this)
                    .setTitle("Help Info!")
                    .setMessage("1.Press and hold an item to delete")

                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
