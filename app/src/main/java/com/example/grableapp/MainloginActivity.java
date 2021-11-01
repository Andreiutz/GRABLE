package com.example.grableapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainloginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlogin);
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            finish();

            Intent intent = new Intent(MainloginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }


        login_btn =(Button)findViewById(R.id.firstpagelogin);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainloginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

    }

    public void register(View view) {
        Intent intent = new Intent(MainloginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

}


