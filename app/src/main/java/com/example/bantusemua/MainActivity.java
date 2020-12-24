package com.example.bantusemua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bantusemua.DbRepo.Donasi;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;

    RecyclerView recyclerView;

    private FirebaseRecyclerOptions<Donasi> options;
    private FirebaseRecyclerAdapter<Donasi,ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ImageView ivAvatar = findViewById(R.id.id_btLogout);
        ivAvatar.setOnClickListener(new logout());

        ImageView ivPhotoUser = findViewById(R.id.ivPhotoUser);
        Glide.with(this).load(currentUser.getPhotoUrl()).fitCenter().into(ivPhotoUser);

        TextView tvNamaUser = findViewById(R.id.tvNamaUser);
        tvNamaUser.setText(currentUser.getDisplayName());

        FloatingTextButton fabGalangDana = findViewById(R.id.fabGalangDana);
        fabGalangDana.setOnClickListener(new goToGalangDana());

        recyclerView = findViewById(R.id.myRecycler);

        DatabaseReference q_Donasi = mDatabase.child("donasi");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Donasi>().setQuery(q_Donasi, Donasi.class).build();
        adapter = new FireAdapterDonasi(options);

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    class logout implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            mAuth.signOut();
            startActivity(it);
            MainActivity.this.finish();
        }
    }

    class goToGalangDana implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(MainActivity.this, PengajuanDonasi.class);
            startActivity(it);
        }
    }

    class FireAdapterDonasi extends FirebaseRecyclerAdapter<Donasi, ViewHolder> {
        public FireAdapterDonasi(@NonNull FirebaseRecyclerOptions<Donasi> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Donasi donasi) {
            holder.get_txKategori().setText(""+donasi.getKategori());
            holder.get_txJudul().setText(""+donasi.getJudul());
            holder.get_txTenggatWaktu().setText(""+donasi.getTenggatWaktu());
            holder.get_txNominal().setText("Rp " + NumberFormat.getNumberInstance(Locale.US).format(donasi.getTerkumpul())
                    + " / " + NumberFormat.getNumberInstance(Locale.US).format(donasi.getTarget()));
            holder.get_txYayasan().setText(""+donasi.getPelaksana());

            Glide.with(MainActivity.this).load(donasi.getPhotoUrl())
                    .centerCrop().into(holder.get_imgDonasi());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
            return new ViewHolder(v);
        }
    }
}