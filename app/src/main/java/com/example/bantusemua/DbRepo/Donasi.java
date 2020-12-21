package com.example.bantusemua.DbRepo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Donasi {
    private String judul, lokasi, pelaksana, kategori;
    private Double target, terkumpul;

    public Donasi(String judul, String lokasi, String pelaksana, String kategori, Double target, Double terkumpul) {
        this.judul = judul;
        this.lokasi = lokasi;
        this.pelaksana = pelaksana;
        this.kategori = kategori;
        this.target = target;
        this.terkumpul = terkumpul;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getPelaksana() {
        return pelaksana;
    }

    public void setPelaksana(String pelaksana) {
        this.pelaksana = pelaksana;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Double getTerkumpul() {
        return terkumpul;
    }

    public void setTerkumpul(Double terkumpul) {
        this.terkumpul = terkumpul;
    }
}
