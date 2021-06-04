package com.example.namtcshop.Model;

import java.io.Serializable;

public class GioHang implements Serializable {
    public int idsp;
    public String tensp;
    public long giasp;
    public String hinhanhsp;
    public int soluongsp;

    public GioHang(int idsp, String tensp, long giasp, String hinhanhsp, int soluongsp) {
        this.idsp = idsp;
        this.tensp = tensp;
        this.giasp = giasp;
        this.hinhanhsp = hinhanhsp;
        this.soluongsp = soluongsp;
    }

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public long getGiasp() {
        return giasp;
    }

    public void setGiasp(long giasp) {
        this.giasp = giasp;
    }

    public String getHinhanhsp() {
        return hinhanhsp;
    }

    public void setHinhanhsp(String hinhanhsp) {
        this.hinhanhsp = hinhanhsp;
    }

    public int getSoluongsp() {
        return soluongsp;
    }

    public void setSoluongsp(int soluongsp) {
        this.soluongsp = soluongsp;
    }

    @Override
    public String toString() {
        return "GioHang{" +
                "idsp=" + idsp +
                ", tensp='" + tensp + '\'' +
                ", giasp=" + giasp +
                ", hinhanhsp='" + hinhanhsp + '\'' +
                ", soluongsp=" + soluongsp +
                '}';
    }
}
