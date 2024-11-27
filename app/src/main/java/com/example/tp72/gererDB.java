package com.example.tp72;

import static com.example.tp72.DatabaseHandler.COURS_NAME;
import static com.example.tp72.DatabaseHandler.COURS_NBHEURE;
import static com.example.tp72.DatabaseHandler.COURS_TYPE;
import static com.example.tp72.DatabaseHandler.ENSG_ID;
import static com.example.tp72.DatabaseHandler.TABLE_COURS;
import static com.example.tp72.DatabaseHandler.TABLE_TEACHER;
import static com.example.tp72.DatabaseHandler.TEACHER_EMAIL;
import static com.example.tp72.DatabaseHandler.TEACHER_ID;
import static com.example.tp72.DatabaseHandler.TEACHER_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class gererDB {

    private SQLiteDatabase mdb;
    private DatabaseHandler handler;

    public gererDB(Context context) {
        handler = new DatabaseHandler(context);
    }

    public SQLiteDatabase openEcriture() {
        mdb = handler.getWritableDatabase();
        return mdb;
    }

    public SQLiteDatabase openLecture() {
        mdb = handler.getReadableDatabase();
        return mdb;
    }

    public SQLiteDatabase retournerDB() {
        return mdb;
    }

    public void fermer() {
        mdb.close();
    }

    public long addTeacher(Teacher t) {
        mdb = this.openEcriture(); // Ouvrir la base en mode écriture
        ContentValues v = new ContentValues();
        v.put(TEACHER_NAME, t.getNom());
        v.put(TEACHER_EMAIL, t.getEmail());
        long result = -1;
        try {
            result = mdb.insert("Teacher", null, v);
            if (result == -1) {
                Log.e("DatabaseError", "Failed to insert teacher: " + t.getNom());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Exception while inserting teacher", e);
        } finally {
            mdb.close();
        }
        return result;
    }

    public void deleteTeacher(Teacher t) {
        SQLiteDatabase db = this.openEcriture();
        db.delete(TABLE_TEACHER, TEACHER_ID + " = ?", new String[]{String.valueOf(t.getId())});
        db.close();
    }

    public ArrayList<Teacher> getTeacherList() {
        mdb = this.openLecture();
        Cursor c = mdb.rawQuery("SELECT * FROM " + TABLE_TEACHER, null);

        if (c.getCount() != 0) {
            if (c.moveToFirst()) {
                ArrayList<Teacher> tl = new ArrayList<>(c.getCount());
                do {
                    Teacher t = new Teacher();
                    t.setId(c.getInt(0));
                    t.setNom(c.getString(1));
                    t.setEmail(c.getString(2));
                    tl.add(t);
                } while (c.moveToNext());
                return tl;
            }
        }
        c.close();
        mdb.close();
        return null;
    }

    public Teacher getTeacher(int id) {
        // Open the database in read mode
        mdb = this.openLecture();

        Cursor c = mdb.rawQuery("SELECT * FROM " + TABLE_TEACHER + " WHERE id = ?", new String[]{String.valueOf(id)});

        Teacher teacher = null;
        if (c != null && c.moveToFirst()) {
            int teacherId = c.getInt(0);
            String name = c.getString(1);
            String subject = c.getString(2);

            teacher = new Teacher();

            teacher.setId(teacherId);
            teacher.setEmail(subject);
            teacher.setNom(name);
        }

        if (c != null) {
            c.close();
        }
        mdb.close();

        return teacher;
    }

    // gestion de cours

    public long addCours(Cours cours) {
        mdb = this.openEcriture();
        ContentValues values = new ContentValues();
        values.put(COURS_NAME, cours.getName());
        values.put(COURS_NBHEURE, cours.getHours());
        values.put(COURS_TYPE, cours.getType());
        values.put(ENSG_ID, cours.getTeacher_id());

        long res = mdb.insert(TABLE_COURS, null, values);
        mdb.close();
        return res;
    }

    public List<Cours> getCoursList() {
        List<Cours> coursList = new ArrayList<>();
        mdb = this.openLecture();

        Cursor cursor = mdb.rawQuery("SELECT * FROM " + TABLE_COURS, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);                // Column 0: id
                String name = cursor.getString(1);        // Column 1: name
                float hours = cursor.getFloat(2);         // Column 2: nbheure
                String type = cursor.getString(3);        // Column 3: type
                int teacher_id = cursor.getInt(4);        // Column 4: ensg_id

                Cours cours = new Cours();
                cours.setId(id);
                cours.setName(name);
                cours.setHours(hours);
                cours.setType(type);
                cours.setTeacher_id(teacher_id);
                coursList.add(cours);
            } while (cursor.moveToNext());
        }

        cursor.close();
        mdb.close();
        return coursList;
    }

    public void updateCours(Cours cours) {
        mdb = this.openEcriture();
        ContentValues values = new ContentValues();
        values.put("name", cours.getName());
        values.put("hours", cours.getHours());
        values.put("teacher", cours.getTeacher());
        values.put("type", cours.getType());
        values.put("teacher_id", cours.getTeacher_id());

        mdb.update(TABLE_COURS, values, "id = ?", new String[]{String.valueOf(cours.getId())});
        mdb.close();
    }

    public void deleteCours(int id) {
        mdb = this.openEcriture();
        mdb.delete(TABLE_COURS, "id = ?", new String[]{String.valueOf(id)});
        mdb.close();
    }

    public void deleteAllCours() {
        mdb = this.openEcriture();
        mdb.delete(TABLE_COURS, null, null); // Deletes all rows in the table
        mdb.close();
    }

    public ArrayList<Cours> getCoursName(String name) {
        ArrayList<Cours> coursList = new ArrayList<>();
        mdb = this.openEcriture();

        Cursor cursor = mdb.rawQuery("SELECT * FROM " + TABLE_COURS + " WHERE name LIKE ?", new String[]{"%" + name + "%"});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String courseName = cursor.getString(1);
                int hours = cursor.getInt(2);
                String teacher = cursor.getString(3);
                String type = cursor.getString(4);
                int teacher_id = cursor.getInt(5);

                Cours cours = new Cours();
                cours.setId(id);
                cours.setName(courseName);
                cours.setHours(hours);
                cours.setTeacher(teacher);
                cours.setType(type);
                cours.setTeacher_id(teacher_id);
                coursList.add(cours);
            } while (cursor.moveToNext());
        }

        cursor.close();
        mdb.close();
        return coursList;
    }
    public void deleteAllCourses() {
        SQLiteDatabase db = handler.getWritableDatabase();
        db.delete("Cours", null, null); // Deletes all rows in the table
        db.close();
    }

    public List<Cours> searchCourseByName(String courseName) {
        List<Cours> courseList = new ArrayList<>();
        SQLiteDatabase db = handler.getWritableDatabase();

        Cursor cursor = db.query(
                TABLE_COURS, // Replace with your table name
                null, // Select all columns
                COURS_NAME+" LIKE ?", // WHERE clause
                new String[]{"%" + courseName + "%"}, // WHERE arguments
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Cours course = new Cours();
                course.setId(cursor.getInt(0)); // Replace with your column name
                course.setName(cursor.getString(1)); // Replace with your column name
                courseList.add(course);
            }
            cursor.close();
        }

        return courseList;
    }

}