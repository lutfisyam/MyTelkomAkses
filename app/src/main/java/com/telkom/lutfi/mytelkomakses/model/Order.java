package com.telkom.lutfi.mytelkomakses.model;

public class Order {
    private String sc;
    private String nama;
    private String alamat;
    private String kontak;
    private String jenis;
    private String ncli;
    private String ndem;
    private String Alproname;
    private String team;
    private String status;
    private String bukti;


    public Order() {
    }

    public Order(String sc, String nama, String alamat, String kontak, String jenis, String ncli, String ndem, String alproname, String team, String status, String bukti) {
        this.sc = sc;
        this.nama = nama;
        this.alamat = alamat;
        this.kontak = kontak;
        this.jenis = jenis;
        this.ncli = ncli;
        this.ndem = ndem;
        Alproname = alproname;
        this.team = team;
        this.status = status;
        this.bukti = bukti;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getNcli() {
        return ncli;
    }

    public void setNcli(String ncli) {
        this.ncli = ncli;
    }

    public String getNdem() {
        return ndem;
    }

    public void setNdem(String ndem) {
        this.ndem = ndem;
    }

    public String getAlproname() {
        return Alproname;
    }

    public void setAlproname(String alproname) {
        Alproname = alproname;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }
}