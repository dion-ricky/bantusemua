package com.example.bantusemua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bantusemua.DbRepo.Donasi;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference mDatabase;

//    Adapter modelAdapter;
//    ArrayList<model> modelList;
    RecyclerView recyclerView;

    private FirebaseRecyclerOptions<model> options;
    private FirebaseRecyclerAdapter<model,ViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        ImageView ivAvatar = findViewById(R.id.imageView11);
        ivAvatar.setOnClickListener(new logout());
        recyclerView = findViewById(R.id.myRecycler);

//        recyclerView.findViewById(R.id.myRecycler);
//        initRecyclerView();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("donasi");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        options = new FirebaseRecyclerOptions.Builder<model>().setQuery(mDatabase,model.class).build();
        adapter = new FirebaseRecyclerAdapter<model, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull model model) {
                holder.get_txKategori().setText(""+model.getKategori());
                holder.get_txJudul().setText(""+model.getJudul());
                holder.get_txLokasi().setText(""+model.getLokasi());
                holder.get_txNominal().setText("Rp " + NumberFormat.getNumberInstance(Locale.US).format(model.getTerkumpul())
                        + " / " + NumberFormat.getNumberInstance(Locale.US).format(model.getTarget()));
                holder.get_txYayasan().setText(""+model.getPelaksana());

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);

                return new ViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);


//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1: snapshot.getChildren()){
//
//                }
//                modelAdapter = new Adapter(modelList,MainActivity.this);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this,"Oppps...",Toast.LENGTH_SHORT).show();
//            }
//        });


    }

//    private void initRecyclerView() {
//        recyclerView = findViewById(R.id.myRecycler);
//        modelAdapter = new Adapter(modelList,this);
//        recyclerView.setAdapter(modelAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }

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