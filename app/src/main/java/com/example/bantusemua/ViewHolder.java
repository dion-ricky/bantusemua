package com.example.bantusemua;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView _txKategori;
    TextView _txJudul;
    TextView _txLokasi;
    TextView _txNominal;
    TextView _txYayasan;
//    ImageView _imgDonasi;

    Context _context;



    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        _context = itemView.getContext();
        _txKategori = itemView.findViewById(R.id.id_txKategori);
        _txJudul = itemView.findViewById(R.id.id_txJudul);
        _txLokasi = itemView.findViewById(R.id.id_txLocation);
        _txNominal = itemView.findViewById(R.id.id_txNominal);
        _txYayasan = itemView.findViewById(R.id.id_txYayasan);
//        _imgDonasi = itemView.findViewById(R.id.id_imgDonasi);
    }

    @Override
    public void onClick(View v) {

    }

    public TextView get_txKategori() {
        return _txKategori;
    }

    public TextView get_txJudul() {
        return _txJudul;
    }

    public TextView get_txLokasi() {
        return _txLokasi;
    }

    public TextView get_txNominal() {
        return _txNominal;
    }

    public TextView get_txYayasan() {
        return _txYayasan;
    }

//    public ImageView get_imgDonasi() {
//        return _imgDonasi;
//    }

    public Context get_context() {
        return _context;
    }
}
