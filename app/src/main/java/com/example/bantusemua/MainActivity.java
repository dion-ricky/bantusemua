package com.example.bantusemua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ImageView ivAvatar = findViewById(R.id.imageView11);
        ivAvatar.setOnClickListener(new logout());

    }

    class logout implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mAuth.signOut();
            MainActivity.this.finish();
        }
    }
}