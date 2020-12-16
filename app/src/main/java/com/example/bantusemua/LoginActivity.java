package com.example.bantusemua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button btnGoToRegister = findViewById(R.id.btnGoToRegister);
        btnGoToRegister.setOnClickListener(new goToRegister());
    }

    class goToRegister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent openRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(openRegister);
        }
    }
}
