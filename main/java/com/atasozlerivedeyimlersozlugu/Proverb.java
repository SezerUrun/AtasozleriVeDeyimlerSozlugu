package com.atasozlerivedeyimlersozlugu;

public class Proverb {
    private static final long serialVersionUID = 1L;
    private int id;
    private String icerik;

    public Proverb() {
        super();
    }

    public Proverb(int id,String icerik) {
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
