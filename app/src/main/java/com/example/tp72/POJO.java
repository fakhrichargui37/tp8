package com.example.tp72;

public class POJO {
    String nom;
    String mail;

    public POJO(String nom, String mail) {
        this.nom = nom;
        this.mail = mail;
    }

    public POJO() {}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
