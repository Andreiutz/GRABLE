package com.example.grableapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button shop;
    Button list;
    ImageView settings;
    Button offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shop = (Button) findViewById(R.id.shop_btn);
        settings = (ImageView) findViewById(R.id.settingsdots);
        offers = (Button) findViewById(R.id.oferrsbutton);


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OffersActivity.class);
                startActivity(intent);
            }
        });


        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Do you want to buy more than 5 products?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder build2 = new AlertDialog.Builder(MainActivity.this);
                        build2.setCancelable(true);
                        build2.setTitle("You can start shopping!");
                        build2.setPositiveButton("Start Shopping", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(MainActivity.this, ListActivity.class);

                                int cart = 0;
                                myIntent.putExtra("cos", cart);
                                startActivity(myIntent);

                            }
                        });
                        build2.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        build2.show();
                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
                        build.setCancelable(true);
                        build.setTitle("Please scan the QR code attached to your shopping cart!");
                        build.setPositiveButton("Scan", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(MainActivity.this, ScannerActivity.class);

                                int cart = 1;
                                int hascart = 0;
                                myIntent.putExtra("cos", cart);
                                myIntent.putExtra("hascart", hascart);
                                startActivity(myIntent);
                            }
                        });
                        build.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        build.show();
                    }
                });

                builder.show();
            }
        });
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

    }
}