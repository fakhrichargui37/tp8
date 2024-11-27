package com.example.tp72;

public class Cours {
    private int id;
    private String name;
    private float hours;
    private String teacher;
    private String type;
    private int teacher_id;

    // Default constructor
    public Cours() {}

    // Constructor with all fields
    public Cours(int id, String name, int hours, String teacher, String type, int teacher_id) {
        this.id = id;
        this.name = name;
        this.hours = hours;
        this.teacher = teacher;
        this.type = type;
        this.teacher_id = teacher_id;
    }

    // Getter and Setter for ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for Hours
    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    // Getter and Setter for Teacher
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    // Getter and Setter for Type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Getter and Setter for Teacher ID
    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hours=" + hours +
                ", teacher='" + teacher + '\'' +
                ", type='" + type + '\'' +
                ", teacher_id=" + teacher_id +
                '}';
    }
}