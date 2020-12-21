package com.example.bantusemua;

public class model {

    String kategori;
    String judul;
    String lokasi;
    Long target;
    Long terkumpul;
    String pelaksana;
//    int imgDonasi;

    public model() {
    }

    public model(String kategori, String judul, String lokasi, Long target, Long terkumpul, String pelaksana) {
        this.kategori = kategori;
        this.judul = judul;
        this.lokasi = lokasi;
        this.target = target;
        this.terkumpul = terkumpul;
        this.pelaksana = pelaksana;
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

    public Long getTarget() {
        return target;
    }

    public Long getTerkumpul() { return terkumpul; }

    public String getPelaksana() {
        return pelaksana;
    }

//    public int getImgDonasi() {
//        return imgDonasi;
//    }
}
