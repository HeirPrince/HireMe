package com.nassaty.hireme.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Job {
    @PrimaryKey
    private int room_id;
    private String id;
    private String title;
    private String description;
    private String owner;
    private String category;


    public Job() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public int getRoom_id() {
        return room_id;
    }

    
    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }
}
