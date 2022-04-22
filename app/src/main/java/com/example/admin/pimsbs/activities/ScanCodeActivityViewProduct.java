package com.example.admin.pimsbs.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.admin.pimsbs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivityViewProduct extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    ZXingScannerView scannerView;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabaseReference;

    TextView productName, productCategory, productPrice, productQuantity;

    private String tempResult;
    private String tempProductName, tempProductCategory, tempProductPrice, tempProductQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        firebaseAuth = FirebaseAuth.getInstance();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Product").child("barcode");

    }

    @Override
    public void handleResult(Result result) {
        tempResult = result.getText();
        getProductData(tempResult);

    }

    private void displayExistProductDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.product_details));

        final View customLayoutDialog = getLayoutInflater().inflate(R.layout.layout_dialog_product, null);

        builder.setView(customLayoutDialog);
        builder.setCancelable(true);

        productName = customLayoutDialog.findViewById(R.id.userviewproductname);
        productCategory = customLayoutDialog.findViewById(R.id.userviewproductcategory);
        productPrice = customLayoutDialog.findViewById(R.id.userviewproductprice);
        productQuantity = customLayoutDialog.findViewById(R.id.userviewproductquantity);

        productName.setText(tempProductName);
        productCategory.setText(tempProductCategory);
        productPrice.setText(tempProductPrice);
        productQuantity.setText(tempProductQuantity);

        builder.setPositiveButton("Yes", (dialog, id)->{
            dialog.dismiss();
            onPostResume();
        });

        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.cancel();
            finish();
        });

        builder.setOnDismissListener(dialogInterface -> onPostResume());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void displayNoProductDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.product_details));
        builder.setCancelable(true);
        builder.setMessage("No Product found. Try again?");

        builder.setPositiveButton("Yes", (dialog, id)->{
            dialog.dismiss();
            onPostResume();
        });

        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.cancel();
            finish();
        });

        builder.setOnDismissListener(dialogInterface -> onPostResume());

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void getProductData(String result){

        firebaseAuth.signInAnonymously().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                mdatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(result).exists()){
                            Map<String,Object> map = (Map<String, Object>) snapshot.child(result).getValue();
                            tempProductName = String.valueOf(map.get("productName"));
                            tempProductCategory = String.valueOf(map.get("productCategory"));
                            tempProductPrice = String.valueOf(map.get("productPrice"));
                            tempProductQuantity = String.valueOf(map.get("productQuantity"));
                            displayExistProductDialog();
                        }
                        else{
                            displayNoProductDialog();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {
                Toast.makeText(this,"No internet connection. Try Again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
   }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
