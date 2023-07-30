package com.example.thuchanhbuoi1.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thuchanhbuoi1.Lab.Lab5;
import com.example.thuchanhbuoi1.Lab.Server;
import com.example.thuchanhbuoi1.MainActivity;
import com.example.thuchanhbuoi1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextInputEditText edittextEmail, editTextPassword;
    Button btnLogin;
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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        edittextEmail = findViewById(R.id.edtemail);
        editTextPassword=findViewById(R.id.edtpassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progessbar);
        tvLogin = findViewById(R.id.tvRegister);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
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
                mAuth.signInWithEmailAndPassword(email, passowrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Authentication success!!.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}