package com.example.admin.pimsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.admin.pimsbs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener  {
    private FirebaseAuth firebaseAuth;
    TextView firebasenameview;

    private CardView addProductItem, deleteProductItem, viewProductInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        firebasenameview = findViewById(R.id.firebasename);

        // this is for username to appear after login
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser = users.getEmail();
        String result = finaluser.substring(0, finaluser.indexOf("@"));
        String resultemail = result.replace(".","");
        firebasenameview.setText(getString(R.string.welcome_user)+resultemail);

        addProductItem = findViewById(R.id.addProductItems);
        deleteProductItem = findViewById(R.id.deleteProductItems);
        viewProductInventory = findViewById(R.id.viewProductInventory);

        addProductItem.setOnClickListener(this);
        deleteProductItem.setOnClickListener(this);
        viewProductInventory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.addProductItems : i = new Intent(this, AddProductActivity.class); startActivity(i); break;
            case R.id.deleteProductItems : i = new Intent(this, DeleteProductActivity.class);startActivity(i); break;
            case R.id.viewProductInventory : i = new Intent(this, ViewProductInventoryActivity.class);startActivity(i); break;
            default: break;
        }
    }

    // logout below
    private void logout()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    firebaseAuth.signOut();
                    finish();
                    Toast.makeText(this,"LOGOUT SUCCESSFUL", Toast.LENGTH_SHORT).show();
                });

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert1 = builder1.create();
        alert1.show();

    }

    @Override
    public void onBackPressed() {
        logout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.logoutMenu:{
                logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
