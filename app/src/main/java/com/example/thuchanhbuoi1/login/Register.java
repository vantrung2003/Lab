package com.example.thuchanhbuoi1.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuchanhbuoi1.MainActivity;
import com.example.thuchanhbuoi1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
TextInputEditText edittextEmail, editTextPassword;
Button btnRegister;
FirebaseAuth mAuth;

ProgressBar progressBar;
TextView tvLogin;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        edittextEmail = findViewById(R.id.edtemail);
        editTextPassword=findViewById(R.id.edtpassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progessbar);
        tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email, passowrd;
                email = String.valueOf(edittextEmail.getText().toString());
                passowrd =String.valueOf(editTextPassword.getText().toString()) ;

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Enter your email!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passowrd)){
                    Toast.makeText(getApplicationContext(),"Enter your password!!",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, passowrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Register.this, "Authentication created.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(Register.this, "Authentication failed. Password should be at least 6 characters",
                                Toast.LENGTH_SHORT).show();
                    }
                    }
                });

            }
        });
    }
}