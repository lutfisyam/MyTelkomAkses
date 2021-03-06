package com.telkom.lutfi.mytelkomakses.model;

import java.util.Date;

public class Order {
    private String sc;
    private String nama;
    private String alamat;
    private String kontak;
    private String jenis;
    private String ncli;
    private String ndem;
    private String alproname;
    private String team;
    private String status;
    private String bukti;
    private String jenis_gangguan;
    private String no_tiket;
    private String no_internet;
    private Date tgl;
    private Date waktumulai;
    private Date waktuselesai;

    public Order() {
    }

    public Order(String sc, String nama, String alamat, String kontak, String jenis, String ncli, String ndem, String alproname, String team, String status, String bukti, String jenis_gangguan, String no_tiket, String no_internet, Date tgl, Date waktumulai, Date waktuselesai) {
        this.sc = sc;
        this.nama = nama;
        this.alamat = alamat;
        this.kontak = kontak;
        this.jenis = jenis;
        this.ncli = ncli;
        this.ndem = ndem;
        this.alproname = alproname;
        this.team = team;
        this.status = status;
        this.bukti = bukti;
        this.jenis_gangguan = jenis_gangguan;
        this.no_tiket = no_tiket;
        this.no_internet = no_internet;
        this.tgl = tgl;
        this.waktumulai = waktumulai;
        this.waktuselesai = waktuselesai;
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
        return alproname;
    }

    public void setAlproname(String alproname) {
        this.alproname = alproname;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getJenis_gangguan() {
        return jenis_gangguan;
    }

    public void setJenis_gangguan(String jenis_gangguan) {
        this.jenis_gangguan = jenis_gangguan;
    }

    public String getNo_tiket() {
        return no_tiket;
    }

    public void setNo_tiket(String no_tiket) {
        this.no_tiket = no_tiket;
    }

    public String getNo_internet() {
        return no_internet;
    }

    public void setNo_internet(String no_internet) {
        this.no_internet = no_internet;
    }

    public Date getTgl() {
        return tgl;
    }

    public void setTgl(Date tgl) {
        this.tgl = tgl;
    }

    public Date getWaktumulai() {
        return waktumulai;
    }

    public void setWaktumulai(Date waktumulai) {
        this.waktumulai = waktumulai;
    }

    public Date getWaktuselesai() {
        return waktuselesai;
    }

    public void setWaktuselesai(Date waktuselesai) {
        this.waktuselesai = waktuselesai;
    }

}