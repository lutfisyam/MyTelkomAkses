package com.telkom.lutfi.mytelkomakses.model;

/**
 * Created by Lutfi on 02/06/2018.
 */

public class User {
    private String nip;
    private String nama;
    private String jenis;
    private String email;
    private String pass;
    private String count;
    private Double latitude;
    private Double longlitude;

    public User() {
    }

    public User(String nip, String nama, String jenis, String email, String pass, String count, Double latitude, Double longlitude) {
        this.nip = nip;
        this.nama = nama;
        this.jenis = jenis;
        this.email = email;
        this.pass = pass;
        this.count = count;
        this.latitude = latitude;
        this.longlitude = longlitude;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLonglitude() {
        return longlitude;
    }

    public void setLonglitude(Double longlitude) {
        this.longlitude = longlitude;
    }
}
