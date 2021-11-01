package com.example.grableapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import static android.Manifest.permission.CAMERA;
import static java.lang.Thread.sleep;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;



public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    Firebase myFirebase;
    DatabaseReference Ref;
    private int cart;
    private float newVal;
    String res;
    private float oldVal;
    private float Value1;
    private float psweight;
    private float Value2;
    private float Value3;
    private float weight;
    private String myCart="";
    private int hascart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        Intent intent=getIntent();
        hascart=intent.getIntExtra("hascart",0);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {

                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(ScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        FirebaseDatabase _database = FirebaseDatabase.getInstance();
        Ref = _database.getReference();
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        Intent intent= getIntent();
        cart = intent.getIntExtra("cos", 0 );
        hascart = intent.getIntExtra("hascart",0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        if (cart ==0){
        builder.setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                String user = currentFirebaseUser.getUid();

                String key = Ref.child("users").child(user).child("Cumparaturi").child("fara cos").push().getKey();

                Ref.child("users").child(user).child("Cumparaturi").child(key).setValue(myResult);


                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent myIntent = new Intent(ScannerActivity.this, ListActivity.class);
                        myIntent.putExtra("cos", cart);
                        myIntent.putExtra("hascart", hascart);
                        startActivity(myIntent);
                    }
                }, 500);


            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(ScannerActivity.this);
            }
        });

        }
        else
            if (hascart==0){
        builder.setPositiveButton("Choose this shopping cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myCart = result.getText();
                Toast.makeText(getApplicationContext(),"You have chosen"+ myCart, Toast.LENGTH_LONG).show();
                hascart = 1;

                Intent myIntent = new Intent(ScannerActivity.this, ListActivity.class);
                myIntent.putExtra("cos", cart);
                myIntent.putExtra("hascart", hascart);
                startActivity(myIntent);

            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(ScannerActivity.this);
            }
        });
        }
            else{

                builder.setPositiveButton("Add to cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(getApplicationContext(), String.valueOf(myResult), Toast.LENGTH_LONG).show();

                        Ref.child("PRODUSE").child(String.valueOf(myResult)).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                String myChildValues = dataSnapshot.getKey();

                                 if(myChildValues.equals("greutate")) {

                                     Value1 =  dataSnapshot.getValue(Float.class);
                                    weight =Value1;
                                     //Toast.makeText(getApplicationContext(), "gr"+ String.valueOf(weight), Toast.LENGTH_LONG).show();
                                 }


                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {}

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                        Ref.child("cosuri").child("costest").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                String myChildValues = (String) dataSnapshot.getKey();
                                 Value2=dataSnapshot.getValue(Float.class);
                                if(myChildValues.equals("oldValue")) {
                                    oldVal = Value2;
                                    //Toast.makeText(getApplicationContext(), "old" + String.valueOf(oldVal), Toast.LENGTH_LONG).show();}
                                }
                                    else{
                                    if(myChildValues.equals("newValue"))
                                    {  newVal =Value2;
                                        //Toast.makeText(getApplicationContext(), "new "+String.valueOf(newVal), Toast.LENGTH_LONG).show();}
                                }}
                            }


                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {}

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                psweight = newVal - oldVal;
                                //Toast.makeText(getApplicationContext(), "ps"+String.valueOf(psweight) + "new" + newVal +"old" + oldVal, Toast.LENGTH_LONG).show();
                                if(psweight<=weight+70&&psweight>=weight-70)
                                { //Toast.makeText(getApplicationContext(), "greutatea produsului scanat corespunde cu greutatea adaugata in cos! ", Toast.LENGTH_LONG).show();
                                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                                    String user = currentFirebaseUser.getUid();
                                    Calendar calendar = Calendar.getInstance();

                                    int thisYear = calendar.get(Calendar.YEAR);
                                    int thisMonth = calendar.get(Calendar.MONTH);
                                    int thisDay = calendar.get(Calendar.DAY_OF_MONTH);


                                    String key = Ref.child("users").child(user).child("Cumparaturi").child("cu cos").push().getKey();

                                    Ref.child("users").child(user).child("Cumparaturi").child(key).setValue(myResult);
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent myIntent = new Intent(ScannerActivity.this, ListActivity.class);
                                            myIntent.putExtra("cos", cart);
                                            myIntent.putExtra("hascart", hascart);
                                            startActivity(myIntent);
                                        }
                                    }, 400);

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "The weight of the scanned product does not equal the weight added to your cart!", Toast.LENGTH_LONG).show();
                                    scannerView.resumeCameraPreview(ScannerActivity.this);                                }
                            }
                        }, 700);



                    }
                });
                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(ScannerActivity.this);
                    }
                });

            }
        builder.setMessage(result.getText());
        AlertDialog alert1 = builder.create();
        alert1.show();
    }

}
