package com.example.bantusemua.DbRepo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Donasi {
    private String judul, pelaksana, kategori, photoUrl, tenggatWaktu;
    private Double target, terkumpul;

    public Donasi(){
        
    }

    public Donasi(String judul, String tenggatWaktu, String pelaksana, String kategori, Double target, Double terkumpul, String photoUrl) {
        this.judul = judul;
        this.pelaksana = pelaksana;
        this.kategori = kategori;
        this.target = target;
        this.terkumpul = terkumpul;
        this.photoUrl = photoUrl;
        this.tenggatWaktu = tenggatWaktu;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getTenggatWaktu() {
        return tenggatWaktu;
    }

    public void setTenggatWaktu(String tenggatWaktu) {
        this.tenggatWaktu = tenggatWaktu;
    }
}
