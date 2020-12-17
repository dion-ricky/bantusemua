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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText tfEmail, tfPassword;
    ImageButton btnLoginGoogle, btnMasuk;
    Button btnGoToRegister;
    private GoogleSignInClient mGoogleSignInClient;

    private int RC_GOOGLE_SIGN_IN = 90;
    private int RC_REGISTER = 91;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        tfEmail = findViewById(R.id.tfLoginEmail);
        tfPassword = findViewById(R.id.tfLoginPass);
        btnLoginGoogle = findViewById(R.id.btnLoginGoogle);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);
        btnMasuk = findViewById(R.id.btnMasuk);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoToRegister.setOnClickListener(new goToRegister());
        btnLoginGoogle.setOnClickListener(new handleLoginGoogle());
        btnMasuk.setOnClickListener(new handleEmailPassLogin());
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToHome();
        }
    }

    class handleEmailPassLogin implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(LoginActivity.this, "Login ...", Toast.LENGTH_LONG).show();
            String email, pass;

            email = tfEmail.getText().toString();
            pass = tfPassword.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) return;

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                goToHome();
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
            Intent googleSignIn = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(googleSignIn, RC_GOOGLE_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("AUTH_GOOGLE", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("AUTH_GOOGLE", "Google sign in failed", e);
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("AUTH_GOOGLE", "signInWithCredential:success");
                            goToHome();
                        } else {
                            Log.w("AUTH_GOOGLE", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    class goToRegister implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent openRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(openRegister, RC_REGISTER);
        }
    }

    private void goToHome() {
        Intent home = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(home);
        LoginActivity.this.finish();
    }
}
