package com.example.test.childlocationtracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegistration extends AppCompatActivity {
    private DBHelper dbHelper;
    private EditText username,password,repassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        dbHelper=new DBHelper(this);
        username=(EditText)findViewById(R.id.txtUsername);
        password=(EditText)findViewById(R.id.txtPassword);
        repassword=(EditText)findViewById(R.id.txtRepassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_registration, menu);
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

    public void Register(View view) {
        if(username.getText().toString().length()>=2 && password.getText().toString().length()>=2 && password.getText().toString().equals(repassword.getText().toString())) {
            dbHelper.createUser(username.getText().toString(), password.getText().toString());
            Intent intent=new Intent(UserRegistration.this,Login.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this,"Password mismatch error\n Username must be 2 or more character long",Toast.LENGTH_LONG).show();
        }

    }
}
