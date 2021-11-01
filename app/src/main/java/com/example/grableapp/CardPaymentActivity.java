package com.example.grableapp;

import android.app.AppComponentFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CardPaymentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardpayment);

        final ImageView loading_animation = findViewById(R.id.loadinganimation);
        final ImageView payment_view = findViewById(R.id.animationview);
        final TextView plsw_wait = findViewById(R.id.pleasewaitvew);
        final TextView processing = findViewById(R.id.proccesingview);


        loading_animation.animate().rotation(3600).setDuration(5000);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                loading_animation.setAlpha(0f);
                payment_view.setAlpha(0f);
                plsw_wait.setAlpha(0f);
                processing.setAlpha(0f);

            }
        }, 7000);



    }
}