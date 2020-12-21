package com.example.bantusemua;

import android.widget.ImageView;

public class model {

    String kategori;
    String judul;
    String lokasi;
    String nominal;
    String yayasan;
//    int imgDonasi;

    public model() {
    }

    public model(String kategori, String judul, String lokasi, String nominal, String yayasan) {
        this.kategori = kategori;
        this.judul = judul;
        this.lokasi = lokasi;
        this.nominal = nominal;
        this.yayasan = yayasan;
//        this.imgDonasi = imgDonasi;
    }

    public String getKategori() {
        return kategori;
    }

    public String getJudul() {
        return judul;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getNominal() {
        return nominal;
    }

    public String getYayasan() {
        return yayasan;
    }

//    public int getImgDonasi() {
//        return imgDonasi;
//    }
}
