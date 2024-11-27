package com.example.tp72;

public class Teacher {
     private int id;
    private String nom;
    private String email;

    public Teacher(String nom, String email) {
        int id;
        this.nom = nom;
        this.email = email;
    }

    public Teacher(){}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.nom; // Display the teacher's name in the spinner
    }
}
