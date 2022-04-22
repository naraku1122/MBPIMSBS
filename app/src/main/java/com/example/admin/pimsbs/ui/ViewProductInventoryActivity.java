package com.example.admin.pimsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.pimsbs.R;
import com.example.admin.pimsbs.adapters.WrapContentLinearLayoutManager;
import com.example.admin.pimsbs.listeners.ProductDataRecyclerViewListener;
import com.example.admin.pimsbs.models.Products;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import com.example.admin.pimsbs.adapters.ProductDataListsAdapter;

public class ViewProductInventoryActivity extends AppCompatActivity {
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    private TextView totalnoofitem, totalnoofsum;
    private int counttotalnoofitem = 0;

    private ProductDataListsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        totalnoofitem= findViewById(R.id.totalnoitem);
        totalnoofsum = findViewById(R.id.totalsum);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Product").child("barcode");

        mrecyclerview = findViewById(R.id.recyclerViews);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<Products> products = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(mdatabaseReference, Products.class)
                .build();

        adapter = new ProductDataListsAdapter(products);
        mrecyclerview.setAdapter(adapter);

        mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    counttotalnoofitem = (int) dataSnapshot.getChildrenCount();
                    totalnoofitem.setText(" "+Integer.toString(counttotalnoofitem));
                }else{
                    totalnoofitem.setText(" 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewProductSum();

        mrecyclerview.addOnItemTouchListener(new ProductDataListsAdapter(products, this, mrecyclerview, new ProductDataRecyclerViewListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongPressed(View view, int position) {
                String tempBarcode = ((TextView) mrecyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.viewproductbarcode)).getText().toString();
                String tempName = ((TextView) mrecyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.viewproductname)).getText().toString();
                String tempPrice = ((TextView) mrecyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.viewproductprice)).getText().toString();
                String tempQuantity = ((TextView) mrecyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.viewproductquantity)).getText().toString();

                editActivity(tempBarcode, tempName, tempPrice, tempQuantity);
            }

            @Override
            public void onItemLongPressedDelete(View view, int position) {

            }
        }));

    }

    private void editActivity(String barcode, String name, String price, String quantity){
        Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);
        intent.putExtra("barcode", barcode);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("quantity", quantity);
        startActivity(intent);
    }

    private void viewProductSum(){
        mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double sum = 0.0, temp;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("productPrice");
                    Object quantity = map.get("productQuantity");
                    double pValue = Double.parseDouble(String.valueOf(price));
                    int pQuantity = Integer.parseInt(String.valueOf(quantity));
                    temp = pValue * pQuantity;
                    sum += temp;
                    totalnoofsum.setText(" Php "+String.format("%.2f",sum));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewProductSum();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

}