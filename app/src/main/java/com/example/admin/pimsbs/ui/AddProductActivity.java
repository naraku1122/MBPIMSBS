package com.example.admin.pimsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.pimsbs.R;
import com.example.admin.pimsbs.activities.ScanCodeActivityAddProduct;
import com.example.admin.pimsbs.models.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProductActivity extends AppCompatActivity {
    private EditText productname, productprice, productquantity;
    private TextView productbarcode;
    private Spinner productcategory;
    public static TextView resulttextview;
    Button scanbutton, additemtodatabase;

    private DatabaseReference mdatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        resulttextview = findViewById(R.id.barcodeview);
        additemtodatabase = findViewById(R.id.addproductbuttontodatabase);
        scanbutton = findViewById(R.id.buttonscan);

        productbarcode= findViewById(R.id.barcodeview);
        productname = findViewById(R.id.edititemname);
        productcategory= findViewById(R.id.editcategory);
        productprice = findViewById(R.id.editprice);
        productquantity = findViewById(R.id.editquantity);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.product_item_categories, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        productcategory.setAdapter(adapter);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Product");

        scanbutton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ScanCodeActivityAddProduct.class)));

        additemtodatabase.setOnClickListener(v -> addProduct());
    }

    // adding product to database
    private void addProduct(){

        String productbarcodeValue = productbarcode.getText().toString();
        String productnameValue = productname.getText().toString();
        String productcategoryValue = productcategory.getSelectedItem().toString();
        String productpriceValue = productprice.getText().toString();
        String productquantityValue = productquantity.getText().toString();

        if (productbarcodeValue.isEmpty()) {
            productbarcode.setError("It's Empty");
            productbarcode.requestFocus();
            return;
        }

        if(!TextUtils.isEmpty(productnameValue)&&!TextUtils.isEmpty(productcategoryValue)&&!TextUtils.isEmpty(productpriceValue)&&!TextUtils.isEmpty(productquantityValue)){

            String id = productbarcodeValue;
            Products products = new Products(productbarcodeValue, productnameValue, productcategoryValue, productpriceValue, productquantityValue);

            mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("barcode").child(id).exists()){
                        Toast.makeText(getApplicationContext(), "The Barcode already used. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mdatabaseReference.child("barcode").child(id).setValue(products);
                        productname.setText("");
                        productbarcode.setText("");
                        productprice.setText("");
                        productquantity.setText("");
                        Toast.makeText(AddProductActivity.this,productnameValue+" Added",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Fail to add product." + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(AddProductActivity.this,"Please Fill all the fields",Toast.LENGTH_SHORT).show();
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
