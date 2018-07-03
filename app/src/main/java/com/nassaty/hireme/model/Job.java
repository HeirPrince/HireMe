package com.nassaty.hireme.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Job {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int salary;
    private String user_phone;

    public Job() {
    }

    @Ignore
    public Job(String title, String description, int salary, String user_phone) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.user_phone = user_phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }
}
