package com.example.admin.pimsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.pimsbs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {

    private TextView barcode;
    private EditText editProductName, editProductPrice, editProductQuantity;
    private Spinner editProductCategory;
    private Button editProductBtn;

    private DatabaseReference mdatabaseReference;

    private String tempBarcode, tempName, tempPrice, tempQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        barcode = findViewById(R.id.editBarcodeTxt);
        editProductName = findViewById(R.id.editproductname);
        editProductPrice = findViewById(R.id.editproductprice);
        editProductQuantity = findViewById(R.id.editproductquantity);

        editProductCategory = findViewById(R.id.editproductcategory);

        editProductBtn = findViewById(R.id.editproductbuttontodatabase);

        Intent intent = getIntent();
        tempBarcode = intent.getStringExtra("barcode");
        tempName = intent.getStringExtra("name");
        tempPrice = intent.getStringExtra("price");
        tempQuantity = intent.getStringExtra("quantity");
        barcode.setText(tempBarcode);
        editProductName.setText(tempName);
        editProductPrice.setText(tempPrice);
        editProductQuantity.setText(tempQuantity);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.product_item_categories, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        editProductCategory.setAdapter(adapter);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Product");

        editProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProduct();
            }
        });

    }

    private void editProduct(){
        String productnameValue = editProductName.getText().toString();
        String productcategoryValue = editProductCategory.getSelectedItem().toString();
        String productpriceValue = editProductPrice.getText().toString();
        String productquantityValue = editProductQuantity.getText().toString();

        if(!TextUtils.isEmpty(productnameValue)&&!TextUtils.isEmpty(productcategoryValue)&&!TextUtils.isEmpty(productpriceValue)&&!TextUtils.isEmpty(productquantityValue)){
            String id = tempBarcode;

            mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("barcode").child(id).exists()){
                        Map<String, Object> updateProduct = new HashMap<String,Object>();
                        updateProduct.put("productName", productnameValue);
                        updateProduct.put("productCategory", productcategoryValue);
                        updateProduct.put("productPrice", productpriceValue);
                        updateProduct.put("productQuantity", productquantityValue);

                        mdatabaseReference.child("barcode").child(id).updateChildren(updateProduct);
                        Toast.makeText(getApplicationContext(), "Product Updated.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else{
            Toast.makeText(getApplicationContext(), "Input fields.", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
