package com.firasshawa.prayertracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firasshawa.prayertracker.Models.User;
import com.firasshawa.prayertracker.R;
import com.firasshawa.prayertracker.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    EditText name_et;
    Button save_btn;

    DatabaseReference dbRef;

    SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Shared Preferences setup
        preferences = getSharedPreferences("SharedPref",getApplicationContext().MODE_PRIVATE);
        editor = preferences.edit();

        //Widget Setup
        name_et = findViewById(R.id.name_et);
        save_btn = findViewById(R.id.save_btn);

        //fireabse database referance setup
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        //onclick listeners
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddUser();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the user has be registered be for go direct to his/her profile
        if(preferences.getAll().containsKey("PrayerUserKey")){
            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();

        }
    }

    public void AddUser(){
        //get username
        String name = name_et.getText().toString().trim();

        //new user object
        User user = new User();

        if(name.isEmpty()){
            name_et.requestFocus();
            name_et.setError("أدخل الاسم !");
            return;
        }else{
            //generate key in firebase database
            String key  = dbRef.push().getKey();

            //fill the user object
            user.setKey(key);
            user.setName(name);

            //update the firebase child(key) with the filed object
            dbRef.child(user.getKey()).setValue(user);

            //add the user key to the shared Preferences
            editor.putString("PrayerUserKey",user.getKey());
            editor.apply();

            //to go to the next activity
            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();
        }
    }
}
