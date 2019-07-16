package com.atasozlerivedeyimlersozlugu;

import java.io.Serializable;

public class Atasozu implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String icerik;

    public Atasozu() {
        super();
    }

    public Atasozu(int id,String icerik) {
        super();
        this.id=id;
        this.icerik = icerik;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcerik() {
        return icerik;
    }

    public void setIcerik(String icerik) {
        this.icerik = icerik;
    }
}