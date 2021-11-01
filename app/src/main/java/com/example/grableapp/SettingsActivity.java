package com.example.grableapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    Button logout_btn;
    Button add_card_btn;
    Button change_name_btn;
    static TextView name;
    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        logout_btn = findViewById(R.id.logOutButton);
        add_card_btn = findViewById(R.id.addCardButton);
        change_name_btn = findViewById(R.id.changeusername);
        username = findViewById(R.id.editTextUsername);


        name = findViewById(R.id.textView4);

        logout_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(SettingsActivity.this, SignInActivity.class));

            }
        });

        change_name_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SettingsActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                String stringUsername = username.getText().toString();
                if (stringUsername.isEmpty() == false) {
                    Users.name = stringUsername;
                    SettingsActivity.name.setText("Hello " + Users.name + "!");
            }
                else {
                    Toast.makeText(SettingsActivity.this, "Enter your new username", Toast.LENGTH_SHORT).show();
                }

            }
        });

        add_card_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SettingsActivity.this, AddCardActivity.class);
                startActivity(intent);
            }
        });
        if(Users.name != null) {
            name.setText("Hello " + Users.name + "!");
        } else {
            name.setText("Hello User!");
        }

    }
}
