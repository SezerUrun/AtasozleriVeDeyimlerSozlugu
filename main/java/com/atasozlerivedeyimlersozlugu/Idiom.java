package com.atasozlerivedeyimlersozlugu;

public class Idiom {

    private static final long serialVersionUID = 1L;
    private int id;
    private String icerik;

    public Idiom() {
        super();
    }

    public Idiom(int id,String icerik) {
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
