package com.example.grableapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity {

    ArrayList<String> arrayList = new ArrayList<>();


    //ArrayAdapter<String> arrayAdapter;
    private ListView lvProduct;
    private ProductListAdapter adapter;
    private List<Product> mProductList;


    public void addItemToList(String name, double price, double weight, String description)
    {
        mProductList.add(new Product("1", name, price,weight, description));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers2);

        lvProduct = (ListView)findViewById(R.id.offersList);

        mProductList = new ArrayList<>();

        addItemToList("Apa plata Dorna", 1.99,0.0,"Old price: 2.99 RON");
        addItemToList("Redbull", 3.99, 0.0,"Old price 4.99 RON");
        addItemToList("Capy Portocale", 4.59, 0.0,"Old price: 5.59");
        addItemToList("Lepte ZUZU", 2.35, 0.0,"Old price: 3.99");
        addItemToList("Paine Velpitar", 2.99, 0.0,"Old price: 4.99");
        addItemToList("Corn 7Days Ciocolata", 4.5, 0.0,"Old price: 5");
        //addItemToList("Capy Portocale", 4.59, 0.0,"Old price: 5.59");

        adapter = new ProductListAdapter(getApplicationContext(), mProductList);
        lvProduct.setAdapter(adapter);
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

}
