package com.example.bantusemua;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter <ViewHolder> {

    private ArrayList<model> modelList;
    private Context _context;

    public Adapter(ArrayList<model> modelList, Context _context) {
        this.modelList = modelList;
        this._context = _context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.get_txKategori().setText(modelList.get(position).getKategori());
        holder.get_txJudul().setText(modelList.get(position).getJudul());
        holder.get_txLokasi().setText(modelList.get(position).getLokasi());
        holder.get_txNominal().setText(("Rp " + NumberFormat.getNumberInstance(Locale.US).format(modelList.get(position).getTerkumpul())
                + " / " + NumberFormat.getNumberInstance(Locale.US).format(modelList.get(position).getTarget())));
        holder.get_txYayasan().setText(modelList.get(position).getPelaksana());
//        Glide.with(_context).load(modelList.get(position).getImgDonasi()).into(holder.get_imgDonasi());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
