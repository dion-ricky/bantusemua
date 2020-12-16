package com.example.bantusemua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ImageView btnDaftar;
    EditText tfNama, tfEmail, tfPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);

        mAuth = FirebaseAuth.getInstance();

        btnDaftar   = findViewById(R.id.btnDaftar);
        tfNama      = findViewById(R.id.tfNamaRegist);
        tfEmail     = findViewById(R.id.tfEmailRegist);
        tfPass      = findViewById(R.id.tfPasswordRegist);

        btnDaftar.setOnClickListener(new handleDaftar());
    }

    class handleDaftar implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String email, password;

            final String nama = tfNama.getText().toString();
            email = tfEmail.getText().toString();
            password = tfPass.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nama)
                                            .build();

                                    user.updateProfile(profileUpdate)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("PROFILE", "Berhasil update profile");
                                                    } else {
                                                        Log.d("PROFILE", "Gagal update profile");
                                                    }
                                                }
                                            });

//                                    Uncomment kalau butuh verifikasi email
//                                    if (user.isEmailVerified()) {
//                                        Intent home = new Intent(RegisterActivity.this, MainActivity.class);
//                                        startActivity(home);
//                                        RegisterActivity.this.finish();
//                                    } else {

//                                        final String userEmail = user.getEmail();

//                                        user.sendEmailVerification()
//                                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//
//                                                        }
//                                                    }
//                                                });


//                                    }

                                }

                            } else {

                            }
                        }
                    });
        }
    }
}
