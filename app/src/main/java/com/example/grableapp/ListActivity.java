package com.example.grableapp;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class ListActivity extends AppCompatActivity  {

    ArrayList<String> arrayList = new ArrayList<>();


    ArrayAdapter<String> arrayAdapter;

    Map<String, String> shoppingList = new HashMap<String, String>();

    Button add_btn;
    Button update_btn;
    Button finish_btn;

    ImageView done;
    ImageView done_circle;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable avd2;
    TextView thank_you_text;


    private float newVal;
    private float oldVal;

    private double psweight;
    private float Value2;

    private float weight;
    private ListView lvProduct;
    private ProductListAdapter adapter;
    private List<Product> mProductList;
    private int number_of_products = 0;
    private int cart;
    private int hascart;
    private double total = 0.0;
    private FirebaseDatabase _database1;
    private String prodID;
    private float price;
    private String ID;
    private String name;
    private String description;
    private String myChildValues;
    private int k;

    private DatabaseReference _databaseRef1;
    private DatabaseReference _databaseRef2;





    public void addItemToList(String name, double price, String description)
    {
//        if (mProductList.isEmpty()) {
//            mProductList.add(new Product(1, name, price, description));
//        }
//        else{
//            int id = mProductList.get(mProductList.size()).getId();
//            mProductList.add(new Product(id+1, name, price, description));
//        }
        //nu merge varianta de mai sus
        //trebuie adaugat id-ul ultimului element din lista + 1

        //mProductList.add(new Product(1, name, price, description));
    }

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Firebase.setAndroidContext(this);

        Intent intent2= getIntent();

        cart = intent2.getIntExtra("cos", 0 );
        hascart = intent2.getIntExtra("hascart",0);


        add_btn = findViewById(R.id.list_btn);
        finish_btn = findViewById(R.id.finish_shopping);

        done = findViewById(R.id.done);
        done_circle = findViewById(R.id.markfundal);
        thank_you_text = findViewById(R.id.thankyoutext);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        final String user = currentFirebaseUser.getUid();
        final ImageView loading_animation = findViewById(R.id.loadinganimation);
        final ImageView payment_view = findViewById(R.id.animationview);
        final TextView plsw_wait = findViewById(R.id.pleasewaitvew);
        final TextView processing = findViewById(R.id.proccesingview);
        FirebaseDatabase _database = FirebaseDatabase.getInstance();
        final DatabaseReference Ref = _database.getReference();

        _database1 = FirebaseDatabase.getInstance();
        _databaseRef1 = _database1.getReference();

        loading_animation.setAlpha(1f);
        payment_view.setAlpha(1f);
        plsw_wait.setAlpha(1f);
        processing.setAlpha(1f);

        loading_animation.animate().translationYBy(-1000f);
        payment_view.animate().translationYBy(-1000f);
        plsw_wait.animate().translationYBy(-1000f);
        processing.animate().translationYBy(-1000f);
        thank_you_text.animate().translationYBy(1000);
        lvProduct = (ListView)findViewById(R.id.listview_produsct);
        mProductList = new ArrayList<>();
        adapter = new ProductListAdapter(getApplicationContext(), mProductList);

        //Add items to list
        mProductList.clear();

        number_of_products=0;
        adapter.notifyDataSetChanged();
        lvProduct.setAdapter(adapter);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 500);


        Ref.child("users").child(user).child("Cumparaturi").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot item_snapshot:dataSnapshot.getChildren()) {
                    myChildValues = (String) item_snapshot.getValue(String.class);
                   // Toast.makeText(getApplicationContext(), myChildValues, Toast.LENGTH_LONG).show();
                    Ref.child("PRODUSE").child(myChildValues).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot item_snapshot2:dataSnapshot.getChildren()){
                                String myChildValues2 = (String) item_snapshot2.getKey();
                            ID= String.valueOf(myChildValues);

                        if(myChildValues2.equals("pret")) {
                            float Value = item_snapshot2.getValue(Float.class);
                            price = Value;
                        }
                        if(myChildValues2.equals("greutate")){
                            float Value=item_snapshot2.getValue(Float.class);
                            weight= Value;
                        }
                        if(myChildValues2.equals("descriere")){
                            String Value=item_snapshot2.getValue(String.class);
                            description=Value;
                        }
                        if(myChildValues2.equals("denumire")){
                            String Value=item_snapshot2.getValue(String.class);
                            name=Value;
                        }
                            }


                            Product product = new Product(ID, name, price, weight, description);
                            mProductList.add(product);
                            number_of_products+=1;
                            adapter.notifyDataSetChanged();




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                                           // Product product = new Product(ID, name, price, weight, description);
//                            mProductList.add(product);
                            //Toast.makeText(ListActivity.this,product.getName() , Toast.LENGTH_SHORT).show();
//                            number_of_products+=1;
//                            adapter.notifyDataSetChanged();

//                                    if(number_of_products>=5)
//                                    { AlertDialog.Builder builder3 = new AlertDialog.Builder(ListActivity.this);
//                                        builder3.setTitle("You need to choose a shopping cart!");
//
//                                        builder3.setPositiveButton("Add Cart", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        });
//                                        builder3.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//
//                                            }
//                                        });
//
//                                        AlertDialog alert3 = builder3.create();
//                                        alert3.show();
//                                    }





            }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        finish();
//        startActivity(getIntent());


//        Ref.child("users").child(user).child("Cumparaturi").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                 myChildValues = (String) dataSnapshot.getValue();
//                  k=0;
//                Toast.makeText(getApplicationContext(), myChildValues, Toast.LENGTH_LONG).show();
//                Ref.child("PRODUSE").child(myChildValues).addChildEventListener(new ChildEventListener() {
//                    @Override
//
//                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                        String myChildValues2 = (String) dataSnapshot.getKey();
//                            ID= String.valueOf(myChildValues);
//
//                        if(myChildValues2.equals("pret")) {
//                            float Value = dataSnapshot.getValue(Float.class);
//                            price = Value;
//                            k=k+1;
//                        }
//                        if(myChildValues2.equals("greutate")){
//                            float Value=dataSnapshot.getValue(Float.class);
//                            weight= Value;
//                            k=k+1;
//                        }
//                        if(myChildValues2.equals("descriere")){
//                            String Value=dataSnapshot.getValue(String.class);
//                            description=Value;
//                            k=k+1;
//                        }
//                        if(myChildValues2.equals("denumire")){
//                            String Value=dataSnapshot.getValue(String.class);
//                            name=Value;
//                            k=k+1;
//                        }
//                       if(k==4) {
//                           Toast.makeText(ListActivity.this, "" + name + weight + description + price, Toast.LENGTH_SHORT).show();
//                           Product product= new Product(ID, name, price, weight, description);
//                           mProductList.add(product);
//                           //Toast.makeText(ListActivity.this,product.getName() , Toast.LENGTH_SHORT).show();
//                           number_of_products+=1;
//                           adapter.notifyDataSetChanged();
//
//                       }
//
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {}
//                });

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Product product = new Product(ID, name, price, weight, description);
//                        mProductList.add(product);
//                        Toast.makeText(ListActivity.this,product.getName() , Toast.LENGTH_SHORT).show();
//                        number_of_products+=1;
//                        if(number_of_products==3)adapter.notifyDataSetChanged();
//
//                    }
//                }, 400);






//            }
//
//
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                adapter.notifyDataSetChanged();
//
//
//            }
//        }, 500);


        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Product Product = (Product) lvProduct.getItemAtPosition(position);

                psweight = Product.getWeight();
                prodID=Product.getId();
                Toast.makeText(getApplicationContext(), prodID+"  "+psweight, Toast.LENGTH_LONG).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle("Do you want to delete the selected item?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(cart==1) {


                            AlertDialog.Builder builder2 = new AlertDialog.Builder(ListActivity.this);
                            builder2.setTitle("Please remove the item from your shopping cart");
                            builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Ref.child("cosuri").child("costest").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                                            String myChildValues = (String) dataSnapshot.getKey();
                                            Value2 = dataSnapshot.getValue(Float.class);
                                            if (myChildValues.equals("oldValue")) {
                                                oldVal = Value2;
                                                Toast.makeText(getApplicationContext(), "old" + String.valueOf(oldVal), Toast.LENGTH_LONG).show();}
                                             else {
                                                if (myChildValues.equals("newValue")) {
                                                    newVal = Value2;
                                                    Toast.makeText(getApplicationContext(), "new "+String.valueOf(newVal), Toast.LENGTH_LONG).show();}

                                            }
                                        }


                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            psweight = oldVal - newVal;
                                            //Toast.makeText(getApplicationContext(), "ps"+String.valueOf(psweight) + "new" + newVal +"old" + oldVal, Toast.LENGTH_LONG).show();
                                            if (psweight <= weight + 70 && psweight >= weight - 70) {
                                                //delete

                                                // Toast.makeText(getApplicationContext(), "The wei! ", Toast.LENGTH_LONG).show();

                                                Ref.child("users").child(user).child("Cumparaturi").addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                        String prod = (String) dataSnapshot.getValue(String.class);
                                                        if (prod.equals(prodID)) {
                                                            Ref.child("users").child(user).child("Cumparaturi").child(dataSnapshot.getKey()).removeValue();
                                                            number_of_products-=1;

                                                        }
                                                    }

                                                    @Override
                                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                                final Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                        startActivity(getIntent());
                                                    }
                                                }, 1000);


                                            } else {
                                                Toast.makeText(getApplicationContext(), "The weight removed from the cart is not equal to the weight of your selected item! ", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }, 700);


                                }
                            });
                            builder2.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                            AlertDialog alert2 = builder2.create();
                            alert2.show();
                        }

                        else{
                            Ref.child("users").child(user).child("Cumparaturi").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    String prod = (String) dataSnapshot.getValue(String.class);
                                    if (prod.equals(prodID)) {
                                        Ref.child("users").child(user).child("Cumparaturi").child(dataSnapshot.getKey()).removeValue();


                                    }
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(getIntent());
                                }
                            }, 1000);


                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                });

                AlertDialog alert1 = builder.create();
                alert1.show();
            }

        });

        //Se poate din database
/*
        mProductList.add(new Product(1, "Kefir", 12, "Covalact, 20% grasime"));
        mProductList.add(new Product(2, "Tastatura Asus", 300, "Tastatura Gaming Asus Cerberus"));
        mProductList.add(new Product(3, "iPhone5s", 1000, "Apple iPhone5s 16GB"));
        mProductList.add(new Product(4, "Paine VelPitar", 3.5, "Paine VelPitar Alba feliata"));
        mProductList.add(new Product(5, "iPhone6s", 1000, "Apple iPhone6s 16GB"));
        mProductList.add(new Product(6, "iPhone7", 1500, "Apple iPhone7 16GB"));
        mProductList.add(new Product(7, "iPhone7Plus", 2000, "Apple iPhone7Plus 16GB"));
        mProductList.add(new Product(8, "iPhone8", 2500, "Apple iPhone8 16GB"));
        mProductList.add(new Product(9, "iPhoneX", 4000, "Apple iPhoneX 16GB"));
        mProductList.add(new Product(10, "iPhone11", 6000, "Apple iPhone11 16GB"));
        addItemToList("Carne", 20.5, "Piept de pui");
*/

        ///more to add


        ///init adapter

//        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //ceva
//                //functie de delete item
//                Toast.makeText(getApplicationContext(), "Clicked product id =" + view.getTag(), Toast.LENGTH_LONG).show();
//            }
//        });


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ListActivity.this, ScannerActivity.class);
                intent.putExtra("cos", cart);
                intent.putExtra("hascart", hascart);

                startActivity(intent);
                //mProductList.add(new Product(1, "Kefir", 12, "Covalact, 20% grasime"));



            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm finish shopping?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder build2 = new AlertDialog.Builder(ListActivity.this);
                        build2.setTitle("Choose payment method");
                        build2.setPositiveButton("Credit card", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialogInterface, int i) {
                                for (Product item : mProductList) {
                                    total += item.getPrice();
                                    //Intent intent = new Intent(ListActivity.this, CardPaymentActivity.class);
                                    //startActivity(intent);
                                }
                                String totall = total + "";
                                lvProduct.setAlpha(0f);
                                add_btn.setAlpha(0f);
                                finish_btn.setAlpha(0f);
                                //Handler handler2 = new Handler();
                                loading_animation.animate().translationYBy(1000f).setDuration(500);
                                payment_view.animate().translationYBy(1000f).setDuration(500);
                                plsw_wait.animate().translationYBy(1000f).setDuration(500);
                                processing.animate().translationYBy(1000f).setDuration(500);


                                Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loading_animation.animate().rotation(3600).setDuration(5000);
                                    }
                                }, 500);

                                //loading_animation.animate().rotation(3600).setDuration(5000);
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

                                Handler handler3 = new Handler();
                                handler3.postDelayed(new Runnable() {

                                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        done_circle.setAlpha(1f);
                                        Drawable drawable = done.getDrawable();
                                        if (drawable instanceof AnimatedVectorDrawableCompat)
                                        {
                                            avd = (AnimatedVectorDrawableCompat) drawable;
                                            avd.start();
                                        }
                                        else if (drawable instanceof AnimatedVectorDrawable)
                                        {
                                            avd2 = (AnimatedVectorDrawable) drawable;
                                            avd2.start();
                                        }
                                    }
                                }, 7500);

                                Handler handler4 = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        thank_you_text.animate().translationYBy(-1075).setDuration(300);
                                    }
                                }, 8000);
//                                handler4.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        byte[] array = new byte[7];
//                                        new Random().nextBytes(array);
//                                        String generatedString = new String(array, Charset.forName("UTF-8"));
//                                        Intent intent = new Intent(ListActivity.this, QRActivity.class);
//                                        intent.putExtra("key", generatedString);
//                                        intent.putExtra("case", 1);
//                                        startActivity(intent);
//                                    }
//                                }, 8500);

                            }
                        });
                        build2.setNegativeButton("Generate QR code", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (Product item : mProductList) {
                                    total += item.getPrice();
                                }
                                String totall = total + "";
                                Intent intent = new Intent(ListActivity.this, QRActivity.class);
                                intent.putExtra("key", totall);
                                intent.putExtra("case", 2);
                                startActivity(intent);

                            }
                        });
                        build2.show();
                    }
                });
                    builder.show();
            }
        });

    }
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        startActivity(new Intent(ListActivity.this, MainActivity.class));
    }
}

