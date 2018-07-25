package com.telkom.lutfi.mytelkomakses.entitas;

/**
 * Created by Lutfi on 02/06/2018.
 */

public class User {
    private String nip;
    private String nama;
    private String jenis;
    private String email;
    private String pass;

    public User() {
    }

    public User(String nip, String nama, String jenis, String email, String pass) {
        this.nip = nip;
        this.nama = nama;
        this.jenis = jenis;
        this.email = email;
        this.pass = pass;
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
}
