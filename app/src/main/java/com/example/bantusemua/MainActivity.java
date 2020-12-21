package com.example.bantusemua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.bantusemua.DbRepo.Donasi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ImageView ivAvatar = findViewById(R.id.imageView11);
        ivAvatar.setOnClickListener(new logout());
    }

    void insertToDb(String judul, String lokasi, String pelaksana, String kategori, Double target, Double terkumpul) {
        Donasi donasi = new Donasi(
                judul, lokasi, pelaksana, kategori, target, terkumpul
        );

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("donasi").push().setValue(donasi);
    }

    class logout implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mAuth.signOut();
            MainActivity.this.finish();
        }
    }
}