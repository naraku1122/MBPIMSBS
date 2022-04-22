package com.example.admin.pimsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admin.pimsbs.R;
import com.example.admin.pimsbs.activities.ScanCodeActivityDeleteProduct;
import com.example.admin.pimsbs.adapters.ProductDataListsAdapter;
import com.example.admin.pimsbs.adapters.WrapContentLinearLayoutManager;
import com.example.admin.pimsbs.listeners.ProductDataRecyclerViewListener;
import com.example.admin.pimsbs.models.Products;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeleteProductActivity extends AppCompatActivity {
    public static TextView resultdeleteview;
    RecyclerView mrecyclerview;
    Button scantodelete, deletebtn;

    private ProductDataListsAdapter adapter;

    DatabaseReference mdatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_products);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Product").child("barcode");
        resultdeleteview = findViewById(R.id.barcodedelete);
        scantodelete = findViewById(R.id.buttonscandelete);
        deletebtn= findViewById(R.id.deleteItemToTheDatabasebtn);

        mrecyclerview = findViewById(R.id.recyclerviewListItems);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FirebaseRecyclerOptions<Products> products = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(mdatabaseReference, Products.class)
                .build();

        adapter = new ProductDataListsAdapter(products);
        mrecyclerview.setAdapter(adapter);

        mrecyclerview.addOnItemTouchListener(new ProductDataListsAdapter(products,this, mrecyclerview, new ProductDataRecyclerViewListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongPressed(View view, int position) {

            }

            @Override
            public void onItemLongPressedDelete(View view, int position) {
                String temp = ((TextView) mrecyclerview.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.viewproductbarcode)).getText().toString();
                deleteProductLongPressed(temp);
            }
        }));

        scantodelete.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScanCodeActivityDeleteProduct.class));
        });

        deletebtn.setOnClickListener(v -> deleteProductBarcode());

    }

    private void deleteProductBarcode()
    {
        String deletebarcodevalue = resultdeleteview.getText().toString();

        if(!TextUtils.isEmpty(deletebarcodevalue)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Product");
            builder.setMessage("Delete Product?");
            builder.setCancelable(true);

            builder.setPositiveButton("Yes", (dialog, id)->{
                dialog.dismiss();
                mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mdatabaseReference.child(deletebarcodevalue).getRef().removeValue();
                        Toast.makeText(DeleteProductActivity.this,"Product Deleted.",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });

            builder.setNegativeButton("No", (dialog, id) -> {
                dialog.cancel();
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
        else{
            Toast.makeText(DeleteProductActivity.this,"Please scan Barcode",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProductLongPressed(String product){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product");
        builder.setMessage("Delete Product?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", (dialog, id)->{
            dialog.dismiss();
            mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mdatabaseReference.child(product).getRef().removeValue();
                    Toast.makeText(DeleteProductActivity.this,"Product Deleted.",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.cancel();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
