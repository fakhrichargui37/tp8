package com.example.tp72;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "MyDataBase";
    // Définir la version de la base de données (si vous changez la version, onUpgrade sera appelé)
    private static final int DATABASE_VERSION = 2;

    // Définir les colonnes pour la table Teacher
     static final String TABLE_TEACHER = "Teacher";
    static final String TEACHER_ID = "id";
    static final String TEACHER_NAME = "name";
    static final String TEACHER_EMAIL = "email";

    // Définir les colonnes pour la table Cours
     static final String TABLE_COURS = "Cours";
    static final String COURS_ID = "id";
     static final String COURS_NAME = "name";
   static final String COURS_NBHEURE = "nbheure";
     static final String COURS_TYPE = "type";
    static final String ENSG_ID = "ensg_id";

    // SQL pour créer la table Teacher
    private static final String CREATE_TABLE_TEACHER = "CREATE TABLE " + TABLE_TEACHER + " (" +
            TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TEACHER_NAME + " TEXT NOT NULL, " +
            TEACHER_EMAIL + " TEXT NOT NULL);";

    // SQL pour créer la table Cours
    private static final String CREATE_TABLE_COURS = "CREATE TABLE " + TABLE_COURS + " (" +
            COURS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COURS_NAME + " TEXT NOT NULL, " +
            COURS_NBHEURE + " FLOAT NOT NULL, " +
            COURS_TYPE + " TEXT NOT NULL, " +
            ENSG_ID + " INTEGER NOT NULL, " + // Reference to the Teacher ID
            "FOREIGN KEY (" + ENSG_ID + ") REFERENCES " + TABLE_TEACHER + "(" + TEACHER_ID + ") " +
            "ON DELETE CASCADE ON UPDATE CASCADE);";

    // Constructeur de la classe
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Cette méthode est appelée pour créer la base de données (tables)
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer les tables
        db.execSQL(CREATE_TABLE_TEACHER);
        db.execSQL(CREATE_TABLE_COURS);
    }

    // Cette méthode est appelée lorsque la version de la base de données est mise à jour
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si la base de données existe déjà, on supprime les tables et on les recrée
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEACHER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURS);
        onCreate(db);  // Recréer les tables
    }
}
