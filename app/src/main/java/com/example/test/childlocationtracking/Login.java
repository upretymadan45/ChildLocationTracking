package com.example.test.childlocationtracking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private DBHelper dbHelper;
    private EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        username=(EditText)findViewById(R.id.txtLoginUsername);
        password=(EditText)findViewById(R.id.txtLoginPassword);
        dbHelper=new DBHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void Login(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        int isUserInDB=dbHelper.getUser(username.getText().toString(),password.getText().toString());
                        if(isUserInDB>0)
                        {
                            Intent i = new Intent(Login.this, MainActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            new AlertDialog.Builder(Login.this)
                                    .setTitle("Error")
                                    .setMessage("User not found !")

                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void Register(View view) {
        Intent intent=new Intent(Login.this,UserRegistration.class);
        startActivity(intent);
    }
}
