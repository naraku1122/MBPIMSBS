package com.example.admin.pimsbs.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.pimsbs.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button Login;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog processDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.emailSignIn);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.Login);

        progressBar = findViewById(R.id.progressbars);
        progressBar.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
        processDialog = new ProgressDialog(this);

        Login.setOnClickListener(view -> {
            if(Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty()){
                Toast.makeText(this, "Cannot Login with empty credentials. Please try again.", Toast.LENGTH_SHORT).show();
            }
            else {
                validate(Email.getText().toString(), Password.getText().toString());
            }
        });

    }

    private void validate(String userEmail, String userPassword){

        processDialog.setMessage("Please Wait");
        processDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                processDialog.dismiss();
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            }
            else{
                Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show();
                processDialog.dismiss();
            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
