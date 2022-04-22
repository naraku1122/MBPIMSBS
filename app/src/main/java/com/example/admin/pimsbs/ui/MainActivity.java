package com.example.admin.pimsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.pimsbs.R;
import com.example.admin.pimsbs.activities.ScanCodeActivityViewProduct;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void admin (View view)
    {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void user (View view) { startActivity(new Intent(this, ScanCodeActivityViewProduct.class)); }
    
    @Override
    public void onBackPressed() {
        finish();
    }
}

