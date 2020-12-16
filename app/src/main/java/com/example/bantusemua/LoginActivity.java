package com.example.bantusemua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText tfEmail, tfPassword;
    ImageButton btnLoginGoogle, btnLoginFacebook, btnMasuk;
    Button btnGoToRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        tfEmail = findViewById(R.id.tfLoginEmail);
        tfPassword = findViewById(R.id.tfLoginPass);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        btnMasuk = findViewById(R.id.btnMasuk);

        btnGoToRegister.setOnClickListener(new goToRegister());
        btnLoginGoogle.setOnClickListener(new handleLoginGoogle());
        btnMasuk.setOnClickListener(new handleEmailPassLogin());
    }

    class handleEmailPassLogin implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(LoginActivity.this, "Login ...", Toast.LENGTH_LONG).show();
            String email, pass;

            email = tfEmail.getText().toString();
            pass = tfPassword.getText().toString();

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent home = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(home);
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login gagal!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    class handleLoginGoogle implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    class goToRegister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent openRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(openRegister);
        }
    }
}
