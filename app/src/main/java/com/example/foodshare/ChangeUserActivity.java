package com.example.foodshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodshare.classes.Helper;
import com.example.foodshare.classes.User;

import java.util.ArrayList;
import java.util.List;

public class ChangeUserActivity extends AppCompatActivity {
    Helper helper;
    List<User> users;
    Spinner spinner2;
    Button btnChange;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        spinner2 = findViewById(R.id.spinnerUsers);
        btnChange = findViewById(R.id.btnChangeUser);
        pos = -1;
        helper = new Helper();
        users = helper.getList(this, "usuarios", User.class);
        String[] usersStr = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            usersStr[i] = users.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, usersStr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos >= 0){
                    User user = users.get(pos);
                    helper.saveLocalUser(ChangeUserActivity.this, user);

                }
            }
        });


    }
}